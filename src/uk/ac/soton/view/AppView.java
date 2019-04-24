package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

//todo Refactor arrows in TopView to use DataArrow instead of InfoArrow
//todo Make a new window for configuring the airfield (if there's time).
//todo Create demo configurations for use with the program, including background image.
//todo Make program prompt the user for a bg image when importing the configuration with a bg image.
//todo Clean up how arrows are displayed in both views. Arrows should move depending on what other arrows are displayed.

public class AppView extends JFrame{

    //An instance of the front end controller used to store the data displayed.
    private ViewController controller;
    //Note that for the runwayDimensions, the length is the x value and the width is the y value.
    private String selectedRunway;
    private CustomMenuBar menuBar;
    private MenuPanel menuPanel;
    private TopViewPanel topView;
    private SideViewPanel sideView;
    private JSplitPane viewSplitPane;
    private JSplitPane mainSplitPane;

    //Constructor calls parent's constructor and initializes member variables
    public AppView(String title){
        super(title);
        selectedRunway = "";
    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        setLookAndFeel();

        //Create all the main UI elements
        menuBar = new CustomMenuBar(controller,this);
        menuPanel = new MenuPanel(this);
        topView = new TopViewPanel(this);
        sideView = new SideViewPanel(this);

        //Create the elements for the notification bar
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new FlowLayout(FlowLayout.LEADING,7,2));
        JButton logButton = new JButton("");
        logButton.addActionListener(e -> new ScrollableTextWindow("Notification History", new Dimension(450,500), NotificationLogger.logger.getAllLog()));
        notificationPanel.add(logButton);
        notificationPanel.add(NotificationLogger.logger.getLabel());

        //Create a split pane for the views, and a split pane for the menu.
        viewSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topView, sideView);
        viewSplitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, viewSplitPane);
        mainSplitPane.setDividerSize(0);
        mainSplitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));

        //Add the split view containing all the UI elements to the center of the window.
        this.add(mainSplitPane, BorderLayout.CENTER);
        //Add the notification window to the bottom fo the screen
        this.add(notificationPanel, BorderLayout.SOUTH);
        //Add the menu bar.
        this.setJMenuBar(menuBar);

        //set the menu panel to visible, and set the split view to not visible.
        setMenuPanelVisible(true);
        setSplitViewVisible(false);

        //set the inital window size to fit the screen
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(windowSize.width, windowSize.height);

        //Pack the UI elements, make the window open in the center of the screen, and display the window.
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //Will either hide or display the split view.
    public void setSplitViewVisible(boolean isVisible){
        //If the bottom component is unset and it should be visible then...
        if(viewSplitPane.getBottomComponent() == null && isVisible){
            viewSplitPane.setBottomComponent(sideView);
            viewSplitPane.setDividerLocation((int)(getHeight()*0.55));
            viewSplitPane.setDividerSize(10);
            //Manually update the size of TopView so that the view centers correctly.
            topView.setSize(topView.getWidth(), (int)(topView.getHeight()*0.55));
        } else if (!isVisible){
            //Remove the bottom component and set the divider size to 0.
            viewSplitPane.setBottomComponent(null);
            viewSplitPane.setDividerSize(0);
        }
        repaint();
    }

    //Will either hide or display the menu.
    public void setMenuPanelVisible(boolean isVisible){
        //If the left component is unset and it should be visible then...
        if(mainSplitPane.getLeftComponent() == null && isVisible){
            mainSplitPane.setLeftComponent(menuPanel);
            mainSplitPane.setDividerLocation(270);
            //Manually update the size of TopView so that the view centers correctly.
            //topView.setSize(topView.getWidth() - 270, topView.getHeight());
            //sideView.setSize(sideView.getWidth() - 270, sideView.getHeight());
        } else if (!isVisible){
            //Remove the left component and remove the divider.
            mainSplitPane.setLeftComponent(null);
        }
        repaint();
    }

    //Returns whether the menu var is visible or not.
    public boolean isMenuBarVisible(){
        if (mainSplitPane.getLeftComponent() == null){
            return false;
        } else {
            return true;
        }
    }

    //Returns the name of the currently selected runway.
    public String getSelectedRunway() {
        return selectedRunway;
    }

    //Sets the currently selected runway to some other runway specified by name.
    public void setSelectedRunway(String selectedRunway) {
        if(selectedRunway == "None"){
            this.selectedRunway = "";
        } else {
            this.selectedRunway = selectedRunway;
        }
        repaint();
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

    //Sets the instance of ViewController used by the view.
    public void setController(ViewController newController){
        this.controller = newController;
    }

    //Returns the instance of ViewController used by the view.
    public ViewController getController(){
        return controller;
    }

    //Returns the TopViewPanel used to display the top view of the runway.
    public TopViewPanel getTopView(){
        return topView;
    }

    public SideViewPanel getSideView(){
        return this.sideView;
    }

    //Returns the MenuPanel used to display menu options.
    public MenuPanel getMenuPanel(){
        return menuPanel;
    }

    //Returns the CustomMenuBar used to display the menu along the top of the screen.
    public CustomMenuBar getAppMenuBar(){
        return menuBar;
    }
}