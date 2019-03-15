package uk.ac.soton.common;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RunwayTest {
    @Test
    public void getLength() throws Exception {
        Runway runway = new Runway("09R/27L", 100, 200, 4678, 1000, 100, 60);
        assertEquals((Integer)4678, runway.getLength());
    }

    @Test
    public void setLength() throws Exception {
        Runway runway = new Runway();
        runway.setLength(5987);
        assertEquals((Integer) 5987, runway.getLength());
    }

    @Test
    public void getWidth() throws Exception {
        Runway runway = new Runway("09R/27L", 100, 200, 4678, 1000, 100, 60);
        assertEquals((Integer)1000, runway.getWidth());
    }

    @Test
    public void setWidth() throws Exception {
        Runway runway = new Runway();
        runway.setWidth(1000);
        assertEquals((Integer) 1000, runway.getWidth());
    }

    @Test
    public void setActive() throws Exception {
        Runway runway = new Runway();
        runway.setActive(false);
        assertEquals(false, runway.isActive());
        runway.setActive(true);
        assertEquals(true, runway.isActive());
    }

    @Test
    public void setStatus() throws Exception {
        Runway runway = new Runway();
        runway.setStatus("Clear runway");
        assertEquals("Clear runway", runway.getStatus());
        runway.setStatus("Obstacle on runway");
        assertEquals("Obstacle on runway", runway.getStatus());
        runway.setStatus("Runway currently used for take-off");
        assertEquals("Runway currently used for take-off", runway.getStatus());
        runway.setStatus("Runway currently used for landing");
        assertEquals("Runway currently used for landing", runway.getStatus());
    }

    @Test
    public void setAls() throws Exception {
        Runway runway = new Runway();
        runway.setAls(50);
        assertEquals((Integer)50, runway.getAls());
    }


    @Test
    public void setResa() throws Exception {
        Runway runway = new Runway();
        runway.setResa(100);
        assertEquals((Integer)100, runway.getResa());
    }

    @Test
    public void setBlastDistance() throws Exception {
        Runway runway = new Runway();
        runway.setBlastDistance(300);
        assertEquals((Integer)300, runway.getBlastDistance());
    }


    @Test
    public void setStripEnd() throws Exception {
        Runway runway = new Runway();
        runway.setStripEnd(50);
        assertEquals((Integer)50, runway.getStripEnd());
    }

    @Test
    public void setStripWidth() throws Exception {
        Runway runway = new Runway();
        runway.setStripWidth(500);
        assertEquals((Integer)500, runway.getStripWidth());
    }

    @Test
    public void getLogicalRunways() throws Exception {
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        LogicalRunway[] logicalRunways = new LogicalRunway[2];
        logicalRunways[0] = lr11;
        logicalRunways[1] = lr12;

        r1.setLogicalRunways(lr11,lr12);

        assertEquals(logicalRunways, r1.getLogicalRunways());

    }

    @Test
    public void getLogicalRunway() throws Exception {
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        r1.setLogicalRunways(lr11,lr12);

        assertEquals(lr11, r1.getLogicalRunway("09L"));
        assertEquals(lr12, r1.getLogicalRunway("27R"));
    }

    @Test
    public void placeAndClearObstacle() throws Exception {
        Runway r1 = new Runway("09L/27R",-1000,-200,1800,80,400,60);
        LogicalRunway lr11 = new LogicalRunway("09L",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));
        LogicalRunway lr12 = new LogicalRunway("27R",r1.getLength(),100,
                new Dimension(350,220), new Dimension(60,r1.getWidth()));

        r1.setLogicalRunways(lr11,lr12);

        Obstacle obstacle = new Obstacle(100, 200, new Airfield.Dimensions(5000.0,1000.0, 1500.0));

        r1.placeObstacle(obstacle, "09L", 306, "27R", 257);

        assertEquals(obstacle, r1.getObstacle());

        r1.clearObstacle();

        assertEquals(null, r1.getObstacle());
    }

    @Test
    public void redeclareScenario1() throws Exception {

        Runway runway = initScenarioRunway09L27R();

        Obstacle obstacle = new Obstacle(256,0, new Airfield.Dimensions(11.0,10.0,12.0));
        runway.placeObstacle(obstacle,"09L",-50,"27R",3646);
        runway.recalculateParameters();

        // Take Off Away, Landing Over
        assertEquals(3346, runway.getLogicalRunway("09L").getTora().getRedeclaredValue().intValue());
        assertEquals(3346, runway.getLogicalRunway("09L").getToda().getRedeclaredValue().intValue());
        assertEquals(3346, runway.getLogicalRunway("09L").getAsda().getRedeclaredValue().intValue());
        assertEquals(2986, runway.getLogicalRunway("09L").getLda().getRedeclaredValue().intValue());

        // Take Off Towards, Landing Towards
        assertEquals(2986, runway.getLogicalRunway("27R").getTora().getRedeclaredValue().intValue());
        assertEquals(2986, runway.getLogicalRunway("27R").getToda().getRedeclaredValue().intValue());
        assertEquals(2986, runway.getLogicalRunway("27R").getAsda().getRedeclaredValue().intValue());
        assertEquals(3346, runway.getLogicalRunway("27R").getLda().getRedeclaredValue().intValue());
    }

    @Test
    public void redeclareScenario2() throws Exception {

        Runway runway = initScenarioRunway09R27L();

        Obstacle obstacle = new Obstacle(3160, -20, new Airfield.Dimensions(11.0,10.0,25.0));
        runway.placeObstacle(obstacle, "09R",2853, "27L", 500 );
        runway.recalculateParameters();

        // Take Off Towards, Landing Towards
        assertEquals(1850, runway.getLogicalRunway("09R").getTora().getRedeclaredValue().intValue());
        assertEquals(1850, runway.getLogicalRunway("09R").getToda().getRedeclaredValue().intValue());
        assertEquals(1850, runway.getLogicalRunway("09R").getAsda().getRedeclaredValue().intValue());
        assertEquals(2553, runway.getLogicalRunway("09R").getLda().getRedeclaredValue().intValue());

        // Take Off Away, Landing Over
        assertEquals(2860, runway.getLogicalRunway("27L").getTora().getRedeclaredValue().intValue());
        assertEquals(2860, runway.getLogicalRunway("27L").getToda().getRedeclaredValue().intValue());
        assertEquals(2860, runway.getLogicalRunway("27L").getAsda().getRedeclaredValue().intValue());
        assertEquals(1850, runway.getLogicalRunway("27L").getLda().getRedeclaredValue().intValue());
    }

    @Test
    public void redeclareScenario3() throws Exception {

        Runway runway = initScenarioRunway09R27L();

        Obstacle obstacle = new Obstacle(457,60, new Airfield.Dimensions(11.0,10.0,15.0));
        runway.placeObstacle(obstacle,"09R",150,"27L",3203);
        runway.recalculateParameters();

        // Take Off Away, Landing Over
        assertEquals(2903, runway.getLogicalRunway("09R").getTora().getRedeclaredValue().intValue());
        assertEquals(2903, runway.getLogicalRunway("09R").getToda().getRedeclaredValue().intValue());
        assertEquals(2903, runway.getLogicalRunway("09R").getAsda().getRedeclaredValue().intValue());
        assertEquals(2393, runway.getLogicalRunway("09R").getLda().getRedeclaredValue().intValue());

        // Take Off Towards, Landing Towards
        assertEquals(2393, runway.getLogicalRunway("27L").getTora().getRedeclaredValue().intValue());
        assertEquals(2393, runway.getLogicalRunway("27L").getToda().getRedeclaredValue().intValue());
        assertEquals(2393, runway.getLogicalRunway("27L").getAsda().getRedeclaredValue().intValue());
        assertEquals(2903, runway.getLogicalRunway("27L").getLda().getRedeclaredValue().intValue());
    }

    @Test
    public void redeclareScenario4() throws Exception {

        Runway runway = initScenarioRunway09L27R();

        Obstacle obstacle = new Obstacle(3852,20, new Airfield.Dimensions(11.0,10.0,20.0));
        runway.placeObstacle(obstacle,"09L",3546,"27R",50);
        runway.recalculateParameters();

        // Take Off Towards, Landing Towards
        assertEquals(2792, runway.getLogicalRunway("09L").getTora().getRedeclaredValue().intValue());
        assertEquals(2792, runway.getLogicalRunway("09L").getToda().getRedeclaredValue().intValue());
        assertEquals(2792, runway.getLogicalRunway("09L").getAsda().getRedeclaredValue().intValue());
        assertEquals(3246, runway.getLogicalRunway("09L").getLda().getRedeclaredValue().intValue());

        // Take Off Away, Landing Over
        assertEquals(3534, runway.getLogicalRunway("27R").getTora().getRedeclaredValue().intValue());
        assertEquals(3612, runway.getLogicalRunway("27R").getToda().getRedeclaredValue().intValue());
        assertEquals(3534, runway.getLogicalRunway("27R").getAsda().getRedeclaredValue().intValue());
        assertEquals(2774, runway.getLogicalRunway("27R").getLda().getRedeclaredValue().intValue());
    }

    @Test // Towards the obstacle, Slope Calculation > RESA
    public void redeclareEquivClass1(){

        Runway runway = initEquivTestingRunway();

        Obstacle obstacle = new Obstacle(2980,0, new Airfield.Dimensions(11.0,10.0,15.0));
        runway.placeObstacle(obstacle, "09",2730, "27",650);
        runway.recalculateParameters();

        // Configuration check: Slope Calculation > RESA
        assertEquals(true, runway.getObstacle().getHeight() * runway.getAls() > runway.getResa());

        // Take Off Towards, Landing Towards
        assertEquals(2170, runway.getLogicalRunway("09").getTora().getRedeclaredValue().intValue());
        assertEquals(2170, runway.getLogicalRunway("09").getToda().getRedeclaredValue().intValue());
        assertEquals(2170, runway.getLogicalRunway("09").getAsda().getRedeclaredValue().intValue());
        assertEquals(2430, runway.getLogicalRunway("09").getLda().getRedeclaredValue().intValue());
    }

    @Test // Towards the obstacle, Slope Calculation <= RESA
    public void redeclareEquivClass2(){

        Runway runway = initEquivTestingRunway();

        Obstacle obstacle = new Obstacle(2980,0, new Airfield.Dimensions(11.0,10.0,3.0));
        runway.placeObstacle(obstacle, "09",2730, "27",650);
        runway.recalculateParameters();

        // Configuration check: Slope Calculation <= RESA
        assertEquals(true, runway.getObstacle().getHeight() * runway.getAls() <= runway.getResa());

        // Take Off Towards, Landing Towards
        assertEquals(2680, runway.getLogicalRunway("09").getTora().getRedeclaredValue().intValue());
        assertEquals(2680, runway.getLogicalRunway("09").getToda().getRedeclaredValue().intValue());
        assertEquals(2680, runway.getLogicalRunway("09").getAsda().getRedeclaredValue().intValue());
        assertEquals(2430, runway.getLogicalRunway("09").getLda().getRedeclaredValue().intValue());
    }

    @Test // Away from the obstacle, Slope Calculation > RESA, Slope + Strip End > Blast Protection
    public void redeclareEquivClass3(){

        Runway runway = initEquivTestingRunway();

        Obstacle obstacle = new Obstacle(2980,0, new Airfield.Dimensions(11.0,10.0,15.0));
        runway.placeObstacle(obstacle, "09",2730, "27",650);
        runway.recalculateParameters();

        // Configuration check: Slope Calculation > RESA
        assertEquals(true, runway.getObstacle().getHeight() * runway.getAls() > runway.getResa());

        // Configuration check: Slope + Strip End > Blast Protection
        assertEquals(true,runway.getObstacle().getHeight() * runway.getAls() + runway.getStripEnd() > runway.getBlastDistance());

        // Take Off Away, Landing Over
        assertEquals(2680, runway.getLogicalRunway("27").getTora().getRedeclaredValue().intValue());
        assertEquals(2750, runway.getLogicalRunway("27").getToda().getRedeclaredValue().intValue());
        assertEquals(2730, runway.getLogicalRunway("27").getAsda().getRedeclaredValue().intValue());
        assertEquals(2170, runway.getLogicalRunway("27").getLda().getRedeclaredValue().intValue());
    }

    @Test // Away from the obstacle, Slope Calculation <= RESA, RESA + Strip End > Blast Protection
    public void redeclareEquivClass4(){

        Runway runway = initEquivTestingRunway();

        Obstacle obstacle = new Obstacle(2980,0, new Airfield.Dimensions(11.0,10.0,3.0));
        runway.placeObstacle(obstacle, "09",2730, "27",650);
        runway.setResa(260);
        runway.recalculateParameters();

        // Configuration check: Slope Calculation <= RESA
        assertEquals(true, runway.getObstacle().getHeight() * runway.getAls() <= runway.getResa());

        // Configuration check: RESA + Strip End > Blast Protection
        assertEquals(true,runway.getResa() + runway.getStripEnd() > runway.getBlastDistance());

        assertEquals(3630, runway.getLogicalRunway("27").getLda().getOriginalValue());
        assertEquals(260, runway.getResa().intValue());
        assertEquals(650, runway.getLogicalRunway("27").getObjectThresholdDistance());
        assertEquals(60, runway.getStripEnd().intValue());

        // Take Off Away, Landing Over
        assertEquals(2660, runway.getLogicalRunway("27").getTora().getRedeclaredValue().intValue());
        assertEquals(2730, runway.getLogicalRunway("27").getToda().getRedeclaredValue().intValue());
        assertEquals(2710, runway.getLogicalRunway("27").getAsda().getRedeclaredValue().intValue());
        assertEquals(2660, runway.getLogicalRunway("27").getLda().getRedeclaredValue().intValue());
    }

    @Test // Away from the obstacle, Slope + Strip End <= Blast Protection, RESA + Strip End <= Blast Protection
    public void redeclareEquivClass5(){

        Runway runway = initEquivTestingRunway();

        Obstacle obstacle = new Obstacle(2980,0, new Airfield.Dimensions(11.0,10.0,3.0));
        runway.placeObstacle(obstacle, "09",2730, "27",650);
        runway.setBlastDistance(500);
        runway.recalculateParameters();

        // Configuration check: Slope + Strip End <= Blast Protection && RESA + Strip End <= Blast Protection
        assertEquals(true, runway.getObstacle().getHeight() * runway.getAls() <= runway.getBlastDistance() &&
                runway.getResa() + runway.getStripEnd() <= runway.getBlastDistance());

        // Take Off Away, Landing Over
        assertEquals(2480, runway.getLogicalRunway("27").getTora().getRedeclaredValue().intValue());
        assertEquals(2550, runway.getLogicalRunway("27").getToda().getRedeclaredValue().intValue());
        assertEquals(2530, runway.getLogicalRunway("27").getAsda().getRedeclaredValue().intValue());
        assertEquals(2480, runway.getLogicalRunway("27").getLda().getRedeclaredValue().intValue());
    }

    public Runway initScenarioRunway09R27L(){

        Runway runway = new Runway("09R/27L", -1000, -200, 3660, 80, 400, 60);
        LogicalRunway r1 = new LogicalRunway("09R",runway.getLength(),307,
                new Dimension(0,0), new Dimension(0, 0));
        LogicalRunway r2 = new LogicalRunway("27L",runway.getLength(),0,
                new Dimension(0,0), new Dimension(0, 0));
        runway.setLogicalRunways(r1,r2);

        return runway;
    }

    public Runway initScenarioRunway09L27R(){

        Runway runway = new Runway("09L/27R", -1000, -200, 3902, 80, 400, 60);
        LogicalRunway r1 = new LogicalRunway("09L",runway.getLength(),306,
                new Dimension(0,0), new Dimension(0, 0));
        LogicalRunway r2 = new LogicalRunway("27R",3884,0,
                new Dimension(78,220), new Dimension(0, 0));
        runway.setLogicalRunways(r1,r2);

        return runway;
    }

    public Runway initEquivTestingRunway(){

        Runway runway = new Runway("09/27", -1000, -200, 3680, 80, 400, 60);
        LogicalRunway r1 = new LogicalRunway("09",runway.getLength(),250,
                new Dimension(0,0), new Dimension(0, 0));
        LogicalRunway r2 = new LogicalRunway("27",runway.getLength(),50,
                new Dimension(70,220), new Dimension(50, runway.getWidth()));
        runway.setLogicalRunways(r1,r2);

        return runway;
    }

}