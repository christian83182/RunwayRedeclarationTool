package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class BrowseObstaclesWindow extends JFrame {

    //An instance of the controller used to link the view to the model.
    ViewController controller;
    //An instance of DefaultListModel used to populate a JList.
    DefaultListModel<String> obstacleModel;

    BrowseObstaclesWindow(ViewController controller){
        super("Edit Predefined Obstacles");
        this.controller = controller;
        obstacleModel = new DefaultListModel();
        init();
    }

    //Called at the end of the constructor. Separates value initialization from UI construction.
    public void init(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(500,500));
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        //Populate the objectModel with the object given by the controller.
        for (String currentObstacle : controller.getPredefinedObstacleIds()){
            obstacleModel.addElement(currentObstacle);
        }

        //Create a new JList and place it in a ScrollPane so it can be scrollable.
        JList<String> obstacleList = new JList<>(obstacleModel);
        obstacleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        obstacleList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane obstacleListScroller = new JScrollPane(obstacleList);

        //Add the Scrollable JList to the UI.
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;c.weightx = 0.8; c.weighty = 1;
        c.gridheight = 5; c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(15,10,10,0);
        this.add(obstacleListScroller, c);

        //Add the "add obstacle" button to the UI.
        JButton addButton = new JButton("Add Obstacle");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 0.2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,2,10);
        this.add(addButton, c);

        //Add the "edit obstacle" button to the UI.
        JButton editButton = new JButton("Edit Obstacle");
        editButton.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2,10,2,10);
        this.add(editButton, c);

        //Add the "delete obstacle" button to the UI.
        JButton deleteButton = new JButton("Delete Obstacle");
        deleteButton.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2,10,2,10);
        this.add(deleteButton, c);

        //Add a spacer so the close button appears at the bottom of the screen.
        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0.9; this.add(spacer, c);

        //Add the "close" button to the UI.
        JButton closeButton = new JButton("Close");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 4; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,10,5,10);
        this.add(closeButton, c);

        //Add a listener to the close button to dispose of the window when clicked.
        closeButton.addActionListener(e -> this.dispose());

        //Add a list selection listener to the JList to enable and disable some buttons when options are enabled or disabled.
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

        //Add an action listener to the add button to open a new window where a new object can be added.
        addButton.addActionListener(e -> {
            addObstacleWindow prompt = new addObstacleWindow("Add Obstacle");
        });

        //Add an action listener to the delete button to delete the currently selected object.
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

    //An inner class used to open a simple dialogue to edit/add an object.
    private class addObstacleWindow extends JFrame{

        //Instantiate the window.
        addObstacleWindow(String title){
            super(title);
            init();
        }

        //Configures and opens the dialogue.
        public void init(){
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setLayout(new GridBagLayout());
            this.setResizable(false);
            this.setPreferredSize(new Dimension(400,230));
            GridBagConstraints c;

            //Adds a name label to the UI.
            JLabel  nameLabel = new JLabel("Name");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
            c.insets = new Insets(10,20,0,5);
            this.add(nameLabel,c);

            //Adds a Jtextfield for the object's name to the UI.
            JTextField nameField = new JTextField();
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 0; c.weightx = 1; c.gridwidth = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(10,0,0,15);
            this.add(nameField,c);

            //Adds a Length label to the UI.
            JLabel  lengthLabel = new JLabel("Length");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 1;
            c.insets = new Insets(2,20,0,5);
            this.add(lengthLabel,c);

            //Adds a spinner for the length property of the new object.
            SpinnerNumberModel lengthModel = new SpinnerNumberModel(0,0,999,0.1);
            JSpinner lengthSpinner = new JSpinner(lengthModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,0,15); c.gridwidth = 3;
            this.add(lengthSpinner,c);

            //Adds a Width label to the UI.
            JLabel  widthLabel = new JLabel("Width");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 2;
            c.insets = new Insets(2,20,0,5);
            this.add(widthLabel,c);

            //Adds a spinner for the width property of the new object.
            SpinnerNumberModel widthModel = new SpinnerNumberModel(0,0,999,0.1);
            JSpinner widthSpinner = new JSpinner(widthModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,0,15); c.gridwidth = 3;
            this.add(widthSpinner,c);

            //Adds a Height label to the UI.
            JLabel  heightLabel = new JLabel("Height");
            c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 3;
            c.insets = new Insets(2,20,0,5);
            this.add(heightLabel,c);

            //Adds a spinner for the height property of the new object.
            SpinnerNumberModel heightModel = new SpinnerNumberModel(0,0,999,0.1);
            JSpinner heightSpinner = new JSpinner(heightModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,5,15); c.gridwidth = 3;
            this.add(heightSpinner,c);

            //Adds a confirm button
            JButton confirmButton = new JButton("Confirm");
            c = new GridBagConstraints();
            c.gridx = 2; c.gridy = 4; c.weightx = 0.5;
            c.anchor = GridBagConstraints.CENTER; c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5,0,10,0);
            this.add(confirmButton,c);

            //Adds a cancel button.
            JButton cancelButton = new JButton("Cancel");
            c = new GridBagConstraints();
            c.gridx = 3; c.gridy = 4; c.weightx = 0.5;
            c.anchor = GridBagConstraints.CENTER; c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5,10,10,15);
            this.add(cancelButton,c);

            cancelButton.addActionListener(e -> this.dispose());

            confirmButton.addActionListener(e -> {
                String objectId = nameField.getText();
                Double length = lengthModel.getNumber().doubleValue();
                Double width = widthModel.getNumber().doubleValue();
                Double height = heightModel.getNumber().doubleValue();

                if(isUserInputValid(objectId, length, width, height)) {
                    controller.addObstacleToList(objectId, length, width, height);
                    obstacleModel.add(0,objectId);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The specified object could not be created. Please check the details and try again.",
                            "Invalid Object Details" ,JOptionPane.ERROR_MESSAGE);
                }
            });

            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        private boolean isUserInputValid(String name, Double length, Double width, Double height){
            if(name.isEmpty()){
                return false;
            } else if(length <= 0 || width <= 0 || height <= 0){
                return false;
            } else if(length > 999 || width > 999 || width > 999){
                return false;
            }
            return true;
        }
    }
}
