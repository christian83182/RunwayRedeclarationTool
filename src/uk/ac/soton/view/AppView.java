package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AppView extends JFrame{

    //Note that for the runwayDimensions, the length is the x value and the width is the y value.
    private Map<String,Point> runwayPositions;
    private Map<String,Dimension> runwayDimensions;

    //Constructor calls parent's constructor and initializes member variables
    public AppView(String title){
        super(title);
        runwayPositions = new HashMap<>();
        runwayDimensions = new HashMap<>();
    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        populateModel();

        TopView2D topView = new TopView2D(runwayPositions, runwayDimensions);
        this.add(topView);

        this.pack();
        this.setVisible(true);
    }

    //Debug function used to generate some data.
    private void populateModel(){
        String runway1 = "06R";
        runwayPositions.put(runway1, new Point(100,600));
        runwayDimensions.put(runway1, new Dimension(600,60));

        String runway2 = "12R";
        runwayPositions.put(runway2, new Point(100,400));
        runwayDimensions.put(runway2, new Dimension(500,60));
    }

}