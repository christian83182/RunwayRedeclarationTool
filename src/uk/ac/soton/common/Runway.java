package uk.ac.soton.common;

//An child of the PositionalObject class which represents an instance of a Runway with a certain length, width, and name.
public class Runway extends PositionalObject{

    private Integer length;
    private Integer width;
    private String name;

    Runway(String id, Integer xPos, Integer yPos, Integer length, Integer width){
        super(xPos, yPos, id);
        this.length = length;
        this.width = width;
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
}
