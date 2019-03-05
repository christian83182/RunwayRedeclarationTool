package uk.ac.soton.view;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JCheckBox autoRotateViewBox;

    MenuPanel(AppView appView, ViewController controller){
        this.appView = appView;
        this.controller = controller;
        this.setPreferredSize(new Dimension(230,100));
        this.setLayout(new GridBagLayout());

        Integer rowCounter = 0;

        //Add a "General label"
        JLabel generalLabel = new JLabel("General");
        generalLabel.setFont(new Font("TimesRoman", Font.BOLD , 16));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,5,0);
        this.add(generalLabel, c);
        rowCounter++;

        //Add a "Selected Runway" label
        JLabel selectedRunwayLabel = new JLabel("Selected Runway:");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 2;
        this.add(selectedRunwayLabel, c);

        //Create a DropDown menu using the items in menuItems.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(controller.getRunways());
        runwayMenu = new JComboBox(menuItems.toArray());
        runwayMenu.addActionListener(e -> appView.setSelectedRunway(runwayMenu.getSelectedItem().toString()));
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = rowCounter;
        c.insets = new Insets(0,10,0,0);
        this.add(runwayMenu, c);
        rowCounter++;

        //Add a "Browse Objects" button.
        browseObstacleButton = new JButton("Browse Obstacles");
        browseObstacleButton.addActionListener(e -> {
            ObstacleBrowserWindow obstacleBrowserWindow = new ObstacleBrowserWindow();
        });
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3;
        this.add(browseObstacleButton, c);
        rowCounter++;

        //Add a "Top Down View" label
        JLabel topDownViewLabel = new JLabel("Top Down View");
        topDownViewLabel.setFont(new Font("TimesRoman", Font.BOLD , 16));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3;
        c.insets = new Insets(30,0,5,0);
        this.add(topDownViewLabel, c);
        rowCounter++;

        //Add the "isolate selected runway" option
        isolateModeBox = new JCheckBox("Isolate Selected Runway");
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(isolateModeBox, c);
        rowCounter++;

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showRunwayParametersBox, c);
        rowCounter++;

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Parameter Breakdown");
        showBreakdownBox.setEnabled(false);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showBreakdownBox, c);
        rowCounter++;

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOtherBox, c);
        rowCounter++;

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOverlayBox, c);
        rowCounter++;

        //Add the "show axis" option
        showAxisBox = new JCheckBox("Show Axis");
        showAxisBox.setSelected(true);
        showAxisBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showAxisBox, c);
        rowCounter++;

        //Add the "auto rotate runway" option
        autoRotateViewBox = new JCheckBox("Match Bearing on Selection");
        autoRotateViewBox.setSelected(true);
        autoRotateViewBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(autoRotateViewBox, c);
        rowCounter++;
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

    public boolean isAutoRotateOnSelection(){
        return autoRotateViewBox.isSelected();
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
