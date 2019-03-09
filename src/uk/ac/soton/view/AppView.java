package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame{

    //An instance of the front end controller used to store the data displayed.
    private ViewController controller;
    //Note that for the runwayDimensions, the length is the x value and the width is the y value.
    private String selectedRunway;

    //Constructor calls parent's constructor and initializes member variables
    public AppView(String title){
        super(title);
        this.controller = new DebugModel();
        selectedRunway = "";
    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        setLookAndFeel();

        AppMenuBar menuBar = new AppMenuBar(controller,this);
        this.setJMenuBar(menuBar);

        MenuPanel menuPanel = new MenuPanel(this, controller);
        this.add(menuPanel, BorderLayout.WEST);

        TopView2D topView = new TopView2D(this, controller, menuPanel);
        this.add(topView,BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
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

        UIManager.put("control", new Color(66, 66, 66)); // Primary
        UIManager.put("nimbusBase", new Color(106, 106, 106)); // The colour of selectors
        UIManager.put("nimbusBlueGrey", new Color(50, 50, 50)); // The colour of buttons
        UIManager.put("text",new Color(255,255,255)); //Sets Default text colour to white
        UIManager.put("ScrollPane.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
        UIManager.put("List.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
        UIManager.put("TextField.background", Color.DARK_GRAY); //Background for the TextField (affects JFileChooser)
        UIManager.put("ComboBox.background",new Color(0, 0, 0));
        UIManager.put("Menu[Enabled].textForeground",new Color(255, 255, 255));
        UIManager.put("nimbusFocus",new Color(0, 104, 208));
        UIManager.put("nimbusLightBackground",new Color(74, 74, 74));

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus not available, using default 'Metal'");
        }
    }

    public void setController(ViewController newController){
        this.controller = newController;
    }

    public ViewController getController(){
        return controller;
    }
}