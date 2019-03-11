package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class EditObstaclesWindow extends JFrame {

    ViewController controller;

    EditObstaclesWindow(ViewController controller){
        super("Edit Predefined Obstacles");
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(500,500));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        JButton addButton = new JButton("Add Obstacle");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0;
        c.weightx = 0.2; c.fill = GridBagConstraints.HORIZONTAL;
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

        JList<Object> obstacleList = new JList<>(controller.getPredefinedObstacleIds().toArray());
        obstacleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        obstacleList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane obstacleListScroller = new JScrollPane(obstacleList);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.weightx = 0.8; c.weighty = 1;
        c.gridheight = 5; c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(15,10,10,0);
        this.add(obstacleListScroller, c);
        obstacleList.addListSelectionListener(e -> {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        });

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

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
