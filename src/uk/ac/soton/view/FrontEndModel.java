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

    FrontEndModel(){
        runwayDimensions = new HashMap<>();
        runwayPositions = new HashMap<>();
        runwayStopway = new HashMap<>();
        runwayClearway = new HashMap<>();
        populateModel();
    }

    //Debug function used to generate some test data.
    private void populateModel(){
        String runway1 = "07L";
        runwayPositions.put(runway1, new Point(-400,100));
        runwayDimensions.put(runway1, new Dimension(1200,80));
        runwayStopway.put(runway1, new Dimension(200,80));
        runwayClearway.put(runway1, new Dimension( 400,120));

        String runway2 = "11L";
        runwayPositions.put(runway2, new Point(-800,-500));
        runwayDimensions.put(runway2, new Dimension(2000,100));
        runwayStopway.put(runway2, new Dimension(300,100));
        runwayClearway.put(runway2, new Dimension( 500,150));
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
}
