package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.UIManager.*;

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

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File"); menuBar.add(fileMenu);
        JMenu settingsMenu = new JMenu("Settings"); menuBar.add(settingsMenu);
        JMenuItem importConfiguration = new JMenuItem("Import Configuration"); fileMenu.add(importConfiguration);
        JMenuItem exportConfiguration = new JMenuItem("Export Configuration"); fileMenu.add(exportConfiguration);
        JMenuItem openSettings = new JMenuItem("Settings"); settingsMenu.add(openSettings);
        this.setJMenuBar(menuBar);

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

        UIManager.put("control", new Color(80,80,80)); // Primary
        UIManager.put("nimbusBase", new Color(70,70,70)); // The colour of selectors
        UIManager.put("nimbusBlueGrey", Color.DARK_GRAY); // The colour of buttons
        UIManager.put("ScrollPane.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
        UIManager.put("List.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
        UIManager.put("TextField.background", Color.DARK_GRAY); //Background for the TextField (affects JFileChooser)
        UIManager.put("text",Color.white); //Sets Default text colour to white
        UIManager.put("Menu[Enabled].textForeground",new Color(255, 255, 255));
        UIManager.put("ComboBox.background",new Color(34, 34, 34));

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
}