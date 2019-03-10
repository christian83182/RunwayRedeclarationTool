package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppMenuBar extends JMenuBar {

    ViewController controller;
    AppView appView;

    AppMenuBar(ViewController controller, AppView appView) {
        this.controller = controller;
        this.appView = appView;
        this.setBorder(BorderFactory.createMatteBorder(1,1,3,1,new Color(50, 50, 50)));

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(fileMenu);

        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(settingsMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        this.add(helpMenu);

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

        JMenuItem openSettings = new JMenuItem("Settings (Unimplemented)");
        openSettings.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        settingsMenu.add(openSettings);

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
}
