package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

//Represents a JPanel designed to view a top-down view of the runways.
public class TopView2D extends JPanel {


    //Instance of the front end model which contains the information.
    private AppView frontEndModel;
    //Instance of the menu panel which controls a lot of the display settings.
    private MenuPanel menuPanel;

    //Constant which controls the distance between the centerline and the edge of the runway.
    private final Integer runwayBorder = 20;
    //The size of the highlight which is displayed around the selected runway.
    private final Integer selectedBorderSize = 5;

    TopView2D(AppView frontEndModel,MenuPanel menuPanel){
        this.frontEndModel = frontEndModel;
        this.menuPanel = menuPanel;
        this.setPreferredSize(new Dimension(1000,1000));
    }


    //Overwritten method which is
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        /*Generate a Buffered Image to draw on instead of using the g2d object.
          This will make it easier to implement panning, zooming, saving, and printing*/
        BufferedImage img = new BufferedImage(1000,1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        //Set the background colour.
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0,0,1000,1000);

        //If Isolate Mode isn't on then draw all runways, otherwise just draw the selected one.
        paintClearAndGraded(g2);
        if(!menuPanel.isIsolateMode()){
            paintRunways(g2);
            paintCenterLines(g2);
        }
        paintSelectedRunway(g2);

        //Use the g2d object to paint the buffered image.
        g2d.drawImage(img,0,0,getWidth(),getHeight(),null);
    }

    //Draws the runway for all runways in the current model.
    private void paintRunways(Graphics2D g2d){
        g2d.setColor(Color.GRAY);

        for(String id : frontEndModel.getRunways()){
            Point pos = frontEndModel.getRunwayPos(id);
            Dimension dim = frontEndModel.getRunwayDim(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = createRunwayTransform(pos,dim,id);
            g2d.setTransform(tx);

            //Draw the runway itself.
            g2d.fillRect(pos.x, pos.y, dim.width, dim.height);
            g2d.setTransform(old);
        }
    }

    private void paintSelectedRunway(Graphics2D g2d){
        //Check if the selected runway is the empty string, if so don't render a selected runway.
        if(!(frontEndModel.getSelectedRunway() == "")){

            String selectedRunway = frontEndModel.getSelectedRunway();
            Point pos = frontEndModel.getRunwayPos(selectedRunway);
            Dimension dim = frontEndModel.getRunwayDim(selectedRunway);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = createRunwayTransform(pos,dim, selectedRunway);
            g2d.setTransform(tx);

            //Drawing the green highlight box.
            g2d.setColor(new Color(255, 165, 83));
            g2d.setStroke(new BasicStroke(selectedBorderSize));
            g2d.drawRect(pos.x, pos.y, dim.width, dim.height);

            //Drawing the runway
            g2d.setColor(new Color(137, 137, 137));
            g2d.fillRect(pos.x, pos.y, dim.width, dim.height);

            //Drawing the centerline
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10,new float[] {10,5} , 1));
            g2d.drawLine(pos.x + runwayBorder, pos.y + dim.height/2, pos.x + dim.width - runwayBorder, pos.y + dim.height/2);

            g2d.setTransform(old);
        }
    }

    //Draws the centerlines for all runways in the current model.
    private void paintCenterLines(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10,new float[] {10,5} , 1));

        for(String id : frontEndModel.getRunways()){
            Point pos = frontEndModel.getRunwayPos(id);
            Dimension dim = frontEndModel.getRunwayDim(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = createRunwayTransform(pos,dim,id);
            g2d.setTransform(tx);

            //Paint he centerline for the respective runway.
            g2d.drawLine(pos.x + runwayBorder, pos.y + dim.height/2, pos.x + dim.width - runwayBorder, pos.y + dim.height/2);
            g2d.setTransform(old);
        }
    }

    //Draws the clear and graded area for all runways.
    private void paintClearAndGraded(Graphics2D g2d){
        g2d.setColor(new Color(80, 160, 79));

        for(String id : frontEndModel.getRunways()){
            Point pos = frontEndModel.getRunwayPos(id);
            Dimension dim = frontEndModel.getRunwayDim(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = createRunwayTransform(pos,dim,id);
            g2d.setTransform(tx);

            //Generate a polygon in the shape of the clear and graded area for the current runway.
            Polygon poly = genClearAndGradedPoly(pos, dim);
            g2d.fillPolygon(poly);
            g2d.setTransform(old);
        }
    }

    //Generates a custom polygon in the shape of the clear and graded area for some runway.
    private Polygon genClearAndGradedPoly(Point pos, Dimension dim){
        //x-positions for all points defining the shape in terms of the location and size of the runway.
        int[] xCoords = {pos.x -60,pos.x + 150,pos.x + 300,
                pos.x + dim.width - 300, pos.x + dim.width - 150,pos.x + dim.width + 60,
                pos.x + dim.width + 60,pos.x + dim.width - 150,pos.x + dim.width - 300,
                pos.x + 300,pos.x + 150,pos.x -60};
        //y-positions for all points defining the shape in terms of the location and size of the runway.
        int[] yCoords = {pos.y - (150-dim.height)/2,pos.y - (150-dim.height)/2,pos.y - (210-dim.height)/2
                ,pos.y - (210-dim.height)/2,pos.y - (150-dim.height)/2,pos.y - (150-dim.height)/2
                ,pos.y + (150+dim.height)/2,pos.y + (150+dim.height)/2,pos.y + (210+dim.height)/2
                ,pos.y + (210+dim.height)/2,pos.y + (150+dim.height)/2,pos.y + (150+dim.height)/2};
        //The number of points in the shape.
        int n = xCoords.length;
        Polygon poly = new Polygon(xCoords, yCoords, n);
        return poly;
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
