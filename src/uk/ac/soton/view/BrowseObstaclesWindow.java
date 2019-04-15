package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BrowseObstaclesWindow extends JFrame {

    //An instance of the controller used to link the view to the model.
    private ViewController controller;
    //An instance of DefaultListModel used to populate a JList.
    private DefaultListModel<String> obstacleModel;
    //An instance of the JList used to select obstacles.
    private JList<String> obstacleList;

    BrowseObstaclesWindow(ViewController controller){
        super("Edit Predefined Obstacles");
        this.controller = controller;

        //Populate the objectModel with the object given by the controller.
        obstacleModel = new DefaultListModel();
        for (String currentObstacle : controller.getPredefinedObstacleIds()){
            obstacleModel.addElement(currentObstacle);
        }

        obstacleList = new JList<>(obstacleModel);
        init();
    }

    //Called at the end of the constructor. Separates value initialization from UI construction.
    public void init(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(500,500));
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        //Create a new JList and place it in a ScrollPane so it can be scrollable.
        obstacleList = new JList<>(obstacleModel);
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

        //Add a text area to display the details of the selected object.
        JTextArea detailsTextField= new JTextArea();
        detailsTextField.setEditable(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 5; c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.3; c.insets = new Insets(0,10,10,0);
        this.add(detailsTextField, c);

        //Add the "close" button to the UI.
        JButton closeButton = new JButton("Close");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 5; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,10,12,10);
        c.anchor = GridBagConstraints.SOUTH;
        this.add(closeButton, c);

        //Add a listener to the close button to dispose of the window when clicked.
        closeButton.addActionListener(e -> this.dispose());

        //Add a list selection listener to the JList to enable and disable some buttons when options are enabled or disabled.
        obstacleList.addListSelectionListener(e -> {
            if(e.getValueIsAdjusting()){
                //If deselected, disable all buttons and clear the text field.
                if(obstacleList.getSelectedIndex() == -1){
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);
                    detailsTextField.setText("");
                } else {
                    //Otherwise, enable both buttons and populate the text field.
                    String obstacleId = obstacleList.getSelectedValue();
                    deleteButton.setEnabled(true);
                    editButton.setEnabled(true);
                    detailsTextField.setText("Name:     " + obstacleId +
                            "\nLength:   " + controller.getPredefinedObstacleLength(obstacleId) +
                            "\nWidth:     " + controller.getPredefinedObstacleWidth(obstacleId) +
                            "\nHeight:   " + controller.getPredefinedObstacleHeight(obstacleId));
                }
            }
        });

        //Add a listener to the add button to open a new window where a new object can be added.
        addButton.addActionListener(e ->{
            new obstacleCustomizationWindow("Add Obstacle").showAddObstacleWindow();
    });

        //Add a listener to the edit button to open a new window where its details can be changed.
        editButton.addActionListener(e -> {
            String selectedObstacle = obstacleList.getSelectedValue();
            new obstacleCustomizationWindow("Edit Obstacle").showEditObstacleWindow(selectedObstacle);
        });

        //Add an action listener to the delete button to delete the currently selected object.
        deleteButton.addActionListener(e -> {
            String selectedObject = obstacleList.getSelectedValue();
            int areYouSureAnswer = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete \"" +selectedObject +"\"?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            //If the use answered yes then delete the object and update the UI elements.
            if(areYouSureAnswer == JOptionPane.YES_OPTION){
                controller.deleteObstacleFromList(selectedObject);
                obstacleModel.removeElement(selectedObject);
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                detailsTextField.setText("");
                NotificationLogger.logger.addToLog("Removed Obstacle: '" + selectedObject + "' from the obstacle list");
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //An inner class used to open a simple dialogue to edit/add an object.
    private class obstacleCustomizationWindow extends JFrame{

        //These are member variables because they can change based on if the object is being created or edited.
        JTextField nameField;
        SpinnerNumberModel lengthModel;
        SpinnerNumberModel widthModel;
        SpinnerNumberModel heightModel;
        JButton confirmButton;

        //Instantiate the window.
        obstacleCustomizationWindow(String title){
            super(title);
            nameField = new JTextField();
            lengthModel = new SpinnerNumberModel(0,0,999,1);
            widthModel = new SpinnerNumberModel(0,0,999,1);
            heightModel = new SpinnerNumberModel(0,0,999,1);
            confirmButton = new JButton("Confirm");
        }

        //Default values are for a new object, all that is needed is the correct listener on the confirm button.
        public void showAddObstacleWindow(){
            confirmButton.addActionListener(e -> {
                //Get the values from the relevant components.
                String objectId = nameField.getText();
                Integer length = lengthModel.getNumber().intValue();
                Integer width = widthModel.getNumber().intValue();
                Integer height = heightModel.getNumber().intValue();

                boolean isValid = isUserInputValid(objectId, length, width, height);
                boolean alreadyExists = controller.getPredefinedObstacleIds().contains(objectId);

                //If the details are valid and the object name is not in use, then add it to the model and object list.
                if(isValid && !alreadyExists) {
                    NotificationLogger.logger.addToLog("New obstacle: '" + objectId +"' added to obstacle list");
                    controller.addObstacleToList(objectId, length, width, height);
                    obstacleModel.add(0,objectId);
                    obstacleList.setSelectedIndex(0);
                    dispose();

                //If the object already exists then create an error dialogue box.
                } else if (alreadyExists){
                    JOptionPane.showMessageDialog(this,
                            "The specified obstacle already exists. Names must be unique.",
                            "Invalid Object Name" ,JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Obstacle: '" + objectId +"' could not be added, obstacle already exists");

                //If the object details are invalid then create an error dialogue box.
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The values entered are not valid. Please check them and try again.",
                            "Invalid Object Values" ,JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Obstacle: '" + objectId +"' could not be added, invalid values");
                }
            });
            openDialogue();
        }

        //Values are set up according to the actual values of the object.
        public void showEditObstacleWindow(String obstacleId){
            //Change the value of the member variables so that their default values are the values of obstacle being edited.
            nameField = new JTextField(obstacleId);
            lengthModel = new SpinnerNumberModel(
                    controller.getPredefinedObstacleLength(obstacleId).intValue(),0,999,1);
            widthModel = new SpinnerNumberModel(
                    controller.getPredefinedObstacleWidth(obstacleId).intValue(),0,999,1);
            heightModel = new SpinnerNumberModel(
                    controller.getPredefinedObstacleHeight(obstacleId).intValue(),0,999,1);
            confirmButton = new JButton("Confirm");

            //Add an action listener to the button which carried out the procedure to finalize the object's edit.
            confirmButton.addActionListener(e -> {
                String newObstacleId = nameField.getText();
                Integer length = lengthModel.getNumber().intValue();
                Integer width = widthModel.getNumber().intValue();
                Integer height = heightModel.getNumber().intValue();

                //If the details are valid, then delete the old values from the list and model, and add the new ones.
                if(isUserInputValid(newObstacleId, length, width, height)) {
                    NotificationLogger.logger.addToLog("Obstacle: '" + newObstacleId +"' was edited");
                    controller.deleteObstacleFromList(obstacleId);
                    controller.addObstacleToList(newObstacleId, length, width, height);
                    obstacleModel.removeElement(obstacleId);
                    obstacleModel.add(0,newObstacleId);
                    obstacleList.setSelectedIndex(0);
                    dispose();

                //If the details are not valid then open a error dialogue box.
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The specified object could not be edited. Please check the values specified and try again.",
                            "Invalid Object Details" ,JOptionPane.ERROR_MESSAGE);
                    NotificationLogger.logger.addToLog("Obstacle: '" + newObstacleId +"' could not be edited, invalid values");
                }
            });
            openDialogue();
        }

        //Opens the dialogue with specified defaults and the specified listener on the confirm button.
        private void openDialogue(){
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

            //Adds a JTextField for the object's name to the UI.
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
            JSpinner heightSpinner = new JSpinner(heightModel);
            c = new GridBagConstraints();
            c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            c.insets = new Insets(2,0,5,15); c.gridwidth = 3;
            this.add(heightSpinner,c);

            //Adds a confirm button
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

            //Add a listener to the close button to dispose of the frame when clicked.
            cancelButton.addActionListener(e -> this.dispose());
            //the confirm button will already have a listener attached depending on which method was initially called.

            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        //Determines if the details entered are valid or not. Name must not be empty, and values between 0-999.
        private boolean isUserInputValid(String name, Integer length, Integer width, Integer height){
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

