package uk.ac.soton.common;

//A child of the PositionalObject class which describes a 3D Box representing an obstacle on the runway.
public class Obstacle extends PositionalObject {

    private Double height;
    private Double length;
    private Double width;
    private Integer distFromThreshold;
    private Integer distFromCentreline;
    private Integer distFromRunway;

    Obstacle(String id, Integer xPos, Integer yPos, Airfield.Dimensions dimensions){
        super(xPos, yPos, id);
        this.length = dimensions.getLength();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }

    Obstacle(){
        super(0,0,"DefaultID");
        this.height = 0.0;
        this.width = 0.0;
        this.length = 0.0;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Integer getDistFromThreshold() {
        return distFromThreshold;
    }

    public void setDistFromThreshold(Integer distFromThreshold) {
        this.distFromThreshold = distFromThreshold;
    }

    public Integer getDistFromCentreline() {
        return distFromCentreline;
    }

    public void setDistFromCentreline(Integer distFromCentreline) {
        this.distFromCentreline = distFromCentreline;
    }

    public Integer getDistFromRunway() {
        return distFromRunway;
    }

    public void setDistFromRunway(Integer distFromRunway) {
        this.distFromRunway = distFromRunway;
    }

    // Turns obstacle sideways (length of the object perpendicular to the length of the runway)
    public void sideways(){
        Double temp = length;
        length = width;
        width = temp;
    }
}
