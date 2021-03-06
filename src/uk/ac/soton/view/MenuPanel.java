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
    private JCheckBox showRelevantDistancesBox;
    private JCheckBox showOtherBox;
    private JCheckBox showOverlayBox;
    private JCheckBox matchViewToSelection;
    private JCheckBox showBgImageBox;
    private JButton placeObstacleButton;
    private JButton removeObstacleButton;
    private JCheckBox runwayParametersSideView;
    private JCheckBox showBreakdownSideView;
    private JCheckBox relevantDistancesSideView;
    private JCheckBox showOverlayBoxSideView;
    private JCheckBox showOtherSideView;

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
        generalBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        generalPane.setBorder(generalBorder);
        generalPane.setLayout(new GridBagLayout());

        JPanel topViewPane = new JPanel();
        TitledBorder topViewBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Top View",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        topViewBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        topViewPane.setBorder(topViewBorder);
        topViewPane.setLayout(new GridBagLayout());

        JPanel sideViewPane = new JPanel();
        TitledBorder sideViewBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Side View",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        sideViewBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
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
        c.anchor = GridBagConstraints.LINE_START;
        generalPane.add(selectedRunwayLabel, c);

        //Add the JComboBox to select a runway.
        runwayMenu = new JComboBox();
        populateRunwayComboBox();
        runwayMenu.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 10; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,0,10);
        c.anchor = GridBagConstraints.LINE_START;
        generalPane.add(runwayMenu, c);

        //Create buttons to place and remove obstacles.
        placeObstacleButton = new JButton("Add Obstacle...");
        placeObstacleButton.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        placeObstacleButton.setEnabled(false);
        removeObstacleButton = new JButton("Remove");
        removeObstacleButton.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        removeObstacleButton.setEnabled(false);

        //Create a JPanel with a grid layout manager to make both buttons the same size.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(placeObstacleButton);
        buttonPanel.add(removeObstacleButton);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20;
        c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(20,10,0,10);
        generalPane.add(buttonPanel, c);

        //Create a button to display the parameter breakdown.
        JButton showCalculationButton = new JButton("Show Calculations");
        showCalculationButton.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showCalculationButton.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30;
        c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        generalPane.add(showCalculationButton, c);

        //Create a button to show the 3D view.
        JButton show3DViewButton = new JButton("Show 3D view");
        show3DViewButton.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40;
        c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        generalPane.add(show3DViewButton, c);

        //  ---- Adding elements to the top view pane ----

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showRunwayParametersBox, c);
        showRunwayParametersBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("'Show Runway Parameters' Toggled: '" +showRunwayParametersBox.isSelected()+ "'");
        });

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Redeclaration Breakdown");
        showBreakdownBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showBreakdownBox.setSelected(true);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showBreakdownBox, c);
        showBreakdownBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("'Show Redeclaration Breakdown' Toggled: '" +showBreakdownBox.isSelected()+ "'");
        });

        //Add the "show relevant breakdown only" option
        showRelevantDistancesBox = new JCheckBox("Show Relevant Breakdown Only");
        showRelevantDistancesBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showRelevantDistancesBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showRelevantDistancesBox, c);
        showRelevantDistancesBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("'Show Relevant Breakdown Only' Toggled: '" +showRelevantDistancesBox.isSelected()+ "'");
        });

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        topViewPane.add(showOtherBox, c);
        showOtherBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("'Show Other Distances' Toggled: '" +showOtherBox.isSelected()+ "'");
        });

        //Add the "isolate selected runway" option
        isolateModeBox = new JCheckBox("Isolate Selected Runway");
        isolateModeBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(isolateModeBox, c);
        isolateModeBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("'Isolate Selected Runway' Toggled: '" +isolateModeBox.isSelected()+ "'");
        });

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 60; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showOverlayBox, c);
        showOverlayBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Top View's 'Show Overlay' Toggled: '" +showOverlayBox.isSelected()+ "'");
        });

        //Add the "show bg image" option
        showBgImageBox = new JCheckBox("Show Background Image");
        showBgImageBox.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showBgImageBox.setSelected(true);
        showBgImageBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 70; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        topViewPane.add(showBgImageBox, c);
        showOverlayBox.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Top View's 'Show Background Image' Toggled: '" +showOverlayBox.isSelected()+ "'");
        });

        //Add the "auto rotate runway" option
        matchViewToSelection = new JCheckBox("Match View to Selection");
        matchViewToSelection.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        matchViewToSelection.setSelected(true);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 80; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        topViewPane.add(matchViewToSelection, c);
        matchViewToSelection.addActionListener(e -> {
            NotificationLogger.logger.addToLog("'Match View to Selection' Toggled: '" +matchViewToSelection.isSelected()+ "'");
        });

        //  ---- Adding elements to the side view pane ----

        //Add the "show runway parameters" option for the side view menu
        runwayParametersSideView = new JCheckBox("Show Runway Parameters");
        runwayParametersSideView.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        runwayParametersSideView.setSelected(true);
        runwayParametersSideView.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START; c.weightx = 1;
        c.insets = new Insets(10,10,0,10);
        sideViewPane.add(runwayParametersSideView, c);
        runwayParametersSideView.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Side View's 'Show Runway Parameters' Toggled: '"
                    +runwayParametersSideView.isSelected()+ "'");
        });

        //Add the "show runway parameters" option for the side view menu
        showBreakdownSideView = new JCheckBox("Show Redeclaration Breakdown");
        showBreakdownSideView.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showBreakdownSideView.setSelected(true);
        showBreakdownSideView.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.anchor = GridBagConstraints.LINE_START; c.weightx = 1;
        c.insets = new Insets(0,10,0,10);
        sideViewPane.add(showBreakdownSideView, c);
        showBreakdownSideView.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Side View's 'Show Redeclaration Breakdown' Toggled: '"
                    +showBreakdownSideView.isSelected()+ "'");
        });

        //Add the "show relevant breakdown only" option for the side view menu
        relevantDistancesSideView = new JCheckBox("Show Relevant Breakdown Only");
        relevantDistancesSideView.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        relevantDistancesSideView.setSelected(false);
        relevantDistancesSideView.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.anchor = GridBagConstraints.LINE_START; c.weightx = 1;
        c.insets = new Insets(0,10,0,10);
        sideViewPane.add(relevantDistancesSideView, c);
        relevantDistancesSideView.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Side View's 'Show Relevant Breakdown Only' Toggled: '"
                    +relevantDistancesSideView.isSelected()+ "'");
        });

        //Add the "show other distances" option for the side view menu
        showOtherSideView = new JCheckBox("Show Other Distances");
        showOtherSideView.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOtherSideView.setSelected(true);
        showOtherSideView.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.anchor = GridBagConstraints.LINE_START; c.weightx = 1;
        c.insets = new Insets(0,10,10,10);
        sideViewPane.add(showOtherSideView, c);
        showOtherSideView.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Side View's 'Show Other Distances' Toggled: '"
                    +showOtherSideView.isSelected()+ "'");
        });

        //Add the "show overlay" option for the side view menu
        showOverlayBoxSideView = new JCheckBox("Show Overlays");
        showOverlayBoxSideView.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        showOverlayBoxSideView.setSelected(true);
        showOverlayBoxSideView.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.anchor = GridBagConstraints.LINE_START; c.weightx = 1;
        c.insets = new Insets(0,10,10,10);
        sideViewPane.add(showOverlayBoxSideView, c);
        showOverlayBoxSideView.addActionListener(e -> {
            NotificationLogger.logger.addToLog("Side View's 'Show Overlay' Toggled: '" +showOverlayBoxSideView.isSelected()+ "'");
        });

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
                showCalculationButton.setEnabled(false);
                appView.setSplitViewVisible(false);
                NotificationLogger.logger.addToLog("Runway Selected: 'None'");
            } else {
                //set the selected runway and enable the split view.
                NotificationLogger.logger.addToLog("Runway Selected: '" + runwayMenu.getSelectedItem().toString() + "'");
                appView.setSelectedRunway(runwayMenu.getSelectedItem().toString());
                appView.setSplitViewVisible(true);
                showCalculationButton.setEnabled(true);
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
            NotificationLogger.logger.addToLog("Obstacle '" +
                    controller.getRunwayObstacle(appView.getSelectedRunway()) +"' removed from runway '" +
                    appView.getSelectedRunway() +"'");
            controller.removeObstacleFromRunway(appView.getSelectedRunway());
            removeObstacleButton.setEnabled(false);
            placeObstacleButton.setEnabled(true);
            appView.repaint();
        });

        showCalculationButton.addActionListener( e -> {
            new ViewCalculationsWindow(controller,appView);
        });

        show3DViewButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new View3D(appView));
        });
    }

    public boolean isIsolateMode(){
        return isolateModeBox.isSelected();
    }

    public boolean isShowBgImage(){
        return showBgImageBox.isSelected();
    }

    public boolean isShowRunwayParametersEnabled(){
        return showRunwayParametersBox.isSelected();
    }

    public boolean isShowBreakDownEnabled(){
        return showBreakdownBox.isSelected();
    }

    public boolean isShowRelevantDistOnlyEnabled(){
        return showRelevantDistancesBox.isSelected();
    }

    public boolean isShowOtherEnabled(){
        return showOtherBox.isSelected();
    }

    public boolean isShowOverlay(){ return showOverlayBox.isSelected(); }

    public boolean isSideViewShowRunwayParametersEnabled(){
        return runwayParametersSideView.isSelected();
    }

    public boolean isSideViewShowBreakdownEnabled() {
        return showBreakdownSideView.isSelected();
    }

    public boolean isSideViewShowRelevantDistOnlyEnabled() {
        return relevantDistancesSideView.isSelected();
    }

    public boolean isSideViewShowOtherEnabled(){
        return showOtherSideView.isSelected();
    }

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

    public void populateRunwayComboBox(){
        MutableComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("None");
        for(String runway: controller.getRunways()){
            model.addElement(runway);
        }
        runwayMenu.setModel(model);
        this.updateUI();
        this.repaint();
    }
}
