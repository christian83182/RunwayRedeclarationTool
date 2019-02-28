package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

//Represents a JPanel designed to view a top-down view of the runways.
public class TopView2D extends JPanel {

    //Instance of the front end model which contains the information.
    private FrontEndModel model;
    //Instance of the AppView class used to access the selected runway.
    private AppView appView;
    //Instance of the menu panel which controls a lot of the display settings.
    private MenuPanel menuPanel;
    //Variables used to keep track of the current level of zoom, and pan location.
    private Point globalPan;
    private Double globalZoom;

    //Default values used throughout the class.
    private final Integer CENTERLINE_PADDING = 20;
    private final Integer SELECTED_RUNWAY_HIGHLIGHT_WIDTH = 3;
    private final Double MAX_ZOOM_FACTOR = 5.0;
    private final Double MIN_ZOOM_FACTOR = 0.05;
    private final Dimension DEFAULT_VIEW_SIZE = new Dimension(800,800);
    private final Point DEFAULT_PAN = new Point(DEFAULT_VIEW_SIZE.width/2, DEFAULT_VIEW_SIZE.height/2);
    private final Double DEFAULT_ZOOM = 0.5;


    TopView2D(AppView appView, FrontEndModel model, MenuPanel menuPanel){
        this.appView = appView;
        this.model = model;
        this.menuPanel = menuPanel;
        this.globalPan = new Point(DEFAULT_PAN);
        this.globalZoom = new Double(DEFAULT_ZOOM);

        this.setPreferredSize(DEFAULT_VIEW_SIZE);
        PanAndZoomListener panListener = new PanAndZoomListener();
        this.addMouseWheelListener(panListener);
        this.addMouseListener(panListener);
        this.addMouseMotionListener(panListener);
    }

    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        //Generate a Buffered Image to draw on instead of using the g2d object.
        BufferedImage img = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Set the background colour.
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0,0,getWidth(),getHeight());

        //Create a global affine transformation which pans and zooms the view accordingly.
        AffineTransform globalTransform = new AffineTransform();
        //Translate and scale the view to match the pan and zoom settings.
        globalTransform.translate(globalPan.x, globalPan.y);
        globalTransform.scale(globalZoom,globalZoom);
        //Set the create transformation to the one used by the image.
        g2.setTransform(globalTransform);

        //Draw runways, centerlines, clear and graded areas and more to the screen.
        paintClearAndGraded(g2);
        //Only draw non-selected runways if not in isolated mode, or if in isolated mode but no runway is selected.
        if(!menuPanel.isIsolateMode() || (menuPanel.isIsolateMode() && appView.getSelectedRunway() == "")){
            paintRunways(g2);
            paintCenterLines(g2);
        }
        //Draw the selected runway on top of everything else.
        paintSelectedRunway(g2);

        //Draw a set of x & y axis for debug(?)
        g2.setColor(new Color(101, 101, 101));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(-10000,0,10000,0);
        g2.drawLine(0,-10000,0,10000);

        //Use the g2d object to paint the buffered image.
        g2d.drawImage(img,0,0,getWidth(),getHeight(),null);

    }

    //Draws the runway for all runways in the current model.
    private void paintRunways(Graphics2D g2d){
        g2d.setColor(Color.GRAY);
        for(String id : model.getRunways()){
            Point pos = model.getRunwayPos(id);
            Dimension dim = model.getRunwayDim(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,id));
            g2d.setTransform(tx);

            //Draw the runway itself.
            g2d.fillRect(pos.x, pos.y, dim.width, dim.height);
            g2d.setTransform(old);
        }
    }

    //Draws only the selected runway, such that it appears above all others and appears selected.
    private void paintSelectedRunway(Graphics2D g2d){
        //Check if the selected runway is the empty string, if so don't render a selected runway.
        if(!(appView.getSelectedRunway() == "")){

            String selectedRunway = appView.getSelectedRunway();
            Point pos = model.getRunwayPos(selectedRunway);
            Dimension dim = model.getRunwayDim(selectedRunway);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,selectedRunway));
            g2d.setTransform(tx);

            //Drawing the runway
            g2d.setColor(new Color(137, 137, 137));
            g2d.fillRect(pos.x, pos.y, dim.width, dim.height);

            //Drawing the centerline
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10,new float[] {15,10} , 1));
            g2d.drawLine(pos.x + CENTERLINE_PADDING, pos.y + dim.height/2, pos.x + dim.width - CENTERLINE_PADDING, pos.y + dim.height/2);

            //Drawing the highlight box.
            g2d.setColor(new Color(255, 165, 83));
            g2d.setStroke(new BasicStroke(SELECTED_RUNWAY_HIGHLIGHT_WIDTH));
            g2d.drawRect(pos.x, pos.y, dim.width, dim.height);

            g2d.setTransform(old);
        }
    }

    //Draws the centerlines for all runways in the current model.
    private void paintCenterLines(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10,new float[] {15,10} , 1));
        for(String id : model.getRunways()){
            Point pos = model.getRunwayPos(id);
            Dimension dim = model.getRunwayDim(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,id));
            g2d.setTransform(tx);

            //Paint he centerline for the respective runway.
            g2d.drawLine(pos.x + CENTERLINE_PADDING, pos.y + dim.height/2, pos.x + dim.width - CENTERLINE_PADDING, pos.y + dim.height/2);
            g2d.setTransform(old);
        }
    }

    //Draws the clear and graded area for all runways.
    private void paintClearAndGraded(Graphics2D g2d){
        g2d.setColor(new Color(80, 160, 79));

        for(String id : model.getRunways()){
            Point pos = model.getRunwayPos(id);
            Dimension dim = model.getRunwayDim(id);

            AffineTransform old = g2d.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,id));
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
        Double bearing = Math.toRadians(model.getBearing(id)-90);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,-dim.height/2);
        AffineTransform rx = new AffineTransform(tx);
        rx.rotate(bearing,pos.x, pos.y+(dim.height/2));
        return rx;
    }

    //Inner class devoted to giving the view zoom and pan functionality.
    private class PanAndZoomListener extends MouseAdapter{

        Point startPoint;
        Point originalGlobalPan;

        PanAndZoomListener(){
            startPoint = new Point(0,0);
            originalGlobalPan = new Point(0,0);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            originalGlobalPan = (Point)globalPan.clone();
            startPoint = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            globalPan.x = originalGlobalPan.x + (e.getX() - startPoint.x);
            globalPan.y = originalGlobalPan.y + (e.getY() - startPoint.y);
            TopView2D.this.repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Integer scaleFactor = e.getWheelRotation();
            if(scaleFactor < 0 & globalZoom * 1/0.95 < MAX_ZOOM_FACTOR ){
                globalZoom = globalZoom * 1/0.95;
            } else  if (scaleFactor > 0 & globalZoom * 0.95 > MIN_ZOOM_FACTOR){
                globalZoom =  globalZoom * 0.95;
            } else {
                return;
            }
            TopView2D.this.repaint();
        }
    }
}
