package uk.ac.soton.view;

import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;

public class SideViewPanel extends InteractivePanel{

    AppView appView;

    SideViewPanel(AppView appView){
        super(new Point(400,200), 1.0);
        this.appView = appView;
    }

    @Override
    public void paintView(Graphics2D g2) {
        paintGradientBackground(g2);

    }

    public void paintGradientBackground(Graphics2D g2){
        Color colorBlue = new Color(84,122,179);
        Color colorGreen = new Color(75,161,79);
        GradientPaint gp = new GradientPaint(0, 0, colorBlue, 0, getHeight(), colorGreen);
        g2.setPaint(gp);
        Point topLeft = new Point(0,0);
        Point bottomRight = new Point(getWidth(), getHeight());

        try {
            g2.getTransform().inverseTransform(topLeft, topLeft);
            g2.getTransform().inverseTransform(bottomRight, bottomRight);
            g2.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

    }
}
