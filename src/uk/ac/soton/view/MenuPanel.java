package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel {
    private AppView appView;
    private ViewController controller;

    private JComboBox runwayMenu;
    private JCheckBox isolateModeBox;
    private JCheckBox showRunwayParametersBox;
    private JCheckBox showBreakdownBox;
    private JCheckBox showOtherBox;
    private JCheckBox showOverlayBox;
    private JCheckBox showAxisBox;
    private JCheckBox matchViewToSelection;
    private JButton placeObstacleButton;
    private JButton removeObstacleButton;


    MenuPanel(AppView appView){
        this.appView = appView;
        this.controller = appView.getController();

        this.setPreferredSize(new Dimension(270,100));
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createMatteBorder(1,1,1,5,new Color(42, 42, 42)));

        //Add a "General label"
        JLabel generalLabel = new JLabel("General");
        generalLabel.setFont(new Font("SansSerif", Font.BOLD , 20));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.gridwidth = 3;
        c.insets = new Insets(25,0,0,0);
        this.add(generalLabel, c);

        //Add a "Selected Runway" label
        JLabel selectedRunwayLabel = new JLabel("Selected Runway:");
        selectedRunwayLabel.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        this.add(selectedRunwayLabel, c);

        //Has to go a the bottom since it uses other components which must be declared first.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(controller.getRunways());
        runwayMenu = new JComboBox(menuItems.toArray());
        runwayMenu.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 10; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,0);
        this.add(runwayMenu, c);

        //Create buttons to place and remove obstacles.
        placeObstacleButton = new JButton("Add Obstacle");
        placeObstacleButton.setEnabled(false);
        removeObstacleButton = new JButton("Remove");
        removeObstacleButton.setEnabled(false);

        //Create a JPanel with a grid layout manager to make both buttons the same size.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(placeObstacleButton);
        buttonPanel.add(removeObstacleButton);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 16;
        c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
        this.add(buttonPanel, c);

        //Add a separator to the menu.
        JSeparator firstSeparator = new JSeparator();
        firstSeparator.setOrientation(JSeparator.HORIZONTAL);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15,0,15,0);
        this.add(firstSeparator, c);

        //Add a "Top Down View" label
        JLabel topDownViewLabel = new JLabel("Top Down View");
        topDownViewLabel.setFont(new Font("SansSerif", Font.BOLD , 20));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.gridwidth = 3;
        this.add(topDownViewLabel, c);

        //Add the "isolate selected runway" option
        isolateModeBox = new JCheckBox("Isolate Selected Runway");
        isolateModeBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(isolateModeBox, c);

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 50; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showRunwayParametersBox, c);

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Parameter Breakdown");
        showBreakdownBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showBreakdownBox.setEnabled(false);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 60; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showBreakdownBox, c);

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 70; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOtherBox, c);

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 80; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOverlayBox, c);

        //Add the "show axis" option
        showAxisBox = new JCheckBox("Show Axis");
        showAxisBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showAxisBox.setSelected(true);
        showAxisBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 90; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showAxisBox, c);

        //Add the "auto rotate runway" option
        matchViewToSelection = new JCheckBox("Match View to Selection");
        matchViewToSelection.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        matchViewToSelection.setSelected(true);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 100; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(matchViewToSelection, c);

        //Creates a test button to disable the split view.
        JButton testButton = new JButton("Disable Split View");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 110;
        c.gridwidth = 3; c.insets = new Insets(50,0,0,0);
        this.add(testButton, c);

        //Creates another test button to enable the split view.
        JButton testButton2 = new JButton("Enable Split View");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 120;
        c.gridwidth = 3;
        this.add(testButton2, c);

        //Create a spacer with high weighty to push everything else up.
        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1000;
        c.gridwidth = 3; c.weighty = 1;
        this.add(spacer, c);

        //Add an action listener to the matchViewToSelection check box, to fit the view to the runway on selection.
        matchViewToSelection.addActionListener(e -> {
            //If the box is selected and there's a runway selected then fit the view to the runway.
            if(matchViewToSelection.isSelected() && !appView.getSelectedRunway().equals("")){
                appView.getTopView().fitViewToRunway(appView.getSelectedRunway());
            }
            appView.repaint();
        });

        //Add an action listener to the runway menu to enable and disable options, and update UI elements.
        runwayMenu.addActionListener(e -> {
            String selectedRunway = runwayMenu.getSelectedItem().toString();
            if(selectedRunway.equals("None")){
                appView.setSelectedRunway("");
                placeObstacleButton.setEnabled(false);
                removeObstacleButton.setEnabled(false);
            } else {
                appView.setSelectedRunway(runwayMenu.getSelectedItem().toString());
                if(isViewMatchedToSelection()){
                    //Automatically fit the view to the selected runway, when selected.
                    appView.getTopView().fitViewToRunway(selectedRunway);
                }
                if(controller.getRunwayObstacle(selectedRunway).equals("")){
                    placeObstacleButton.setEnabled(true);
                    removeObstacleButton.setEnabled(false);
                } else {
                    placeObstacleButton.setEnabled(false);
                    removeObstacleButton.setEnabled(true);
                }
            }
        });

        //Add an action listener to open a new PlaceObstacleWindow.
        placeObstacleButton.addActionListener(e -> {
            new PlaceObstacleWindow(controller,appView);
        });

        //Add an action listener to the remove button to remove the obstacle from the current runway.
        removeObstacleButton.addActionListener(e -> {
            controller.removeObstacleFromRunway(appView.getSelectedRunway());
            removeObstacleButton.setEnabled(false);
            placeObstacleButton.setEnabled(true);
            appView.getTopView().repaint();
        });

        //Action listener to disable the split view.
        testButton.addActionListener(e ->{
            appView.setSplitViewVisible(false);
        });

        //Action listener to enable the split view.
        testButton2.addActionListener(e ->{
            appView.setSplitViewVisible(true);
        });

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

    public boolean isViewMatchedToSelection(){
        return matchViewToSelection.isSelected();
    }

    public void setPlaceButtonEnabled(boolean isEnabled){
        this.placeObstacleButton.setEnabled(isEnabled);
    }

    public void setRemoveButtonEnabled(boolean isEnabled){
        this.removeObstacleButton.setEnabled(isEnabled);
    }
}
