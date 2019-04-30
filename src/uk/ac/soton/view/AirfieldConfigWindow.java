package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class AirfieldConfigWindow extends JDialog {

    AppView appView;
    ViewController controller;

    AirfieldConfigWindow(AppView appView){
        this.appView = appView;
        this.controller = appView.getController();
        init();
    }

    private void init(){
        this.setTitle("Airfield Configuration Editor");
        this.setModal(true);
        this.setPreferredSize(new Dimension(1600,900));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.setContentPane(root);
        root.setRightComponent(prepareMenu());
        root.setLeftComponent(prepareView());
        root.setDividerLocation(1200);
        root.setDividerSize(0);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel prepareMenu(){
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints c;

        JPanel selectorPanel =  prepareSelectorPanel();
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx =1;
        c.insets = new Insets(20,10,20,10);
        c.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(selectorPanel,c);

        JPanel basicPanel =  prepareBasicPanel();
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 10;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx =1;
        c.insets = new Insets(10,10,5,10);
        c.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(basicPanel,c);

        JPanel stopwayPanel =  prepareStopwayPanel();
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 20;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx =1;
        c.insets = new Insets(10,10,5,10);
        c.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(stopwayPanel,c);

        JPanel stripPanel =  prepareStripPanel();
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 30;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx =1;
        c.insets = new Insets(10,10,5,10);
        c.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(stripPanel,c);

        JPanel othersPanel =  prepareOthersPanel();
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 40;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx =1;
        c.insets = new Insets(10,10,0,10);
        c.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(othersPanel,c);

        //Add a spacer to push everything up.
        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1000; c.weighty = 1;
        menuPanel.add(spacer, c);

        JPanel confirmCancelPanel =  prepareConfirmCancelPanel();
        c = new GridBagConstraints(); c.gridx = 0; c.gridy = 1100;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx =1;
        c.insets = new Insets(10,10,10,10);
        c.anchor = GridBagConstraints.PAGE_START;
        menuPanel.add(confirmCancelPanel,c);

        return menuPanel;
    }

    private JPanel prepareConfirmCancelPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        JButton confirmButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        return buttonPanel;
    }

    private JPanel prepareSelectorPanel(){
        GridBagConstraints c;

        JPanel selectorPanel =  new JPanel();
        TitledBorder stripBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Runway Selection",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        stripBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        selectorPanel.setBorder(stripBorder);
        selectorPanel.setLayout(new GridBagLayout());

        JLabel selectedRunwayLabel = new JLabel("Selected Runway: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        selectorPanel.add(selectedRunwayLabel,c);

        DefaultComboBoxModel<String> runwaysModel = new DefaultComboBoxModel<>();
        JComboBox runwaySelector = new JComboBox(runwaysModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        selectorPanel.add(runwaySelector,c);

        JButton addButton = new JButton("New Runway");
        JButton deleteButton = new JButton("Delete Runway");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        selectorPanel.add(buttonPanel,c);


        return selectorPanel;
    }

    private JPanel prepareOthersPanel(){
        GridBagConstraints c;

        JPanel othersPanel =  new JPanel();
        TitledBorder stripBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Other Settings",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        stripBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        othersPanel.setBorder(stripBorder);
        othersPanel.setLayout(new GridBagLayout());

        JLabel thresholdLabel = new JLabel("Displaced Threshold: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        othersPanel.add(thresholdLabel,c);

        SpinnerNumberModel thresholdModel = new SpinnerNumberModel(0,0,10000,1);
        JSpinner thresholdSpinner = new JSpinner(thresholdModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        othersPanel.add(thresholdSpinner,c);

        JLabel resaLabel = new JLabel("RESA Length: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10;
        c.insets = new Insets(0,10,10,0);
        c.anchor = GridBagConstraints.LINE_START;
        othersPanel.add(resaLabel,c);

        SpinnerNumberModel resaModel = new SpinnerNumberModel(0,0,10000,1);
        JSpinner resaSpinner = new JSpinner(resaModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        othersPanel.add(resaSpinner,c);

        return othersPanel;
    }

    private JPanel prepareStopwayPanel(){
        GridBagConstraints c;

        JPanel stopwayPanel =  new JPanel();
        TitledBorder stripBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Stopway & Clearway",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        stripBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        stopwayPanel.setBorder(stripBorder);
        stopwayPanel.setLayout(new GridBagLayout());

        JLabel stopwayLengthLabel = new JLabel("Stopway Length: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        stopwayPanel.add(stopwayLengthLabel,c);

        SpinnerNumberModel stopwayLengthModel = new SpinnerNumberModel(0,0,10000,1);
        JSpinner stopwayLengthSpinner = new JSpinner(stopwayLengthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        stopwayPanel.add(stopwayLengthSpinner,c);

        JLabel stopwayWidthLabel = new JLabel("Stopway Width: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        stopwayPanel.add(stopwayWidthLabel,c);

        SpinnerNumberModel stopwayWidthModel = new SpinnerNumberModel(0,0,10000,1);
        JSpinner stopwayWidthSpinner = new JSpinner(stopwayWidthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        stopwayPanel.add(stopwayWidthSpinner,c);

        JLabel clearwayLengthLabel = new JLabel("Clearway Length: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        stopwayPanel.add(clearwayLengthLabel,c);

        SpinnerNumberModel clearwayLengthModel = new SpinnerNumberModel(0,0,10000,1);
        JSpinner clearwayLengthSpinner = new JSpinner(clearwayLengthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 20; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        stopwayPanel.add(clearwayLengthSpinner,c);

        JLabel clearwayWidthLabel = new JLabel("Clearway Width: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30;
        c.insets = new Insets(0,10,10,0);
        c.anchor = GridBagConstraints.LINE_START;
        stopwayPanel.add(clearwayWidthLabel,c);

        SpinnerNumberModel clearwayWidthModel = new SpinnerNumberModel(0,0,10000,1);
        JSpinner clearwayWidthSpinner = new JSpinner(clearwayWidthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 30; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        stopwayPanel.add(clearwayWidthSpinner,c);

        return stopwayPanel;
    }

    private JPanel prepareStripPanel(){
        GridBagConstraints c;

        JPanel stripPanel =  new JPanel();
        TitledBorder stripBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Runway Strip Settings",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        stripBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        stripPanel.setBorder(stripBorder);
        stripPanel.setLayout(new GridBagLayout());

        JLabel stripEndLabel = new JLabel("Strip End Size: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        stripPanel.add(stripEndLabel,c);

        SpinnerNumberModel stripEndModel = new SpinnerNumberModel(0,0,1000,1);
        JSpinner stripEndSpinner = new JSpinner(stripEndModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        stripPanel.add(stripEndSpinner,c);

        JLabel stripWidthLabel = new JLabel("Strip Width: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10;
        c.insets = new Insets(0,10,10,0);
        c.anchor = GridBagConstraints.LINE_START;
        stripPanel.add(stripWidthLabel,c);

        SpinnerNumberModel stripWidthModel = new SpinnerNumberModel(0,0,5000,1);
        JSpinner stripWidthSpinner = new JSpinner(stripWidthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        stripPanel.add(stripWidthSpinner,c);

        return stripPanel;
    }

    private JPanel prepareBasicPanel(){
        GridBagConstraints c;

        JPanel basicPanel =  new JPanel();
        TitledBorder basicBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Basic Runway Settings",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        basicBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 18));
        basicPanel.setBorder(basicBorder);
        basicPanel.setLayout(new GridBagLayout());

        JLabel nameLabel = new JLabel("Runway Name:");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        basicPanel.add(nameLabel,c);

        JTextField nameField = new JTextField("Foo Bar");
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        basicPanel.add(nameField,c);

        JLabel xOffsetLabel = new JLabel("Horizontal Position: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        basicPanel.add(xOffsetLabel,c);

        SpinnerNumberModel xOffsetModel = new SpinnerNumberModel(0,-50000,50000,1);
        JSpinner xOffsetSpinner = new JSpinner(xOffsetModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        basicPanel.add(xOffsetSpinner,c);

        JLabel yOffsetLabel = new JLabel("Vertical Position: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        basicPanel.add(yOffsetLabel,c);

        SpinnerNumberModel yOffsetModel = new SpinnerNumberModel(0,-50000,50000,1);
        JSpinner yOffsetSpinner = new JSpinner(yOffsetModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 20; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        basicPanel.add(yOffsetSpinner,c);

        JLabel lengthLabel = new JLabel("Length: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        basicPanel.add(lengthLabel,c);

        SpinnerNumberModel lengthModel = new SpinnerNumberModel(0,0,50000,1);
        JSpinner lengthSpinner = new JSpinner(lengthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 30; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        basicPanel.add(lengthSpinner,c);

        JLabel widthLabel = new JLabel("Width: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40;
        c.insets = new Insets(0,10,10,0);
        c.anchor = GridBagConstraints.LINE_START;
        basicPanel.add(widthLabel,c);

        SpinnerNumberModel widthModel = new SpinnerNumberModel(0,0,1000,1);
        JSpinner widthSpinner = new JSpinner(widthModel);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 40; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        basicPanel.add(widthSpinner,c);

        return basicPanel;
    }

    private InteractivePanel prepareView(){
        InteractivePanel viewPanel = new InteractivePanel(new Point(600,450),1.0) {
            @Override
            public void paintView(Graphics2D g2) {
                paintBackground(g2);
                g2.setTransform(new AffineTransform());
                paintScale(g2);
            }
        };
        return viewPanel;
    }

    private void paintBackground(Graphics2D g2){
        g2.setColor(Settings.AIRFIELD_COLOUR);
        g2.fillRect(-1000000,-1000000,2000000,2000000);

        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(0,-1000000,0,1000000);
        g2.drawLine(-1000000,0,1000000,0);
    }
}
