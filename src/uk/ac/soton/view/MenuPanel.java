package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel {

    private AppView frontEndModel;
    private JCheckBox isolateModeBox;

    MenuPanel(AppView frontEndModel){
        this.setPreferredSize(new Dimension(230,1000));
        this.frontEndModel = frontEndModel;

        //Create a new ArrayList, in order to add "None" at the start.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(frontEndModel.getRunways());

        //Create a DropDown menu using the items in menuItems.
        JComboBox runwayMenu = new JComboBox(menuItems.toArray());
        //Set the selected runway in the front end model to the option selected from the menu when that event is triggered.
        runwayMenu.addActionListener(e -> frontEndModel.setSelectedRunway(runwayMenu.getSelectedItem().toString()));
        this.add(runwayMenu);

        //Add a check box which prevents all runways but the selected one from being displayed.
        isolateModeBox = new JCheckBox("Isolate Runway");
        isolateModeBox.addActionListener(e -> frontEndModel.repaint());
        this.add(isolateModeBox);
    }

    public boolean isIsolateMode(){
        return isolateModeBox.isSelected();
    }
}
