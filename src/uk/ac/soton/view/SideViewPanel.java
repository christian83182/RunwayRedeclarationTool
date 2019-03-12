package uk.ac.soton.view;

import java.awt.*;

public class SideViewPanel extends InteractivePanel{

    AppView appView;

    SideViewPanel(AppView appView){
        super(new Point(400,200), 1.0);
        this.appView = appView;
    }

    @Override
    public void paintView(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(-50,-50,100,100);
    }
}
