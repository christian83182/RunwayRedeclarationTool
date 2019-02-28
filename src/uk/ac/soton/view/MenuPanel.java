package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel {

    private AppView appView;
    private FrontEndModel model;
    private JCheckBox isolateModeBox;

    MenuPanel(AppView appView, FrontEndModel model){
        this.appView = appView;
        this.model = model;
        this.setPreferredSize(new Dimension(230,100));
        this.setLayout(new GridBagLayout());

        //Create a new ArrayList, in order to add "None" at the start.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(model.getRunways());

        //Create a DropDown menu using the items in menuItems.
        JComboBox runwayMenu = new JComboBox(menuItems.toArray());
        //Set the selected runway in the front end model to the option selected from the menu when that event is triggered.
        runwayMenu.addActionListener(e -> appView.setSelectedRunway(runwayMenu.getSelectedItem().toString()));
        this.add(runwayMenu);

        //Add a check box which prevents all runways but the selected one from being displayed.
        isolateModeBox = new JCheckBox("Isolate Runway");
        isolateModeBox.addActionListener(e -> appView.repaint());
        this.add(isolateModeBox);
    }

    public boolean isIsolateMode(){
        return isolateModeBox.isSelected();
    }
}
