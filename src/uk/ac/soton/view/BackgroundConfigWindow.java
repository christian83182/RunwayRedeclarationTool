package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class BackgroundConfigWindow extends JFrame{

    AppView appView;
    ViewController controller;

    BufferedImage bgImage;
    Double xOffset, yOffset, scale, rotation;

    BackgroundConfigWindow(AppView appView){
        super("Configure Background Image");
        this.appView = appView;
        this.controller = appView.getController();

        if(controller.getBackgroundImage() != null){
            this.bgImage = controller.getBackgroundImage();
            this.xOffset = controller.getBackgroundImageOffset().getX();
            this.yOffset = controller.getBackgroundImageOffset().getY();
            this.scale = controller.getBackgroundImageScale();
            this.rotation = controller.getBackgroundRotation();
        } else {
            this.bgImage = null;
            this.xOffset = 0.0;
            this.yOffset = 0.0;
            this.scale = 1.0;
            this.rotation = 0.0;
        }

        init();
    }

    private void init(){
        this.setPreferredSize(new Dimension(1600,900));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setLayout(new BorderLayout());
        this.add(prepareSideMenu(), BorderLayout.EAST);
        this.add(prepareInteractivePanel(), BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel prepareSideMenu(){
        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(350,350));
        sideMenu.setLayout(new GridBagLayout());
        GridBagConstraints c;

        //Create a panel to display image settings.
        JPanel imagePanel = new JPanel();
        TitledBorder imagePanelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Image Settings",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        imagePanel.setBorder(imagePanelBorder);
        imagePanel.setLayout(new GridBagLayout());

        //Create a panel to display image configuration options.
        JPanel configPanel = new JPanel();
        TitledBorder configPanelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY,1),"Image Configuration",0,2,
                Settings.MENU_BAR_DEFAULT_FONT,Color.WHITE);
        configPanel.setBorder(configPanelBorder);
        configPanel.setLayout(new GridBagLayout());

        //Add the image panel to the sideMenu
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(20,20,0,20);
        sideMenu.add(imagePanel, c);

        //Add the configuration panel to the sideMenu.
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15,20,10,20);
        sideMenu.add(configPanel, c);

        //Add a spacer to push everything up.
        JPanel spacer = new JPanel();
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1000;
        c.gridwidth = 3; c.weighty = 1;
        sideMenu.add(spacer, c);

        //Add a close button and confirm button
        JButton closeButton = new JButton("Close");
        JButton confirmButton = new JButton("Confirm");
        JPanel confirmClosePanel = new JPanel();
        confirmClosePanel.setLayout(new GridLayout(1,2));
        confirmClosePanel.add(closeButton);
        confirmClosePanel.add(confirmButton);

        //Add the panel containing the open and close buttons to the side menu.
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1100;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,20,10,20);
        sideMenu.add(confirmClosePanel, c);

        //Create a label displaying "Current Image";
        JLabel selectedImageLabel = new JLabel("Current Image:");
        selectedImageLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10,10,0,5);
        imagePanel.add(selectedImageLabel,c);

        //Create a label used to display the current filename.
        JLabel imageNameLabel = new JLabel("No Image Selected");
        imageNameLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 10; c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10,10,0,10);
        imagePanel.add(imageNameLabel,c);

        //Create a Button to add/change the image.
        JButton changeImageButton = new JButton("Add Background Image");
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        c.insets = new Insets(5,10,0,10);
        imagePanel.add(changeImageButton, c);

        //Create a button to clear/remove the image.
        JButton clearImageButton = new JButton("Remove Background Image");
        clearImageButton.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        c.insets = new Insets(0,10,10,10);
        imagePanel.add(clearImageButton, c);

        //Create a label displaying "Horizontal Offset"
        JLabel xOffsetLabel = new JLabel("Horizontal Offset");
        xOffsetLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(xOffsetLabel,c);

        //Create a slider to control the xOffset
        JSlider xOffsetSlider = new JSlider(JSlider.HORIZONTAL,-2000,2000,0);
        xOffsetSlider.setMajorTickSpacing(1000);
        xOffsetSlider.setMinorTickSpacing(500);
        xOffsetSlider.setPaintTicks(true);
        xOffsetSlider.setPaintLabels(true);
        xOffsetSlider.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(xOffsetSlider,c);

        //Create a label displaying "Vertical Offset";
        JLabel yOffsetLabel = new JLabel("Vertical Offset");
        yOffsetLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(yOffsetLabel,c);

        //Create a slider to control the yOffset
        JSlider yOffsetSlider = new JSlider(JSlider.HORIZONTAL,-2000,2000,0);
        yOffsetSlider.setMajorTickSpacing(1000);
        yOffsetSlider.setMinorTickSpacing(500);
        yOffsetSlider.setPaintTicks(true);
        yOffsetSlider.setPaintLabels(true);
        yOffsetSlider.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(yOffsetSlider,c);

        //Create a label displaying "Scale";
        JLabel scaleLabel = new JLabel("Scale");
        scaleLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 50; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(scaleLabel,c);

        //Create a slider to control the yOffset
        JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL,0,200,100);
        Hashtable labelTable = new Hashtable();
        labelTable.put( 0, new JLabel("0.0")); labelTable.put( 50, new JLabel("0.5"));
        labelTable.put( 100, new JLabel("1.0")); labelTable.put( 150, new JLabel("1.5"));
        labelTable.put( 200, new JLabel("2.0"));
        scaleSlider.setLabelTable(labelTable);
        scaleSlider.setMajorTickSpacing(50);
        scaleSlider.setMinorTickSpacing(10);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPaintLabels(true);
        scaleSlider.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 60; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(scaleSlider,c);

        //Create a label displaying "Vertical Offset";
        JLabel rotationLabel = new JLabel("Rotation");
        rotationLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 70; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(rotationLabel,c);

        //Create a slider to control the yOffset
        JSlider rotationSlider = new JSlider(JSlider.HORIZONTAL,-180,180,0);
        rotationSlider.setMajorTickSpacing(90);
        rotationSlider.setMinorTickSpacing(45);
        rotationSlider.setPaintTicks(true);
        rotationSlider.setPaintLabels(true);
        rotationSlider.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 80; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(rotationSlider,c);

        //If the application already has an image loaded, change a few labels to reflect this.
        if(bgImage != null){
            changeImageButton.setText("Change Background Image");
            imageNameLabel.setText("Imported from Application");
            clearImageButton.setEnabled(true);
            xOffsetSlider.setEnabled(true);
            yOffsetSlider.setEnabled(true);
            scaleSlider.setEnabled(true);
            rotationSlider.setEnabled(true);
        }

        //Add an action listener to the changeImage button.
        changeImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","jpeg","png");
            fileChooser.setFileFilter(filter);

            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try {
                    File imgFile = fileChooser.getSelectedFile();
                    bgImage = ImageIO.read(imgFile);
                    imageNameLabel.setText(imgFile.getName());
                    clearImageButton.setEnabled(true);
                    clearImageButton.setEnabled(true);
                    xOffsetSlider.setEnabled(true);
                    yOffsetSlider.setEnabled(true);
                    scaleSlider.setEnabled(true);
                    rotationSlider.setEnabled(true);
                    this.repaint();
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(fileChooser,
                            "There was an issue importing that image: '" +
                                    err.getMessage() + "'", "IO Error" ,JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearImageButton.addActionListener(e -> {
            bgImage = null;
            imageNameLabel.setText("No Image Selected");
            changeImageButton.setText("Add Background Image");
            clearImageButton.setEnabled(false);
            xOffsetSlider.setEnabled(false);
            yOffsetSlider.setEnabled(false);
            scaleSlider.setEnabled(false);
            rotationSlider.setEnabled(false);
            this.repaint();
            this.revalidate();
        });

        xOffsetSlider.addChangeListener(e -> {
            xOffset = (double) xOffsetSlider.getValue();
            this.repaint();
        });

        yOffsetSlider.addChangeListener(e -> {
            yOffset = (double) -yOffsetSlider.getValue();
            this.repaint();
        });

        scaleSlider.addChangeListener(e -> {
            scale = (double) scaleSlider.getValue()/100;
            this.repaint();
        });

        rotationSlider.addChangeListener(e -> {
            rotation = (double) rotationSlider.getValue();
            this.repaint();
        });

        closeButton.addActionListener(e -> this.dispose());

        confirmButton.addActionListener(e -> {
            controller.setBackgroundImage(bgImage);
            controller.setBackgroundImageOffset(new Point(xOffset.intValue(), yOffset.intValue()));
            controller.setBackgroundImageScale(scale);
            controller.setBackgroundRotation(rotation);
            appView.repaint();
            this.dispose();
        });

        return sideMenu;
    }

    private JPanel prepareInteractivePanel(){
        InteractivePanel mainPanel = new InteractivePanel(new Point(800,400),0.4) {
            @Override
            public void paintView(Graphics2D g2) {
                g2.setColor(Settings.AIRFIELD_COLOUR);
                g2.fillRect(-100000,-100000,200000,200000);

                if(bgImage != null){
                    Integer width = (int)(bgImage.getWidth() * scale);
                    Integer height = (int)(bgImage.getHeight() * scale);
                    Integer xPos = -width/2 + xOffset.intValue();
                    Integer yPos = -height/2 + yOffset.intValue();

                    AffineTransform old = g2.getTransform();
                    AffineTransform tx = (AffineTransform) old.clone();
                    AffineTransform rx = new AffineTransform();
                    rx.setToRotation(Math.toRadians(rotation),xPos+width/2,yPos+height/2);
                    tx.concatenate(rx);
                    g2.setTransform(tx);

                    g2.drawImage(bgImage,xPos,yPos, width, height,null);
                    g2.setTransform(old);
                }

                for (String runwayId : controller.getRunways()){
                    Point pos = controller.getRunwayPos(runwayId);
                    Dimension dim = controller.getRunwayDim(runwayId);

                    AffineTransform old = g2.getTransform();
                    AffineTransform tx = (AffineTransform) old.clone();
                    tx.concatenate(createRunwayTransform(pos,dim,runwayId));
                    g2.setTransform(tx);

                    Color runwayColour = Settings.RUNWAY_COLOUR;

                    g2.setColor(new Color(runwayColour.getRed(), runwayColour.getGreen(), runwayColour.getBlue(),70));
                    g2.fillRect(pos.x, pos.y, dim.width, dim.height);

                    g2.setColor(Color.DARK_GRAY);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRect(pos.x, pos.y, dim.width, dim.height);

                    g2.setTransform(old);
                }
            }
        };
        return mainPanel;
    }

    private AffineTransform createRunwayTransform(Point pos, Dimension dim, String id){
        Double bearing = Math.toRadians(controller.getBearing(id)-90);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,-dim.height/2);
        AffineTransform rx = new AffineTransform(tx);
        rx.rotate(bearing,pos.x, pos.y+(dim.height/2));
        return rx;
    }
}
