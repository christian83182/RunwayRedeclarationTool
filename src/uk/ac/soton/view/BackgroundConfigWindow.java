package uk.ac.soton.view;

import javafx.scene.control.Spinner;
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

public class BackgroundConfigWindow extends JDialog{

    //An instance of the appView class used by the application.
    AppView appView;
    //An instance of the controller which is used by appView to access the model.
    ViewController controller;

    //Various variables to keep track of the background image's configuration.
    BufferedImage bgImage;
    Double xOffset, yOffset, scale, rotation;

    BackgroundConfigWindow(AppView appView){
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

    //Swing initialization code.
    private void init(){
        this.setTitle("Configure Background Image");
        this.setModal(true);
        this.setPreferredSize(new Dimension(1600,900));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/uk/ac/soton/resources/images/Applicationicon.png"));
        this.setIconImage(icon.getImage());

        this.setLayout(new BorderLayout());
        this.add(prepareSideMenu(), BorderLayout.EAST);
        this.add(prepareInteractivePanel(), BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //Returns a JPanel which is populated with functional menu elements.
    private JPanel prepareSideMenu(){//Create a JPanel to populate with the menu.
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

        //Create and populate a JPanel with buttons for modifying or clearing the current image.
        JButton changeImageButton = new JButton("Add Image");
        JButton clearImageButton = new JButton("Clear Image");
        clearImageButton.setEnabled(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(changeImageButton);
        buttonPanel.add(clearImageButton);

        //Add the buttonPanel to the imagePanel.
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        c.insets = new Insets(5,10,0,10);
        imagePanel.add(buttonPanel, c);

        //Create a label displaying "Horizontal Offset"
        JLabel xOffsetLabel = new JLabel("Horizontal Offset");
        xOffsetLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 10; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(xOffsetLabel,c);

        //Create a spinner to control the xOffset
        SpinnerNumberModel xOffsetModel = new SpinnerNumberModel(0,-10000,10000,1);
        JSpinner xOffsetSpinner = new JSpinner(xOffsetModel);
        xOffsetSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 20; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(xOffsetSpinner,c);

        //Create a label displaying "Vertical Offset";
        JLabel yOffsetLabel = new JLabel("Vertical Offset");
        yOffsetLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 30; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(yOffsetLabel,c);

        //Create a spinner to control the yOffset
        SpinnerNumberModel yOffsetModel = new SpinnerNumberModel(0,-10000,10000,1);
        JSpinner yOffsetSpinner = new JSpinner(yOffsetModel);
        yOffsetSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 40; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(yOffsetSpinner,c);

        //Create a label displaying "Scale";
        JLabel scaleLabel = new JLabel("Scale");
        scaleLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 50; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(scaleLabel,c);

        //Create a slider to control the scale. Custom labels are used because sliders don't support doubles.
        SpinnerNumberModel scaleModel = new SpinnerNumberModel(1,0.1,5,0.1);
        JSpinner scaleSpinner = new JSpinner(scaleModel);
        scaleSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 60; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(scaleSpinner,c);

        //Create a label displaying "Rotation";
        JLabel rotationLabel = new JLabel("Rotation");
        rotationLabel.setFont(Settings.MENU_BAR_DEFAULT_FONT);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 70; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,0,10);
        configPanel.add(rotationLabel,c);

        //Create a slider to control the rotation
        SpinnerNumberModel rotationModel = new SpinnerNumberModel(0,0,360,1);
        JSpinner rotationSpinner = new JSpinner(rotationModel);
        rotationSpinner.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 80; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,10,20,10);
        configPanel.add(rotationSpinner,c);

        //If the application already has an image loaded, change a few labels and enable the sliders to reflect this.
        if(bgImage != null){
            changeImageButton.setText("Change Image");
            imageNameLabel.setText("Imported from Application");
            clearImageButton.setEnabled(true);
            xOffsetSpinner.setEnabled(true);
            yOffsetSpinner.setEnabled(true);
            scaleSpinner.setEnabled(true);
            rotationSpinner.setEnabled(true);
        }

        //Add an action listener to the changeImage button.
        changeImageButton.addActionListener(e -> {
            //Create a file chooser, add a filter so only images can be selected.
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","jpeg","png");
            fileChooser.setFileFilter(filter);

            //If the user chooses a file, then load that file, and enable all the sliders.
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try {
                    File imgFile = fileChooser.getSelectedFile();
                    bgImage = ImageIO.read(imgFile);
                    imageNameLabel.setText(imgFile.getName());
                    clearImageButton.setEnabled(true);
                    clearImageButton.setEnabled(true);
                    xOffsetSpinner.setEnabled(true);
                    yOffsetSpinner.setEnabled(true);
                    scaleSpinner.setEnabled(true);
                    rotationSpinner.setEnabled(true);
                    this.repaint();
                } catch (Exception err) {
                    //Output an error message in the event that an exception was thrown.
                    JOptionPane.showMessageDialog(fileChooser,
                            "There was an issue importing that image: '" +
                                    err.getMessage() + "'", "IO Error" ,JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Add a listener to the clear button, which removes the current image, updates a few labels, and disables all the sliders.
        clearImageButton.addActionListener(e -> {
            bgImage = null;
            imageNameLabel.setText("No Image Selected");
            changeImageButton.setText("Add Background Image");
            clearImageButton.setEnabled(false);
            xOffsetSpinner.setEnabled(false);
            yOffsetSpinner.setEnabled(false);
            scaleSpinner.setEnabled(false);
            rotationSpinner.setEnabled(false);
            this.repaint();
            this.revalidate();
        });

        //Add a change listener to update the xOffset value when this slider changes.
        xOffsetSpinner.addChangeListener(e -> {
            xOffset = xOffsetModel.getNumber().doubleValue();
            this.repaint();
        });

        //Add a change listener to update the yxOffset value when this slider changes.
        yOffsetSpinner.addChangeListener(e -> {
            yOffset = -yOffsetModel.getNumber().doubleValue();
            this.repaint();
        });

        //Add a change listener to update the scale value when this slider changes.
        scaleSpinner.addChangeListener(e -> {
            scale = scaleModel.getNumber().doubleValue();
            this.repaint();
        });

        //Add a change listener to update the rotation value when this slider changes.
        rotationSpinner.addChangeListener(e -> {
            rotation = rotationModel.getNumber().doubleValue();
            this.repaint();
        });

        //Add an action listener to the close button which closes the window.
        closeButton.addActionListener(e -> this.dispose());

        //Add an action listener to the confirm button which updates the configuration in the controller, and closes the window.
        confirmButton.addActionListener(e -> {
            controller.setBackgroundImage(bgImage);
            controller.setBackgroundImageOffset(new Point(xOffset.intValue(), yOffset.intValue()));
            controller.setBackgroundImageScale(scale);
            controller.setBackgroundRotation(rotation);
            appView.repaint();
            NotificationLogger.logger.addToLog("Background Image configuration changed. ");
            this.dispose();
        });

        //Return the newly created side panel.
        return sideMenu;
    }

    //Returns an fully functional interactive panel which displays a semi-transparent outline of all the runways,
    //... along with any background image, displayed according to the offset, scale and rotation.
    private JPanel prepareInteractivePanel(){
        InteractivePanel mainPanel = new InteractivePanel(new Point(800,450),0.4) {
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

                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(10));
                    g2.drawRect(xPos,yPos,width,height);
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

                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRect(pos.x, pos.y, dim.width, dim.height);

                    g2.setTransform(old);
                }

                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(-100000,0,10000,0);
                g2.drawLine(0,-100000,0,100000);
            }
        };
        return mainPanel;
    }

    //Creates a transform used to rotate a runway to the correct orientation, and translate to the correct position.
    private AffineTransform createRunwayTransform(Point pos, Dimension dim, String id){
        Double bearing = Math.toRadians(controller.getBearing(id)-90);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,-dim.height/2);
        AffineTransform rx = new AffineTransform(tx);
        rx.rotate(bearing,pos.x, pos.y+(dim.height/2));
        return rx;
    }
}
