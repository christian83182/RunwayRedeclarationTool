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
    //Variable used to keep track of the current pan location.
    private Point globalPan;
    //Variable used to keep track of the current level of zoom,
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Generate a Buffered Image to draw on instead of using the g2d object.
        BufferedImage img = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        //Set the background colour.
        g2.setColor(Settings.AIRFIELD_COLOUR);
        g2.fillRect(0,0,getWidth(),getHeight());

        //Configure the graphic's transformation to account for pan and zoom.
        configureGlobalTransform(g2);
        //Draw main view components.
        paintView(g2);
        //Use the g2d object to paint the buffered image.
        g2d.drawImage(img,0,0,getWidth(),getHeight(),null);
        drawCompass(g2d);

    }

    private void drawCompass(Graphics2D g2d){
        Point center = new Point(getWidth()-70, 70);

        Polygon northArrow = new Polygon(new int[] {center.x-10, center.x, center.x + 10}, new int[] {center.y, center.y-40, center.y}, 3);
        Polygon southArrow = new Polygon(new int[] {center.x-10, center.x, center.x + 10}, new int[] {center.y, center.y+40, center.y}, 3);
        g2d.setColor(new Color(255,0, 16));
        g2d.fillPolygon(northArrow);

        g2d.setColor(new Color(223, 223, 223));
        g2d.fillPolygon(southArrow);

        g2d.setColor(new Color(0, 0, 0,50));
        g2d.fillOval(center.x-50, center.y-50, 100,100);

        g2d.setColor(new Color(219, 219, 219));
        g2d.setFont(new Font("Times New Roman", Font.BOLD, 14));
        g2d.drawString("N", center.x-5, center.y-5);

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

    //Draws the runway's name at the start of the runway.
    private void paintRunwayName(String id, Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension dim = model.getRunwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        if(id.length() == 2){
            g2.setColor(Settings.RUNWAY_COLOUR);
            g2.setFont(new Font("TimesRoman", 0, (int)(dim.height*0.8)));
            g2.fillRect(pos.x, pos.y, g2.getFontMetrics().stringWidth(id) + Settings.CENTERLINE_PADDING*2, dim.height);

            g2.setColor(Settings.RUNWAY_NAME_COLOUR);
            g2.drawString(id, pos.x+Settings.CENTERLINE_PADDING, pos.y + (int)(dim.height*0.8));
        } else if (id.length() > 2) {
            g2.setColor(Settings.RUNWAY_COLOUR);
            g2.setFont(new Font("TimesRoman", 0, (int)(dim.height*0.4)));
            g2.fillRect(pos.x, pos.y, g2.getFontMetrics().stringWidth(id.substring(0,2)) + Settings.CENTERLINE_PADDING*2, dim.height);

            Integer upperStringLength = g2.getFontMetrics().stringWidth(id.substring(0,2));
            Integer lowerStringLength = g2.getFontMetrics().stringWidth(id.substring(2));
            Integer lowerStringXOffset =  Settings.CENTERLINE_PADDING + (upperStringLength-lowerStringLength)/2;
            g2.setColor(Settings.RUNWAY_NAME_COLOUR);
            g2.drawString(id.substring(0,2), pos.x+Settings.CENTERLINE_PADDING, pos.y + (int)(dim.height*0.5));
            g2.drawString(id.substring(2), pos.x+lowerStringXOffset , pos.y + (int)(dim.height*0.85));
        }
        g2.setTransform(old);
    }

    //Draws an arrow in the landing direction of the runway.
    private void paintLandingDirection(String id, Graphics2D g2){
        Point pos = model.getRunwayPos(id);
        Dimension dim = model.getRunwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        Point arrowStart = new Point(pos.x + Settings.CENTERLINE_PADDING + (int)(dim.width*0.15), pos.y + dim.height/2);

        Integer arrowHeight = (int)(dim.height*0.2), arrowLength = arrowHeight*2;
        Polygon poly = new Polygon(new int[] {arrowStart.x, arrowStart.x, arrowStart.x + arrowLength,},
                new int[] {arrowStart.y - arrowHeight, arrowStart.y + arrowHeight, arrowStart.y}, 3);
        g2.setColor(Settings.CENTERLINE_COLOUR);
        g2.fillPolygon(poly);

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
        for(String id : model.getRunways()){
            //paintLandingDirection(id, g2);
        }
        for(String id : model.getRunways()){
            paintRunwayName(id, g2);
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

    //Draws the runway strip for all runways in the model.
    private void paintStrips(Graphics2D g2){
        for(String id : model.getRunways()){
            paintStrip(id,g2);
        }
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
            paintRunwayName(selectedRunway, g2);
            paintLandingDirection(selectedRunway, g2);

            //Create an Affine Transformation for the current runway.
            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(createRunwayTransform(pos,dim,selectedRunway));
            g2.setTransform(tx);

            //Drawing the highlight box.
            g2.setColor(Settings.SELECTED_RUNWAY_HIGHLIGHT);
            g2.setStroke(Settings.SELECTED_RUNWAY_STROKE);
            g2.drawRect(pos.x, pos.y, dim.width, dim.height);

            //Draw the displaced threshold indicator.
            if(model.getRunwayThreshold(selectedRunway) > 0){
                g2.setColor(Settings.THRESHOLD_INDICATOR_COLOUR);
                g2.fillRect(pos.x, pos.y, model.getRunwayThreshold(selectedRunway), dim.height);
            }
            g2.setTransform(old);

            //Draw other information based on selected checked boxes in the menu panel.
            paintClearway(selectedRunway, g2);
            paintStopway(selectedRunway, g2);
            if(menuPanel.isShowOtherEnabled()){
                paintOtherLengths(selectedRunway, g2);
            }
            if(menuPanel.isShowRunwayParametersEnabled()){
                paintRunwayParameters(selectedRunway, g2);
                if(menuPanel.isShowBreakDownEnabled()) paintBreakdownLengths(selectedRunway, g2);
            }
        }
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

        //Draw a set of axis if the option is selected in the menu panel.
        if(menuPanel.isShowAxis()) paintAxis(g2);
    }

    //Prints the TODA, TORA, ASDA, and LDA for a given runway.
    private void paintRunwayParameters(String id, Graphics2D g2){
        Integer stripHeight = model.getStripWidthFromCenterline(id);

        //Draw the TORA length.
        Integer toraLength = model.getRunwayTORA(id);
        InfoArrow toraLengthInfo = new InfoArrow(0,stripHeight+150,toraLength,"TORA: " + toraLength + "m");
        toraLengthInfo.drawInfoArrow(id, g2);

        //Draw the TODA length.
        Integer todaLength = model.getRunwayTODA(id);
        InfoArrow todaLengthInfo = new InfoArrow(0,stripHeight+350,todaLength,"TODA: " + todaLength + "m");
        todaLengthInfo.drawInfoArrow(id, g2);

        //Draw the ASDA length.
        Integer asdaLength = model.getRunwayASDA(id);
        InfoArrow asdaLengthInfo = new InfoArrow(0,stripHeight+250,asdaLength,"ASDA: " + asdaLength + "m");
        asdaLengthInfo.drawInfoArrow(id, g2);

        //Draw the LDA length.
        Integer ldaLength = model.getRunwayLDA(id);
        InfoArrow ldaLengthInfo = new InfoArrow(model.getRunwayThreshold(id), stripHeight+50, ldaLength,"LDA: " + ldaLength + "m");
        ldaLengthInfo.drawInfoArrow(id, g2);
    }

    //Prints the breakdown for the runway parameters given some runway.
    private void paintBreakdownLengths(String id, Graphics2D g2){
        //soon to come.
    }

    //Draws the length of various runway components for some specified runway.
    private void paintOtherLengths(String id, Graphics2D g2){
        Dimension clearwayDim = model.getClearwayDim(id);
        Dimension runwayDim = model.getRunwayDim(id);
        Dimension stopwayDim = model.getStopwayDim(id);
        Integer stripHeight = model.getStripWidthFromCenterline(id);

        //Draw the clearway length.
        InfoArrow clearwayLengthInfo = new InfoArrow(runwayDim.width, -stripHeight-50, clearwayDim.width, clearwayDim.width +"m");
        clearwayLengthInfo.drawInfoArrow(id, g2);

        //Draw the stopway length.
        InfoArrow stopwayLengthInfo = new InfoArrow(runwayDim.width, -stripHeight-130, stopwayDim.width, stopwayDim.width +"m");
        stopwayLengthInfo.drawInfoArrow(id, g2);

        //Draw the displaced threshold length if it exists.
        if(model.getRunwayThreshold(id) > 0){
            InfoArrow thresholdLength = new InfoArrow(0, -stripHeight-50, model.getRunwayThreshold(id), model.getRunwayThreshold(id) + "m");
            thresholdLength.drawInfoArrow(id, g2);
        }
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

    //Draws a set of axis which intersect at (0,0).
    private void paintAxis(Graphics2D g2){
        g2.setColor(Settings.AXIS_COLOUR);
        g2.setStroke(Settings.AXIS_STROKE);
        g2.drawLine(-10000,0,10000,0);
        g2.drawLine(0,-10000,0,10000);
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

    //Inner class representing an instance of an arrow displaying some information.
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
            if(yOffset > 0){
                g2.drawString(label,(lineStart.x + lineEnd.x - stringLength)/2, yOffset+stringHeight+ Settings.TOP_DOWN_INFO_TEXT_PADDING);
            } else {
                g2.drawString(label,(lineStart.x + lineEnd.x - stringLength)/2, yOffset-Settings.TOP_DOWN_INFO_TEXT_PADDING);
            }



            g2.setTransform(old);
        }
    }

}
