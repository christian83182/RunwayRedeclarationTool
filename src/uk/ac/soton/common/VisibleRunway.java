package uk.ac.soton.common;

public class VisibleRunway extends PositionalObject{

    private Integer length;
    private Integer width;
    private String status;
    private Boolean active = true;

    private Integer clearway;
    private Integer stopway;
    private Integer resa; // runway end safety area
    private Integer als;  // approach landing surface
    private Integer blastDistance;
    private Integer stripEnd;

    private LogicalRunway[] runways = new LogicalRunway[2];

    private Obstacle obstacle = null; // no obstacle present initially

    VisibleRunway(String id, Integer xPos, Integer yPos, Integer length, Integer width){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
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

    public void setObstacle(Obstacle obstacle){ this.obstacle = obstacle; }

    public void clearObstacle() { this.obstacle = null; }

    public Obstacle getObstacle() { return obstacle; }

    public Integer getAls() { return als; }

    public void setAls(Integer als) { this.als = als; }
}
