package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPanel extends JPanel {
    private AppView appView;
    private ViewController controller;

    //UI elements
    private JComboBox runwayMenu;
    private JCheckBox isolateModeBox;
    private JCheckBox showRunwayParametersBox;
    private JCheckBox showBreakdownBox;
    private JCheckBox showOtherBox;
    private JCheckBox showOverlayBox;
    private JCheckBox matchViewToSelection;
    private JButton placeObstacleButton;
    private JButton removeObstacleButton;
    private JCheckBox showOverlayBoxSideView;

    MenuPanel(AppView appView){
        this.appView = appView;
        this.controller = appView.getController();

        this.setPreferredSize(new Dimension(270,100));
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createMatteBorder(1,1,1,5,new Color(42, 42, 42)));

        //Create three panels to group components into, with a titled border.
        JPanel generalPane =  new JPanel();
        TitledBorder generalBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"General",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        generalPane.setBorder(generalBorder);
        generalPane.setLayout(new GridBagLayout());

        JPanel topViewPane = new JPanel();
        TitledBorder topViewBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Top View",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        topViewPane.setBorder(topViewBorder);
        topViewPane.setLayout(new GridBagLayout());

        JPanel sideViewPane = new JPanel();
        TitledBorder sideViewBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Side View",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        sideViewPane.setBorder(sideViewBorder);
        sideViewPane.setLayout(new GridBagLayout());

        //Add the three panels to the menu. Add a spacer with high weighty to push everything up.
        GridBagConstraints c;
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,5,25,5);
        this.add(generalPane,c);

        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,5,25,5);
        this.add(topViewPane,c);

        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,5,25,5);
        this.add(sideViewPane,c);

        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 3; c.weighty = 1;
        this.add(new JPanel(),c);


        //Create a spacer with high weighty to push everything else up.
        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1000;
        c.gridwidth = 3; c.weighty = 1;
        sideViewPane.add(spacer, c);

        //  ---- Creating & adding elements to the general Pane ----

        //Add a "Selected Runway" label
        JLabel selectedRunwayLabel = new JLabel("Selected Runway:");
        selectedRunwayLabel.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        c.insets = new Insets(10,10,0,0);
        generalPane.add(selectedRunwayLabel, c);

        //Add the JComboBox to select a runway.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(controller.getRunways());
        runwayMenu = new JComboBox(menuItems.toArray());
        runwayMenu.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 10; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        generalPane.add(runwayMenu, c);

        //Create buttons to place and remove obstacles.
        placeObstacleButton = new JButton("Add Obstacle...");
        placeObstacleButton.setEnabled(false);
        removeObstacleButton = new JButton("Remove");
        removeObstacleButton.setEnabled(false);

        //Create a JPanel with a grid layout manager to make both buttons the same size.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(placeObstacleButton);
        buttonPanel.add(removeObstacleButton);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20;
        c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,10,10,10);
        generalPane.add(buttonPanel, c);

        //  ---- Adding elements to the top view pane ----

        //Add the "isolate selected runway" option
        isolateModeBox = new JCheckBox("Isolate Selected Runway");
        isolateModeBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,10,0,10);
        topViewPane.add(isolateModeBox, c);

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showRunwayParametersBox, c);

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Parameter Breakdown");
        showBreakdownBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showBreakdownBox.setEnabled(false);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showBreakdownBox, c);

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showOtherBox, c);

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showOverlayBox, c);

        //Add the "auto rotate runway" option
        matchViewToSelection = new JCheckBox("Match View to Selection");
        matchViewToSelection.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        matchViewToSelection.setSelected(true);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 50; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        topViewPane.add(matchViewToSelection, c);

        //  ---- Adding elements to the side view pane ----

        //Add the "show overlay" option for the side view menu
        showOverlayBoxSideView = new JCheckBox("Show Overlays");
        showOverlayBoxSideView.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOverlayBoxSideView.setSelected(true);
        showOverlayBoxSideView.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START; c.weightx = 1;
        c.insets = new Insets(10,10,10,10);
        sideViewPane.add(showOverlayBoxSideView, c);

        //  ---- Adding action listeners to elements in the menu ----

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
                appView.setSplitViewVisible(false);
            } else {
                //set the selected runway and enable the split view.
                appView.setSelectedRunway(runwayMenu.getSelectedItem().toString());
                appView.setSplitViewVisible(true);
                if(isViewMatchedToSelection()){
                    //Automatically fit the view to the selected runway, when selected.
                    appView.getTopView().fitViewToRunway(selectedRunway);
                }
                //add or enable the buttons depending on the obstacle configuration of the runway.
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
            appView.repaint();
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

    public boolean isShowOverlay(){ return showOverlayBox.isSelected(); }

    public boolean isSideViewShowOverlay() { return showOverlayBoxSideView.isSelected(); }

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
