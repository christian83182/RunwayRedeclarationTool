package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class PlaceObstacleWindow extends JFrame {

    ViewController controller;
    AppView appView;

    PlaceObstacleWindow(ViewController controller, AppView appView){
        super("Place Obstacle");
        this.controller = controller;
        this.appView = appView;
        init();
    }

    private void init(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(400,230));
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        JLabel obstacleLabel = new JLabel("Obstacle:");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.gridwidth = 1;
        c.insets = new Insets(10,15,0,5);
        c.anchor = GridBagConstraints.LINE_START;
        this.add(obstacleLabel,c);

        List<String> obstacles = new ArrayList<>(controller.getPredefinedObstacleIds());
        JComboBox<String> obstacleComboBox = new JComboBox(obstacles.toArray());
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,0,0,15);
        this.add(obstacleComboBox,c);

        JLabel horizontalDistanceLabel = new JLabel("Distance From Runway Start");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2;
        c.insets = new Insets(0,15,0,10);
        c.anchor = GridBagConstraints.LINE_START;
        this.add(horizontalDistanceLabel,c);

        JLabel verticalDistanceLabel = new JLabel("Distance From Centerline");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.insets = new Insets(0,15,10,10);
        c.anchor = GridBagConstraints.LINE_START;
        this.add(verticalDistanceLabel,c);

        Dimension runwayDim = controller.getRunwayDim(appView.getSelectedRunway());
        SpinnerNumberModel edgeDistanceModel = new SpinnerNumberModel(0,0,runwayDim.width,0.1);
        JSpinner edgeDistanceSpinner = new JSpinner(edgeDistanceModel);
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 1; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,15);
        this.add(edgeDistanceSpinner,c);

        SpinnerNumberModel centerlineDistanceModel = new SpinnerNumberModel(0,-999,999,0.1);
        JSpinner centerlineDistanceSpinner = new JSpinner(centerlineDistanceModel);
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 2; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,10,15);
        this.add(centerlineDistanceSpinner,c);

        JButton browseObstaclesButton = new JButton("Edit Obstacles");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,15,15,0);
        c.anchor = GridBagConstraints.PAGE_END;
        this.add(browseObstaclesButton,c);

        JButton cancelButton = new JButton("Cancel");
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 3; c.gridwidth = 1; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,15,0);
        c.anchor = GridBagConstraints.PAGE_END;
        this.add(cancelButton,c);

        JButton confirmButton = new JButton("Confirm");
        c = new GridBagConstraints();
        c.gridx = 3; c.gridy = 3; c.gridwidth = 1; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,15,15);
        c.anchor = GridBagConstraints.PAGE_END;
        this.add(confirmButton,c);

        cancelButton.addActionListener(e -> this.dispose());

        browseObstaclesButton.addActionListener(e -> {
            BrowseObstaclesWindow browseWindow = new BrowseObstaclesWindow(controller);
            browseWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosing(e);
                    obstacleComboBox.removeAllItems();
                    for (String obstacleId : new ArrayList<>(controller.getPredefinedObstacleIds())){
                        obstacleComboBox.addItem(obstacleId);
                    }
                    PlaceObstacleWindow.this.repaint();
                }
            });
        });

        confirmButton.addActionListener(e ->{
            String obstacleId = obstacleComboBox.getSelectedItem().toString();
            Integer centerlineDistance = centerlineDistanceModel.getNumber().intValue();
            Integer edgeDistance = edgeDistanceModel.getNumber().intValue();
            if(isInputValid(obstacleId, centerlineDistance, edgeDistance)){
                controller.addObstacleToRunway(appView.getSelectedRunway(), obstacleId, centerlineDistance, edgeDistance);
                appView.getMenuPanel().setPlaceButtonEnabled(false);
                appView.getMenuPanel().setRemoveButtonEnabled(true);
                appView.getTopView().repaint();
                PlaceObstacleWindow.this.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Could not add obstacle to runway. Please check the details you entered and try again.",
                        "Invalid Specification",JOptionPane.ERROR_MESSAGE);
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private boolean isInputValid(String obstacleId, Integer centerlineDistance, Integer edgeDistance){
        if(!controller.getPredefinedObstacleIds().contains(obstacleId)){
            return false;
        } else if (edgeDistance < 0){
            return false;
        } else {
            return true;
        }
    }
}
