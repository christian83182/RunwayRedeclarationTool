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

        // Test runways
        
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        r1.setLogicalRunways(lr11,lr12);

        Runway r2 = new Runway("09R/27L",-600,450,1800,80,400,60);
        LogicalRunway lr21 = new LogicalRunway("09R",r2.getLength(),250,
                new Dimension(0,0), new Dimension(60,r2.getWidth()));
        LogicalRunway lr22 = new LogicalRunway("27L",r2.getLength(),250,
                new Dimension(0,0), new Dimension(60,r2.getWidth()));
        r2.setLogicalRunways(lr21,lr22);

        Runway r3 = new Runway("13/31",-800,-650,2400,80,400,60);
        LogicalRunway lr31 = new LogicalRunway("13",r3.getLength(),0,
                new Dimension(350,220), new Dimension(60,r3.getWidth()));
        LogicalRunway lr32 = new LogicalRunway("31",r3.getLength(),50,
                new Dimension(350,220), new Dimension(60,r3.getWidth()));
        r3.setLogicalRunways(lr31, lr32);

        Runway r4 = new Runway("00/18",-1200,-900,1800,80,400,60);
        LogicalRunway lr41 = new LogicalRunway("00",r4.getLength(),0,
                new Dimension(350,220), new Dimension(60,r4.getWidth()));
        LogicalRunway lr42 = new LogicalRunway("18",r4.getLength(),50,
                new Dimension(350,220), new Dimension(60,r4.getWidth()));
        r4.setLogicalRunways(lr41, lr42);

        Runway r5 = new Runway("04/22",-550,-730,1400,80,400,60);
        LogicalRunway lr51 = new LogicalRunway("04",r5.getLength(),0,
                new Dimension(350,220), new Dimension(60,r5.getWidth()));
        LogicalRunway lr52 = new LogicalRunway("22",r5.getLength(),50,
                new Dimension(350,220), new Dimension(60,r5.getWidth()));
        r5.setLogicalRunways(lr51, lr52);

        airfield.addRunway(r1);
        airfield.addRunway(r2);
        airfield.addRunway(r3);
        airfield.addRunway(r4);
        airfield.addRunway(r5);

    }

    @Override
    public Set<String> getRunways() {

        return new HashSet<String>(airfield.getAllLogicalRunways().stream().map(r
                -> r.getName()).collect(Collectors.toList()));
    }

    @Override
    public Point getRunwayPos(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        Point pos = new Point(r.getxPos(), r.getyPos());

        double angle = Integer.parseInt(runwayId.substring(0,2))*10;

        if(angle > 180 && angle != 0 && angle != 270){

            double trigoAngle = Math.toRadians(360.0 - angle);
            System.out.println(trigoAngle);
            int xOffset = (int) (Math.abs(Math.sin(trigoAngle)*r.getLength()));
            System.out.println(xOffset);
            int yOffset = (int) (Math.sqrt(r.getLength().doubleValue()*r.getLength().doubleValue() - xOffset*xOffset));
            System.out.println(yOffset);

            if(angle < 270){
                pos = new Point(r.getxPos() + xOffset, r.getyPos() - yOffset);
            }
            else{
                pos = new Point(r.getxPos() + xOffset, r.getyPos() + yOffset);
            }
        }
        if(angle == 0){
            pos = new Point(r.getxPos(), r.getyPos() + r.getLength());
        }
        if(angle == 270){
            pos = new Point(r.getxPos() + r.getLength(), r.getyPos());
        }

        return pos;
    }

    @Override
    public Dimension getRunwayDim(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return new Dimension(r.getLength(), r.getWidth());
    }

    @Override
    public Integer getBearing(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        Integer angle = Integer.parseInt(lr.getName().substring(0,2))*10;

        return angle;
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





    public Map<String,Airfield.Dimensions> getPredifinedObstacles() { return airfield.getPredefinedObstacles(); }

    public void addPredefinedObstacle(String type, Double length, Double width, Double height) {
        airfield.defineNewObstacle(type, length, width, height);
    }

    public void removePredefinedObstacle(String type) { airfield.removePredefinedObstacle(type); }

    public void editPredefinedObstacle(String type, Double newLength, Double newWidth, Double newHeight){
        airfield.redefineObstacle(type, newLength, newWidth, newHeight);
    }

    public ArrayList<Runway> getRunwayObjects(){ return airfield.getRunways(); }

    public Runway getRunway(String name){ return airfield.getRunway(name); }

    public void addRunway(String id, Integer xPos, Integer yPos, Integer length, Integer width, Integer stripWidth, Integer stripEnd) {
        airfield.addRunway(new Runway(id, xPos, yPos, length, width, stripWidth, stripEnd));
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

    public Dimension getClearway(LogicalRunway logicalRunway) { return logicalRunway.getClearway(); }

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
