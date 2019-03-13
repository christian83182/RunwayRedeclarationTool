package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.imageio.ImageIO;
import javax.swing.*;
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
        JMenuItem importConfiguration = new JMenuItem("Import Configuration");
        importConfiguration.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(importConfiguration);
        importConfiguration.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                controller.importAirfieldConfiguration(fileChooser.getSelectedFile().getAbsolutePath());
                appView.repaint();
            }
        });

        //Adding the "export configuration" option to the file menu.
        JMenuItem exportConfiguration = new JMenuItem("Export Configuration");
        exportConfiguration.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportConfiguration);
        exportConfiguration.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                controller.exportAirfieldConfiguration(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        //Adding the "export top view button"
        fileMenu.addSeparator();
        JMenuItem exportTopViewImage = new JMenuItem("Export Top View");
        exportTopViewImage.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportTopViewImage);
        exportTopViewImage.addActionListener(e -> {
            //If the image has size 0 in either dimension then show an error prompt.
            if(appView.getTopView().getWidth() == 0 || appView.getTopView().getHeight() == 0){
                JOptionPane.showMessageDialog(null, "Width and Height cannot be 0","Could Not Save Image",JOptionPane.ERROR_MESSAGE);
            } else {
                BufferedImage image = appView.getTopView().generateSnapshot();
                saveImage(image);
            }
        });

        //Adding the "export side view button".
        JMenuItem exportSideView = new JMenuItem("Export Side View");
        exportSideView.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        fileMenu.add(exportSideView);
        exportSideView.addActionListener(e -> {
            if(appView.getSideView().getWidth() == 0 || appView.getSideView().getHeight() == 0){
                JOptionPane.showMessageDialog(null, "Width and Height cannot be 0","Could Not Save Image",JOptionPane.ERROR_MESSAGE);
            } else {
                BufferedImage image = appView.getSideView().generateSnapshot();
                saveImage(image);
            }
        });

        //Adding the "edit obstacles" option to the edit menu.
        JMenuItem editObstaclesMenu = new JMenuItem("Edit Predefined Obstacles");
        editObstaclesMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        editMenu.add(editObstaclesMenu);
        editObstaclesMenu.addActionListener(e -> new BrowseObstaclesWindow(controller));

        //Adding the "settings" option to the settings menu.
        JMenuItem openSettings = new JMenuItem("Settings (Unimplemented)");
        openSettings.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        settingsMenu.add(openSettings);

        //Adding the "open help" option to the help menu.
        JMenuItem openHelp = new JMenuItem("Open Help");
        openHelp.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        helpMenu.add(openHelp);
        openHelp.addActionListener(e -> {
            JFrame helpMenuFrame = new JFrame("Application Help");
            helpMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            helpMenuFrame.setPreferredSize(new Dimension(700,800));

            String helpMessage = "This is a generic help menu.\nEventually it will contain detailed" +
                    " documentation on the functions of the program.\n This may also not exist, and " +
                    "just open a text file or PDF instead, as formatting a text string like this can become " +
                    "cumbersome.\n\nFor now:\n1) Use the drop down menu in the left tool bar to select a runway. " +
                    "\n2) Selecting a runway will highlight it in the view and display various of its attributes " +
                    "including TORA, TODA, ASDA, LDA, width, length, clearway, stopway, strip, etc.\n3) Options exist in" +
                    " the left panel to enable or disable most of the information displayed.\n4) Select File>Export Configuration" +
                    " to export the current airfield's configuration. Similarly, use File>Import Configuration to import an " +
                    "existing configuration.";

            helpMenuFrame.setLayout(new BorderLayout());
            JTextArea textArea = new JTextArea(helpMessage);
            textArea.setEditable(false);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollArea = new JScrollPane(textArea);
            helpMenuFrame.add(scrollArea,BorderLayout.CENTER);

            helpMenuFrame.pack();
            helpMenuFrame.setLocationRelativeTo(null);
            helpMenuFrame.setVisible(true);
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
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
