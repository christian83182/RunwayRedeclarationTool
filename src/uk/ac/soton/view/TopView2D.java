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

    TopView2D(AppView appView, FrontEndModel model, MenuPanel menuPanel){
        this.appView = appView;
        this.model = model;
        this.menuPanel = menuPanel;
        this.globalPan = Settings.TOP_DOWN_DEFAULT_PAN;
        this.globalZoom = Settings.TOP_DOWN_DEFAULT_ZOOM;

        this.setPreferredSize(Settings.TOP_DOWN_DEFAULT_SIZE);
        PanAndZoomListener panListener = new PanAndZoomListener();
        this.addMouseWheelListener(panListener);
        this.addMouseListener(panListener);
        this.addMouseMotionListener(panListener);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        //Generate a Buffered Image to draw on instead of using the g2d object.
        BufferedImage img = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Set the background colour.
        g2.setColor(Settings.AIRFIELD_COLOUR);
        g2.fillRect(0,0,getWidth(),getHeight());

        //Configure the graphic's transformation to account for pan and zoom.
        configureGlobalTransform(g2);
        //Draw main view components.
        paintView(g2);
        //Draw a set of axis for debug purposes.
        paintAxis(g2);
        //Use the g2d object to paint the buffered image.
        g2d.drawImage(img,0,0,getWidth(),getHeight(),null);

    }

    //Paints all main components of the UI.
    private void paintView(Graphics2D g2){
        boolean isIsolated = menuPanel.isIsolateMode();
        boolean isRunwaySelected = !(appView.getSelectedRunway() == "");

        //Only draw all runways if isolate mode isn't on, or if it is on but no runway is selected
        if(!isIsolated  || (isIsolated && !isRunwaySelected)){
            paintStrips(g2);
            paintAllClearAndGraded(g2);
            paintRunways(g2);
        } else {
            paintStrip(appView.getSelectedRunway(), g2);
            paintClearAndGraded(appView.getSelectedRunway(),g2);
        }

        //Draw the selected runway on top of everything else.
        paintSelectedRunway(g2);
    }

    //Paints an outline of the clearway for a specified runway.
    private void paintClearway(String id,Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension runwayDim = model.getRunwayDim(id);
        Dimension clearwayDim = model.getClearwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,runwayDim,id));
        g2.setTransform(tx);

        g2.setColor(Settings.CLEARWAY_FILL_COLOUR);
        g2.fillRect(pos.x+runwayDim.width, pos.y-(clearwayDim.height-runwayDim.height)/2,
                clearwayDim.width, clearwayDim.height);
        g2.setColor(Settings.CLEARWAY_STROKE_COLOUR);
        g2.setStroke(Settings.CLEARWAY_STROKE);
        g2.drawRect(pos.x+runwayDim.width, pos.y-(clearwayDim.height-runwayDim.height)/2,
                clearwayDim.width, clearwayDim.height);
        g2.setTransform(old);
    }

    //Draws the runway strip for the specified runway.
    private void paintStrip(String id, Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension runwayDim = model.getRunwayDim(id);
        Integer stripWidthFromCenterline = model.getStripWidthFromCenterline(id);
        Integer stripEndSize = model.getStripEndSize(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,runwayDim,id));
        g2.setTransform(tx);

        g2.setColor(Settings.RUNWAY_STRIP_COLOUR);
        g2.fillRect(pos.x - stripEndSize, pos.y - (stripWidthFromCenterline - (runwayDim.height/2)),
                stripEndSize*2 + runwayDim.width, stripWidthFromCenterline*2);
        g2.setTransform(old);
    }

    //Draws the runway strip for all runways in the model.
    private void paintStrips(Graphics2D g2){
        for(String id : model.getRunways()){
            paintStrip(id,g2);
        }
    }

    //Paints an outline of the stopway for a specified runway.
    private void paintStopway(String id,Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension runwayDim = model.getRunwayDim(id);
        Dimension stopwayDim = model.getStopwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,runwayDim,id));
        g2.setTransform(tx);

        g2.setColor(Settings.STOPWAY_FILL_COLOUR);
        g2.fillRect(pos.x+runwayDim.width, pos.y-(stopwayDim.height-runwayDim.height)/2,
                stopwayDim.width, stopwayDim.height);
        g2.setColor(Settings.STOPWAY_STROKE_COLOUR);
        g2.setStroke(Settings.STOPWAY_STROKE);
        g2.drawRect(pos.x+runwayDim.width, pos.y-(stopwayDim.height-runwayDim.height)/2,
                stopwayDim.width, stopwayDim.height);
        g2.setTransform(old);
    }

    //Draws only the selected runway, such that it appears above all others and appears selected.
    private void paintSelectedRunway(Graphics2D g2){
        //Check if the selected runway is the empty string, if so don't render a selected runway.
        if(appView.getSelectedRunway() == ""){
            return;
        } else {
            String selectedRunway = appView.getSelectedRunway();
            Point pos = model.getRunwayPos(selectedRunway);
            Dimension dim = model.getRunwayDim(selectedRunway);

            paintRunway(selectedRunway, g2);
            paintCenterline(selectedRunway, g2);

            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,selectedRunway));
            g2.setTransform(tx);

            //Drawing the highlight box.
            g2.setColor(Settings.SELECTED_RUNWAY_HIGHLIGHT);
            g2.setStroke(Settings.SELECTED_RUNWAY_STROKE);
            g2.drawRect(pos.x, pos.y, dim.width, dim.height);
            g2.setTransform(old);

            paintClearway(selectedRunway, g2);
            paintStopway(selectedRunway, g2);
        }
    }

    //Configures the specified graphics object such that pan and zoom are taken into account.
    private void configureGlobalTransform(Graphics2D g2){
        //Create a global affine transformation which pans and zooms the view accordingly.
        AffineTransform globalTransform = new AffineTransform();

        //Translate and scale the view to match the pan and zoom settings.
        globalTransform.translate(globalPan.x, globalPan.y);
        globalTransform.scale(globalZoom,globalZoom);

        //Set the create transformation to the one used by the image.
        g2.setTransform(globalTransform);
    }

    //Draws a set of axis which intersect at (0,0).
    private void paintAxis(Graphics2D g2){
        g2.setColor(Settings.AXIS_COLOUR);
        g2.setStroke(Settings.AXIS_STROKE);
        g2.drawLine(-10000,0,10000,0);
        g2.drawLine(0,-10000,0,10000);
    }

    //Draws a runway given the name.
    private void paintRunway(String id, Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension dim = model.getRunwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        g2.setColor(Settings.RUNWAY_COLOUR);
        g2.fillRect(pos.x, pos.y, dim.width, dim.height);
        g2.setTransform(old);
    }

    //Draws the centerline for a runway given the name.
    private void paintCenterline(String id, Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension dim = model.getRunwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        //Paint he centerline for the respective runway.
        g2.setColor(Settings.CENTERLINE_COLOUR);
        g2.setStroke(Settings.CENTERLINE_STROKE);
        Integer padding = Settings.CENTERLINE_PADDING;
        g2.drawLine(pos.x + padding, pos.y + dim.height/2, pos.x + dim.width - padding, pos.y + dim.height/2);
        g2.setTransform(old);
    }

    //Draws the runway & centerline for all runways in the current model.
    private void paintRunways(Graphics2D g2){
        for(String id : model.getRunways()){
            paintRunway(id, g2);
        }

        for(String id : model.getRunways()){
            paintCenterline(id, g2);
        }
    }

    //Draws the clear and graded area for a given runway
    private void paintClearAndGraded(String id, Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension dim = model.getRunwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        //Generate a polygon in the shape of the clear and graded area for the current runway.
        g2.setColor(Settings.CLEAR_AND_GRADED_COLOUR);
        g2.fillPolygon(genClearAndGradedPoly(pos, dim));
        g2.setTransform(old);
    }

    //Draws the clear and graded area for all runways.
    private void paintAllClearAndGraded(Graphics2D g2){
        for(String id : model.getRunways()){
            paintClearAndGraded(id, g2);
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
            Double maxZoom = Settings.TOP_DOWN_MAX_ZOOM;
            Double minZoom = Settings.TOP_DOWN_MIN_ZOOM;
            if(scaleFactor < 0 & globalZoom * 1/0.95 < maxZoom ){
                globalZoom = globalZoom * 1/0.95;
            } else  if (scaleFactor > 0 & globalZoom * 0.95 > minZoom){
                globalZoom =  globalZoom * 0.95;
            } else {
                return;
            }
            TopView2D.this.repaint();
        }
    }

    private class InfoArrow {
        private Integer xOffset;
        private Integer yOffset;
        private Integer length;
        private String label;

        InfoArrow(Integer xOffset, Integer yOffset, Integer length, String label){
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.length = length;
            this.label = label;
        }

        /* Generates an Affine Transformation to local runway coordinates, so that 0,0 refers to the center of the start of
           the runway. */
        public AffineTransform genInfoArrowTransform(String runwayId){
            Point pos = model.getRunwayPos(runwayId);
            Dimension dim = model.getRunwayDim(runwayId);
            AffineTransform tx = createRunwayTransform(pos,dim,runwayId);
            tx.translate(pos.x,pos.y);
            tx.translate(0,dim.height/2);
            return tx;
        }

        public void drawInfoArrow(String runwayId, Graphics2D g2){
            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(genInfoArrowTransform(runwayId));
            g2.setTransform(tx);

            //Draw helper lines.
            g2.setColor(Settings.INFO_ARROW_COLOUR);
            g2.setStroke(Settings.INFO_ARROW_HELPER_STROKE);
            g2.drawLine(xOffset,0,xOffset,yOffset);
            g2.drawLine(xOffset+length, 0,xOffset+length, yOffset);

            //Draw arrow line.
            Point lineStart = new Point(xOffset + Settings.TOP_DOWN_INFO_ARROW_PADDING, yOffset);
            Point lineEnd = new Point(xOffset+length - Settings.TOP_DOWN_INFO_ARROW_PADDING, yOffset);
            g2.setStroke(Settings.INFO_ARROW_STROKE);
            g2.drawLine(lineStart.x + Settings.TOP_DOWN_INFO_ARROW_LENGTH, lineStart.y,
                    lineEnd.x - Settings.TOP_DOWN_INFO_ARROW_LENGTH, lineEnd.y);

            //Draw arrows at each end.
            Integer arrowHeight = Settings.TOP_DOWN_INFO_ARROW_HEIGHT;
            Integer arrowLength = Settings.TOP_DOWN_INFO_ARROW_LENGTH;
            Polygon startArrow = new Polygon(new int[] {lineStart.x, lineStart.x+arrowLength, lineStart.x+arrowLength},
                    new int[] {lineStart.y, lineStart.y-arrowHeight, lineStart.y+arrowHeight},3);
            Polygon endArrow = new Polygon(new int[] {lineEnd.x, lineEnd.x-arrowLength, lineEnd.x-arrowLength},
                    new int[] {lineEnd.y, lineEnd.y-arrowHeight, lineEnd.y+arrowHeight},3);
            g2.fillPolygon(startArrow);
            g2.fillPolygon(endArrow);

            //Draw the label;
            g2.setColor(Settings.INFO_TEXT_COLOUR);
            g2.setFont(Settings.INFO_TEXT_FONT);
            Integer stringLength = g2.getFontMetrics().stringWidth(label);
            Integer stringHeight = Settings.INFO_TEXT_FONT.getSize();
            g2.drawString(label,(lineStart.x + lineEnd.x - stringLength)/2,
                    yOffset+stringHeight+ Settings.TOP_DOWN_INFO_TEXT_PADDING);

            g2.setTransform(old);
        }
    }
}
