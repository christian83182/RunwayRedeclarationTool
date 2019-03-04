package uk.ac.soton.controller;


import com.sun.org.apache.xpath.internal.operations.Bool;
import uk.ac.soton.common.*;
import uk.ac.soton.view.AppView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AppController implements ViewController {

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

        // Uncomment to view test

        /*
        airfield.addRunway(new Runway("09L/27R",-800,-300,1800,80,400));
        Runway r1 = airfield.getRunway("09L");
        LogicalRunway lr1 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220),new Dimension(60,r1.getWidth()));
        r1.getLogicalRunways()[0] = lr1;

        airfield.addRunway(new Runway("09R/27L",-400,350,1800,80,400));
        Runway r2 = airfield.getRunway("09R");
        LogicalRunway lr2 = new LogicalRunway("09R",r2.getLength(),250,
                new Dimension(350,220),new Dimension(60,r2.getWidth()));
        r2.getLogicalRunways()[0] = lr2;

        airfield.addRunway(new Runway("13/23",-600,-750,2400,80,400));
        Runway r3 = airfield.getRunway("13");
        LogicalRunway lr3 = new LogicalRunway("13",r3.getLength(),0,
                new Dimension(350,220),new Dimension(60,r3.getWidth()));
        r3.getLogicalRunways()[0] = lr3;
        */

    }

    @Override
    public Set<String> getRunways() {

        return new HashSet<String>(airfield.getAllLogicalRunways().stream().map(r
                -> r.getName()).collect(Collectors.toList()));
    }

    @Override
    public Point getRunwayPos(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return new Point(r.getxPos(), r.getyPos());
    }

    @Override
    public Dimension getRunwayDim(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return new Dimension(r.getLength(), r.getWidth());
    }

    @Override
    public Integer getBearing(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return Integer.parseInt(r.getId().substring(0,2))*10;
    }

    @Override
    public Dimension getStopwayDim(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getStopway();
    }

    @Override
    public Dimension getClearwayDim(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getClearway();
    }

    @Override
    public Integer getStripWidthFromCenterline(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return r.getStripWidth() / 2;
    }

    @Override
    public Integer getStripEndSize(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return r.getStripEnd();
    }

    @Override
    public Integer getRunwayTORA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getTora().getCurrentValue().intValue();
    }

    @Override
    public Integer getRunwayTODA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getToda().getCurrentValue().intValue();
    }

    @Override
    public Integer getRunwayASDA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getAsda().getCurrentValue().intValue();
    }

    @Override
    public Integer getRunwayLDA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getLda().getCurrentValue().intValue();
    }

    @Override
    public Integer getRunwayThreshold(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getThreshold().intValue();
    }


    public void placeObstacle (String runwayId, Obstacle obstacle, String runwayOne, Number distanceOne, String runwayTwo, Number distanceTwo){
        airfield.getRunway(runwayId).placeObstacle(obstacle, runwayOne, distanceOne, runwayTwo, distanceTwo);
    }

    public void removeObstacle(String runwayId){
        airfield.getRunway(runwayId).clearObstacle();
    }

    public Number getObstacleHeight(String runwayId){
        Runway runway = airfield.getRunway(runwayId);
        if(runway.getObstacle() == null){
            return runway.getObstacle().getHeight();
        }
        return 0;
    }

    public Number getObstacleLength(String runwayId){
        Runway runway = airfield.getRunway(runwayId);
        if(runway.getObstacle() == null){
            return runway.getObstacle().getLength();
        }
        return 0;
    }


    public Number getObstacleWidth(String runwayId){
        Runway runway = airfield.getRunway(runwayId);
        if(runway.getObstacle() == null){
            return runway.getObstacle().getWidth();
        }
        return 0;
    }

    public Number getObjectDistFromCentreline(String runwayId){
        Runway runway = airfield.getRunway(runwayId);
        return runway.getObstacle().getCentrelineDistance();
    }

    public Number getObjectDistFromStart(String runwayId){
        Runway runway = airfield.getRunway(runwayId);
        return runway.getObstacle().getStartDistance();
    }


    public Map<String,Airfield.Dimensions> getPredifinedObstacles() { return airfield.getPredefinedObstacles(); }

    public void addPredefinedObstacle(String type, Double length, Double width, Double height) {
        airfield.defineNewObstacle(type, length, width, height);
    }

    public void removePredefinedObstacle(String type) { airfield.removePredefinedObstacle(type); }

    public void editPredefinedObstacle(String type, Double newLength, Double newWidth, Double newHeight){
        airfield.redefineObstacle(type, newLength, newWidth, newHeight);
    }

    public void placeObstacle(String runwayId, String type, Integer centrelineDistance, Integer startDistance, String runwayOne, Number distanceOne, String runwayTwo, Number distanceTwo) {

        Obstacle obstacle = new Obstacle(startDistance, centrelineDistance, getPredifinedObstacles().get(type));
        Runway runway = airfield.getRunway(runwayId);
        runway.placeObstacle(obstacle, runwayOne, distanceOne, runwayTwo, distanceTwo);
    }


    public void redeclareRunway(String runwayId){
        airfield.getRunway(runwayId).recalculateParameters();
    }

    public void setResa(Runway runway, Integer resa) { runway.setResa(resa);}

    public Integer getResa(Runway runway) { return runway.getResa(); }

    public void setAls(Runway runway, Integer als) { runway.setAls(als); }

    public void getAls(Runway runway) { runway.getAls(); }

    public Boolean isActive(Runway runway) { return runway.isActive(); }

    public void setActive(Runway runway, Boolean state) { runway.setActive(state); }

    public void toggleActive(Runway runway) { runway.setActive(!runway.isActive()); }

    public String getRunwayStatus(Runway runway) { return runway.getStatus(); }

    public void setRunwayStatus(Runway runway, String status) { runway.setStatus(status);  }

    public void setBlastDistance(Runway runway, Integer blastDistance){ runway.setBlastDistance(blastDistance);}

    public Integer getBlastDistance(Runway runway) { return runway.getBlastDistance(); }
}
