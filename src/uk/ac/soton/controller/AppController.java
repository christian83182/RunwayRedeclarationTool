package uk.ac.soton.controller;


import uk.ac.soton.common.*;
import uk.ac.soton.view.AppView;

import java.util.ArrayList;
import java.util.Map;

public class AppController {

    //The controller's instance of the application's view.
    private AppView appView;
    //The configurer used to change the configuration of the model.
    private Configurer appConfigurer;
    //The model which the controller would interact with
    private Airfield airfield;

    public AppController(AppView appView){
        this.appView = appView;
        this.appConfigurer = new Configurer();
        this.airfield = new Airfield();

        appView.init();
    }

    //public void update(){  }

    public Map<String,Airfield.Dimensions> getPredifinedObstacles() { return airfield.getPredefinedObstacles(); }

    public void addPredefinedObstacle(String type, Double length, Double width, Double height) {
        airfield.defineNewObstacle(type, length, width, height);
    }

    public void removePredefinedObstacle(String type) { airfield.removePredefinedObstacle(type); }

    public void editPredefinedObstacle(String type, Double newLength, Double newWidth, Double newHeight){
        airfield.redefineObstacle(type, newLength, newWidth, newHeight);
    }

    public ArrayList<Runway> getRunways(){ return airfield.getRunways(); }

    public Runway getRunway(String name){ return airfield.getRunway(name); }

    public void addRunway(String id, Integer xPos, Integer yPos, Integer length, Integer width) {
        airfield.addRunway(new Runway(id, xPos, yPos, length, width));
    }

    public void removeRunway(Runway runway){ airfield.removeRunway(runway); }

    public Obstacle getObstacle(LogicalRunway logicalRunway){ return logicalRunway.getObstacle(); }

    //TODO
    /*public void placeObstacle(Runway runway, String type, Integer thresholdDistance, Integer centrelineDistance, Integer runwayDistance) {

        runway.placeObstacle(type, thresholdDistance, centrelineDistance, runwayDistance, getPredifinedObstacles().get(type));
    }*/

    public void clearObstacle(LogicalRunway logicalRunway){ logicalRunway.clearObstacle(); }

    //public void redeclareRunway(Runway runway){}

    public Number getTora(LogicalRunway logicalRunway) { return logicalRunway.getTora().getCurrentValue(); }

    //public void setTora(Runway runway, Integer tora) { runway.setTora(tora); }

    public Number getToda(LogicalRunway logicalRunway) { return logicalRunway.getToda().getCurrentValue(); }

    //public void  setToda(Runway runway, Integer toda) { runway.setToda(toda); }

    public Number getAsda(LogicalRunway logicalRunway) { return logicalRunway.getAsda().getCurrentValue(); }

    //public void setAsda(Runway runway, Integer asda) { runway.setAsda(asda); }

    public Number getLda(LogicalRunway logicalRunway) { return logicalRunway.getLda().getCurrentValue(); }

    //public void setLda(Runway runway, Integer lda) { runway.setLda(lda); }

    public Integer getResa(Runway runway) { return runway.getResa(); }

    //public void setResa(Runway runway, Integer resa) { runway.setResa(resa); }

    //public Integer getTocs(Runway runway) { return runway.getTocs(); }

    //public void setTocs(Runway runway, Integer tocs) { runway.setTocs(tocs); }

    public Integer getAls(Runway runway) { return runway.getAls(); }

    public void setAls(Runway runway, Integer als) { runway.setAls(als); }

    public Integer getRunwayLength(Runway runway) { return runway.getLength(); }

    public void setRunwayLength(Runway runway, Integer length) { runway.setLength(length); }

    public Integer getRunwayWidth(Runway runway) { return runway.getWidth(); }

    public void setRunwayWidth(Runway runway, Integer width) { runway.setWidth(width); }

    public String getRunwayId(Runway runway) { return runway.getId(); }

    public void setRunwayId(Runway runway, String id) { runway.setId(id); }

    public Boolean isActive(Runway runway) { return runway.isActive(); }

    public void setActive(Runway runway, Boolean state) { runway.setActive(state); }

    public void toggleActive(Runway runway) { runway.setActive(!runway.isActive()); }

    public String getRunwayStatus(Runway runway) { return runway.getStatus(); }

    public void setRunwayStatus(Runway runway, String status) { runway.setStatus(status);  }

    public Number getClearway(LogicalRunway logicalRunway) { return logicalRunway.getClearway(); }

    /*public void setClearway(LogicalRunway logicalRunway, Integer clearway) { logicalRunway.setClearway(clearway); }

    public Integer getStopway(Runway runway) { return runway.getStopway(); }

    public void setStopway(Runway runway, Integer stopway) { runway.setStopway(stopway); }

    public Integer getThreshold(Runway runway) { return runway.getThreshold(); }

    public void setThreshold(Runway runway, Integer threshold) { runway.setThreshold(threshold); }

    public Double getObstacleHeight(Runway runway) { return runway.getObstacle().getHeight(); }

    public void setObstacleHeight(Runway runway, Double height) { runway.getObstacle().setHeight(height); }

    public Double getObstacleLength(Runway runway) { return runway.getObstacle().getLength(); }

    public void setObstaleLength(Runway runway, Double length) { runway.getObstacle().setLength(length); }

    public Double getObstacleWidth(Runway runway) { return runway.getObstacle().getWidth(); }

    public void setObstacleWidth(Runway runway, Double width) { runway.getObstacle().setWidth(width); }

    public Integer getObstacleDistFromThreshold(Runway runway) { return runway.getObstacle().getThresholdDistance(); }

    public void setObstacleDistFromThreshold(Runway runway, Integer distance){ runway.getObstacle().setThresholdDistance(distance);}

    public Integer getObjectDistFromCentreline(Runway runway) { return runway.getObstacle().getCentrelineDistance(); }

    public void setObstacleDistFromCentreline(Runway runway, Integer distance) { runway.getObstacle().setCentrelineDistance(distance); }

    public Integer getObstacleDistFromRunway(Runway runway) { return runway.getObstacle().getRunwayDistance(); }

    public void setObstacleDistFromRunway(Runway runway, Integer distance) { runway.getObstacle().setRunwayDistance(distance);}

    public void changeObjectPosition(Obstacle obstacle, Integer xPos, Integer yPos,
                                     Integer distThreshold, Integer distCentreline, Integer distRunway){
        obstacle.setxPos(xPos);
        obstacle.setyPos(yPos);
        obstacle.setThresholdDistance(distThreshold);
        obstacle.setCentrelineDistance(distCentreline);
        obstacle.setRunwayDistance(distRunway);
    }*/


}
