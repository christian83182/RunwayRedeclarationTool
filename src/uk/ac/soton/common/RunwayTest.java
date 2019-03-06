package uk.ac.soton.common;

import java.awt.*;

import static org.junit.Assert.*;

public class RunwayTest {
    Runway runway = new Runway("runway1", 50, 60, 500, 30, 60, 50);
    LogicalRunway logicalRunway = new LogicalRunway("logical", 50, 30, new Dimension(30, 30), new Dimension(40, 60));
    LogicalRunway logicalRunway1 = new LogicalRunway("logical1", 50, 25, new Dimension(50, 30), new Dimension(40, 40));
    LogicalRunway[] expected = new LogicalRunway[2];

    Obstacle obstacle = new Obstacle("obstacle", 70, 90, 50, 90, new Airfield.Dimensions(50.0, 10.0, 30.0));


    @org.junit.Test
    public void getLength() {
        int length = runway.getLength();
        assertEquals(500, length);
    }

    @org.junit.Test
    public void getLogicalRunways() {
        runway.setLogicalRunways(logicalRunway, logicalRunway1);
        LogicalRunway[] logicalRunways = runway.getLogicalRunways();
        expected[0] = logicalRunway;
        expected[1] = logicalRunway1;
        assertEquals(expected[0], logicalRunways[0]);
        assertEquals(expected[1], logicalRunways[1]);
    }

    @org.junit.Test
    public void getAls() {
        int als = runway.getAls();
        assertEquals(50, als);
    }

    @org.junit.Test
    public void redeclareToda() {
        logicalRunway.redeclareToda(200);
        assertEquals(200, logicalRunway.getToda().getCurrentValue());
    }

    @org.junit.Test
    public void clearObstacleTest() {
        runway.clearObstacle();
        assertEquals(null, runway.getObstacle());
    }

}
