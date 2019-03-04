package uk.ac.soton.common;

// A child of the PositionalObject class which represents an instance of a Runway with a certain length, width, and id.
public class Runway extends PositionalObject{

    private Integer length = 0;
    private Integer width = 0;
    private String status = "";
    private Boolean active = true;

    // Runway end safety area
    private Integer resa = 240;
    // Approach landing surface
    private Integer als = 0;

    private Integer blastDistance = 300;
    private  Integer stripEnd = 60;
    private Integer stripWidth = 0;

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
        setAls(50);
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

    public Integer getBlastDistance() {
        return blastDistance;
    }

    public void setBlastDistance(Integer blastDistance) {
        this.blastDistance = blastDistance;
    }

    public Integer getStripEnd() {
        return stripEnd;
    }

    public Integer getStripWidth() {
        return stripWidth;
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

    public void clearObstacle(){
        runways[0].clearObstacle();
        runways[1].clearObstacle();
    }

    /**
     * Re-declares the runway by performing recalculations of all the affected parameters for both logical
     * runways either towards or away from the obstacle, which is dependent on the position of the obstacle.
     */
    public void redeclareRunway(){

        // Taking off and landing towards the obstacle must occur in the direction of the runway where the distance
        // of the object from threshold is greater and the opposite applies for taking off away and landing over.
        if(runways[0].getObstacle().getThresholdDistance() < runways[1].getObstacle().getThresholdDistance()){
            recalculateTowardsObstacle(runways[1]);
            recalculateAwayFromObstacle(runways[0]);
        }else{
            recalculateTowardsObstacle(runways[0]);
            recalculateAwayFromObstacle(runways[1]);
        }
    }

    /**
     * Recalculates all the affected runway parameters for take-off/landing towards the obstacle.
     * @param runway The logical runway to perform the calculations for.
     */
    private void recalculateTowardsObstacle(LogicalRunway runway){
        Obstacle obstacle = runway.getObstacle();

        // TORA = Distance from Threshold + Displaced Threshold - Slope Calculation - Strip End
        runway.redeclareTora(obstacle.getThresholdDistance() + runway.getThreshold().intValue()
                - obstacle.getHeight() * getAls() - getStripEnd());

        // TODA = Redeclared TORA
        runway.redeclareToda(runway.getTora().getRedeclaredValue());

        // ASDA = Redeclared TORA
        runway.redeclareAsda(runway.getTora().getRedeclaredValue());

        // LDA = Distance from Threshold - RESA - Strip End
        runway.redeclareLda(obstacle.getThresholdDistance() - getResa() - getStripEnd());
    }

    /**
     * Recalculates all the affected runway parameters for take-off/landing away from the obstacle.
     * @param runway The logical runway to perform the calculations for.
     */
    private void recalculateAwayFromObstacle(LogicalRunway runway){
        Obstacle obstacle = runway.getObstacle();

        // If theshold is displaced
        if(runway.getThreshold().intValue() != 0){

            // TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold
            runway.redeclareTora(runway.getTora().getOriginalValue().intValue() - blastDistance
                    - obstacle.getThresholdDistance() - runway.getThreshold().intValue());
        } else{

            // TORA = Original TORA - Strip End - RESA - Distance from Threshold
            runway.redeclareTora(runway.getTora().getOriginalValue().intValue() - getStripEnd()
                    - getResa() - obstacle.getThresholdDistance());
        }

        // TODA = Redeclared TORA + Clearway Length
        runway.redeclareToda(runway.getTora().getRedeclaredValue().intValue() + runway.getClearway().width);

        // ASDA = Redeclared TORA + Stopway Length
        runway.redeclareAsda(runway.getTora().getRedeclaredValue().intValue() + runway.getStopway().width);

        // LDA = Original LDA - Slope Calculation - Distance from Threshold - Strip End
        runway.redeclareLda(runway.getLda().getOriginalValue().intValue() - obstacle.getHeight() * getAls()
                - obstacle.getThresholdDistance() - getStripEnd());
    }

}
