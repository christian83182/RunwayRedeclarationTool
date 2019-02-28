package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame{

    //An instance of the front end model used to store the data displayed.
    private FrontEndModel model;
    //Note that for the runwayDimensions, the length is the x value and the width is the y value.
    private String selectedRunway;

    //Constructor calls parent's constructor and initializes member variables
    public AppView(String title){
        super(title);
        model = new FrontEndModel();
        selectedRunway = "";
    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        setLookAndFeel();

        MenuPanel menuPanel = new MenuPanel(this, model);
        this.add(menuPanel, BorderLayout.WEST);

        TopView2D topView = new TopView2D(this, model, menuPanel);
        this.add(topView,BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
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