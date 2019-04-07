package uk.ac.soton.tests.uk.ac.soton.common;

import org.junit.Test;
import uk.ac.soton.common.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

public class AirfieldTest {

    public List<Runway> createRunwayList(){
        List<Runway> runways = new ArrayList<>();
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

        runways.add(r1);
        runways.add(r2);

        return runways;
    }


    public Runway getSingleRunway(){
        List<Runway> runways = new ArrayList<>();
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        r1.setLogicalRunways(lr11,lr12);

        return r1;
    }

    @Test
    public void getRunways() throws Exception {
        Airfield airfield = new Airfield("");
        List<Runway> runways = createRunwayList();
        for(Runway runway: runways){
            airfield.addRunway(runway);
        }

        assertEquals(runways, airfield.getRunways());
    }

    @Test
    public void addRunway() throws Exception {
        Airfield airfield = new Airfield("");
        Runway runway = getSingleRunway();
        airfield.addRunway(runway);

        assertEquals(airfield.getRunways().contains(runway), true);

    }

    @Test
    public void getRunway() throws Exception {
        Airfield airfield = new Airfield("");
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        r1.setLogicalRunways(lr11,lr12);

        airfield.addRunway(r1);

        assertEquals(r1, airfield.getRunway("09L"));

    }

    @Test
    public void removeRunway() throws Exception {

        Airfield airfield = new Airfield("");
        List<Runway> runways = createRunwayList();
        for(Runway runway: runways){
            airfield.addRunway(runway);
        }

        Runway removeRunway = runways.get(0);

        airfield.removeRunway(removeRunway);

        assertEquals(false, airfield.getRunways().contains(removeRunway));
    }

    @Test
    public void getAllLogicalRunways() throws Exception {
        Airfield airfield = new Airfield("");

        List<Runway> runways = createRunwayList();

        List<LogicalRunway> logicalRunways = new ArrayList<>();

        for(Runway runway: runways){
            airfield.addRunway(runway);
            logicalRunways.add(runway.getLogicalRunways()[0]);
            logicalRunways.add(runway.getLogicalRunways()[1]);
        }

        assertEquals(logicalRunways, airfield.getAllLogicalRunways());
    }

    @Test
    public void getLogicalRunwaysOf() throws Exception {
        Airfield airfield = new Airfield("");
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        r1.setLogicalRunways(lr11,lr12);

        airfield.addRunway(r1);

        assertEquals(r1.getLogicalRunways(), airfield.getLogicalRunwaysOf(r1));
    }

    @Test
    public void setPredefinedObstacles() throws Exception {
        Airfield airfield = new Airfield("");

        HashMap<String, Airfield.Dimensions> obstacles = new HashMap<>();
        obstacles.put("object1", new Airfield.Dimensions(1.2,2.1,1.1));
        obstacles.put("object2", new Airfield.Dimensions(30.6,60.3,70.8));
        obstacles.put("object3", new Airfield.Dimensions(1000.9,900.45,1700.1));

        airfield.setPredefinedObstacles(obstacles);

        assertEquals(obstacles, airfield.getPredefinedObstacles());
    }

    @Test
    public void defineNewObstacle() throws Exception {
        Airfield airfield = new Airfield("");
        airfield.defineNewObstacle("object type", 20.09, 98.3, 4.00);

        assertEquals( true, airfield.getPredefinedObstacles().containsKey("object type"));
    }

    @Test
    public void removePredefinedObstacle() throws Exception {
        Airfield airfield = new Airfield("");

        HashMap<String, Airfield.Dimensions> obstacles = new HashMap<>();
        obstacles.put("object1", new Airfield.Dimensions(1.2,2.1,1.1));
        obstacles.put("object2", new Airfield.Dimensions(30.6,60.3,70.8));
        obstacles.put("object3", new Airfield.Dimensions(1000.9,900.45,1700.1));

        airfield.setPredefinedObstacles(obstacles);

        airfield.removePredefinedObstacle("object1");

        assertEquals(false, airfield.getPredefinedObstacles().containsKey("object1"));
    }

    @Test
    public void redefineObstacle() throws Exception {
        Airfield airfield = new Airfield("");

        HashMap<String, Airfield.Dimensions> obstacles = new HashMap<>();
        obstacles.put("object1", new Airfield.Dimensions(1.2,2.1,1.1));
        obstacles.put("object2", new Airfield.Dimensions(30.6,60.3,70.8));
        obstacles.put("object3", new Airfield.Dimensions(1000.9,900.45,1700.1));

        airfield.setPredefinedObstacles(obstacles);

        airfield.redefineObstacle("object1", 9.00, 80.6, 12.3);

        assertEquals(true, airfield.getPredefinedObstacles().get("object1").getLength() == 9.00);
        assertEquals(true, airfield.getPredefinedObstacles().get("object1").getWidth() == 80.6);
        assertEquals(true, airfield.getPredefinedObstacles().get("object1").getHeight() == 12.3);
    }

    @Test
    public void setBlastProtection() throws Exception {
        Airfield.setBlastProtection(500);
        assertEquals((Integer)500, Airfield.getBlastProtection());
        Airfield.setBlastProtection(300);
        assertEquals((Integer)300, Airfield.getBlastProtection());
    }
}