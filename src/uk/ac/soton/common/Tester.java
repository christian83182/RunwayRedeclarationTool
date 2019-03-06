package uk.ac.soton.common;

import java.awt.*;

public class Tester {
    public static void main(String[] args) {
        Runway runway = new Runway("runway1", 50, 60, 500,30, 60, 50);
        LogicalRunway logicalRunway = new LogicalRunway("logical",50,30,new Dimension(30,30),new Dimension(40,60));
        LogicalRunway logicalRunway1 = new LogicalRunway("logical1",50,25,new Dimension(50,30),new Dimension(40,40));
        LogicalRunway [] expected = new LogicalRunway[2];

        logicalRunway.setObjectThresholdDistance(30);
        System.out.println(runway.getAls());
    }



 /*   public static void main(String[] args) {


        Dimension dimension1 = new Dimension(20, 15);
        Dimension dimension2 = new Dimension(15, 10);
        Dimension dimension3 = new Dimension(30, 40);

        Airfield.Dimensions dimensions = null;
        dimensions.setHeight(10.0);
        dimensions.setLength(15.0);
        dimensions.setWidth(5.0);


        Airfield airfield = new Airfield();
        Obstacle obstacle = new Obstacle("obstacle", 70, 70, 15, 30, 70, dimensions);


        LogicalRunway logicalRunway = new LogicalRunway("09L", 1500, 25, dimension1, dimension2);
        LogicalRunway logicalRunway1 = new LogicalRunway("09R", 1500, 20, dimension3, dimension2);
        Runway runway = new Runway("09L", 100, 100, 1000, 50, 30);

        logicalRunway.setObstacle(obstacle);


        airfield.addRunway(runway);
        runway.setLogicalRunways(logicalRunway, logicalRunway1);


        System.out.println("Current Value: " + logicalRunway.getAsda().getCurrentValue());
        logicalRunway.redeclareAsda(1520);
        System.out.println("Asda redeclared to 1520");

        System.out.println("Original Value: " + logicalRunway.getAsda().getOriginalValue());
        System.out.println("Redeclared Value: " + logicalRunway.getAsda().getRedeclaredValue());
        System.out.println("Current Value: " + logicalRunway.getAsda().getCurrentValue());



/*        airfield.getRunway("runway1").placeObstacle("Airbus A320", 20,10,20,dimensions1);
        airfield.defineNewObstacle("Aircraft",50.0,30.0,10.0);

        for(Runway r : airfield.getRunways()){
           // System.out.println( r.obstaclePresent());
            System.out.println(r.getName());
        }

        System.out.println("Removing runway 2...");
        airfield.removeRunway(run2);

        for(Runway r : airfield.getRunways()) {
            System.out.println(r.getName());
        }

        System.out.println(run1.getObstacle().getId());

        airfield.removePredefinedObstacle("Airbus A318");
        System.out.println(airfield.getPredefinedObstacles().keySet());

        run1.setTora(7);
        run1.setAsda(10);
        run1.setStopway(5);
        System.out.println(run1.getStopway());

        Obstacle obstacle = run1.getObstacle();
        System.out.println(obstacle.getHeight());



    }



    }
    */
}