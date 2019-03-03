package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel {

    private AppView appView;
    private ViewController controller;
    private JCheckBox isolateModeBox;
    private JCheckBox showRunwayParametersBox;
    private JCheckBox showBreakdownBox;
    private JCheckBox showOtherBox;
    private JCheckBox showOverlayBox;
    private JCheckBox showAxisBox;

    MenuPanel(AppView appView, ViewController controller){
        this.appView = appView;
        this.controller = controller;
        this.setPreferredSize(new Dimension(230,100));
        this.setLayout(new GridBagLayout());

        //Add a "General label"
        JLabel generalLabel = new JLabel("General");
        generalLabel.setFont(new Font("TimesRoman", Font.BOLD , 16));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,5,0);
        this.add(generalLabel, c);

        //Add a "Selected Runway" label
        JLabel selectedRunwayLabel = new JLabel("Selected Runway:");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 2;
        this.add(selectedRunwayLabel, c);

        //Create a DropDown menu using the items in menuItems.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(controller.getRunways());
        JComboBox runwayMenu = new JComboBox(menuItems.toArray());
        runwayMenu.addActionListener(e -> appView.setSelectedRunway(runwayMenu.getSelectedItem().toString()));
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 1;
        c.insets = new Insets(0,10,0,0);
        this.add(runwayMenu, c);

        //Add a "Top Down View" label
        JLabel topDownViewLabel = new JLabel("Top Down View");
        topDownViewLabel.setFont(new Font("TimesRoman", Font.BOLD , 16));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 2;
        c.gridwidth = 3;
        c.insets = new Insets(30,0,5,0);
        this.add(topDownViewLabel, c);

        //Add the "isolate selected runway" option
        isolateModeBox = new JCheckBox("Isolate Selected Runway");
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 3;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(isolateModeBox, c);

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 4;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showRunwayParametersBox, c);

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Parameter Breakdown");
        showBreakdownBox.setEnabled(false);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 5;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showBreakdownBox, c);

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 6;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOtherBox, c);

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 7;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOverlayBox, c);

        //Add the "show axis" option
        showAxisBox = new JCheckBox("Show Axis");
        showAxisBox.setSelected(true);
        showAxisBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 8;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showAxisBox, c);
    }

    public boolean isIsolateMode(){
        return isolateModeBox.isSelected();
    }

    public boolean isShowRunwayParametersEnabled(){
        return showRunwayParametersBox.isSelected();
    }

    public boolean isShowBreakDownEnabled(){
        return showBreakdownBox.isSelected();
    }

    public boolean isShowOtherEnabled(){
        return showOtherBox.isSelected();
    }

    public boolean isShowOverlay(){
        return showOverlayBox.isSelected();
    }

    public boolean isShowAxis(){
        return showAxisBox.isSelected();
    }
}
