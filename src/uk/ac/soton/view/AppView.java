package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppView extends JFrame{

    //Note that for the runwayDimensions, the length is the x value and the width is the y value.
    private Map<String,Point> runwayPositions;
    private Map<String,Dimension> runwayDimensions;
    private String selectedRunway;

    //Constructor calls parent's constructor and initializes member variables
    public AppView(String title){
        super(title);
        runwayPositions = new HashMap<>();
        runwayDimensions = new HashMap<>();
        selectedRunway = "";
        populateModel();

    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        MenuPanel menuPanel = new MenuPanel(this);
        this.add(menuPanel, BorderLayout.WEST);

        TopView2D topView = new TopView2D(this,menuPanel);
        this.add(topView,BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }

    //Debug function used to generate some data.
    private void populateModel(){
        String runway1 = "06L";
        runwayPositions.put(runway1, new Point(100,500));
        runwayDimensions.put(runway1, new Dimension(600,60));

        String runway2 = "12L";
        runwayPositions.put(runway2, new Point(100,300));
        runwayDimensions.put(runway2, new Dimension(800,80));
    }

    public Set<String> getRunways(){
        return runwayPositions.keySet();
    }

    public Point getRunwayPos(String runwayId) {
        return runwayPositions.get(runwayId);
    }

    public Dimension getRunwayDim(String runwayId) {
        return runwayDimensions.get(runwayId);
    }

    public String getSelectedRunway() {
        return selectedRunway;
    }

    public void setSelectedRunway(String selectedRunway) {
        if(selectedRunway == "None"){
            this.selectedRunway = "";
        } else {
            this.selectedRunway = selectedRunway;
        }
        repaint();
    }
}