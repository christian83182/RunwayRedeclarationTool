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
    private JCheckBox autoRotateViewBox;

    MenuPanel(AppView appView, ViewController controller){
        this.appView = appView;
        this.controller = controller;
        this.setPreferredSize(new Dimension(270,100));
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createMatteBorder(1,1,1,5,new Color(42, 42, 42)));

        Integer rowCounter = 0;

        //Add a "General label"
        JLabel generalLabel = new JLabel("General");
        generalLabel.setFont(new Font("SansSerif", Font.BOLD , 20));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,5,0);
        this.add(generalLabel, c);
        rowCounter++;

        //Add a "Selected Runway" label
        JLabel selectedRunwayLabel = new JLabel("Selected Runway:");
        selectedRunwayLabel.setFont(new Font("SansSerif", Font.PLAIN , 16));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 2;
        this.add(selectedRunwayLabel, c);

        //Create a DropDown menu using the items in menuItems.
        List<String> menuItems = new ArrayList<>();
        menuItems.add("None");
        menuItems.addAll(controller.getRunways());
        runwayMenu = new JComboBox(menuItems.toArray());
        runwayMenu.setFont(new Font("SansSerif", Font.PLAIN , 16));
        runwayMenu.addActionListener(e -> appView.setSelectedRunway(runwayMenu.getSelectedItem().toString()));
        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = rowCounter;
        c.insets = new Insets(0,10,0,0);
        this.add(runwayMenu, c);
        rowCounter++;

        //Add a "Browse Objects" button.
        browseObstacleButton = new JButton("Browse Obstacles");
        browseObstacleButton.setFont(new Font("SansSerif", Font.PLAIN , 16));
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
        topDownViewLabel.setFont(new Font("SansSerif", Font.BOLD , 20));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3;
        c.insets = new Insets(30,0,5,0);
        this.add(topDownViewLabel, c);
        rowCounter++;

        //Add the "isolate selected runway" option
        isolateModeBox = new JCheckBox("Isolate Selected Runway");
        isolateModeBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        isolateModeBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(isolateModeBox, c);
        rowCounter++;

        //Add the "show runway parameters" option
        showRunwayParametersBox = new JCheckBox("Show Runway Parameters");
        showRunwayParametersBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showRunwayParametersBox.setSelected(true);
        showRunwayParametersBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showRunwayParametersBox, c);
        rowCounter++;

        //Add the "show breakdown" option
        showBreakdownBox = new JCheckBox("Show Parameter Breakdown");
        showBreakdownBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showBreakdownBox.setEnabled(false);
        showBreakdownBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showBreakdownBox, c);
        rowCounter++;

        //Add the "show other distances" option
        showOtherBox = new JCheckBox("Show Other Distances");
        showOtherBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showOtherBox.setSelected(true);
        showOtherBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOtherBox, c);
        rowCounter++;

        //Add the "show overlay" option
        showOverlayBox = new JCheckBox("Show Overlays");
        showOverlayBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showOverlayBox.setSelected(true);
        showOverlayBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showOverlayBox, c);
        rowCounter++;

        //Add the "show axis" option
        showAxisBox = new JCheckBox("Show Axis");
        showAxisBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
        showAxisBox.setSelected(true);
        showAxisBox.addActionListener(e -> appView.repaint());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = rowCounter;
        c.gridwidth = 3; c.anchor = GridBagConstraints.LINE_START;
        this.add(showAxisBox, c);
        rowCounter++;

        //Add the "auto rotate runway" option
        autoRotateViewBox = new JCheckBox("Match Bearing on Selection");
        autoRotateViewBox.setFont(new Font("SansSerif", Font.PLAIN , 16));
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
