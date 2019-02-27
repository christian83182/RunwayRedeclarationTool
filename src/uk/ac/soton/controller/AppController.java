package uk.ac.soton.controller;


import uk.ac.soton.common.*;
import uk.ac.soton.view.AppView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private List<Obstacle> predefinedObstacles = new ArrayList<>();

    public void addAllPredefinedObstacles(List<Obstacle> obstacles) { predefinedObstacles.addAll(obstacles); }

    public List<Obstacle> getPredifinedObstacles() { return predefinedObstacles; }

    public void addPredefinedObstacle(Obstacle obstacle) { predefinedObstacles.add(obstacle); }

    public void removePredefinedObstacle(Obstacle obstacle) { predefinedObstacles.remove(obstacle); }

    //public void update(){  }

    public ArrayList<Runway> getRunways(){ return airfield.getRunways(); }

    //public Runway getRunway(){ }

    public void addRunway(Runway runway){ airfield.addRunway(runway); }

    public void removeRunway(Runway runway){ airfield.removeRunway(runway); }

    public Obstacle getObstacle(Runway runway){ return runway.getObstacle(); }

    public void setObstacle(Runway runway, Obstacle obstacle) { runway.setObstacle(obstacle); }

    public void removeObstacle(Runway runway){ runway.clearObstacle(); }

    //public void redeclareRunway(Runway runway){}

    public Integer getTora(Runway runway) { return runway.getTora(); }

    public void setTora(Runway runway, Integer tora) { runway.setTora(tora); }

    public Integer getToda(Runway runway) { return runway.getToda(); }

    public void  setToda(Runway runway, Integer toda) { runway.setToda(toda); }

    public Integer getAsda(Runway runway) { return runway.getAsda(); }

    public void setAsda(Runway runway, Integer asda) { runway.setAsda(asda); }

    public Integer getLda(Runway runway) { return runway.getLda(); }

    public void setLda(Runway runway, Integer lda) { runway.setLda(lda); }

    public Integer getResa(Runway runway) { return runway.getResa(); }

    public void setResa(Runway runway, Integer resa) { runway.setResa(resa); }

    public Integer getTocs(Runway runway) { return runway.getTocs(); }

    public void setTocs(Runway runway, Integer tocs) { runway.setTocs(tocs); }

    public Integer getAls(Runway runway) { return runway.getAls(); }

    public void setAls(Runway runway, Integer als) { runway.setAls(als); }

    public Integer getRunwayLength(Runway runway) { return runway.getLength(); }

    public void setRunwayLength(Runway runway, Integer length) { runway.setLength(length); }

    public Integer getRunwayWidth(Runway runway) { return runway.getWidth(); }

    public void setRunwayWidth(Runway runway, Integer width) { runway.setWidth(width); }

    public String getRunwayName(Runway runway) { return runway.getName(); }

    public void setRunwayName(Runway runway, String name) { runway.setName(name); }

    public Boolean getActive(Runway runway) { return runway.getActive(); }

    public void setActive(Runway runway, Boolean state) { runway.setActive(state); }

    public void toggleActive(Runway runway) { runway.setActive(!runway.getActive()); }

    public String getRunwayStatus(Runway runway) { return runway.getStatus(); }

    public void setRunwayStatus(Runway runway, String status) { runway.setStatus(status);  }

    public Integer getClearway(Runway runway) { return runway.getClearway(); }

    public void setClearway(Runway runway, Integer clearway) { runway.setClearway(clearway);}

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

    public Integer getObstacleDistFromThreshold(Runway runway) { return runway.getObstacle().getDistFromThreshold(); }

    public void setObstacleDistFromThreshold(Runway runway, Integer distance){ runway.getObstacle().setDistFromThreshold(distance);}

    public void changeObjectPosition(Obstacle obstacle, Integer xPos, Integer yPos,
                                     Integer distThreshold, Integer distCentreline, Integer distRunway){
        obstacle.setxPos(xPos);
        obstacle.setyPos(yPos);
        obstacle.setDistFromThreshold(distThreshold);
        obstacle.setDistFromCentreline(distCentreline);
        obstacle.setDistFromRunway(distRunway);
    }


}
