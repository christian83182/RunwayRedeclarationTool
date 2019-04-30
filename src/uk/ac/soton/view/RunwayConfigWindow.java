package uk.ac.soton.view;

import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class RunwayConfigWindow extends JDialog {

    AppView appView;
    ViewController controller;

    RunwayConfigWindow(AppView appView){
        this.appView = appView;
        this.controller = appView.getController();
        init();
    }

    private void init(){
        this.setTitle("Airfield Configuration Editor");
        this.setModal(true);
        this.setPreferredSize(new Dimension(1600,900));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JSplitPane root = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.setContentPane(root);
        root.setTopComponent(prepareMenu());
        root.setBottomComponent(prepareView());
        root.setDividerLocation(500);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel prepareMenu(){
        JPanel menuPanel = new JPanel();
        return menuPanel;
    }

    private InteractivePanel prepareView(){
        InteractivePanel viewPanel = new InteractivePanel(new Point(0,0),1.0) {
            @Override
            public void paintView(Graphics2D g2) {
                g2.setColor(Color.WHITE);
                g2.fillRect(0,0,100,100);
            }
        };
        return viewPanel;
    }
}
