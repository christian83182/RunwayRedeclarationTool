package uk.ac.soton.common;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class LogicalRunwayTest {
    @Test
    public void setName() throws Exception {
        LogicalRunway logicalRunway = new LogicalRunway("09R", 5478, 0, new Dimension(200, 300), new Dimension(300,300));
        logicalRunway.setName("Changed name");
        assertEquals("Changed name", logicalRunway.getName());
    }

    @Test
    public void getName() throws Exception {
    }

    @Test
    public void getStopway() throws Exception {
    }

    @Test
    public void getClearway() throws Exception {
    }

    @Test
    public void redeclareTora() throws Exception {
    }

    @Test
    public void redeclareToda() throws Exception {
    }

    @Test
    public void redeclareAsda() throws Exception {
    }

    @Test
    public void redeclareLda() throws Exception {
    }

    @Test
    public void revertParameters() throws Exception {
    }

    @Test
    public void getTora() throws Exception {
    }

    @Test
    public void getToda() throws Exception {
    }

    @Test
    public void getAsda() throws Exception {
    }

    @Test
    public void getLda() throws Exception {
    }

    @Test
    public void getThreshold() throws Exception {
    }

    @Test
    public void setThreshold() throws Exception {
    }

    @Test
    public void setObjectThresholdDistance() throws Exception {
    }

    @Test
    public void getObjectThresholdDistance() throws Exception {
    }

}