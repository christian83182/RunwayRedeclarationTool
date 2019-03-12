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
    public void recalculateParameters() throws Exception {
        Runway runway1 = new Runway("09R/27L", -1000, -200, 3660, 80, 400, 60);
        LogicalRunway r11 = new LogicalRunway("09R",runway1.getLength(),307,
                new Dimension(350,220), new Dimension(60, runway1.getWidth()));
        LogicalRunway r12 = new LogicalRunway("27L",runway1.getLength(),0,
                new Dimension(350,220), new Dimension(60,runway1.getWidth()));
        runway1.setLogicalRunways(r11,r12);

        Obstacle obstacle1 = new Obstacle(3160, -20, new Airfield.Dimensions(11.0,10.0,25.0));

        runway1.placeObstacle(obstacle1, "09R",2853, "27L", 500 );

        runway1.recalculateParameters();

        assertEquals(1850, runway1.getLogicalRunway("09R").getTora().getRedeclaredValue().intValue());
        assertEquals(1850, runway1.getLogicalRunway("09R").getToda().getRedeclaredValue().intValue());
        assertEquals(1850, runway1.getLogicalRunway("09R").getAsda().getRedeclaredValue().intValue());
        assertEquals(2553, runway1.getLogicalRunway("09R").getLda().getRedeclaredValue().intValue());


//
//        Runway runway2 = new Runway("09L/27R", -1000, -200, 3902, 80, 400, 60);
//        LogicalRunway r21 = new LogicalRunway("09L",runway2.getLength(),306,
//                new Dimension(350,220), new Dimension(60, runway2.getWidth()));
//        LogicalRunway r22 = new LogicalRunway("27R",runway2.getLength(),0,
//                new Dimension(350,220), new Dimension(60,runway2.getWidth()));
//        runway2.setLogicalRunways(r21,r22);
//
//        Obstacle obstacle2 = new Obstacle(100, 0, new Airfield.Dimensions(11.0,10.0,12.0));
//
//        runway2.placeObstacle(obstacle2, "09L",-50, "27R", 3646 );
//
//        runway2.recalculateParameters();
//
//        assertEquals(3902, runway2.getLogicalRunway("09L").getTora().getRedeclaredValue().intValue());
//        assertEquals(3902, runway2.getLogicalRunway("09L").getToda().getRedeclaredValue().intValue());
//        assertEquals(3902, runway2.getLogicalRunway("09L").getAsda().getRedeclaredValue().intValue());
//        assertEquals(3596, runway2.getLogicalRunway("09L").getLda().getRedeclaredValue().intValue());
    }

}