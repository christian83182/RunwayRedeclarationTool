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
        setLookAndFeel();

        MenuPanel menuPanel = new MenuPanel(this);
        this.add(menuPanel, BorderLayout.WEST);

        TopView2D topView = new TopView2D(this,menuPanel);
        this.add(topView,BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }

    //Debug function used to generate some test data.
    private void populateModel(){
        String runway1 = "07L";
        runwayPositions.put(runway1, new Point(-400,0));
        runwayDimensions.put(runway1, new Dimension(1200,80));

        String runway2 = "11L";
        runwayPositions.put(runway2, new Point(-800,-500));
        runwayDimensions.put(runway2, new Dimension(2000,100));
    }

    //Returns a set of strings representing runways.
    public Set<String> getRunways(){
        return runwayPositions.keySet();
    }

    //Returns the position of a runway as a Point given the name.
    public Point getRunwayPos(String runwayId) {
        return runwayPositions.get(runwayId);
    }

    //Returns the size of the runway as a Dimension given the name.
    public Dimension getRunwayDim(String runwayId) {
        return runwayDimensions.get(runwayId);
    }

    //Returns the name of the currently selected runway.
    public String getSelectedRunway() {
        return selectedRunway;
    }

    //Sets the currently selected runway to some other runway specified by name.
    public void setSelectedRunway(String selectedRunway) {
        if(selectedRunway == "None"){
            this.selectedRunway = "";
        } else {
            this.selectedRunway = selectedRunway;
        }
        repaint();
    }

    //Returns the bearing of the runway in degrees given the runway's name.
    public Integer getBearing(String runwayId){
        Integer bearing = Integer.parseInt(runwayId.substring(0,2))*10;
        return bearing;
    }

    //Set's the Look and Feel of the application to a custom theme.
    private void setLookAndFeel(){
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            UIManager.put("control", new Color(80,80,80)); // Primary
            UIManager.put("nimbusBase", new Color(70,70,70)); // The colour of selectors
            UIManager.put("nimbusBlueGrey", Color.DARK_GRAY); // The colour of buttons
            UIManager.put("ComboBox:\"ComboBox.listRenderer\".background", Color.darkGray); //Backgroud for the drop-down menu
            UIManager.put("ScrollPane.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
            UIManager.put("List.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
            UIManager.put("TextField.background", Color.DARK_GRAY); //Background for the TextField (affects JFileChooser)

            UIManager.put("text",Color.white); //Sets Default text colour to white

        } catch(Exception e){
            System.err.println("'Nimbus' Look and Feel not found, using default 'Metal'");
        }
    }
}