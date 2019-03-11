package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class BrowseObstaclesWindow extends JFrame {

    ViewController controller;

    BrowseObstaclesWindow(ViewController controller){
        super("Edit Predefined Obstacles");
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(500,500));
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        DefaultListModel<String> obstacleModel = new DefaultListModel<>();
        for (String currentObstacle : controller.getPredefinedObstacleIds()){
            obstacleModel.addElement(currentObstacle);
        }

        JList<String> obstacleList = new JList<>(obstacleModel);
        obstacleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        obstacleList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane obstacleListScroller = new JScrollPane(obstacleList);

        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;c.weightx = 0.8; c.weighty = 1;
        c.gridheight = 5; c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(15,10,10,0);
        this.add(obstacleListScroller, c);

        JButton addButton = new JButton("Add Obstacle");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 0.2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,2,10);
        this.add(addButton, c);

        JButton editButton = new JButton("Edit Obstacle");
        editButton.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2,10,2,10);
        this.add(editButton, c);

        JButton deleteButton = new JButton("Delete Obstacle");
        deleteButton.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2,10,2,10);
        this.add(deleteButton, c);

        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0.9; this.add(spacer, c);

        JButton closeButton = new JButton("Close");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 4; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,10,5,10);
        this.add(closeButton, c);

        closeButton.addActionListener(e -> this.dispose());

        obstacleList.addListSelectionListener(e -> {
            if(e.getValueIsAdjusting()){
                if(obstacleList.getSelectedIndex() == -1){
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                    editButton.setEnabled(true);
                }
            }
        });

        addButton.addActionListener(e -> {
            addObstacleWindow prompt = new addObstacleWindow(controller, "Add Obstacle");
            prompt.showAddDialogue();
        });

        deleteButton.addActionListener(e -> {
            String selectedObject = obstacleList.getSelectedValue();
            int areYouSureAnswer = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete \"" +selectedObject +"\"?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if(areYouSureAnswer == JOptionPane.YES_OPTION){
                controller.deleteObstacleFromList(selectedObject);
                obstacleModel.removeElement(selectedObject);
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private class addObstacleWindow extends JFrame{

        ViewController controller;

        addObstacleWindow(ViewController controller, String title){
            super(title);
            this.controller = controller;
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setLayout(new GridBagLayout());
            this.setResizable(false);
        }

        public void showAddDialogue(){
            this.setPreferredSize(new Dimension(400,230));
            GridBagConstraints c;

            JLabel  nameLabel = new JLabel("Name");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
            c.insets = new Insets(10,20,0,5);
            this.add(nameLabel,c);

            JTextField nameField = new JTextField();
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 0; c.weightx = 1; c.gridwidth = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(10,0,0,15);
            this.add(nameField,c);

            JLabel  lengthLabel = new JLabel("Length");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 1;
            c.insets = new Insets(2,20,0,5);
            this.add(lengthLabel,c);

            SpinnerNumberModel lengthModel = new SpinnerNumberModel(0,0,999,1);
            JSpinner lengthSpinner = new JSpinner(lengthModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,0,15); c.gridwidth = 3;
            this.add(lengthSpinner,c);

            JLabel  widthLabel = new JLabel("Width");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 2;
            c.insets = new Insets(2,20,0,5);
            this.add(widthLabel,c);

            SpinnerNumberModel widthModel = new SpinnerNumberModel(0,0,999,1);
            JSpinner widthSpinner = new JSpinner(widthModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,0,15); c.gridwidth = 3;
            this.add(widthSpinner,c);

            JLabel  heightLabel = new JLabel("Height");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 3;
            c.insets = new Insets(2,20,0,5);
            this.add(heightLabel,c);

            SpinnerNumberModel heightModel = new SpinnerNumberModel(0,0,999,1);
            JSpinner heightSpinner = new JSpinner(heightModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,5,15); c.gridwidth = 3;
            this.add(heightSpinner,c);

            JButton confirmButton = new JButton("Confirm");
            c = new GridBagConstraints();
            c.gridx = 2; c.gridy = 4; c.weightx = 0.5;
            c.anchor = GridBagConstraints.CENTER; c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5,0,10,0);
            this.add(confirmButton,c);

            JButton cancelButton = new JButton("Cancel");
            c = new GridBagConstraints();
            c.gridx = 3; c.gridy = 4; c.weightx = 0.5;
            c.anchor = GridBagConstraints.CENTER; c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5,10,10,15);
            this.add(cancelButton,c);

            cancelButton.addActionListener(e -> this.dispose());

            confirmButton.addActionListener(e -> {
                //do something
            });

            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
    }
}
