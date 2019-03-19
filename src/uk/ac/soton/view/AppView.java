package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame{

    //An instance of the front end controller used to store the data displayed.
    private ViewController controller;
    //Note that for the runwayDimensions, the length is the x value and the width is the y value.
    private String selectedRunway;

    CustomMenuBar menuBar;
    MenuPanel menuPanel;
    TopViewPanel topView;
    SideViewPanel sideView;
    JSplitPane viewPanel;

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

        menuBar = new CustomMenuBar(controller,this);
        this.setJMenuBar(menuBar);

        menuPanel = new MenuPanel(this);
        this.add(menuPanel, BorderLayout.WEST);

        topView = new TopViewPanel(this);
        sideView = new SideViewPanel(this);

        viewPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topView, sideView);
        viewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
        this.add(viewPanel, BorderLayout.CENTER);
        setSplitViewVisible(false);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //Will either hide or display the split view.
    public void setSplitViewVisible(boolean isVisible){
        //If the bottom component is unset and it should be visible.
        if(viewPanel.getBottomComponent() == null && isVisible){
            viewPanel.setBottomComponent(sideView);
            viewPanel.setDividerLocation((int)(getHeight()*0.55));
            viewPanel.setDividerSize(10);
            //Manually update the size of TopView so that the view centers correctly.
            topView.setSize(topView.getWidth(), (int)(topView.getHeight()*0.55));
        } else if (!isVisible){
            //remove the bottom component and set the divider size to 0.
            viewPanel.setBottomComponent(null);
            viewPanel.setDividerSize(0);
        }
        repaint();
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