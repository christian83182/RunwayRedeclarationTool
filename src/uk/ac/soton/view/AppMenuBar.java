package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;

public class AppMenuBar extends JMenuBar {

    ViewController controller;

    AppMenuBar(ViewController controller) {
        this.controller = controller;

        JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);

        JMenu settingsMenu = new JMenu("Settings");
        this.add(settingsMenu);

        JMenuItem importConfiguration = new JMenuItem("Import Configuration");
        fileMenu.add(importConfiguration);
        importConfiguration.addActionListener(e -> {

        });

        JMenuItem exportConfiguration = new JMenuItem("Export Configuration");
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
        settingsMenu.add(openSettings);
    }
}
