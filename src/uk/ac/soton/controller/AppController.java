package uk.ac.soton.controller;

import org.xml.sax.SAXException;
import uk.ac.soton.common.*;
import uk.ac.soton.view.AppView;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AppController implements ViewController {

    //The controller's instance of the application's view.
    private AppView appView;

    //The model which the controller would interact with
    public Airfield airfield;

    private BufferedImage backgroundImage;
    private Point bgImageOffset;
    private Double bgImageScale;
    private Double bgImageRotation;

    public AppController(AppView appView){
        this.appView = appView;
        this.airfield = new Airfield();
        this.backgroundImage = null;
        this.bgImageOffset = new Point(0,0);
        this.bgImageScale = 1.0;
        this.bgImageRotation = 0.0;
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

    public synchronized Airfield getAirfield() {
        return airfield;
    }

    public synchronized void setAirfield(Airfield airfield) {
        this.airfield = airfield;
    }

    @Override
    public synchronized Set<String> getRunways() {
        Set<String> logicalRunways = new TreeSet<>();
        for(LogicalRunway runway : airfield.getAllLogicalRunways()){
            logicalRunways.add(runway.getName());
        }
        return logicalRunways;
    }

    @Override
    public synchronized Point getRunwayPos(String runwayId) {

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
    public synchronized Dimension getRunwayDim(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return new Dimension(r.getLength(), r.getWidth());
    }

    @Override
    public synchronized Integer getBearing(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return Integer.parseInt(lr.getName().substring(0,2))*10;
    }

    @Override
    public synchronized Dimension getStopwayDim(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getStopway();
    }

    @Override
    public synchronized Dimension getClearwayDim(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getClearway();
    }

    @Override
    public synchronized Integer getStripWidthFromCenterline(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return r.getStripWidth() / 2;
    }

    @Override
    public synchronized Integer getStripEndSize(String runwayId) {

        Runway r = airfield.getRunway(runwayId);
        return r.getStripEnd();
    }

    @Override
    public synchronized Integer getRunwayTORA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getTora().getCurrentValue().intValue();
    }

    @Override
    public synchronized Integer getRunwayTODA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getToda().getCurrentValue().intValue();
    }

    @Override
    public synchronized Integer getRunwayASDA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getAsda().getCurrentValue().intValue();
    }

    @Override
    public synchronized Integer getRunwayLDA(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getLda().getCurrentValue().intValue();
    }

    @Override
    public synchronized Integer getRunwayThreshold(String runwayId) {

        LogicalRunway lr = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return lr.getThreshold().intValue();
    }

    @Override
    public synchronized Integer getTORAOffset(String runwayId) {

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
    public synchronized Integer getTODAOffset(String runwayId) {

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
    public synchronized Integer getASDAOffset(String runwayId) {

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
    public synchronized Integer getLDAOffset(String runwayId) {

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
    public String getTORABreakdown(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getTora().getBreakdown();
    }

    @Override
    public String getTODABreakdown(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getToda().getBreakdown();
    }

    @Override
    public String getASDABreakdown(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getAsda().getBreakdown();
    }

    @Override
    public String getLDABreakdown(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getLda().getBreakdown();
    }

    @Override
    public Integer getTORAOriginal(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getTora().getOriginalValue().intValue();
    }

    @Override
    public Integer getTORARedeclared(String runwayId) {
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getTora().getRedeclaredValue();

        if(redeclared != null){
            return redeclared.intValue();
        }
        else{
            return null;
        }
    }

    @Override
    public Integer getTODAOriginal(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getToda().getOriginalValue().intValue();
    }

    @Override
    public Integer getTODARedeclared(String runwayId) {
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getToda().getRedeclaredValue();

        if(redeclared != null){
            return redeclared.intValue();
        }
        else{
            return null;
        }
    }

    @Override
    public Integer getASDAOriginal(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getAsda().getOriginalValue().intValue();
    }

    @Override
    public Integer getASDARedeclared(String runwayId) {
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getAsda().getRedeclaredValue();

        if(redeclared != null){
            return redeclared.intValue();
        }
        else{
            return null;
        }
    }

    @Override
    public Integer getLDAOriginal(String runwayId) {
        return airfield.getRunway(runwayId).getLogicalRunway(runwayId).getLda().getOriginalValue().intValue();
    }

    @Override
    public Integer getLDARedeclared(String runwayId) {
        Number redeclared = airfield.getRunway(runwayId).getLogicalRunway(runwayId).getLda().getRedeclaredValue();

        if(redeclared != null){
            return redeclared.intValue();
        }
        else{
            return null;
        }
    }

    @Override
    public synchronized Set<String> getPredefinedObstacleIds() {
        return new TreeSet<>(airfield.getPredefinedObstacles().keySet());
    }

    @Override
    public synchronized Double getPredefinedObstacleWidth(String obstacleId) {
        return airfield.getPredefinedObstacles().get(obstacleId).getWidth();
    }

    @Override
    public synchronized Double getPredefinedObstacleHeight(String obstacleId) {
        return airfield.getPredefinedObstacles().get(obstacleId).getHeight();
    }

    @Override
    public synchronized Double getPredefinedObstacleLength(String obstacleId) {
        return airfield.getPredefinedObstacles().get(obstacleId).getLength();
    }

    @Override
    public synchronized void addObstacleToList(String id, Double length, Double width, Double height) {
        airfield.defineNewObstacle(id, length, width, height);
    }

    @Override
    public synchronized void deleteObstacleFromList(String obstacleId) {
        airfield.removePredefinedObstacle(obstacleId);
    }

    private synchronized List<Runway> getPhysicalRunways(){
        return airfield.getRunways();
    }

    @Override
    public synchronized void addObstacleToRunway(String runwayId, String obstacleId, Integer distanceFromCenterline, Integer distanceFromEdge) {
        Runway runway = airfield.getRunway(runwayId);
        Obstacle obstacle = new Obstacle(obstacleId, distanceFromEdge, distanceFromCenterline, airfield.getPredefinedObstacles().get(obstacleId));
        runway.placeObstacle(obstacle, runwayId);
        redeclareRunway(runwayId);
    }

    @Override
    public synchronized void removeObstacleFromRunway(String runwayId) {
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
    public synchronized String getRunwayObstacle(String runwayId) {
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
    public synchronized Integer getDistanceFromCenterline(String runwayId) {
        LogicalRunway runway = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return runway.getObjectDistanceFromCentreline().intValue();
    }

    @Override
    public synchronized Integer getDistanceFromThreshold(String runwayId) {
        LogicalRunway runway = airfield.getRunway(runwayId).getLogicalRunway(runwayId);
        return runway.getGetObjectDistanceFromStart().intValue();
    }

    @Override
    public synchronized void exportAirfieldConfiguration(String absolutePath) throws TransformerException, ParserConfigurationException {
        Exporter exporter = new Exporter(this);
        exporter.exportConfiguration(absolutePath);
    }

    @Override
    public synchronized void importAirfieldConfiguration(String path) throws ImporterException, ParserConfigurationException, SAXException, IOException {
        XMLImporter importer = new XMLImporter();
        this.airfield = importer.importAirfieldInfo(path);
    }

    @Override
    public synchronized LogicalRunway getLogicalRunwayCloserToObstacle(String runwayId) {
        Runway runway = airfield.getRunway(runwayId);
        if (!getRunwayObstacle(runwayId).equals("")){
            return runway.getObjectCloserToThisThreshold();
        }else{
            return null;
        }
    }

    @Override
    public synchronized String getAirfieldName() {
        return "Heathrow";
    }

    @Override
    public synchronized String getSiblingLogicalRunway(String runwayId) {
        Runway runway = airfield.getRunway(runwayId);
        if(runway.getLogicalRunways()[0].getName().equals(runwayId)){
            return runway.getLogicalRunways()[1].getName();
        }
        return runway.getLogicalRunways()[0].getName();
    }

    @Override
    public synchronized Integer getBlastingDistance() {
        return Airfield.getBlastProtection();
    }

    @Override
    public synchronized Integer getRESADistance(String runwayId) {
        return airfield.getRunway(runwayId).getResa();
    }

    @Override
    public synchronized Integer getALS(String runwayId) {
        return airfield.getRunway(runwayId).getAls();
    }

    @Override
    public synchronized boolean isRedeclared(String runwayId) {
        return airfield.getRunway(runwayId).isRedeclared();
    }

    @Override
    public synchronized void setRESADistance(String runwayId, Integer RESAvalue) {
        airfield.getRunway(runwayId).setResa(RESAvalue);
    }

    @Override
    public synchronized void setBlastingDistance(Integer blastingDistance) {
        Airfield.setBlastProtection(blastingDistance);

        ArrayList<Runway> runways = airfield.getRunways();

        for(int i = 0; i < runways.size(); i++){
            runways.get(i).recalculateParameters();
        }
    }

    @Override
    public synchronized BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    public void setBackgroundImage(BufferedImage img){
        this.backgroundImage = img;
    }

    @Override
    public synchronized Point getBackgroundImageOffset() {
        return bgImageOffset;
    }

    @Override
    public void setBackgroundImageOffset(Point offset) {
        this.bgImageOffset = offset;
    }

    @Override
    public synchronized Double getBackgroundImageScale() {
        return bgImageScale;
    }

    @Override
    public void setBackgroundImageScale(Double scale) {
        this.bgImageScale = scale;
    }

    @Override
    public Double getBackgroundRotation() {
        return bgImageRotation;
    }

    @Override
    public void setBackgroundRotation(Double rotation) {
        this.bgImageRotation = rotation;
    }



    //  --------- Non-Interface Methods ----------

    private synchronized void redeclareRunway(String runwayId){
        airfield.getRunway(runwayId).recalculateParameters();
    }

    private synchronized Map<String,Airfield.Dimensions> getPredefinedObstacles() { return airfield.getPredefinedObstacles(); }

    public Runway getRunway(String name){ return airfield.getRunway(name); }

    public void addRunway(String id, Integer xPos, Integer yPos, Integer length, Integer width, Integer stripWidth, Integer stripEnd) {
        airfield.addRunway(new Runway(id, xPos, yPos, length, width, stripWidth, stripEnd));
    }

    public void addPredefinedObstacle(String type, Double length, Double width, Double height) {
        airfield.defineNewObstacle(type, length, width, height);
    }

    public void removePredefinedObstacle(String type) { airfield.removePredefinedObstacle(type); }

    public void editPredefinedObstacle(String type, Double newLength, Double newWidth, Double newHeight){
        airfield.redefineObstacle(type, newLength, newWidth, newHeight);
    }

    public void placeObstacle(String runwayId, String type, Integer centrelineDistance, Integer startDistance) {
        Obstacle obstacle = new Obstacle(startDistance, centrelineDistance, airfield.getPredefinedObstacles().get(type));
        Runway runway = airfield.getRunway(runwayId);
        runway.placeObstacle(obstacle, runwayId);
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

    public void setBlastDistance(Runway runway, Integer blastDistance){ Airfield.setBlastProtection(blastDistance);}

    public Integer getBlastDistance(Runway runway) { return Airfield.getBlastProtection(); }
}
