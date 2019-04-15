package uk.ac.soton.common;

// A child of the PositionalObject class which represents an instance of a Runway with a certain length, width, and id.
public class Runway extends PositionalObject{

    private Integer length = 0;
    private Integer width = 0;
    private String status = "";
    private Boolean active = true;
    private Boolean redeclared = false;

    // Runway end safety area
    private Integer resa = 240;
    // Approach landing surface
    private Integer als = 50;

    private Integer stripEnd = 60;
    private Integer stripWidth = 0;
    private Obstacle obstacle = null;

    private LogicalRunway objectCloserToThisThreshold = null;

    // The 2 logical runways associated with the physical one.
    private LogicalRunway[] runways = new LogicalRunway[2];

    /**
     * Constructor for the physical/visible runway.
     * @param id Identifier of the physical runway, for example "09/27" or "09R/27L".
     * @param xPos X position of the top left corner of the runway.
     * @param yPos Y position of the top left corner of the runway.
     * @param length Length of the runway in metres.
     * @param width Width of the runway in metres.
     * @param stripWidth Runway strip width in metres.
     */
    public Runway(String id, Integer xPos, Integer yPos, Integer length, Integer width, Integer stripWidth, Integer stripEnd){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
        this.stripWidth = stripWidth;
        this.stripEnd = stripEnd;
    }

