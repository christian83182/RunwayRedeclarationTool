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
        paintBackground(g2);

        g2.setColor(Color.BLACK);
        g2.drawLine(-100000,0,100000,0);
        g2.drawLine(0,-100000, 0,100000);

        Point p1 = new Point(100,0);
        Point p2 = new Point(400,200);
        DataArrow arrow = new DataArrow(p1,p2, -100,"Hello World");
        arrow.drawArrow(g2);
    }

    public void paintBackground(Graphics2D g2){
        GradientPaint skyGradient = new GradientPaint(0,0,Settings.SIDEVIEW_SKY_COLOUR_BOTTOM, 0,-2000, Settings.SIDEVIEW_SKY_COLOUR_TOP);
        g2.setPaint(skyGradient);
        g2.fillRect(-100000,-100000,200000,100000);
        GradientPaint groundGradient = new GradientPaint(0,0,Settings.SIDEVIEW_GROUND_COLOUR_TOP, 0,2000, Settings.SIDEVIEW_GROUND_COLOUR_BOTTOM);
        g2.setPaint(groundGradient);
        g2.fillRect(-100000,0,200000,100000);
    }
}
