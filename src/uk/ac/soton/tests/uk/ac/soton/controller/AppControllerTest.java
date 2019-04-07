package uk.ac.soton.tests.uk.ac.soton.controller;

import org.junit.Test;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;
import uk.ac.soton.common.Obstacle;
import uk.ac.soton.common.Runway;
import uk.ac.soton.controller.AppController;
import uk.ac.soton.view.AppView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class AppControllerTest {

    Runway r1;
    Runway r2;

    LogicalRunway lr11;
    LogicalRunway lr12;
    LogicalRunway lr21;
    LogicalRunway lr22;

    Airfield airfield;


    public AppController getAppController(){
        AppView appView = new AppView("App");
        AppController appController = new AppController(appView);
        airfield = new Airfield("Unnamed Airfield");
        airfield.setPredefinedObstacles(airfield.getPredefinedObstacles());

        appController.setAirfield(airfield);

        List<Runway> runways = new ArrayList<>();
        r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        r1.setLogicalRunways(lr11,lr12);

        r2 = new Runway("09R/27L",-600,450,1800,80,400,60);
        lr21 = new LogicalRunway("09R",r2.getLength(),250,
                new Dimension(0,0), new Dimension(60,r2.getWidth()));
        lr22 = new LogicalRunway("27L",r2.getLength(),250,
                new Dimension(0,0), new Dimension(60,r2.getWidth()));
        r2.setLogicalRunways(lr21,lr22);

        runways.add(r1);
        runways.add(r2);

        for(Runway runway : runways){
            airfield.addRunway(runway);
        }
        appController.getRunways();

        return appController;
    }


    @Test
    public void getRunways() {
        AppController appController = getAppController();

        assertEquals(false, appController.getRunways().isEmpty());
    }

    @Test
    public void getRunwayPos() {
    }

    @Test
    public void getRunwayDim() {
        AppController appController = getAppController();
        Dimension dimension = new Dimension(1800,80);

        assertEquals(appController.getRunwayDim("09L"), dimension);

    }

    @Test
    public void getBearing() {
        AppController appController = getAppController();

        assertEquals((long) 270, (long) appController.getBearing("27L"));
    }

    @Test
    public void getStopwayDim() {
        AppController appController = getAppController();
        Dimension stopway = new Dimension(60,80);

        assertEquals(stopway, appController.getStopwayDim("27R"));

    }

    @Test
    public void getClearwayDim() {
        AppController appController = getAppController();
        Dimension clearway = new Dimension(350,220);

        assertEquals(clearway, appController.getClearwayDim("27R"));
    }

    @Test
    public void getStripWidthFromCenterline() {
        AppController appController = getAppController();
        int stripWidthFromCentreline = 400 / 2;

        assertEquals((long) stripWidthFromCentreline, (long) appController.getStripWidthFromCenterline("27R"));
    }

    @Test
    public void getStripEndSize() {
        AppController appController = getAppController();

        assertEquals((long) 60, (long) appController.getStripEndSize("27L"));

    }

    @Test
    public void getRunwayTORA() {
        AppController appController = getAppController();
        Number runwayTora = lr11.getTora().getCurrentValue();

        assertEquals(runwayTora, appController.getRunwayTORA("09L"));
    }

    @Test
    public void getRunwayTODA() {
    }

    @Test
    public void getRunwayASDA() {
        AppController appController = getAppController();
        Number runwayAsda = lr11.getAsda().getCurrentValue();

        assertEquals(runwayAsda, appController.getRunwayASDA("09L"));
    }

    @Test
    public void getRunwayLDA() {
    }

    @Test
    public void getRunwayThreshold() {
        AppController appController = getAppController();
        Number runwayThreshold = lr11.getThreshold();

        assertEquals(runwayThreshold, appController.getRunwayThreshold("09L") );
    }

    @Test
    public void getTORAOffset() {
        AppController appController = getAppController();
        Obstacle obstacle = new Obstacle(50,30,new Airfield.Dimensions(35.2,30.4,14.7));
        r1.placeObstacle(obstacle, "09L");
        r1.recalculateParameters();

        int originalTora = (int) lr11.getTora().getOriginalValue();
        int redeclaredTora = (int) lr11.getTora().getRedeclaredValue();

        int offset = originalTora - redeclaredTora;

        assertEquals((long) offset, (long) appController.getTORAOffset("09L"));


    }

    @Test
    public void getTODAOffset() {
        AppController appController = getAppController();
        Obstacle obstacle = new Obstacle(150,30, new Airfield.Dimensions(35.0,30.0,15.0));
        r1.placeObstacle(obstacle,"09L");
        r1.recalculateParameters();

        int originalToda = (int) lr11.getToda().getOriginalValue();
        int redeclaredToda = (int) lr11.getToda().getRedeclaredValue();

        int offset = originalToda - redeclaredToda;

        assertEquals((long) offset, (long) appController.getTODAOffset("09L"));


    }


    @Test
    public void getLDAOffset() {
        AppController appController = getAppController();
        Obstacle obstacle = new Obstacle(150,30, new Airfield.Dimensions(35.0,30.0,15.0));
        r1.placeObstacle(obstacle,"09L");
        r1.recalculateParameters();

        int originalLda = (int) lr11.getLda().getOriginalValue();
        int redeclaredLda = lr11.getLda().getRedeclaredValue().intValue();

        int offset = originalLda - redeclaredLda;

        assertEquals((long) offset, (long) appController.getLDAOffset("09L"));

    }

    @Test
    public void getPredefinedObstacleIds() {
        AppController appController = getAppController();
        Set<String> stringSet = airfield.getPredefinedObstacles().keySet();

        assertEquals(stringSet, appController.getPredefinedObstacleIds());
    }

    @Test
    public void getPredefinedObstacleWidthHeightLength() {
        AppController appController = getAppController();

        assertEquals((long) 59.6, appController.getPredefinedObstacleWidth("Boeing 747-100").intValue());
        assertEquals((long) 19.3, appController.getPredefinedObstacleHeight("Boeing 747-100").intValue());
        assertEquals((long) 70.6, appController.getPredefinedObstacleLength("Boeing 747-100").intValue());
    }


    @Test
    public void addObstacleToList() {
        AppController appController = getAppController();
        appController.addObstacleToList("Airbus A220-100", 35.0,30.3,11.5);
        Airfield.Dimensions dimensions = new Airfield.Dimensions(35.0,30.3,11.5);

        assertEquals(true,airfield.getPredefinedObstacles().containsKey("Airbus A220-100"));
        assertEquals(dimensions.getHeight(), airfield.getPredefinedObstacles().get("Airbus A220-100").getHeight());
        assertEquals(dimensions.getWidth(), airfield.getPredefinedObstacles().get("Airbus A220-100").getWidth());
        assertEquals(dimensions.getLength(), airfield.getPredefinedObstacles().get("Airbus A220-100").getLength());
    }

    @Test
    public void deleteObstacleFromList() {
        AppController appController = getAppController();

        assertEquals(true, airfield.getPredefinedObstacles().containsKey("Boeing 747-100"));

        appController.deleteObstacleFromList("Boeing 747-100");
        assertEquals(false, airfield.getPredefinedObstacles().containsKey("Boeing 747-100"));


    }

    @Test
    public void addRemoveObstacleToRunway() {
        AppController appController = getAppController();

        appController.addObstacleToRunway("09L","Boeing 747-100", 30,50);
        assertEquals("Boeing 747-100", r1.getObstacle().getId());

        appController.removeObstacleFromRunway("09L");
        assertEquals(null, r1.getObstacle());

    }


    @Test
    public void getRunwayObstacle() {
        AppController appController = getAppController();
        appController.addObstacleToRunway("09L","Boeing 747-100", 30,50);

        assertEquals(r1.getObstacle().getId(), appController.getRunwayObstacle("09L"));
        assertEquals("", appController.getRunwayObstacle("09R"));

    }

    @Test
    public void getDistanceFromCenterline() {
        AppController appController = getAppController();
        appController.addObstacleToRunway("09L","Boeing 747-100", 30,50);

        assertEquals(30, (long) appController.getDistanceFromCenterline("09L"));

    }

    @Test
    public void getDistanceFromThreshold() {
        AppController appController = getAppController();
        appController.addObstacleToRunway("09L","Boeing 747-100", 30,50);

        assertEquals(50, (long) appController.getDistanceFromThreshold("09L"));

    }

}