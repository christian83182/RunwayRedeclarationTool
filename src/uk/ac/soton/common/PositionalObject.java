package uk.ac.soton.common;

//An abstract class which describes any object with a 2D position and an ID.
public abstract class PositionalObject {

    private Integer xPos;
    private Integer yPos;
    private String id;

    PositionalObject(Integer xPos, Integer yPos,String id){
        this.xPos = xPos;
        this.yPos = yPos;
        this.id = id;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
