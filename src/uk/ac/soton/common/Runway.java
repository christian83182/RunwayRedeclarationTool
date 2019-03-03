package uk.ac.soton.common;

// A child of the PositionalObject class which represents an instance of a Runway with a certain length, width, and id.
public class Runway extends PositionalObject{

    private Integer length;
    private Integer width;
    private String status;
    private Boolean active = true;
    // runway end safety area
    private Integer resa = 240;
    // approach landing surface
    private Integer als;
    private Integer blastDistance = 300;
    private Integer stripEnd = 60;

    private LogicalRunway[] runways = new LogicalRunway[2];

    public Runway(String id, Integer xPos, Integer yPos, Integer length, Integer width){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
        setAls(50); //TODO ???
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

    //not needed? bc strip end is always 60
    //public void setStripEnd(Integer stripEnd) { this.stripEnd = stripEnd; }

    public LogicalRunway[] getLogicalRunways() {
        return runways;
    }

    public void setLogicalRunways(LogicalRunway directionL, LogicalRunway directionR) {
        runways[0] = directionL;
        runways[1] = directionR;
    }

    public void clearObstacle(){
        runways[0].clearObstacle();
        runways[1].clearObstacle();
    }

    //taking off and landing towards the obstacle must occur in the direction of the runway where the distance of the object
    //from threshold is greater and the opposite applies for taking off away and landing over

    public void recalculateParameters(){
        if(runways[0].getObstacle().getThresholdDistance() < runways[1].getObstacle().getThresholdDistance()){
            /*recalculateToraTowardsObstacle(runways[1]);
            recalculateTodaTowardsObstacle(runways[1]);
            recalculateAsdaTowardsObstacle(runways[1]);
            recalculateLdaTowardsObstacle(runways[1]);*/
            recalculateTowardsObstacle(runways[1]);
            recalculateAwayFromObstacle(runways[0]);
        }else{
            /*recalculateToraTowardsObstacle(runways[0]);
            recalculateTodaTowardsObstacle(runways[0]);
            recalculateAsdaTowardsObstacle(runways[0]);
            recalculateLdaTowardsObstacle(runways[0]);*/
            recalculateTowardsObstacle(runways[0]);
            recalculateAwayFromObstacle(runways[1]);
        }
    }

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

        // TODA = Redeclared TORA + Clearway
        runway.redeclareToda(runway.getTora().getRedeclaredValue().intValue() + runway.getClearway().intValue());

        // ASDA = Redeclared TORA + Stopway
        runway.redeclareAsda(runway.getTora().getRedeclaredValue().intValue() + runway.getStopway().intValue());

        // LDA = Original LDA - Slope Calculation - Distance from Threshold - Strip End
        runway.redeclareLda(runway.getLda().getOriginalValue().intValue() - obstacle.getHeight() * getAls()
                - obstacle.getThresholdDistance() - getStripEnd());
    }

    /*public void recalculateToraTowardsObstacle(LogicalRunway runway){
        Obstacle obstacle = runway.getObstacle();

        runway.redeclareTora(obstacle.getThresholdDistance() + runway.getThreshold() - obstacle.getHeight() * getAls() - getStripEnd());
    }


    public void recalculateTodaTowardsObstacle(LogicalRunway runway){
        runway.redeclareToda(runway.getToda().getRedeclaredValue());
    }


    public void recalculateAsdaTowardsObstacle(LogicalRunway runway){
        runway.redeclareToda(runway.getAsda().getRedeclaredValue());
    }

    public void recalculateLdaTowardsObstacle(LogicalRunway runway){
        Obstacle obstacle = runway.getObstacle();

        runway.redeclareLda(obstacle.getThresholdDistance() - getResa() - getStripEnd());
    }*/

}
