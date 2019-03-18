package uk.ac.soton.tests.uk.ac.soton.common;

import org.junit.Test;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.Obstacle;

import static org.junit.Assert.*;

public class ObstacleTest {

    public Obstacle getObstacle(){
        Obstacle obstacle = new Obstacle(50,30,new Airfield.Dimensions(35.2,30.4,14.7));

        return obstacle;
    }

    @Test
    public void getSetHeight() {
        Obstacle obstacle = getObstacle();

        obstacle.setHeight(16.0);

        assertEquals((long) 16.0, (long) obstacle.getHeight().doubleValue());
    }

    @Test
    public void getSetLength() {
        Obstacle obstacle = getObstacle();

        obstacle.setLength(33.0);

        assertEquals((long) 33.0, (long) obstacle.getLength().doubleValue());
    }

    @Test
    public void getSetWidth() {
        Obstacle obstacle = getObstacle();

        obstacle.setWidth(29.3);

        assertEquals((long) 29.3, (long) obstacle.getWidth().doubleValue());
    }

    @Test
    public void getSetCentrelineDistance() {
        Obstacle obstacle = getObstacle();

        obstacle.setCentrelineDistance(35);

        assertEquals( 35, (long) obstacle.getCentrelineDistance());
    }

    @Test
    public void getSetRunwayStartDistance() {
        Obstacle obstacle = getObstacle();
        obstacle.setRunwayStartDistance(60);

        assertEquals(60, (long) obstacle.getStartDistance());

    }

    @Test
    public void sideways() {
        Obstacle obstacle = getObstacle();
        obstacle.sideways();

        assertEquals( (long) 35.2, (long) obstacle.getWidth().doubleValue());
        assertEquals( (long) 30.4, (long) obstacle.getLength().doubleValue());
    }
}