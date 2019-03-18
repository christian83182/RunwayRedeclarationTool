package uk.ac.soton.tests.uk.ac.soton.common;

import org.junit.Test;
import uk.ac.soton.common.LogicalRunway;

import java.awt.*;

import static org.junit.Assert.*;

public class LogicalRunwayTest {


    public LogicalRunway getLogicalRunway(){
        LogicalRunway logicalRunway = new LogicalRunway("09R", 5478, 0, new Dimension(200, 300), new Dimension(300,300));
        return logicalRunway;
    }


    @Test
    public void setName() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.setName("Changed name");
        assertEquals("Changed name", logicalRunway.getName());
    }

    @Test
    public void getName() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        assertEquals("09R", logicalRunway.getName());
    }

    @Test
    public void getStopway() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        Dimension expected = new Dimension(300,300);
        assertEquals(expected, logicalRunway.getStopway());
    }

    @Test
    public void getClearway() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        Dimension expected = new Dimension(200,300);
        assertEquals(expected, logicalRunway.getClearway());
    }

    @Test
    public void redeclareTora() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.redeclareTora(4200);
        Number actual = logicalRunway.getTora().getCurrentValue();
        assertEquals(4200,actual);

        //Tora should not be negative
        //logicalRunway.redeclareTora(-1000);
        //assertEquals(4200, logicalRunway.getTora().getCurrentValue());

    }

    @Test
    public void redeclareToda() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.redeclareToda(4500);
        Number actual = logicalRunway.getToda().getCurrentValue();
        assertEquals(4500,actual);

        //Toda should not be negative
        //logicalRunway.redeclareTora(-1200);
        //assertEquals(4500, logicalRunway.getTora().getCurrentValue());

    }

    @Test
    public void redeclareAsda() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.redeclareAsda(4500);
        Number actual = logicalRunway.getAsda().getCurrentValue();
        assertEquals(4500,actual);

        //Toda should not be negative
        //logicalRunway.redeclareTora(-1200);
        //assertEquals(4500, logicalRunway.getTora().getCurrentValue());
    }

    @Test
    public void redeclareLda() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.redeclareLda(5100);
        Number actual = logicalRunway.getLda().getCurrentValue();
        assertEquals(5100, actual);

        //Lda should not be negative
        //logicalRunway.redeclareLda(-1500);
        //assertEquals(5100,logicalRunway.getTora().getCurrentValue());
    }

    @Test
    public void revertParameters() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        Number originalTora = logicalRunway.getTora().getOriginalValue();
        Number originalLda = logicalRunway.getLda().getOriginalValue();
        Number originalAsda = logicalRunway.getAsda().getOriginalValue();
        Number originalToda = logicalRunway.getToda().getOriginalValue();

        logicalRunway.redeclareTora(3000);
        logicalRunway.redeclareLda(4000);
        logicalRunway.redeclareAsda(5100);
        logicalRunway.redeclareToda(4000);

        logicalRunway.revertParameters();

        assertEquals(originalTora, logicalRunway.getTora().getCurrentValue());
        assertEquals(originalLda, logicalRunway.getLda().getCurrentValue());
        assertEquals(originalAsda, logicalRunway.getAsda().getCurrentValue());
        assertEquals(originalToda, logicalRunway.getToda().getCurrentValue());

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
    public void getSetThreshold() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.setThreshold(300);

        assertEquals(300,logicalRunway.getThreshold());

    }


    @Test
    public void getObjectThresholdDistance() throws Exception {
        LogicalRunway logicalRunway = getLogicalRunway();
        logicalRunway.setThreshold(300);
        logicalRunway.setObjectDistances(200, 150, 600);

        int objectThresholdDistance = 600 - logicalRunway.getThreshold().intValue();

        assertEquals(300,objectThresholdDistance);

    }


}