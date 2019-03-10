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
    private JButton browseObstacleButton;
    private JCheckBox isolateModeBox;
    private JCheckBox showRunwayParametersBox;
    private JCheckBox showBreakdownBox;
    private JCheckBox showOtherBox;
    private JCheckBox showOverlayBox;
    private JCheckBox showAxisBox;
    private JCheckBox matchViewToSelection;

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
        selectedRunwayLabel.setFont(new Font("SansSerif", Font.PLAIN , 16));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        this.add(selectedRunwayLabel, c);

        //Add a "Browse Objects" button.
        browseObstacleButton = new JButton("Browse Obstacles");
        browseObstacleButton.setFont(new Font("SansSerif", Font.PLAIN , 16));
        browseObstacleButton.addActionListener(e -> new ObstacleBrowserWindow());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(browseObstacleButton, c);

        //Add a separator to the menu.
        JSeparator firstSeparator = new JSeparator();
        firstSeparator.setOrientation(JSeparator.HORIZONTAL);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 21; c.gridwidth = 3;
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
        isolateModeBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(isolateModeBox, c);

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 50; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showRunwayParametersBox, c);

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Parameter Breakdown");
        showBreakdownBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showBreakdownBox.setEnabled(false);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 60; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showBreakdownBox, c);

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 70; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOtherBox, c);

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 80; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOverlayBox, c);

        //Add the "show axis" option
        showAxisBox = new JCheckBox("Show Axis");
        showAxisBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showAxisBox.setSelected(true);
        showAxisBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 90; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showAxisBox, c);

        //Add the "auto rotate runway" option
        matchViewToSelection = new JCheckBox("Match View to Selection");
        matchViewToSelection.setFont(new Font("SansSerif", Font.PLAIN , 16));
        matchViewToSelection.setSelected(true);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 100; c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(matchViewToSelection, c);
        matchViewToSelection.addActionListener(e -> {
            //If the box is selected and there's a runway selected then fit the view to the runway.
            if(matchViewToSelection.isSelected() && !appView.getSelectedRunway().equals("")){
                appView.getTopView().fitViewToRunway(appView.getSelectedRunway());
            }
            appView.repaint();
        });

        //Create a DropDown menu using the items in menuItems.
        //Has to go a the bottom since it uses other components which must be declared first.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(controller.getRunways());
        runwayMenu = new JComboBox(menuItems.toArray());
        runwayMenu.setFont(new Font("SansSerif", Font.PLAIN , 16));
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 10; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,0);
        this.add(runwayMenu, c);
        runwayMenu.addActionListener(e -> {
            //Update the selected runway property in AppView.
            appView.setSelectedRunway(runwayMenu.getSelectedItem().toString());
            //Fit the view to the selected runway if the appropriate options are selected.
            if(isViewMatchedToSelection() && !appView.getSelectedRunway().equals("")){
                appView.getTopView().fitViewToRunway(appView.getSelectedRunway());
            }
        });

        //Create a spacer with high weighty to push everything else up.
        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1000;
        c.gridwidth = 3; c.weighty = 1;
        this.add(spacer, c);
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

    class ObstacleBrowserWindow extends JFrame{
        ObstacleBrowserWindow(){
            super("Obstacle Browser");
            init();
        }

        public void init(){
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setPreferredSize(new Dimension(300,180));
            this.setLayout(new BorderLayout(10,0));

            JTextArea textArea = new JTextArea("Select an obstacle...");
            textArea.setEditable(false);
            this.add(textArea, BorderLayout.CENTER);

            ArrayList<String> obstacleList = new ArrayList<>(controller.getPredefinedObstacleIds());
            JComboBox obstacleComboBox = new JComboBox(obstacleList.toArray());
            obstacleComboBox.addActionListener(e -> {
                Double height = controller.getPredefinedObstacleHeight((String)obstacleComboBox.getSelectedItem());
                Double length = controller.getPredefinedObstacleLength((String)obstacleComboBox.getSelectedItem());
                Double width = controller.getPredefinedObstacleWidth((String)obstacleComboBox.getSelectedItem());
                textArea.setText("Height: " + height + "m\nWidth: " + width + "m\nLength: " + length + "m");
            });
            this.add(obstacleComboBox, BorderLayout.NORTH);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> ObstacleBrowserWindow.this.dispose());
            this.add(closeButton, BorderLayout.SOUTH);

            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
    }
}