    public Runway(){
        super(0,0,"00L/00R");
        this.length = 0;
        this.width = 0;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean isActive() { return active; }

    public void setActive(Boolean state) { this.active = state; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Integer getAls() { return als; }

    public void setAls(Integer als) { this.als = als; }

    public Integer getResa() {
        return resa;
    }

    public void setResa(Integer resa) {
        this.resa = resa;
    }

    public Integer getStripEnd() {
        return stripEnd;
    }

    public Integer getStripWidth() {
        return stripWidth;
    }

    public void setStripEnd(Integer stripEnd) {
        this.stripEnd = stripEnd;
    }

    public void setStripWidth(Integer stripWidth) {
        this.stripWidth = stripWidth;
    }

    /**
     * Gets both logical runways of a physical runway.
     * @return An array of length 2 that contains the 2 logical runways.
     */
    public LogicalRunway[] getLogicalRunways() {
        return runways;
    }

    /**
     * Gets the specified logical runway from the physical runway object.
     * @param name Identifier of the logical runway, for example "09".
     * @return The logical runway object.
     */
    public LogicalRunway getLogicalRunway(String name){

        if(runways[0].getName().equals(name)){
            return runways[0];
        }
        else if(runways[1].getName().equals(name)){
            return runways[1];
        }
        else{
            return null;
        }
    }

    /**
     * Assigns the two logical runways of a physical one.
     * @param direction1 The logical runway with a smaller angle, for example "09".
     * @param direction2 The logical runway with a bigger angle, for example "27".
     */
    public void setLogicalRunways(LogicalRunway direction1, LogicalRunway direction2) {
        runways[0] = direction1;
        runways[1] = direction2;
    }

    /**
     * Removes obstacle from the runway and resets all parameters.
     */
    public void clearObstacle(){
        this.obstacle = null;
        runways[0].revertParameters();
        runways[0].setObstacleOffset(0);
        runways[1].revertParameters();
        runways[1].setObstacleOffset(0);
        redeclared = false;
//        runways[0].setObjectDistances(null, null);
//        runways[1].setObjectDistances(null, null);
        //this.resa = 240; // Resets the RESA in case it was temporarily redefined
    }

    /**
     * returns the Logical Runway on which the obstacle is closer to its threshold
     */
    public LogicalRunway getObjectCloserToThisThreshold() {
        return objectCloserToThisThreshold;
    }

    //Runway id refers to the logical runway
    public void placeObstacle(Obstacle obstacle, String runwayId){
        this.obstacle = obstacle;
        LogicalRunway current = null;
        LogicalRunway sibling = null;
        if(getLogicalRunways()[0].getName().equals(runwayId)){
            current = getLogicalRunways()[0];
            sibling = getLogicalRunways()[1];
        }else if(getLogicalRunways()[1].getName().equals(runwayId)){
            current = getLogicalRunways()[1];
            sibling = getLogicalRunways()[0];
        }

        current.setObjectDistances(obstacle.getStartDistance(), obstacle.getCentrelineDistance(), obstacle.getStartDistance());
        sibling.setObjectDistances(getLength() - obstacle.getStartDistance(), -obstacle.getCentrelineDistance(), getLength() - obstacle.getStartDistance());
    }

    public Obstacle getObstacle(){
        return this.obstacle;
    }

    /**
     * Re-declares the runway by performing recalculations of all the affected parameters for both logical
     * runways either towards or away from the obstacle, which is dependent on the position of the obstacle.
     */
    public void recalculateParameters(){

        if(obstacle == null){
            return;
        }
        else if(obstacle.getStartDistance() < -stripEnd || obstacle.getStartDistance() > length + stripEnd ||
                obstacle.getCentrelineDistance() > 75 || obstacle.getCentrelineDistance() < -75){

            // Obstacle is not within 60m (strip end) of the runway or within 75m from the centreline.
            return;
        }
        else{

            redeclared = true;

            if(runways[0].getObjectThresholdDistance().intValue() < (runways[1].getObjectThresholdDistance().intValue())){
                objectCloserToThisThreshold = runways[0];
                recalculateTowardsObstacle(runways[1]);
                recalculateAwayFromObstacle(runways[0]);
            }
            else{
                objectCloserToThisThreshold = runways[1];
                recalculateTowardsObstacle(runways[0]);
                recalculateAwayFromObstacle(runways[1]);
            }
        }
    }

    public boolean isRedeclared(){
        return this.redeclared;
    }

    /**
     * Recalculates all the affected runway parameters for take-off/landing towards the obstacle.
     * @param runway The logical runway to perform the calculations for.
     */
    private void recalculateTowardsObstacle(LogicalRunway runway){

        double slope = obstacle.getHeight().intValue() * als;

        if(slope > resa){

            // TORA = Distance from Threshold + Displaced Threshold - Slope Calculation - Strip End
            runway.redeclareTora(runway.getObjectThresholdDistance().intValue() + runway.getThreshold().intValue()
                    - slope - stripEnd);
            runway.getTora().setBreakdown("TORA = Distance from Threshold + Displaced Threshold - Slope Calculation - Strip End\n\n" +
                    "TORA = " + runway.getObjectThresholdDistance().intValue() + " + " + runway.getThreshold().intValue() +
                    " - " + obstacle.getHeight() + " * " + als + " - " + stripEnd +
                    " = " + runway.getTora().getRedeclaredValue().intValue());
        }
        else{

            // TORA = Distance from Threshold + Displaced Threshold - RESA - Strip End
            runway.redeclareTora(runway.getObjectThresholdDistance().intValue() + runway.getThreshold().intValue()
                    - resa - stripEnd);
            runway.getTora().setBreakdown("TORA = Distance from Threshold + Displaced Threshold - RESA - Strip End\n\n" +
                    "TORA = " + runway.getObjectThresholdDistance().intValue() + " + " + runway.getThreshold().intValue() +
                    " - " + resa + " - " + stripEnd + " = " + runway.getTora().getRedeclaredValue().intValue());
        }

        // TODA = Redeclared TORA
        runway.redeclareToda(runway.getTora().getRedeclaredValue());
        runway.getToda().setBreakdown("TODA = Redeclared TORA\n\n" +
                "TODA = " + runway.getTora().getRedeclaredValue().intValue());

        // ASDA = Redeclared TORA
        runway.redeclareAsda(runway.getTora().getRedeclaredValue());
        runway.getAsda().setBreakdown("ASDA = Redeclared TORA\n\n" +
                "ASDA = " + runway.getTora().getRedeclaredValue().intValue());

        // LDA = Distance from Threshold - RESA - Strip End
        runway.redeclareLda(runway.getObjectThresholdDistance().intValue() - resa - stripEnd);
        runway.getLda().setBreakdown("LDA = Distance from Threshold - RESA - Strip End\n\n" +
                "LDA = " + runway.getObjectThresholdDistance().intValue() + " - " + resa + " - " + stripEnd + " = " +
                runway.getLda().getRedeclaredValue().intValue());
    }

    /**
     * Recalculates all the affected runway parameters for take-off/landing away from the obstacle.
     * @param runway The logical runway to perform the calculations for.
     */
    private void recalculateAwayFromObstacle(LogicalRunway runway){

        double slope = obstacle.getHeight().intValue() * als;

        if(resa + stripEnd > Airfield.getBlastProtection()){

            // TORA = Original TORA - Strip End - RESA - Distance from Threshold - Displaced Threshold
            runway.redeclareTora(runway.getTora().getOriginalValue().intValue() - stripEnd - resa
                    - runway.getObjectThresholdDistance().intValue() - runway.getThreshold().intValue());
            runway.getTora().setBreakdown("TORA = Original TORA - Strip End - RESA - Distance from Threshold - Displaced Threshold\n\n" +
                    "TORA = " + runway.getTora().getOriginalValue() + " - " + stripEnd + " - " + resa +
                    " - " + runway.getObjectThresholdDistance() + " - " + runway.getThreshold().intValue() +
                    " = " + runway.getTora().getRedeclaredValue().intValue());

        }
        else{

            // TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold
            runway.redeclareTora(runway.getTora().getOriginalValue().intValue() - Airfield.getBlastProtection()
                    - runway.getObjectThresholdDistance().intValue() - runway.getThreshold().intValue());
            runway.getTora().setBreakdown("TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold\n\n" +
                    "TORA = " + runway.getTora().getOriginalValue() + " - " + Airfield.getBlastProtection() +
                    " - " + runway.getObjectThresholdDistance() + " - " + runway.getThreshold().intValue() +
                    " = " + runway.getTora().getRedeclaredValue().intValue());
        }

        // TODA = Redeclared TORA + Clearway Length
        runway.redeclareToda(runway.getTora().getRedeclaredValue().intValue() + runway.getClearway().width);
        runway.getToda().setBreakdown("TODA = Redeclared TORA + Clearway\n\n" +
                "TODA = " + runway.getTora().getRedeclaredValue() + " + " + runway.getClearway().width +
                " = " + runway.getToda().getRedeclaredValue().intValue());

        // ASDA = Redeclared TORA + Stopway Length
        runway.redeclareAsda(runway.getTora().getRedeclaredValue().intValue() + runway.getStopway().width);
        runway.getAsda().setBreakdown("ASDA = Redeclared TORA + Stopway\n\n" +
                "ASDA = " + runway.getTora().getRedeclaredValue() + " + " + runway.getStopway().width +
                " = " + runway.getAsda().getRedeclaredValue().intValue());


        if(slope + stripEnd > Airfield.getBlastProtection() || resa + stripEnd > Airfield.getBlastProtection()){

            if(slope > resa){

                // LDA = Original LDA - Distance from Threshold - Slope Calculation - Strip End
                runway.redeclareLda(runway.getLda().getOriginalValue().intValue() - runway.getObjectThresholdDistance().intValue()
                        - slope - stripEnd);
                runway.getLda().setBreakdown("LDA = Original LDA - Distance from Threshold - Slope Calculation - Strip End\n\n" +
                        "LDA = " + runway.getLda().getOriginalValue().intValue() + " - " + runway.getObjectThresholdDistance().intValue()
                        + " - " + obstacle.getHeight() + " * " + als + " - " + stripEnd +
                        " = " + runway.getLda().getRedeclaredValue().intValue());

            }
            else{

                // LDA = Original LDA - Distance from Threshold - RESA - Strip End
                runway.redeclareLda(runway.getLda().getOriginalValue().intValue() - runway.getObjectThresholdDistance().intValue()
                        - resa - stripEnd);
                runway.getLda().setBreakdown("LDA = Original LDA - Distance from Threshold - RESA - Strip End\n\n" +
                        "LDA = " + runway.getLda().getOriginalValue().intValue() + " - " + runway.getObjectThresholdDistance().intValue()
                        + " - " + resa + " - " + stripEnd + " = " + runway.getLda().getRedeclaredValue().intValue());
            }
        }
        else{

            // LDA = Original LDA - Distance from Threshold - Blast Protection
            runway.redeclareLda(runway.getLda().getOriginalValue().intValue() - runway.getObjectThresholdDistance().intValue()
                    - Airfield.getBlastProtection());
            runway.getLda().setBreakdown("LDA = Original LDA - Distance from Threshold - Blast Protection\n\n" +
                    "LDA = " + runway.getLda().getOriginalValue().intValue() + " - " + runway.getObjectThresholdDistance().intValue()
                    + " - " + Airfield.getBlastProtection() + " = " + runway.getLda().getRedeclaredValue().intValue());
        }
    }

}
