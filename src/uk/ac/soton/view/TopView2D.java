package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

//Represents a JPanel designed to view a top-down view of the runways.
public class TopView2D extends JPanel {

    //Backend-independent storage of the current model.
    private Map<String,Point> runwayPos;
    private Map<String,Dimension> runwayDims;
    //Constant which controls the distance between the centerline and the edge of the runway.
    private final Integer runwayBorder = 20;

    TopView2D(Map<String,Point> runwayPos, Map<String,Dimension> runwayDims){
        this.runwayDims = runwayDims;
        this.runwayPos = runwayPos;
        this.setPreferredSize(new Dimension(1000,1000));
    }

    //Overwritten method which is
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        paintRunways(g2d);
        paintCenterLines(g2d);
        super.paint(g);
    }

    //Draws the runway for all runways in the current model.
    private void paintRunways(Graphics2D g2d){
        g2d.setColor(Color.GRAY);

        for(String id : runwayPos.keySet()){
            Point pos = runwayPos.get(id);
            Dimension dim = runwayDims.get(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = createRunwayTransform(pos,dim,id);
            g2d.setTransform(tx);

            //Draw the runway itself.
            g2d.fillRect(pos.x, pos.y, dim.width, dim.height);
            g2d.setTransform(old);
        }
    }

    //Draws the centerlines for all runways in the current model.
    private void paintCenterLines(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10,new float[] {10,5} , 1));

        for (String id : runwayPos.keySet()){
            Point pos = runwayPos.get(id);
            Dimension dim = runwayDims.get(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = createRunwayTransform(pos,dim,id);
            g2d.setTransform(tx);

            //Paint he centerline for the respective runway.
            g2d.drawLine(pos.x + runwayBorder, pos.y + dim.height/2, pos.x + dim.width - runwayBorder, pos.y + dim.height/2);
            g2d.setTransform(old);
        }
    }

    /* Generates an Affine transformation which rotates the runway to match its bearing and moves the centre of translation to
       the center of the left side. */
    private AffineTransform createRunwayTransform(Point pos, Dimension dim, String id){
        Double bearing = Math.toRadians(Integer.parseInt(id.substring(0,2))*10-90);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,-dim.height/2);
        AffineTransform rx = new AffineTransform(tx);
        rx.rotate(bearing,pos.x, pos.y+(dim.height/2));
        return rx;
    }
}
