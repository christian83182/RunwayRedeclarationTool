package uk.ac.soton.common;

public class VisibleRunway extends PositionalObject{

    private Integer length;
    private Integer width;
    private String status;
    private Boolean active = true;
    private Integer clearway;
    private Integer stopway;
    // runway end safety area
    private Integer resa;
    // approach landing surface
    private Integer als;
    private Integer blastDistance;
    private Integer stripEnd;

    private LogicalRunway[] runways = new LogicalRunway[2];

    VisibleRunway(String id, Integer xPos, Integer yPos, Integer length, Integer width){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
        setAls(50);
    }

    VisibleRunway(){
        super(0,0,"00A");
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

    public Boolean getActive() { return active; }

    public void setActive(Boolean state) { this.active = state; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Integer getAls() { return als; }

    public void setAls(Integer als) { this.als = als; }

    public Integer getClearway() {
        return clearway;
    }

    public void setClearway(Integer clearway) {
        this.clearway = clearway;
    }

    public Integer getStopway() {
        return stopway;
    }

    public void setStopway(Integer stopway) {
        this.stopway = stopway;
    }

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

    public void setStripEnd(Integer stripEnd) {
        this.stripEnd = stripEnd;
    }

    public LogicalRunway[] getRunways() {
        return runways;
    }

    public void setRunways(LogicalRunway[] runways) {
        this.runways = runways;
    }


    //taking off and landing towards the obstacle must occur in the direction of the runway where the distance of the object
    //from threshold is greater and the opposite applies for taking off away and landing over

    public void recalculateParameters(){
        if(runways[0].getObstacle().getThresholdDistance() < runways[1].getObstacle().getThresholdDistance()){
            recalculateToraTowardsObstacle(runways[1]);
            recalculateTodaTowardsObstacle(runways[1]);
            recalculateAsdaTowardsObstacle(runways[1]);
            recalculateLdaTowardsObstacle(runways[1]);
        }else{
            recalculateToraTowardsObstacle(runways[0]);
            recalculateTodaTowardsObstacle(runways[0]);
            recalculateAsdaTowardsObstacle(runways[0]);
            recalculateLdaTowardsObstacle(runways[0]);
        }
    }


    public void recalculateToraTowardsObstacle(LogicalRunway runway){
        Obstacle obstacle = runway.getObstacle();

        runway.redeclareTora(obstacle.getThresholdDistance() + runway.getDisplacedThreshold() - obstacle.getHeight() * getAls() - getStripEnd());
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
    }

}
