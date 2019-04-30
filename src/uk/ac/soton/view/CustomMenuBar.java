package uk.ac.soton.view;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.controller.ViewController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
        importConfiguration.addActionListener(e -> importConfiguration());

        //Adding the "export configuration" option to the file menu.
        JMenuItem exportConfiguration = new JMenuItem("Export Configuration...");
        exportConfiguration.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportConfiguration);
        exportConfiguration.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(
                    "XML (*.xml)", "xml");
            fileChooser.setFileFilter(xmlFilter);
            fileChooser.setDialogTitle("Save XML Configuration");
            fileChooser.setFont(Settings.MENU_BAR_DEFAULT_FONT);
            int returnVal = fileChooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try {
                    controller.exportAirfieldConfiguration(fileChooser.getSelectedFile().getAbsolutePath());
                    NotificationLogger.logger.addToLog("The current configuration was exported as '" + fileChooser.getSelectedFile().getName()+"'");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(fileChooser,
                            "There was an issue exporting that configuration: '" + e1.getMessage() + "'",
                            "Export Error" ,JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Configuration '"+ fileChooser.getSelectedFile().getName()+"' could not be exported");
                }
            }
        });

        //Adding the "save parameters button"
        JMenuItem saveParameters = new JMenuItem("Save Parameters...");
        saveParameters.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(saveParameters);
        saveParameters.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter txtFilter = new FileNameExtensionFilter(
                    "Text Documents (*.txt)", "txt");
            fileChooser.setFileFilter(txtFilter);
            fileChooser.setFont(Settings.MENU_BAR_DEFAULT_FONT);
            int returnVal = fileChooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try {
                    controller.saveRunwayParameters(fileChooser.getSelectedFile().getAbsolutePath());
                    NotificationLogger.logger.addToLog("The runway parameters were saved as '" + fileChooser.getSelectedFile().getName()+".txt'");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(fileChooser,
                            "There was an issue saving the parameters '" + e1.getMessage() + "'",
                            "Export Error" ,JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Parameters '"+ fileChooser.getSelectedFile().getName()+"' could not be exported");
                }
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

        //Adding the Edit Blast Protection option to the edit menu.
        JMenuItem editBlastMenu = new JMenuItem("Edit Blast Protection...");
        editBlastMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(editBlastMenu);
        editBlastMenu.addActionListener(e -> {
            blastValueInputLoop();
            appView.repaint();
        });

        //Adding the Edit Min Angle of Decent option to the edit menu.
        JMenuItem editMinAngleOfDecent = new JMenuItem("Edit Minimum Angle of Decent...");
        editMinAngleOfDecent.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(editMinAngleOfDecent);
        editMinAngleOfDecent.addActionListener(e -> {
            minAngleOfDecentInputLoop();
            appView.repaint();
        });

        //Adding the "edit obstacles" option to the edit menu.
        JMenuItem editObstaclesMenu = new JMenuItem("Edit Predefined Obstacles...");
        editObstaclesMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(editObstaclesMenu);
        editObstaclesMenu.addActionListener(e -> new BrowseObstaclesWindow(controller));

        //Adding the Configure Background Image option
        JMenuItem configureBgMenu = new JMenuItem("Configure Background Image...");
        configureBgMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(configureBgMenu);
        configureBgMenu.addActionListener(e -> new BackgroundConfigWindow(appView));

        //Adding the Configure Background Image option
        JMenuItem configureAirfield = new JMenuItem("Configure Airfield...");
        configureAirfield.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(configureAirfield);
        configureAirfield.addActionListener(e -> new RunwayConfigWindow(appView));

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

    public void importConfiguration(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(
                "XML (*.xml)", "xml");
        fileChooser.setFileFilter(xmlFilter);
        fileChooser.setDialogTitle("Open XML Configuration");
        fileChooser.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            try {
                controller.importAirfieldConfiguration(fileChooser.getSelectedFile().getAbsolutePath());
                appView.getMenuPanel().populateRunwayComboBox();
                NotificationLogger.logger.addToLog("Configuration '"+ fileChooser.getSelectedFile().getName()+"' was imported");
                appView.setSelectedRunway("");
                appView.getTopView().fitViewToAllRunways();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(fileChooser,
                        "There was an issue importing that configuration: '" + e1.getMessage() + "'",
                        "Import Error" ,JOptionPane.ERROR_MESSAGE);
                NotificationLogger.logger.addToLog("Configuration '"+ fileChooser.getSelectedFile().getName()+"' could not be imported");
            }
            appView.repaint();
        }
    }

    // TODO different image export formats
    private void saveImage(BufferedImage image){
        //Create a new JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter(
                "JPEG (*.jpg;*.jpeg;*.jpe;*.jfif)", "jpg", "jpeg", "jpe", "jfif");
        fileChooser.addChoosableFileFilter(jpegFilter);
        FileNameExtensionFilter gifFilter = new FileNameExtensionFilter(
                "GIF (*.gif)", "gif");
        fileChooser.addChoosableFileFilter(gifFilter);
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
                "PNG (*.png)", "png");
        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.setDialogTitle("Save Image");
        fileChooser.setFont(Settings.MENU_BAR_DEFAULT_FONT);
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

    private void blastValueInputLoop(){
        while(true){
            try{
                String input = JOptionPane.showInputDialog(
                        appView,
                        "Enter Blast Protection value (100m-1000m):",
                        "Edit Blast Protection",
                        JOptionPane.PLAIN_MESSAGE
                );

                if(input == null){
                    return;
                }
                Integer newValue = Integer.parseInt(input);

                while(newValue < 100 || newValue > 1000){
                    JOptionPane.showMessageDialog(appView,
                            "Please enter a value between 100-1000.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Blast protection value input was not a valid input.");

                    input = JOptionPane.showInputDialog(
                            appView,
                            "Enter Blast Protection value:",
                            "Edit Blast Protection",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if(input == null){
                        return;
                    }
                    newValue = Integer.parseInt(input);
                }

                controller.setBlastingDistance(newValue);
                NotificationLogger.logger.addToLog("Blast protection was set to: '" +controller.getBlastingDistance()+ "'");
                break;
            }
            catch(NumberFormatException nfe){

                JOptionPane.showMessageDialog(appView,
                        "Non-digit characters recognised, please enter a valid number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void minAngleOfDecentInputLoop(){
        while(true){
            try{
                String input = JOptionPane.showInputDialog(appView, "Enter Minimum Angle of Decent (1m-500m):", "Edit Minimum Angle of Decent", JOptionPane.PLAIN_MESSAGE);

                if(input == null){
                    return;
                }
                Integer newValue = Integer.parseInt(input);

                while(newValue < 1 || newValue > 500){
                    JOptionPane.showMessageDialog(appView, "Please enter a value between 1-500.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Minimum Angle of Decent could not be changed - invalid input.");
                    input = JOptionPane.showInputDialog(appView, "Enter Minimum Angle of Decent (1m-500m):", "Edit Minimum Angle of Decent", JOptionPane.PLAIN_MESSAGE);

                    if(input == null){
                        return;
                    }
                    newValue = Integer.parseInt(input);
                }

                Airfield.setMinAngleOfDecent(newValue);
                NotificationLogger.logger.addToLog("Minimum Angle of Decent was set to: '" +newValue+ "'");
                break;
            }
            catch(NumberFormatException nfe){
                JOptionPane.showMessageDialog(appView, "Non-digit characters recognised, please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
