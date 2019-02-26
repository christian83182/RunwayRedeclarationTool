package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

    //Debug function used to generate some data.
    private void populateModel(){
        String runway1 = "05L";
        runwayPositions.put(runway1, new Point(-400,0));
        runwayDimensions.put(runway1, new Dimension(1000,60));

        String runway2 = "12L";
        runwayPositions.put(runway2, new Point(-800,-500));
        runwayDimensions.put(runway2, new Dimension(2000,100));
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