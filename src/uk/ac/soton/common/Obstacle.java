package uk.ac.soton.common;

//A child of the PositionalObject class which describes a 3D Box representing an obstacle on the runway.
public class Obstacle extends PositionalObject {

    private Integer height;
    private Integer length;
    private Integer width;
    private Integer distance; // distance from start of the runway

    Obstacle(String id, Integer xPos, Integer yPos, Integer length, Integer width, Integer height){
        super(xPos, yPos, id);
        this.height = height;
        this.length = length;
        this.width = width;
    }

    Obstacle(){
        super(0,0,"DefaultID");
        this.height = 0;
        this.width = 0;
        this.length = 0;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Integer getDistance() { return distance; }

    public void setDistance(Integer distance) { this.distance = distance; }
}
