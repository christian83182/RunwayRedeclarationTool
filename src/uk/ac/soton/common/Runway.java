package uk.ac.soton.common;

//A child of the PositionalObject class which represents an instance of a Runway with a certain length, width, and name.
public class Runway extends PositionalObject{

    private Integer length;
    private Integer width;
    private String name;
    private String status;
    private Boolean active = true;

    private Integer clearway;
    private Integer stopway;

    private Integer tora; // take-off run available
    private Integer toda; // take-off distance available
    private Integer asda; // accelerate-stop distance available
    private Integer lda;  // landing distance available
    private Integer resa; // runway end safety area
    private Integer tocs; // take-off climb surface
    private Integer als;  // approach landing surface
    private Integer threshold = 0;

    private Obstacle obstacle = null; // no obstacle present initially

    public Runway(String id, Integer xPos, Integer yPos, Integer length, Integer width){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
        this.toda = length;
        this.asda = length;
        this.lda = length;
    }

    public Runway(){
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

    public Boolean getActive() { return active; }

    public void setActive(Boolean state) { this.active = state; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

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

    public void placeObstacle(String id, Integer thresholdDistance, Integer centrelineDistance, Integer runwayDistance,
                              Airfield.Dimensions dimensions){

        Integer xPos = this.getxPos() + thresholdDistance;
        Integer yPos = this.getyPos(); //TODO

        this.obstacle = new Obstacle(id, xPos, yPos, thresholdDistance, centrelineDistance, runwayDistance, dimensions);
    }

    public void clearObstacle() { this.obstacle = null; }

    public Obstacle getObstacle() { return obstacle; }

    public Integer getTora() { return tora; }

    public void setTora(Integer tora) { this.tora = tora; }

    public Integer getToda() { return toda; }

    public void  setToda(Integer toda) { this.toda = toda; }

    public Integer getAsda() { return asda; }

    public void setAsda(Integer asda) { this.asda = asda; }

    public Integer getLda() { return lda; }

    public void setLda(Integer lda) { this.lda = lda; }

    public Integer getResa() { return resa; }

    public void setResa(Integer resa) { this.resa = resa; }

    public Integer getTocs() { return tocs; }

    public void setTocs(Integer tocs) { this.tocs = tocs; }

    public Integer getAls() { return als; }

    public void setAls(Integer als) { this.als = als; }
}
