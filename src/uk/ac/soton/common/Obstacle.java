package uk.ac.soton.common;

//A child of the PositionalObject class which describes a 3D Box representing an obstacle on the runway.
public class Obstacle extends PositionalObject {

    private Double height = 0.0;
    private Double length = 0.0;
    private Double width = 0.0;

    private Integer startDistance = 0;
    private Integer centrelineDistance = 0;

    /**
     * Constructor for a physical obstacle to be placed on a runway.
     * @param id Identifier of the obstacle.
     * @param xPos X position of the top left corner of the obstacle.
     * @param yPos Y position of the top left corner of the obstacle.
     * @param startDistance Distance from start of the physical runway from the side of the logical runway with a smaller bearing.
     * @param centrelineDistance Distance from the centreline of the runway.
     * @param dimensions Predefined dimensions (length, width, height) of the obstacle.
     */
    public Obstacle(String id, Integer xPos, Integer yPos, Integer startDistance, Integer centrelineDistance,
                    Airfield.Dimensions dimensions){

        super(xPos, yPos, id);

        this.centrelineDistance = centrelineDistance;
        this.startDistance = startDistance;
        this.length = dimensions.getLength();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }


    /**
     * Constructor for a physical obstacle to be placed on a runway.
     * @param startDistance Distance from start of the physical runway from the side of the logical runway with a smaller bearing
     *                      (if obstacle is positioned before the start then this value is negative).
     * @param centrelineDistance Distance from the centreline of the runway (north of the centreline is positive, south negative).
     * @param dimensions Predefined dimensions (length, width, height) of the obstacle.
     */
    public Obstacle(Integer startDistance, Integer centrelineDistance, Airfield.Dimensions dimensions){
        super(0,0, "defaultId");
        this.startDistance = startDistance;
        this.centrelineDistance = centrelineDistance;
        this.length = dimensions.getLength();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }

    public Obstacle(String id, Integer startDistance, Integer centrelineDistance, Airfield.Dimensions dimensions){
        super(0,0, id);
        this.startDistance = startDistance;
        this.centrelineDistance = centrelineDistance;
        this.length = dimensions.getLength();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }

    public Obstacle(){
        super(0,0,"DefaultID");

        this.centrelineDistance = 0;

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

    public Integer getCentrelineDistance() {
        return centrelineDistance;
    }

    public void setCentrelineDistance(Integer centrelineDistance) {
        this.centrelineDistance = centrelineDistance;
    }

    public void setRunwayStartDistance(Integer startDistance) { this.startDistance = startDistance; }

    public Integer getStartDistance() {return  startDistance; }

    /**
     * Turns obstacle sideways (length of the object perpendicular to the length of the runway).
     */
    public void sideways(){
        Double temp = length;
        length = width;
        width = temp;
    }
}
