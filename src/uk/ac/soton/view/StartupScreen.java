package uk.ac.soton.view;

import uk.ac.soton.controller.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StartupScreen extends JFrame {

    public StartupScreen(){
        super("Runway Redeclaration Tool");
        init();
    }

    private void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setPreferredSize(new Dimension(700,700));

        ImageIcon icon = new ImageIcon(getClass().getResource("/uk/ac/soton/resources/images/Applicationicon.png"));
        this.setIconImage(icon.getImage());

        setLookAndFeel();

        JPanel root = prepareRootPanel();
        this.setContentPane(root);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel prepareRootPanel(){
        JPanel root = new JPanel();
        root.setLayout(new GridBagLayout());
        GridBagConstraints c;

        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/uk/ac/soton/resources/images/Applicationicon.png"));
        iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(200,200,Image.SCALE_SMOOTH)));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0,0,25,0);
        root.add(iconLabel,c);

        JLabel titleLabel = new JLabel("Runway Redeclaration Tool");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1;
        root.add(titleLabel,c);

        JLabel subtitleLabel1 = new JLabel("Software Engineering Group Project");
        subtitleLabel1.setFont(new Font("SansSerif", Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 2;
        root.add(subtitleLabel1,c);

        JLabel subtitleLabel2 = new JLabel("Southampton University 2019");
        subtitleLabel2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 3;
        root.add(subtitleLabel2,c);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        buttonPanel.setPreferredSize(new Dimension(300,150));
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 4;
        c.insets = new Insets(50,0,50,0);
        root.add(buttonPanel,c);

        JButton newButton = new JButton("New Airfield");
        buttonPanel.add(newButton);
        JButton loadButton = new JButton("Load Airfield");
        buttonPanel.add(loadButton);
        JButton demoButton = new JButton("Demo Configurations");
        buttonPanel.add(demoButton);
        JButton helpButton = new JButton("Help");
        buttonPanel.add(helpButton);

        newButton.addActionListener(e -> {
            AppView appView = new AppView("Runway Redeclaration Tool");
            AppController appController = new AppController(appView);
            appView.setController(appController);
            appView.init();
            this.dispose();
        });

        loadButton.addActionListener(e -> {
            AppView appView = new AppView("Runway Redeclaration Tool");
            AppController appController = new AppController(appView);
            appView.setController(appController);
            appView.init();
            appView.getAppMenuBar().importConfiguration();
            this.dispose();
        });

        helpButton.addActionListener(e -> openHelp());

        return root;
    }

    public static void openHelp(){
        JDialog dialogBox = new javax.swing.JDialog(null, "User Manual", JDialog.ModalityType.DOCUMENT_MODAL);
        try {
            InputStream in = StartupScreen.class.getResourceAsStream("/uk/ac/soton/resources/files/userManual.pdf");
            File outputFile = new File("UserGuide.pdf");
            FileOutputStream out = new FileOutputStream(outputFile);
            while(in.available() >0){
                out.write(in.read());
            }
            out.close();
            java.awt.Desktop.getDesktop().open(outputFile);
            dialogBox.toBack();
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    //Set's the Look and Feel of the application to a custom theme.
    private void setLookAndFeel(){
        UIManager.put("control", new Color(55, 55, 55)); // Primary
        UIManager.put("nimbusBase", new Color(42, 42, 42)); // The colour of selectors
        UIManager.put("nimbusBlueGrey", new Color(52, 52, 52)); // The colour of buttons
        UIManager.put("text",new Color(255,255,255)); //Sets Default text colour to white
        UIManager.put("ScrollPane.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
        UIManager.put("List.background", Color.DARK_GRAY); //Background for the ScrollPane (affects JFileChooser)
        UIManager.put("TextField.background", Color.DARK_GRAY); //Background for the TextField (affects JFileChooser)
        UIManager.put("Menu[Enabled].textForeground",new Color(255, 255, 255));
        UIManager.put("nimbusFocus",new Color(0, 104, 208));
        UIManager.put("nimbusLightBackground",new Color(74, 74, 74));
        UIManager.put("nimbusSelectionBackground",new Color(0, 104, 208));
        UIManager.put("List.background",new Color(80, 80, 80));
        UIManager.put("List[Selected].textForeground",new Color(250, 251, 255));
        UIManager.put("Slider.tickColor",new Color(250, 251, 255));

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus not available, using default 'Metal'");
        }
    }
}