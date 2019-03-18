package uk.ac.soton.controller;

import org.xml.sax.SAXException;
import uk.ac.soton.common.*;
import uk.ac.soton.view.AppView;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AppController implements ViewController {

    //The controller's instance of the application's view.
    private AppView appView;
    //The model which the controller would interact with
    private Airfield airfield;

    public AppController(AppView appView){
        this.appView = appView;
        this.airfield = new Airfield();
    }

    public void testRunways(){

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
        Set<String> logicalRunways = new TreeSet<>();
        for(LogicalRunway runway : airfield.getAllLogicalRunways()){
            logicalRunways.add(runway.getName());
        }
        return logicalRunways;
        //return new TreeSet<String>(airfield.getAllLogicalRunways().stream().map(r
        //        -> r.getName()).collect(Collectors.toList()));
    }

    @Override
    public Point getRunwayPos(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        Point pos = new Point(r.getxPos(), r.getyPos());

        double angle = Integer.parseInt(runwayId.substring(0,2))*10;

        if(angle > 180 && angle != 0 && angle != 270){

            double trigoAngle = Math.toRadians(360.0 - angle);
            int xOffset = (int) (Math.abs(Math.sin(trigoAngle)*r.getLength()));
            int yOffset = (int) (Math.sqrt(r.getLength().doubleValue()*r.getLength().doubleValue() - xOffset*xOffset));

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
        return Integer.parseInt(lr.getName().substring(0,2))*10;
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

    @Override
    public Integer getTORAOffset(String runwayId) {

        Number original = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getTora().getOriginalValue();
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getTora().getRedeclaredValue();
        LogicalRunway lr = airfield.getRunway(runwayId).getObjectCloserToThisThreshold();

        if(redeclared == null){
            return 0;
        }
        else{
            if(runwayId.equals(lr.getName())){
                return original.intValue() - redeclared.intValue();
            }
            return 0;
        }
    }

    @Override
    public Integer getTODAOffset(String runwayId) {

        Number original = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getToda().getOriginalValue();
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getToda().getRedeclaredValue();
        LogicalRunway lr = airfield.getRunway(runwayId).getObjectCloserToThisThreshold();

        if(redeclared == null){
            return 0;
        }
        else{
            if(runwayId.equals(lr.getName())){
                return original.intValue() - redeclared.intValue();
            }
            return 0;
        }
    }

    @Override
    public Integer getASDAOffset(String runwayId) {

        Number original = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getAsda().getOriginalValue();
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getAsda().getRedeclaredValue();
        LogicalRunway lr = airfield.getRunway(runwayId).getObjectCloserToThisThreshold();

        if(redeclared == null){
            return 0;
        }
        else{
            if(runwayId.equals(lr.getName())){
                return original.intValue() - redeclared.intValue();
            }
            return 0;
        }
    }

    @Override
    public Integer getLDAOffset(String runwayId) {

        Number original = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getLda().getOriginalValue();
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getLda().getRedeclaredValue();
        LogicalRunway lr = airfield.getRunway(runwayId).getObjectCloserToThisThreshold();

        if(redeclared == null){
            return getRunwayThreshold(runwayId);
        }
        else{
            if(runwayId.equals(lr.getName())){
                return original.intValue() - redeclared.intValue();
            }
            return  getRunwayThreshold(runwayId);
        }
    }

    @Override
    public Set<String> getPredefinedObstacleIds() {
        return new TreeSet<>(airfield.getPredefinedObstacles().keySet());
    }

    @Override
    public Double getPredefinedObstacleWidth(String obstacleId) {
        return airfield.getPredefinedObstacles().get(obstacleId).getWidth();
    }

    @Override
    public Double getPredefinedObstacleHeight(String obstacleId) {
        return airfield.getPredefinedObstacles().get(obstacleId).getHeight();
    }

    @Override
    public Double getPredefinedObstacleLength(String obstacleId) {
        return airfield.getPredefinedObstacles().get(obstacleId).getLength();
    }

    @Override
    public void addObstacleToList(String id, Double length, Double width, Double height) {
        airfield.defineNewObstacle(id, length, width, height);
    }

    @Override
    public void deleteObstacleFromList(String obstacleId) {
        airfield.removePredefinedObstacle(obstacleId);
    }

    private List<Runway> getPhysicalRunways(){
        return airfield.getRunways();
    }

    @Override
    public void addObstacleToRunway(String runwayId, String obstacleId, Integer distanceFromCenterline, Integer distanceFromEdge) {
        Runway runway = airfield.getRunway(runwayId);
        Obstacle obstacle = new Obstacle(distanceFromEdge, distanceFromCenterline, getPredifinedObstacles().get(obstacleId));
        obstacle.setId(obstacleId);
        runway.placeObstacle(obstacle, runwayId);
        redeclareRunway(runwayId);
    }

    @Override
    public void removeObstacleFromRunway(String runwayId) {
        for(Runway runway: getPhysicalRunways()){
            if(runway.getLogicalRunways()[0].getName().equals(runwayId)){
                runway.clearObstacle();
                break;
            }else if(runway.getLogicalRunways()[1].getName().equals(runwayId)) {
                runway.clearObstacle();
                break;
            }
        }
    }

    @Override
    public String getRunwayObstacle(String runwayId) {
        for(Runway runway: getPhysicalRunways()){
            if(runway.getLogicalRunways()[0].getName().equals(runwayId)){
                if(runway.getObstacle() == null){
                    return "";
                }
                return runway.getObstacle().getId();
            }else if(runway.getLogicalRunways()[1].getName().equals(runwayId)) {
                if(runway.getObstacle() == null){
                    return "";
                }
                return runway.getObstacle().getId();
            }
        }
        return "";
    }

    @Override
    public Integer getDistanceFromCenterline(String runwayId) {
        LogicalRunway runway = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return runway.getObjectDistanceFromCentreline().intValue();
    }

    @Override
    public Integer getDistanceFromThreshold(String runwayId) {
        LogicalRunway runway = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return runway.getGetObjectDistanceFromStart().intValue();
    }

    @Override
    public void exportAirfieldConfiguration(String absolutePath) {
        XMLExporter exporter = new XMLExporter();
        exporter.saveAirfieldInfo(airfield, absolutePath);
    }

    @Override
    public void importAirfieldConfiguration(String path) {
        XMLImporter importer = new XMLImporter();
        try {
            this.airfield = importer.importAirfieldInfo(path);
            System.out.println("!");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ImporterExceptions importerExceptions) {
            importerExceptions.printStackTrace();
        }
    }





    public Runway getRunway(String name){ return airfield.getRunway(name); }

    public void addRunway(String id, Integer xPos, Integer yPos, Integer length, Integer width, Integer stripWidth, Integer stripEnd) {
        airfield.addRunway(new Runway(id, xPos, yPos, length, width, stripWidth, stripEnd));
    }

    public Map<String,Airfield.Dimensions> getPredifinedObstacles() { return airfield.getPredefinedObstacles(); }

    public void addPredefinedObstacle(String type, Double length, Double width, Double height) {
        airfield.defineNewObstacle(type, length, width, height);
    }

    public void removePredefinedObstacle(String type) { airfield.removePredefinedObstacle(type); }

    public void editPredefinedObstacle(String type, Double newLength, Double newWidth, Double newHeight){
        airfield.redefineObstacle(type, newLength, newWidth, newHeight);
    }

    public void placeObstacle(String runwayId, String type, Integer centrelineDistance, Integer startDistance) {
        Obstacle obstacle = new Obstacle(startDistance, centrelineDistance, getPredifinedObstacles().get(type));
        Runway runway = airfield.getRunway(runwayId);
        runway.placeObstacle(obstacle, runwayId);
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
