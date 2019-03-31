package uk.ac.soton.view;
import org.xml.sax.SAXException;
import uk.ac.soton.controller.ImporterException;
import uk.ac.soton.controller.ViewController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomMenuBar extends JMenuBar {

    ViewController controller;
    AppView appView;

    CustomMenuBar(ViewController controller, AppView appView) {
        this.controller = controller;
        this.appView = appView;
        this.setBorder(BorderFactory.createMatteBorder(1,1,3,1,new Color(50, 50, 50)));

        //Creates the "File" menu in the menu bar.
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(fileMenu);

        //Creates the "Edit" menu in the menu bar.
        JMenu editMenu = new JMenu("Edit");
        editMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(editMenu);

        //Creates the "Settings" menu in the menu bar.
        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(settingsMenu);

        //Creates the "Help" menu in the menu bar.
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(helpMenu);

        //Adding the "import configuration" option to the file menu.
        JMenuItem importConfiguration = new JMenuItem("Import Configuration...");
        importConfiguration.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(importConfiguration);
        importConfiguration.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try {
                    controller.importAirfieldConfiguration(fileChooser.getSelectedFile().getAbsolutePath());
                    NotificationLogger.logger.addToLog("Configuration '"+ fileChooser.getSelectedFile().getName()+"' was imported");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(fileChooser,
                            "There was an issue importing that configuration: '" + e1.getMessage() + "'",
                            "Import Error" ,JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Configuration '"+ fileChooser.getSelectedFile().getName()+"' could not be imported");
                }
                appView.repaint();
            }
        });

        //Adding the "export configuration" option to the file menu.
        JMenuItem exportConfiguration = new JMenuItem("Export Configuration...");
        exportConfiguration.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportConfiguration);
        exportConfiguration.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                controller.exportAirfieldConfiguration(fileChooser.getSelectedFile().getAbsolutePath());
                NotificationLogger.logger.addToLog("The current configuration was exported as '" + fileChooser.getSelectedFile().getName()+"'");
            }
        });

        //Adding the "export top view button"
        fileMenu.addSeparator();
        JMenuItem exportTopViewImage = new JMenuItem("Export Top View...");
        exportTopViewImage.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportTopViewImage);
        exportTopViewImage.addActionListener(e -> {
            //If the image has size 0 in either dimension then show an error prompt.
            if(appView.getTopView().getWidth() == 0 || appView.getTopView().getHeight() == 0){
                JOptionPane.showMessageDialog(null, "Width and Height cannot be 0","Could Not Save Image",JOptionPane.ERROR_MESSAGE);
                NotificationLogger.logger.addToLog("Top View could not be exported");
            } else {
                BufferedImage image = appView.getTopView().generateSnapshot();
                saveImage(image);
            }
        });

        //Adding the "export side view button".
        JMenuItem exportSideView = new JMenuItem("Export Side View...");
        exportSideView.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportSideView);
        exportSideView.addActionListener(e -> {
            if(appView.getSideView().getWidth() == 0 || appView.getSideView().getHeight() == 0){
                JOptionPane.showMessageDialog(null, "Width and Height cannot be 0","Could Not Save Image",JOptionPane.ERROR_MESSAGE);
                NotificationLogger.logger.addToLog("Side View could not be exported");
            } else {
                BufferedImage image = appView.getSideView().generateSnapshot();
                saveImage(image);
            }
        });

        //Adding the "edit obstacles" option to the edit menu.
        JMenuItem editObstaclesMenu = new JMenuItem("Edit Predefined Obstacles...");
        editObstaclesMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(editObstaclesMenu);
        editObstaclesMenu.addActionListener(e -> new BrowseObstaclesWindow(controller));

        //Adding the "Set Default Colour Theme" option to the settings menu.
        JMenuItem setDefaultTheme = new JMenuItem("Set Default Colour Theme");
        setDefaultTheme.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        settingsMenu.add(setDefaultTheme);
        setDefaultTheme.addActionListener(e -> {
            Settings.setDefaultTheme();
            NotificationLogger.logger.addToLog("Colour Theme set to: 'Default'");
            appView.repaint();
        });

        //Adding the "Set Red-Green Coloublind Theme" option to the settings menu.
        JMenuItem setRedGreenColourblindTheme = new JMenuItem("Set Red-Green Coloublind Theme");
        setRedGreenColourblindTheme.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        settingsMenu.add(setRedGreenColourblindTheme);
        setRedGreenColourblindTheme.addActionListener(e -> {
            Settings.setRedGreenColourblindTheme();
            NotificationLogger.logger.addToLog("Colour Theme set to: 'Red-Green'");
            appView.repaint();
        });

        //Adding the "Set Blue-Yellow Coloublind Theme" option to the settings menu.
        JMenuItem setYellowBlueColourblindTheme = new JMenuItem("Set Blue-Yellow Coloublind Theme");
        setYellowBlueColourblindTheme.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        settingsMenu.add(setYellowBlueColourblindTheme);
        setYellowBlueColourblindTheme.addActionListener(e -> {
            Settings.setBlueYellowColourblindTheme();
            NotificationLogger.logger.addToLog("Colour Theme set to: 'Blue-Yellow'");
            appView.repaint();
        });

        //Adding the "open help" option to the help menu.
        JMenuItem openHelp = new JMenuItem("Open Help...");
        openHelp.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        helpMenu.add(openHelp);
        openHelp.addActionListener(e -> {
            String helpMessage = "This is a generic help menu.\nEventually it will contain detailed" +
                    " documentation on the functions of the program.\n This may also not exist, and " +
                    "just open a text file or PDF instead, as formatting a text string like this can become " +
                    "cumbersome.\n\nFor now:\n1) Use the drop down menu in the left tool bar to select a runway. " +
                    "\n2) Selecting a runway will highlight it in the view and display various of its attributes " +
                    "including TORA, TODA, ASDA, LDA, width, length, clearway, stopway, strip, etc.\n3) Options exist in" +
                    " the left panel to enable or disable most of the information displayed.\n4) Select File>Export Configuration" +
                    " to export the current airfield's configuration. Similarly, use File>Import Configuration to import an " +
                    "existing configuration.";
            new ScrollableTextWindow("Application Help", new Dimension(400,600), helpMessage);
        });
    }

    private void saveImage(BufferedImage image){
        //Create a new JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        //Open a save dialogue create a file from the selected file.
        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath()+".png");
            try {
                //Write the image to the file.
                ImageIO.write(image,"png",file.getAbsoluteFile());
                NotificationLogger.logger.addToLog("View exported as was exported as '" + fileChooser.getSelectedFile().getName()+"'");
            } catch (IOException e1) {
                e1.printStackTrace();
                NotificationLogger.logger.addToLog("View could not be exported");
            }
        }
    }
}
