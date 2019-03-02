package uk.ac.soton.view;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrontEndModel {

    private Map<String, Point> runwayPositions;
    private Map<String, Dimension> runwayDimensions;
    private Map<String, Dimension> runwayStopway;
    private Map<String, Dimension> runwayClearway;
    private Map<String, Dimension> runwayStrip;
    // ^Width refers to distance from stat/end of the runway, height refers to distance from the centerline.
    private Map<String, Integer> runwayTORA;
    private Map<String, Integer> runwayTODA;
    private Map<String, Integer> runwayASDA;
    private Map<String, Integer> runwayLDA;
    private Map<String, Integer> runwayThreshold;

    FrontEndModel(){
        runwayDimensions = new HashMap<>();
        runwayPositions = new HashMap<>();
        runwayStopway = new HashMap<>();
        runwayClearway = new HashMap<>();
        runwayStrip = new HashMap<>();
        runwayTORA = new HashMap<>();
        runwayTODA = new HashMap<>();
        runwayASDA = new HashMap<>();
        runwayLDA = new HashMap<>();
        runwayThreshold = new HashMap<>();
        populateModel();
    }

    //Debug function used to generate some test data.
    private void populateModel(){
        String runway1 = "07L";
        addToModel(runway1, -400, 100, 1200, 80,200);
        setDimData(runway1, 60, 80, 400, 190, 60, 200);
        setVariableLengths(runway1, 1200, 1200+400, 1200 + 60, 1200 - 200);

        String runway2 = "11L";
        addToModel(runway2, -800, -500, 2000, 100,0);
        setDimData(runway2, 60, 100, 350, 220, 60, 200);
        setVariableLengths(runway2, 2000, 2000+350, 2000 + 60, 2000 - 0);
    }

    private void addToModel(String name, Integer xPos, Integer yPos, Integer xDim, Integer yDim, Integer threshold){
        runwayPositions.put(name, new Point(xPos,yPos));
        runwayDimensions.put(name, new Dimension(xDim, yDim));
        runwayThreshold.put(name, threshold);
    }

    private void setDimData(String name, Integer stopwayX, Integer stopwayY, Integer clearwayX, Integer clearwayY, Integer stripEnd, Integer stripWidth){
        runwayClearway.put(name, new Dimension( clearwayX,clearwayY));
        runwayStopway.put(name, new Dimension(stopwayX,stopwayY));
        runwayStrip.put(name, new Dimension(stripEnd,stripWidth));
    }

    private void setVariableLengths(String name, Integer tora, Integer toda, Integer asda, Integer lda){
        runwayTORA.put(name, tora);
        runwayTODA.put(name, toda);
        runwayASDA.put(name, asda);
        runwayLDA.put(name, lda);
    }

    //Returns a set of strings representing runways.
    public Set<String> getRunways(){
        return runwayPositions.keySet();
    }

    //Returns the position of a runway as a Point given the name.
    public Point getRunwayPos(String runwayId) {
        return runwayPositions.get(runwayId);
    }

    //Returns the size of the runway as a Dimension given the name.
    public Dimension getRunwayDim(String runwayId) {
        return runwayDimensions.get(runwayId);
    }

    //Returns the bearing of the runway in degrees given the runway's name.
    public Integer getBearing(String runwayId){
        Integer bearing = Integer.parseInt(runwayId.substring(0,2))*10;
        return bearing;
    }

    //Returns the Dimension of a given runway's stopway.
    public Dimension getStopwayDim(String runwayId){
        return  runwayStopway.get(runwayId);
    }

    //Returns the Dimensions of a given runway's clearway.
    public Dimension getClearwayDim(String runwayId){
        return runwayClearway.get(runwayId);
    }

    //Returns the distance from the centerline to the edge of the runway strip given a runway.
    public Integer getStripWidthFromCenterline(String runwayId){
        return runwayStrip.get(runwayId).height;
    }

    //Returns the distance from the start/end of the runway to the edge of the runway strip given a runway.
    public Integer getStripEndSize(String runwayId){
        return runwayStrip.get(runwayId).width;
    }

    //Returns the Take Off Run Available for a given runway.
    public Integer getRunwayTORA(String runwayId){
        return runwayTORA.get(runwayId);
    }

    //Returns the Take Off Distance Available for a given runway.
    public Integer getRunwayTODA(String runwayId){
        return runwayTODA.get(runwayId);
    }

    //Returns the Accelerate Stop Distance Available for a given runway.
    public Integer getRunwayASDA(String runwayId){
        return runwayASDA.get(runwayId);
    }

    //Returns the Landing Distance Available for a given runway.
    public Integer getRunwayLDA(String runwayId){
        return runwayLDA.get(runwayId);
    }

    //Returns the threshold for a given runway.
    public Integer getRunwayThreshold(String runwayId){
        return runwayThreshold.get(runwayId);
    }
}
