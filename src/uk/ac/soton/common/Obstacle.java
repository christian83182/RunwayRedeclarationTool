package uk.ac.soton.common;

//A child of the PositionalObject class which describes a 3D Box representing an obstacle on the runway.
public class Obstacle extends PositionalObject {

    private Double height;
    private Double length;
    private Double width;
    private Integer thresholdDistance; // TODO needs 2 threshold distances - one for each logical runway
    private Integer centrelineDistance;
    private Integer runwayDistance;

    /**
     * Constructor for a physical obstacle to be placed on a runway.
     * @param id Identifier of the obstacle.
     * @param xPos X position of the top left corner of the obstacle.
     * @param yPos Y position of the top left corner of the obstacle.
     * @param thresholdDistance Will be changed...
     * @param centrelineDistance Distance from the centreline of the runway.
     * @param runwayDistance Distance from the end of the runway (0 if on the runway / next to the runway)
     * @param dimensions Predefined dimensions (length, width, height) of the obstacle.
     */
    public Obstacle(String id, Integer xPos, Integer yPos, Integer thresholdDistance, Integer centrelineDistance,
                    Integer runwayDistance, Airfield.Dimensions dimensions){

        super(xPos, yPos, id);

        this.thresholdDistance = thresholdDistance;
        this.centrelineDistance = centrelineDistance;
        this.runwayDistance = runwayDistance;

        this.length = dimensions.getLength();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }

    public Obstacle(){
        super(0,0,"DefaultID");

        this.thresholdDistance = 0;
        this.centrelineDistance = 0;
        this.runwayDistance = 0;

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

    public Integer getThresholdDistance() {
        return thresholdDistance;
    }

    public void setThresholdDistance(Integer thresholdDistance) {
        this.thresholdDistance = thresholdDistance;
    }

    public Integer getCentrelineDistance() {
        return centrelineDistance;
    }

    public void setCentrelineDistance(Integer centrelineDistance) {
        this.centrelineDistance = centrelineDistance;
    }

    public Integer getRunwayDistance() {
        return runwayDistance;
    }

    public void setRunwayDistance(Integer runwayDistance) {
        this.runwayDistance = runwayDistance;
    }

    // Turns obstacle sideways (length of the object perpendicular to the length of the runway)
    public void sideways(){
        Double temp = length;
        length = width;
        width = temp;

        // changes to xPos and yPos as well
    }
}
