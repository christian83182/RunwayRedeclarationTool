package uk.ac.soton.view;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;
import uk.ac.soton.common.Runway;
import uk.ac.soton.controller.AppController;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class AirfieldConfigWindow extends JDialog {

    AppView appView;
    ViewController outerController;
    ViewController innerController;
    DefaultComboBoxModel<String> runwaySelectorModel;
    JComboBox<String> runwaySelector;

    AirfieldConfigWindow(AppView appView){
        this.appView = appView;
        this.outerController = appView.getController();
        this.innerController = new AppController(null);
        this.innerController.setAirfield((Airfield) outerController.getAirfield().deepClone());
        runwaySelectorModel = new DefaultComboBoxModel<>();
        runwaySelector = new JComboBox<>(runwaySelectorModel);
        init();
    }

    private void init(){
        this.setTitle("Airfield Configuration Editor");
        this.setModal(true);
        this.setPreferredSize(new Dimension(1600,900));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/uk/ac/soton/resources/images/Applicationicon.png");
        this.setIconImage(icon.getImage());

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

        cancelButton.addActionListener(e -> {
            appView.getMenuPanel().populateRunwayComboBox();
            appView.repaint();
            this.dispose();
        });

        confirmButton.addActionListener(e ->{
            outerController.setAirfield(innerController.getAirfield());
            NotificationLogger.logger.addToLog("Airfield has been updated...");
            appView.getMenuPanel().populateRunwayComboBox();
            appView.repaint();
            this.dispose();
        });

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

        runwaySelectorModel.addElement("None");
        for(String id: innerController.getRunways()){
            runwaySelectorModel.addElement(id);
        }

        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,0,10);
        selectorPanel.add(runwaySelector,c);

        JButton addButton = new JButton("New Runway");
        addButton.setEnabled(false);
        JButton deleteButton = new JButton("Delete Runway");
        deleteButton.setEnabled(false);
        addButton.setToolTipText("Creating new runways is not supported. Please create a custom XML file for greater control over the Airfield's settings.");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        selectorPanel.add(buttonPanel,c);

        runwaySelector.addActionListener(e ->{
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            if(selectedRunway.equals("None")){
                deleteButton.setEnabled(false);
            } else {
                deleteButton.setEnabled(true);
            }
            this.repaint();
        });

        deleteButton.addActionListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            int areYouSureAnswer = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete \"" + runway.getId() +"\"?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if(areYouSureAnswer == JOptionPane.YES_OPTION){
                innerController.getAirfield().removeRunway(runway);
                runwaySelector.removeItem(runway.getId().split("/")[0]);
                runwaySelector.removeItem(runway.getId().split("/")[1]);
                this.repaint();
            }
        });

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
        thresholdSpinner.setEnabled(false);
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
        resaSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        othersPanel.add(resaSpinner,c);

        runwaySelector.addActionListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            if(selectedRunway.equals("None")){
                thresholdSpinner.setEnabled(false);
                resaSpinner.setEnabled(false);

            } else {
                thresholdSpinner.setEnabled(true);
                thresholdSpinner.setValue(innerController.getRunwayThreshold(selectedRunway));

                resaSpinner.setEnabled(true);
                resaSpinner.setValue(innerController.getRESADistance(selectedRunway));
            }
        });

        thresholdSpinner.addChangeListener(e ->{
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            LogicalRunway runway = innerController.getAirfield().getRunway(selectedRunway).getLogicalRunway(selectedRunway);
            runway.setThreshold((Integer)thresholdSpinner.getValue());
            this.repaint();
        });

        resaSpinner.addChangeListener(e ->{
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setResa((Integer)resaSpinner.getValue());
            this.repaint();
        });

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
        stopwayLengthSpinner.setEnabled(false);
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
        stopwayWidthSpinner.setEnabled(false);
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
        clearwayLengthSpinner.setEnabled(false);
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
        clearwayWidthSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 30; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        stopwayPanel.add(clearwayWidthSpinner,c);

        runwaySelector.addActionListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            if(selectedRunway.equals("None")){
                stopwayLengthSpinner.setEnabled(false);
                stopwayWidthSpinner.setEnabled(false);
                clearwayLengthSpinner.setEnabled(false);
                clearwayWidthSpinner.setEnabled(false);
            } else {
                stopwayLengthSpinner.setEnabled(true);
                stopwayLengthSpinner.setValue(innerController.getStopwayDim(selectedRunway).width);

                stopwayWidthSpinner.setEnabled(true);
                stopwayWidthSpinner.setValue(innerController.getStopwayDim(selectedRunway).height);

                clearwayLengthSpinner.setEnabled(true);
                clearwayLengthSpinner.setValue(innerController.getClearwayDim(selectedRunway).width);

                clearwayWidthSpinner.setEnabled(true);
                clearwayWidthSpinner.setValue(innerController.getClearwayDim(selectedRunway).height);
            }
        });

        stopwayLengthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            LogicalRunway runway = innerController.getAirfield().getRunway(selectedRunway).getLogicalRunway(selectedRunway);
            runway.getStopway().width = (Integer) stopwayLengthSpinner.getValue();
            this.repaint();
        });

        stopwayWidthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            LogicalRunway runway = innerController.getAirfield().getRunway(selectedRunway).getLogicalRunway(selectedRunway);
            runway.getStopway().height = (Integer) stopwayWidthSpinner.getValue();
            this.repaint();
        });

        clearwayLengthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            LogicalRunway runway = innerController.getAirfield().getRunway(selectedRunway).getLogicalRunway(selectedRunway);
            runway.getClearway().width = (Integer) clearwayLengthSpinner.getValue();
            this.repaint();
        });

        clearwayWidthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            LogicalRunway runway = innerController.getAirfield().getRunway(selectedRunway).getLogicalRunway(selectedRunway);
            runway.getClearway().height = (Integer) clearwayWidthSpinner.getValue();
            this.repaint();
        });

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
        stripEndSpinner.setEnabled(false);
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
        stripWidthSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        stripPanel.add(stripWidthSpinner,c);

        runwaySelector.addActionListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            if(selectedRunway.equals("None")){
                stripEndSpinner.setEnabled(false);
                stripWidthSpinner.setEnabled(false);
            } else {
                stripEndSpinner.setEnabled(true);
                stripEndSpinner.setValue(innerController.getStripEndSize(selectedRunway));

                stripWidthSpinner.setEnabled(true);
                stripWidthSpinner.setValue(innerController.getStripWidthFromCenterline(selectedRunway)*2);
            }
        });

        stripWidthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setStripWidth((Integer) stripWidthSpinner.getValue());
            this.repaint();
        });

        stripEndSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setStripEnd((Integer) stripEndSpinner.getValue());
            this.repaint();
        });

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

        JLabel xOffsetLabel = new JLabel("Horizontal Position: ");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10;
        c.insets = new Insets(0,10,0,0);
        c.anchor = GridBagConstraints.LINE_START;
        basicPanel.add(xOffsetLabel,c);

        SpinnerNumberModel xOffsetModel = new SpinnerNumberModel(0,-50000,50000,1);
        JSpinner xOffsetSpinner = new JSpinner(xOffsetModel);
        xOffsetSpinner.setEnabled(false);
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
        yOffsetSpinner.setEnabled(false);
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
        lengthSpinner.setEnabled(false);
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
        widthSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 40; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        basicPanel.add(widthSpinner,c);

        runwaySelector.addActionListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            if(selectedRunway.equals("None")){
                xOffsetSpinner.setEnabled(false);
                yOffsetSpinner.setEnabled(false);
                lengthSpinner.setEnabled(false);
                widthSpinner.setEnabled(false);
            } else {
                xOffsetSpinner.setEnabled(true);
                xOffsetSpinner.setValue(innerController.getAirfield().getRunway(selectedRunway).getxPos());

                yOffsetSpinner.setEnabled(true);
                yOffsetSpinner.setValue(innerController.getAirfield().getRunway(selectedRunway).getyPos());

                lengthSpinner.setEnabled(true);
                lengthSpinner.setValue(innerController.getRunwayDim(selectedRunway).width);

                widthSpinner.setEnabled(true);
                widthSpinner.setValue(innerController.getRunwayDim(selectedRunway).height);
            }
        });

        xOffsetSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelectorModel.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setxPos((Integer) xOffsetSpinner.getValue());
            this.repaint();
        });

        yOffsetSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelectorModel.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setyPos((Integer) yOffsetSpinner.getValue());
            this.repaint();
        });

        lengthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setLength((Integer)lengthSpinner.getValue());
            this.repaint();
        });

        widthSpinner.addChangeListener(e -> {
            String selectedRunway = runwaySelector.getSelectedItem().toString();
            Runway runway = innerController.getAirfield().getRunway(selectedRunway);
            runway.setWidth((Integer)widthSpinner.getValue());
            this.repaint();
        });

        return basicPanel;
    }

    private InteractivePanel prepareView(){
        InteractivePanel viewPanel = new InteractivePanel(new Point(600,450),1.0) {
            @Override
            public void paintView(Graphics2D g2) {
                paintBackground(g2);
                paintRunways(g2);
                if(!runwaySelectorModel.getSelectedItem().equals("None")){
                    paintSelectedRunway(g2,runwaySelectorModel.getSelectedItem().toString());
                }
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

        if(outerController.getBackgroundImage()!=null) {
            BufferedImage img = outerController.getBackgroundImage();
            Point offset = outerController.getBackgroundImageOffset();
            Double scale = outerController.getBackgroundImageScale();
            Double rotation = outerController.getBackgroundRotation();

            Integer xPos = (int) (-img.getWidth() * scale) / 2 + offset.x;
            Integer yPos = (int) (-img.getHeight() * scale) / 2 + offset.y;
            Integer width = (int) (img.getWidth() * scale);
            Integer height = (int) (img.getHeight() * scale);

            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            AffineTransform rx = new AffineTransform();
            rx.setToRotation(Math.toRadians(rotation), xPos + width / 2, yPos + height / 2);
            tx.concatenate(rx);
            g2.setTransform(tx);

            g2.drawImage(img, xPos, yPos, width, height, null);
            g2.setTransform(old);
        }
    }

    private void paintRunways(Graphics2D g2){
        for (String runwayId : innerController.getRunways()){
            Point pos = innerController.getRunwayPos(runwayId);
            Dimension dim = innerController.getRunwayDim(runwayId);
            Integer stripEndSize = innerController.getStripEndSize(runwayId);
            Integer stripWidth = innerController.getStripWidthFromCenterline(runwayId);

            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,runwayId));
            g2.setTransform(tx);

            Color stripColour = Settings.RUNWAY_STRIP_COLOUR;
            g2.setColor(new Color(stripColour.getRed(), stripColour.getGreen(), stripColour.getBlue(),70));
            g2.fillRect(pos.x - stripEndSize, pos.y - (stripWidth - (dim.height/2)),
                    stripEndSize*2 + dim.width, stripWidth*2);

            g2.setColor(stripColour);
            g2.drawRect(pos.x - stripEndSize, pos.y - (stripWidth - (dim.height/2)),
                    stripEndSize*2 + dim.width, stripWidth*2);

            Color runwayColour = Settings.RUNWAY_COLOUR;
            g2.setColor(new Color(runwayColour.getRed(), runwayColour.getGreen(), runwayColour.getBlue(),70));
            g2.fillRect(pos.x, pos.y, dim.width, dim.height);

            g2.setColor(runwayColour);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(pos.x, pos.y, dim.width, dim.height);

            g2.setTransform(old);
        }
    }

    private void paintSelectedRunway(Graphics2D g2, String runwayId){
        Point pos = innerController.getRunwayPos(runwayId);
        Dimension dim = innerController.getRunwayDim(runwayId);
        Dimension stopwayDim = innerController.getStopwayDim(runwayId);
        Dimension clearwayDim = innerController.getClearwayDim(runwayId);
        Integer thresholdLength = innerController.getRunwayThreshold(runwayId);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,runwayId));
        g2.setTransform(tx);

        g2.setColor(Settings.SELECTED_RUNWAY_HIGHLIGHT);
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(pos.x, pos.y, dim.width, dim.height);

        g2.setColor(Settings.THRESHOLD_INDICATOR_COLOUR);
        g2.fillRect(pos.x, pos.y, thresholdLength, dim.height);

        g2.setColor(Settings.CLEARWAY_FILL_COLOUR);
        g2.fillRect(pos.x+dim.width, pos.y-(clearwayDim.height-dim.height)/2, clearwayDim.width, clearwayDim.height);
        g2.setColor(Settings.CLEARWAY_STROKE_COLOUR);
        g2.setStroke(Settings.CLEARWAY_STROKE);
        g2.drawRect(pos.x+dim.width, pos.y-(clearwayDim.height-dim.height)/2, clearwayDim.width, clearwayDim.height);

        g2.setColor(Settings.STOPWAY_FILL_COLOUR);
        g2.fillRect(pos.x+dim.width, pos.y-(stopwayDim.height-dim.height)/2, stopwayDim.width, stopwayDim.height);
        g2.setColor(Settings.STOPWAY_STROKE_COLOUR);
        g2.setStroke(Settings.STOPWAY_STROKE);
        g2.drawRect(pos.x+dim.width, pos.y-(stopwayDim.height-dim.height)/2, stopwayDim.width, stopwayDim.height);

        Point arrowStart = new Point(pos.x + Settings.CENTERLINE_PADDING + (int)(dim.width*0.15), pos.y + dim.height/2);
        Integer arrowHeight = (int)(dim.height*0.2), arrowLength = arrowHeight*2;
        Polygon poly = new Polygon(new int[] {arrowStart.x, arrowStart.x, arrowStart.x + arrowLength,},
                new int[] {arrowStart.y - arrowHeight, arrowStart.y + arrowHeight, arrowStart.y}, 3);
        g2.setColor(Settings.CENTERLINE_COLOUR);
        g2.fillPolygon(poly);

        g2.setTransform(old);
    }

    //Creates a transform used to rotate a runway to the correct orientation, and translate to the correct position.
    private AffineTransform createRunwayTransform(Point pos, Dimension dim, String id){
        Double bearing = Math.toRadians(innerController.getBearing(id)-90);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,-dim.height/2);
        AffineTransform rx = new AffineTransform(tx);
        rx.rotate(bearing,pos.x, pos.y+(dim.height/2));
        return rx;
    }
}
