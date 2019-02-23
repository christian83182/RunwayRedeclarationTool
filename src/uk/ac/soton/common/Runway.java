package uk.ac.soton.common;

//A child of the PositionalObject class which represents an instance of a Runway with a certain length, width, and name.
public class Runway extends PositionalObject{

    private Integer length;
    private Integer width;
    private String name;

    private Integer clearway;
    private Integer stopway;

    private Integer tora; // take-off run available
    private Integer toda; // take-off distance available
    private Integer asda; // accelerate-stop distance available
    private Integer lda;  // landing distance available
    private Integer threshold = 0;

    private Obstacle obstacle = null; // no obstacle present initially

    Runway(String id, Integer xPos, Integer yPos, Integer length, Integer width){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
        this.toda = length;
        this.asda = length;
        this.lda = length;
    }

    Runway(){
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClearway() { return clearway; }

    public void setClearway(Integer clearway) {
        this.clearway = clearway;
        this.toda = tora + clearway;
    }

    public Integer getStopway() { return stopway; }

    public void setStopway(Integer stopway) {
        this.stopway = stopway;
        this.asda = tora + stopway;
    }

    public Integer getThreshold() { return threshold; }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
        this.lda = tora - threshold;
    }

    public void setObstacle(Obstacle obstacle){ this.obstacle = obstacle; }

    public Obstacle getObstacle() { return obstacle; }
}
