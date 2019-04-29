package uk.ac.soton.view;

import javafx.scene.transform.Affine;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.controller.ViewController;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;

//Represents a JPanel designed to view a top-down view of the runways.
public class TopViewPanel extends InteractivePanel {

    //Instance of the AppView class used to access the selected runway.
    private AppView appView;
    //Instance of the front end controller which contains the information.
    private ViewController controller;
    //Instance of the menu panel which controls a lot of the display settings.
    private MenuPanel menuPanel;

    TopViewPanel(AppView appView){
        super(Settings.TOP_DOWN_DEFAULT_PAN,Settings.TOP_DOWN_DEFAULT_ZOOM);
        this.appView = appView;
        this.controller = appView.getController();
        this.menuPanel = appView.getMenuPanel();
        this.setPreferredSize(Settings.TOP_DOWN_DEFAULT_SIZE);
        this.setSize(Settings.TOP_DOWN_DEFAULT_SIZE);

        fitViewToAllRunways();
    }

    @Override
    public void paintView(Graphics2D g2){
        boolean isIsolated = menuPanel.isIsolateMode();
        boolean isRunwaySelected = !(appView.getSelectedRunway() == "");

        //Rotate the view to match the rotation of the selected runway.
        if(menuPanel.isViewMatchedToSelection()){
            g2.rotate(Math.toRadians(-getRotationOfSelectedRunway()+90));
        }

        //Draw the background.
        paintBackground(g2,Settings.AIRFIELD_COLOUR);

        //Only draw all runways if isolate mode isn't on, or if it is on but no runway is selected
        if(!isIsolated || (isIsolated && !isRunwaySelected)){
            paintStrips(g2);
            paintAllClearAndGraded(g2);
            paintRunways(g2);
        } else {
            paintStrip(appView.getSelectedRunway(), g2);
            paintClearAndGraded(appView.getSelectedRunway(),g2);
        }

        //Draw the selected runway on top of everything else.
        paintSelectedRunway(g2);

        //Reset the transformation used by the graphics object so the overlay doesn't pan or zoom.
        g2.setTransform(new AffineTransform());
        //Paint the compass and legend if the option is selected.
        if(menuPanel.isShowOverlay()) {
            paintCompass(getRotationOfSelectedRunway(), g2);
            paintLegend(g2);
            paintScale(g2);
        }
    }

    //Paints the background a certain colour.
    private void paintBackground(Graphics2D g2, Color color){
        g2.setColor(color);

        g2.fillRect(Integer.MIN_VALUE/2, Integer.MIN_VALUE/2, Integer.MAX_VALUE, Integer.MAX_VALUE);

        if(controller.getBackgroundImage()!=null){
            BufferedImage img = controller.getBackgroundImage();
            Point offset = controller.getBackgroundImageOffset();
            Double scale = controller.getBackgroundImageScale();
            Double rotation = controller.getBackgroundRotation();

            Integer xPos = (int)(-img.getWidth()*scale)/2 + offset.x;
            Integer yPos = (int)(-img.getHeight()*scale)/2 + offset.y;
            Integer width = (int)(img.getWidth()*scale);
            Integer height = (int)(img.getHeight()*scale);

            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            AffineTransform rx = new AffineTransform();
            rx.setToRotation(Math.toRadians(rotation),xPos+width/2,yPos+height/2);
            tx.concatenate(rx);
            g2.setTransform(tx);

            g2.drawImage(img, xPos, yPos, width, height,null);
            g2.setTransform(old);
        }
    }

    //Draws the legend on the bottom right corner of the screen.
    private void paintLegend(Graphics2D g2){
        Legend legend = new Legend("Legend");
        legend.addToLegend("Clear & Graded Area", Settings.CLEAR_AND_GRADED_COLOUR);
        legend.addToLegend("Runway Strip", Settings.RUNWAY_STRIP_COLOUR);
        legend.addToLegend("Runway", Settings.RUNWAY_COLOUR);
        legend.addToLegend("Displaced Threshold", Settings.SELECTED_RUNWAY_HIGHLIGHT);
        legend.addToLegend("Stopway", Settings.STOPWAY_STROKE_COLOUR);
        legend.addToLegend("Clearway", Settings.CLEARWAY_STROKE_COLOUR);
        legend.addToLegend("Obstacle", Settings.OBSTACLE_FILL_COLOUR);
        legend.drawLegend(g2, new Point(getWidth()-10,getHeight()-10));
    }

    //Draws a compass in the top left corner of the screen
    private void paintCompass(Integer rotation, Graphics2D g2){
        Point center = new Point(getWidth()-50, 50);
        AffineTransform old = (AffineTransform) g2.getTransform().clone();

        //Use a transform to rotate the compass the relevant amount.
        if(menuPanel.isViewMatchedToSelection()){
            AffineTransform rx = g2.getTransform();
            rx.setToRotation(Math.toRadians(-rotation+90),center.x, center.y);
            g2.setTransform(rx);
        }

        //Draw transparent ovals for the compass to lie on.
        g2.setColor(new Color(17, 17, 17,100));
        g2.fillOval(center.x-35, center.y-35, 70,70);
        g2.setColor(new Color(17, 17, 17));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(center.x-35, center.y-35, 70,70);

        //Define polygons for the north and south needles.
        Polygon northArrow = new Polygon(new int[] {center.x-8, center.x, center.x + 8}, new int[] {center.y, center.y-30, center.y}, 3);
        Polygon southArrow = new Polygon(new int[] {center.x-8, center.x, center.x + 8}, new int[] {center.y, center.y+30, center.y}, 3);

        //Draw the needles
        g2.setColor(new Color(255,0, 16));
        g2.fillPolygon(northArrow);
        g2.setColor(new Color(223, 223, 223));
        g2.fillPolygon(southArrow);

        //Draw an "N" on the north needle.
        g2.setColor(new Color(219, 219, 219));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("N", center.x-4, center.y-5);

        g2.setTransform(old);

    }

    //Generates an Affine transformation which rotates the runway to match its bearing
    private AffineTransform createRunwayTransform(Point pos, Dimension dim, String id){
        Double bearing = Math.toRadians(controller.getBearing(id)-90);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,-dim.height/2);
        AffineTransform rx = new AffineTransform(tx);
        rx.rotate(bearing,pos.x, pos.y+(dim.height/2));
        return rx;
    }

    //Draws a runway given the name.
    private void paintRunway(String id, Graphics2D g2){
        Point pos = controller.getRunwayPos(id);
        Dimension dim = controller.getRunwayDim(id);

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
        Point pos = controller.getRunwayPos(id);
        Dimension dim = controller.getRunwayDim(id);

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
        Point pos = controller.getRunwayPos(id);
        Dimension dim = controller.getRunwayDim(id);

        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        if(id.length() == 2){
            g2.setColor(Settings.RUNWAY_COLOUR);
            g2.setFont(new Font("SansSerif", 0, (int)(dim.height*0.8)));
            g2.fillRect(pos.x, pos.y, g2.getFontMetrics().stringWidth(id) + Settings.CENTERLINE_PADDING*2, dim.height);

            g2.setColor(Settings.RUNWAY_NAME_COLOUR);
            g2.drawString(id, pos.x+Settings.CENTERLINE_PADDING, pos.y + (int)(dim.height*0.8));
        } else if (id.length() > 2) {
            g2.setColor(Settings.RUNWAY_COLOUR);
            g2.setFont(new Font("SansSerif", 0, (int)(dim.height*0.4)));
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
        Point pos = controller.getRunwayPos(id);
        Dimension dim = controller.getRunwayDim(id);

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

    //Draws the runway & centerline for all runways in the current controller.
    private void paintRunways(Graphics2D g2){
        for(String id : controller.getRunways()){
            paintRunway(id, g2);
        }
        ArrayList<Integer> drawnRunways = new ArrayList<>();
        for(String id : controller.getRunways()){
            Integer currentRunwayBearing = controller.getBearing(id);
            if(!(drawnRunways.contains(currentRunwayBearing+180) || drawnRunways.contains(currentRunwayBearing-180))){
                paintCenterline(id,g2);
                drawnRunways.add(currentRunwayBearing);
            }
        }
        for(String id : controller.getRunways()){
            paintRunwayName(id, g2);
        }
        drawnRunways = new ArrayList<>();
        for(String id : controller.getRunways()){
            Integer currentRunwayBearing = controller.getBearing(id);
            if(!(drawnRunways.contains(currentRunwayBearing+180) || drawnRunways.contains(currentRunwayBearing-180))){
                paintObstacles(id,g2);
                drawnRunways.add(currentRunwayBearing);
            }
        }
    }

    //Draws the clear and graded area for a given runway
    private void paintClearAndGraded(String id, Graphics2D g2){
        Point pos = controller.getRunwayPos(id);
        Dimension dim = controller.getRunwayDim(id);

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
        for(String id : controller.getRunways()){
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

    //Draws the runway strip for all runways in the controller.
    private void paintStrips(Graphics2D g2){
        for(String id : controller.getRunways()){
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
            Point pos = controller.getRunwayPos(selectedRunway);
            Dimension dim = controller.getRunwayDim(selectedRunway);

            //Paint basic runway features
            paintRunway(selectedRunway, g2);
            paintCenterline(selectedRunway, g2);
            paintRunwayName(selectedRunway, g2);
            paintLandingDirection(selectedRunway, g2);
            paintObstacles(selectedRunway, g2);

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
            if(controller.getRunwayThreshold(selectedRunway) > 0){
                g2.setColor(Settings.THRESHOLD_INDICATOR_COLOUR);
                g2.fillRect(pos.x, pos.y, controller.getRunwayThreshold(selectedRunway), dim.height);
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
                if(menuPanel.isShowBreakDownEnabled()){
                    if(!controller.getRunwayObstacle(selectedRunway).equals("") && controller.isRedeclared(selectedRunway)){
                        paintObstacleParameters(selectedRunway, controller.getRunwayObstacle(selectedRunway), g2);
                    }
                }
            }
        }
    }

    //Paints the obstacle (if one is present) on the specified runway.
    private void paintObstacles(String id, Graphics2D g2){
        Point pos = controller.getRunwayPos(id);
        Dimension dim = controller.getRunwayDim(id);
        String obstacleName = controller.getRunwayObstacle(id);

        //Create a transformation to match the rotation of the runway.
        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(pos,dim,id));
        g2.setTransform(tx);

        //If there is an obstacle on the runway then draw it, otherwise move on.
        if(!obstacleName.equals("")){
            //Set up some variables use for calculations.
            Integer centerLineDistance = controller.getDistanceFromCenterline(id);
            Integer edgeDistance = controller.getDistanceFromThreshold(id) + controller.getObstacleOffset(id);
            Integer obstacleLength = controller.getPredefinedObstacleLength(obstacleName);
            Integer obstacleWidth = controller.getPredefinedObstacleWidth(obstacleName);

            if(controller.getLogicalRunwayCloserToObstacle(id).getName().equals(id)){
                edgeDistance = edgeDistance - obstacleLength;
            }
            else{
                edgeDistance = edgeDistance + obstacleLength;
            }

            //Draw the fill of the rectangle.
            g2.setColor(Settings.OBSTACLE_FILL_COLOUR);
            g2.fillRect(pos.x + edgeDistance,
                    pos.y + (int)(dim.height - obstacleWidth)/2 - centerLineDistance,
                    obstacleLength.intValue(),obstacleWidth.intValue());
            //Draw the stroke for the rectangle.
            g2.setColor(Settings.OBSTACLE_STROKE_COLOUR);
            g2.setStroke(Settings.OBSTACLE_STROKE);
            g2.drawRect(pos.x + edgeDistance,
                    pos.y + (int)(dim.height - obstacleWidth)/2 - centerLineDistance,
                    obstacleLength.intValue(),obstacleWidth.intValue());
        }
        //restore the previous transformation
        g2.setTransform(old);
    }

    private void paintObstacleParameters(String runwayId, String obstacleId, Graphics2D g2){
        Point runwayPos = controller.getRunwayPos(runwayId);
        Dimension runwayDim = controller.getRunwayDim(runwayId);

        //Create an Affine Transformation for the current runway.
        AffineTransform old = g2.getTransform();
        AffineTransform tx = (AffineTransform) old.clone();
        tx.concatenate(createRunwayTransform(runwayPos, runwayDim, runwayId));
        g2.setTransform(tx);

        //helperHeights for drawing obstacle parameters
        Integer stripHeight = controller.getStripWidthFromCenterline(runwayId);

        Integer distanceFromAlsHelperHeight = -stripHeight -140;
        Integer resaHelperHeight = -stripHeight -40;
        Integer blastDistanceHelperHeight = stripHeight + 50;

        //if the obstacle has triggered a redeclaration of the runway, draw the redeclared parameters
        if(controller.isRedeclared(runwayId)) {

            Integer distanceFromEdge = controller.getDistanceFromThreshold(runwayId) + controller.getObstacleOffset(runwayId);
            Integer distanceFromCenterline = controller.getDistanceFromCenterline(runwayId);
            Integer oLength = controller.getPredefinedObstacleLength(obstacleId);

            //helper distance to draw a helper length from the centerline
            Integer helperStartPoint = runwayDim.height/2;

            // Breakdown parameter lengths and labels
            Integer resa = controller.getRESADistance(runwayId);
            String resaLabel = "RESA: " + resa +"m";
            Integer alsDistance = controller.getPredefinedObstacleHeight(obstacleId) * controller.getALS(runwayId);
            String alsLabel = "ALS/TOCS: " + (controller.getPredefinedObstacleHeight(obstacleId)* Airfield.getMinAngleOfDecent()) +"m";
            Integer blastDist = controller.getBlastingDistance();
            String blasDistLabel = "Blast Dist: " + blastDist+"m";
            Integer newStripEnd = controller.getStripEndSize(runwayId);
            String newStripEndLabel = newStripEnd + "m";

            // Draw parameters to the right
            if(controller.getLogicalRunwayCloserToObstacle(runwayId).getName().equals(runwayId)) {

                Point startResa = new Point(runwayPos.x + distanceFromEdge, runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point endResa = new Point (runwayPos.x + distanceFromEdge + resa, runwayPos.y + helperStartPoint);
                Point startResaStripEnd = new Point(endResa.x, runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point endResaStripEnd = new Point (endResa.x +newStripEnd, runwayPos.y + helperStartPoint);

                Point startAls = new Point(runwayPos.x + distanceFromEdge - oLength, runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point endAls = new Point (runwayPos.x + distanceFromEdge + alsDistance, runwayPos.y + helperStartPoint);
                Point startAlsStripEnd = new Point(endAls.x, runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point endAlsStripEnd = new Point (endAls.x + newStripEnd, runwayPos.y + helperStartPoint);

                Point startBlastDist = new Point(runwayPos.x + distanceFromEdge,runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point endBlastDist = new Point (runwayPos.x + distanceFromEdge + blastDist, runwayPos.y + helperStartPoint);

                if(menuPanel.isShowRelevantDistOnlyEnabled()){

                    if(blastDist > resa + newStripEnd && blastDist > alsDistance + newStripEnd){
                        // Blast Protection
                        DataArrow blastDistArrow = new DataArrow(startBlastDist, endBlastDist, blastDistanceHelperHeight, blasDistLabel);
                        blastDistArrow.drawHorizontalArrow(g2);
                    }
                    else if(resa + newStripEnd > blastDist){
                        // RESA + Strip End for TORA etc
                        DataArrow resaArrow = new DataArrow(startResa, endResa, resaHelperHeight, resaLabel);
                        resaArrow.drawHorizontalArrow(g2);
                        DataArrow resaStripEndArrow = new DataArrow(startResaStripEnd, endResaStripEnd, resaHelperHeight, newStripEndLabel);
                        resaStripEndArrow.drawHorizontalArrow(g2);

                        if(alsDistance > resa){
                            // Slope + Strip End for LDA
                            distanceFromAlsHelperHeight = -stripHeight -40;
                            DataArrow alsArrow = new DataArrow(startAls, endAls, distanceFromAlsHelperHeight, alsLabel);
                            alsArrow.drawHorizontalArrow(g2);
                            DataArrow alsStripEndArrow = new DataArrow(startAlsStripEnd, endAlsStripEnd, distanceFromAlsHelperHeight, newStripEndLabel);
                            alsStripEndArrow.drawHorizontalArrow(g2);
                        }
                        // Else RESA will be used in LDA as well
                    }
                    else{
                        // Blast Protection for TORA etc
                        DataArrow blastDistArrow = new DataArrow(startBlastDist, endBlastDist, blastDistanceHelperHeight, blasDistLabel);
                        blastDistArrow.drawHorizontalArrow(g2);

                        // Slope + Strip End for LDA
                        distanceFromAlsHelperHeight = -stripHeight -40;
                        DataArrow alsArrow = new DataArrow(startAls, endAls, distanceFromAlsHelperHeight, alsLabel);
                        alsArrow.drawHorizontalArrow(g2);
                        DataArrow alsStripEndArrow = new DataArrow(startAlsStripEnd, endAlsStripEnd, distanceFromAlsHelperHeight, newStripEndLabel);
                        alsStripEndArrow.drawHorizontalArrow(g2);
                    }
                }
                else{

                    // RESA + Strip End
                    DataArrow resaArrow = new DataArrow(startResa, endResa, resaHelperHeight, resaLabel);
                    resaArrow.drawHorizontalArrow(g2);
                    DataArrow resaStripEndArrow = new DataArrow(startResaStripEnd, endResaStripEnd, resaHelperHeight, newStripEndLabel);
                    resaStripEndArrow.drawHorizontalArrow(g2);

                    // Slope + Strip End
                    DataArrow alsArrow = new DataArrow(startAls, endAls, distanceFromAlsHelperHeight, alsLabel);
                    alsArrow.drawHorizontalArrow(g2);
                    DataArrow alsStripEndArrow = new DataArrow(startAlsStripEnd, endAlsStripEnd, distanceFromAlsHelperHeight, newStripEndLabel);
                    alsStripEndArrow.drawHorizontalArrow(g2);

                    // Blast Protection
                    DataArrow blastDistArrow = new DataArrow(startBlastDist, endBlastDist, blastDistanceHelperHeight, blasDistLabel);
                    blastDistArrow.drawHorizontalArrow(g2);
                }

            }else{

                Point startResa = new Point(runwayPos.x + distanceFromEdge + oLength - resa,runwayPos.y + helperStartPoint);
                Point endResa = new Point (runwayPos.x + distanceFromEdge + oLength, runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point startResaStripEnd = new Point(startResa.x - newStripEnd,runwayPos.y + helperStartPoint);
                Point endResaStripEnd = new Point (startResa.x, runwayPos.y + helperStartPoint);

                Point startAls = new Point(runwayPos.x + distanceFromEdge - alsDistance + oLength,runwayPos.y + helperStartPoint);
                Point endAls = new Point (runwayPos.x + distanceFromEdge + oLength + oLength, runwayPos.y + helperStartPoint - distanceFromCenterline);
                Point startAlsStripEnd = new Point(startAls.x - newStripEnd,runwayPos.y + helperStartPoint);
                Point endAlsStripEnd = new Point (startAls.x, runwayPos.y + helperStartPoint);

                if(menuPanel.isShowRelevantDistOnlyEnabled() && alsDistance <= resa){

                    // RESA + Strip End
                    DataArrow resaArrow = new DataArrow(startResa, endResa, resaHelperHeight, resaLabel);
                    resaArrow.drawHorizontalArrow(g2);
                    DataArrow resaStripEndArrow = new DataArrow(startResaStripEnd, endResaStripEnd, resaHelperHeight, newStripEndLabel);
                    resaStripEndArrow.drawHorizontalArrow(g2);
                }
                else{
                    // RESA + Strip End
                    DataArrow resaArrow = new DataArrow(startResa, endResa, resaHelperHeight, resaLabel);
                    resaArrow.drawHorizontalArrow(g2);
                    DataArrow resaStripEndArrow = new DataArrow(startResaStripEnd, endResaStripEnd, resaHelperHeight, newStripEndLabel);
                    resaStripEndArrow.drawHorizontalArrow(g2);

                    // Slope + Strip End
                    DataArrow alsArrow = new DataArrow(startAls, endAls, distanceFromAlsHelperHeight, alsLabel);
                    alsArrow.drawHorizontalArrow(g2);
                    DataArrow alsStripEndArrow = new DataArrow(startAlsStripEnd, endAlsStripEnd, distanceFromAlsHelperHeight, newStripEndLabel);
                    alsStripEndArrow.drawHorizontalArrow(g2);
                }
            }
        }
    }

    //Prints the TODA, TORA, ASDA, and LDA for a given runway.
    private void paintRunwayParameters(String id, Graphics2D g2){
        Integer stripHeight = controller.getStripWidthFromCenterline(id);

        //Draw the TORA length.
        Integer toraLength = controller.getRunwayTORA(id);
        InfoArrow toraLengthInfo = new InfoArrow(controller.getTORAOffset(id),
                stripHeight+150,toraLength,"TORA: " + toraLength + "m", true);
        toraLengthInfo.drawInfoArrow(id, g2);

        //Draw the TODA length.
        Integer todaLength = controller.getRunwayTODA(id);
        InfoArrow todaLengthInfo = new InfoArrow(controller.getTODAOffset(id),
                stripHeight+350,todaLength,"TODA: " + todaLength + "m", true);
        todaLengthInfo.drawInfoArrow(id, g2);

        //Draw the ASDA length.
        Integer asdaLength = controller.getRunwayASDA(id);
        InfoArrow asdaLengthInfo = new InfoArrow(controller.getASDAOffset(id),
                stripHeight+250,asdaLength,"ASDA: " + asdaLength + "m", true);
        asdaLengthInfo.drawInfoArrow(id, g2);

        //Draw the LDA length.
        Integer ldaLength = controller.getRunwayLDA(id);
        Integer ldaOffset = controller.getLDAOffset(id);

        if(!controller.getRunwayObstacle(id).equals("")){
            if(controller.isRedeclared(id)) {

                // If LDA will be drawn to the right of the obstacle
                if (controller.getLogicalRunwayCloserToObstacle(id).getName().equals(id)) {
                    ldaOffset = ldaOffset + controller.getRunwayThreshold(id);
                }
            }
        }

        InfoArrow ldaLengthInfo = new InfoArrow(ldaOffset,
                stripHeight+50, ldaLength,"LDA: " + ldaLength + "m", true);
        ldaLengthInfo.drawInfoArrow(id, g2);
    }

    //Draws the length of various runway components for some specified runway.
    private void paintOtherLengths(String id, Graphics2D g2){
        Dimension clearwayDim = controller.getClearwayDim(id);
        Dimension runwayDim = controller.getRunwayDim(id);
        Dimension stopwayDim = controller.getStopwayDim(id);
        Integer stripHeight = controller.getStripWidthFromCenterline(id);
        Integer stripEndSize = controller.getStripEndSize(id);

        //Draw the clearway length.
        InfoArrow clearwayLengthInfo = new InfoArrow(runwayDim.width, -stripHeight-40,
                clearwayDim.width, clearwayDim.width +"m", true);
        clearwayLengthInfo.drawInfoArrow(id, g2);

        //Draw the clearway width
        InfoArrow clearwayWidthInfo = new InfoArrow(-(clearwayDim.height - runwayDim.height)/2, 50,
                clearwayDim.height, clearwayDim.height + "m", false);
        clearwayWidthInfo.globalXOffset = runwayDim.width + clearwayDim.width;
        clearwayWidthInfo.drawInfoArrow(id, g2);

        //Draw the stopway width
        InfoArrow stopwayWidthInfo = new InfoArrow(-(stopwayDim.height - runwayDim.height)/2, 50,
                stopwayDim.height, stopwayDim.height + "m", false);
        stopwayWidthInfo.globalXOffset = runwayDim.width + stopwayDim.width;
        stopwayWidthInfo.drawInfoArrow(id, g2);

        //Draw runway width
        InfoArrow runwayWidthInfo = new InfoArrow(0, -stripEndSize-30,
                runwayDim.height, runwayDim.height + "m", false);
        runwayWidthInfo.drawInfoArrow(id, g2);

        //Draw runwayStrip width.
        InfoArrow runwayStripWidthInfo = new InfoArrow(-(2*stripHeight - runwayDim.height)/2, -stripEndSize-100,
                stripHeight*2, stripHeight*2 + "m", false);
        runwayStripWidthInfo.globalXOffset = -stripEndSize;
        runwayStripWidthInfo.drawInfoArrow(id, g2);

        Integer runwayLengthHelperHeight = -stripHeight - 130;
        Integer thresholdHelperHeight = -stripHeight - 230;
        Integer stopwayLengthHelperHeight = -stripHeight-240;

        if(controller.isRedeclared(id) && menuPanel.isShowBreakDownEnabled()){
            if(menuPanel.isShowRelevantDistOnlyEnabled()){
                runwayLengthHelperHeight = -stripHeight - 140;
                thresholdHelperHeight = -stripHeight - 140;
                stopwayLengthHelperHeight = -stripHeight - 140;
            }else{
                runwayLengthHelperHeight = -stripHeight - 240;
                thresholdHelperHeight = -stripHeight - 340;
                stopwayLengthHelperHeight = -stripHeight - 340;
            }
        }

        //Draw runwayStrip length.
        InfoArrow runwayStripLengthInfo = new InfoArrow(-stripEndSize, runwayLengthHelperHeight,
                stripEndSize*2 + runwayDim.width, (stripEndSize*2 + runwayDim.width) + "m", true);
        runwayStripLengthInfo.drawInfoArrow(id, g2);

        //Draw the stopway length.
        InfoArrow stopwayLengthInfo = new InfoArrow(runwayDim.width, stopwayLengthHelperHeight,
                stopwayDim.width, stopwayDim.width +"m", true);
        stopwayLengthInfo.drawInfoArrow(id, g2);

        //Draw the displaced threshold length if it exists.
        if(controller.getRunwayThreshold(id) > 0){
            InfoArrow thresholdLength = new InfoArrow(0, thresholdHelperHeight,
                    controller.getRunwayThreshold(id), controller.getRunwayThreshold(id) + "m", true);
            thresholdLength.drawInfoArrow(id, g2);
        }
    }

    //Paints an outline of the clearway for a specified runway.
    private void paintClearway(String id,Graphics2D g2){
        Point pos = controller.getRunwayPos(id);
        Dimension runwayDim = controller.getRunwayDim(id);
        Dimension clearwayDim = controller.getClearwayDim(id);

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
        Point pos = controller.getRunwayPos(id);
        Dimension runwayDim = controller.getRunwayDim(id);
        Integer stripWidthFromCenterline = controller.getStripWidthFromCenterline(id);
        Integer stripEndSize = controller.getStripEndSize(id);

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
        Point pos = controller.getRunwayPos(id);
        Dimension runwayDim = controller.getRunwayDim(id);
        Dimension stopwayDim = controller.getStopwayDim(id);

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

    //Returns the rotation of the currently selected runway.
    private Integer getRotationOfSelectedRunway(){
        String selectedRunway = appView.getSelectedRunway();
        if(selectedRunway == ""){
            return Settings.DEFAULT_ROTATION;
        } else {
            return controller.getBearing(selectedRunway);
        }
    }

    //Returns the full length of the runway including any displayed text.
    private Integer getFullRunwayLength(String id){
        Integer runwayLength = controller.getRunwayDim(id).width;
        Integer stripEndSize = controller.getStripEndSize(id);
        Integer stopwayLength = controller.getStopwayDim(id).width;
        Integer clearwayLength = controller.getClearwayDim(id).width;

        //If no runway is selected then nothing is displayed but the runway and the runway strip.
        if(appView.getSelectedRunway().equals("")){
            return runwayLength + stripEndSize*2;
        }

        //The full length of the runway includes the stopway/clearway & strip end.
        Integer fullRunwayLength = runwayLength + stripEndSize + Math.max(stripEndSize, Math.max(stopwayLength, clearwayLength));

        if(menuPanel.isShowOtherEnabled()){
            //Account for the text displaying the strip width
            fullRunwayLength += 300;
            //Account for the text displaying the clearway/stopway width
            fullRunwayLength += 200;
        }
        return fullRunwayLength;
    }

    //Returns the full height of the runway including any displayed text.
    private Integer getFullRunwayHeight(String id){
        Integer stripWidthFromCenterline = controller.getStripWidthFromCenterline(id);
        Integer fullRunwayHeight = stripWidthFromCenterline *2;

        //If no runway is selected, then the only thing displayed is the runway and runway strip.
        if(appView.getSelectedRunway().equals("")){
            return fullRunwayHeight;
        }

        if(menuPanel.isShowOtherEnabled()) {
            //Account for the text displaying the stopway length
            fullRunwayHeight += 350;
        }

        if (menuPanel.isShowRunwayParametersEnabled()){
            //Account for all the text displaying the runway parameters.
            fullRunwayHeight += 450;
        }

        if(menuPanel.isShowBreakDownEnabled()){
            if(menuPanel.isShowRelevantDistOnlyEnabled()){
                fullRunwayHeight += 100;
            } else {
                fullRunwayHeight += 200;
            }
        }

        return fullRunwayHeight;
    }

    //Returns the bounding box for the specified runway. This will depend what options are selected in the menu.
    public Polygon getCurrentBoundingBox(String id){
        Integer stripWidthFromCenterline = controller.getStripWidthFromCenterline(id);
        Integer stripEndSize = controller.getStripEndSize(id);
        Point runwayStart = controller.getRunwayPos(id);

        Integer fullRunwayLength = getFullRunwayLength(id);
        Integer fullRunwayHeight = getFullRunwayHeight(id);

        //The point at the top left corner of the bounding box
        Point boundingBoxPos = (Point)runwayStart.clone();
        boundingBoxPos.translate(-stripEndSize, -stripWidthFromCenterline);

        //Add some padding around the bounding box
        fullRunwayLength+= 100;
        fullRunwayHeight+= 200;
        boundingBoxPos.translate(-50,-50);

        //If ShowOtherEnabled is true then account for the extra space used.
        if(menuPanel.isShowOtherEnabled()){
            //Account for the text displaying the strip width
            boundingBoxPos.translate(-300,-50);

            if(menuPanel.isShowBreakDownEnabled()){
                if(menuPanel.isShowRelevantDistOnlyEnabled()){
                    boundingBoxPos.translate(0,-300);
                } else {
                    boundingBoxPos.translate(0,-400);
                }
            }else{
                boundingBoxPos.translate(0,-250);
            }
        }


        //Create a polygon bounding the runway.
        Polygon p = new Polygon(new int[] {boundingBoxPos.x, boundingBoxPos.x, boundingBoxPos.x + fullRunwayLength, boundingBoxPos.x + fullRunwayLength},
                new int[] {boundingBoxPos.y, boundingBoxPos.y + fullRunwayHeight, boundingBoxPos.y + fullRunwayHeight, boundingBoxPos.y }, 4);

        //Rotate and return the polygon to fit around the runway.
        Double runwayAngle = new Double(Math.toRadians(controller.getBearing(id)-90));
        return  rotatePolygon(p, runwayAngle, runwayStart.x, runwayStart.y);
    }

    //Pans and zooms the view such that the specified runway appears in the center of the screen and fully visible.
    public void fitViewToRunway(String id){
        //Calculates the bounding box and centerpoint of the full runway.
        Rectangle2D boundingBox = getCurrentBoundingBox(id).getBounds2D();
        Point centerPoint = new Point((int)boundingBox.getCenterX(), (int)boundingBox.getCenterY());

        //Transforms the centerpoint to match it's position in the world.
        AffineTransform rx = new AffineTransform();
        rx.setToRotation(Math.toRadians(-getRotationOfSelectedRunway() + 90));
        rx.transform(centerPoint, centerPoint);

        //If the option is selected, pan and zoom accordingly
        if(menuPanel.isViewMatchedToSelection()){
            setPan(new Point(-centerPoint.x + getWidth()/2, -centerPoint.y + getHeight()/2));
            setZoom(Math.min((double)getHeight() / (double)getFullRunwayHeight(id),
                    (double)getWidth() / (double)getFullRunwayLength(id)));
            //If not then just pan, dont zoom.
        } else {
            setPan(new Point(-centerPoint.x + getWidth()/2, -centerPoint.y + getHeight()/2));
        }
    }

    //Pans and Zooms the view such that all runways fit within the view.
    public void fitViewToAllRunways(){
        Integer maxX = 0, maxY = 0, minX = 0, minY= 0;

        for (String id : controller.getRunways()){
            Rectangle2D boundingBox = getCurrentBoundingBox(id).getBounds2D();
            maxX = Math.max(maxX, (int)boundingBox.getMaxX());
            maxY = Math.max(maxY, (int)boundingBox.getMaxY());
            minX = Math.min(minX, (int)boundingBox.getMinX());
            minY = Math.min(minY, (int)boundingBox.getMinY());
        }

        Point centerPoint = new Point((minX+maxX)/2, (minY+maxY)/2);
        setPan(new Point(-centerPoint.x + getWidth()/2, -centerPoint.y + getHeight()/2));
        setZoom(Math.min((double)getHeight() / (double)(maxY-minY), (double)getWidth() / (double)(maxX-minX)));
    }

    //Rotates a polygon around a given point through a given angle.
    private Polygon rotatePolygon(Polygon p, Double theta, Integer anchorx, Integer anchory){
        int[] xPoints = new int[p.npoints];
        int[] yPoints = new int[p.npoints];
        AffineTransform rx = new AffineTransform();
        rx.rotate(theta, anchorx, anchory);
        for (int i = 0; i < p.npoints; i++){
            Point point = new Point(p.xpoints[i], p.ypoints[i]);
            rx.transform(point, point);
            xPoints[i] = point.x;
            yPoints[i] = point.y;
        }
        return  new Polygon(xPoints, yPoints, p.npoints);
    }

    //Inner class representing an instance of an arrow displaying some information.
    private class InfoArrow {
        private Integer globalXOffset;
        private Integer globalYOffset;
        private Integer offset;
        private Integer helperLength;
        private Integer length;
        private String label;
        private boolean isHorizontal;

        InfoArrow(Integer offset, Integer helperLength, Integer length, String label, boolean isHorizontal){
            globalXOffset = 0;
            globalYOffset = 0;
            this.offset = offset;
            this.helperLength = helperLength;
            this.length = length;
            this.label = label;
            this.isHorizontal = isHorizontal;
        }

        public void drawInfoArrow(String runwayId, Graphics2D g2){
            if(length <1){
                return;
            } else if (isHorizontal){
                drawHorizontalArrow(runwayId, g2);
            } else {
                drawVerticalArrow(runwayId, g2);
            }
        }

        private void drawHorizontalArrow(String runwayId, Graphics2D g2){
            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(genHorizontalInfoArrowTransform(runwayId));
            g2.setTransform(tx);

            //Draw helper lines.
            g2.setColor(Settings.INFO_ARROW_COLOUR);
            g2.setStroke(Settings.INFO_ARROW_HELPER_STROKE);
            g2.drawLine(offset,0, offset, helperLength);
            g2.drawLine(offset +length, 0, offset +length, helperLength);

            //Draw arrow line.
            Point lineStart = new Point(offset + Settings.TOP_DOWN_INFO_ARROW_PADDING, helperLength);
            Point lineEnd = new Point(offset +length - Settings.TOP_DOWN_INFO_ARROW_PADDING, helperLength);
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
            if(helperLength > 0){
                g2.drawString(label,(lineStart.x + lineEnd.x - stringLength)/2, helperLength +stringHeight+ Settings.TOP_DOWN_INFO_TEXT_PADDING);
            } else {
                g2.drawString(label,(lineStart.x + lineEnd.x - stringLength)/2, helperLength -Settings.TOP_DOWN_INFO_TEXT_PADDING);
            }

            g2.setTransform(old);
        }

        private void drawVerticalArrow(String runwayId, Graphics2D g2) {
            AffineTransform old = g2.getTransform();
            AffineTransform tx = (AffineTransform) old.clone();
            tx.concatenate(genVerticalInfoArrowTransform(runwayId));
            g2.setTransform(tx);

            //Draw helper lines.
            g2.setColor(Settings.INFO_ARROW_COLOUR);
            g2.setStroke(Settings.INFO_ARROW_HELPER_STROKE);
            g2.drawLine(0,offset, helperLength, offset);
            g2.drawLine(0, offset + length , helperLength,offset +length);

            //Draw arrow line.
            Point lineStart = new Point(helperLength,offset + Settings.TOP_DOWN_INFO_ARROW_PADDING);
            Point lineEnd = new Point(helperLength, offset + length - Settings.TOP_DOWN_INFO_ARROW_PADDING);
            g2.setStroke(Settings.INFO_ARROW_STROKE);
            g2.drawLine(lineStart.x, lineStart.y + Settings.TOP_DOWN_INFO_ARROW_LENGTH,
                    lineEnd.x, lineEnd.y - Settings.TOP_DOWN_INFO_ARROW_LENGTH);

            //Draw arrows at each end.
            Integer arrowHeight = Settings.TOP_DOWN_INFO_ARROW_HEIGHT;
            Integer arrowLength = Settings.TOP_DOWN_INFO_ARROW_LENGTH;

            Polygon startArrow = new Polygon(new int[] {lineStart.x - arrowHeight, lineStart.x, lineStart.x+arrowHeight},
                    new int[] {lineStart.y+arrowLength, lineStart.y, lineStart.y+arrowLength},3);
            Polygon endArrow = new Polygon(new int[] {lineEnd.x - arrowHeight, lineEnd.x, lineEnd.x+arrowHeight},
                    new int[] {lineEnd.y-arrowLength, lineEnd.y, lineEnd.y-arrowLength},3);
            g2.fillPolygon(startArrow);
            g2.fillPolygon(endArrow);

            //Draw the label;
            g2.setColor(Settings.INFO_TEXT_COLOUR);
            g2.setFont(Settings.INFO_TEXT_FONT);
            Integer textPadding = Settings.TOP_DOWN_INFO_TEXT_PADDING;
            Integer stringLength = g2.getFontMetrics().stringWidth(label);
            Integer stringHeight = Settings.INFO_TEXT_FONT.getSize();
            if(helperLength > 0){
                g2.drawString(label,helperLength + textPadding,(lineEnd.y + lineStart.y + stringHeight)/2 -3);
            } else {
                g2.drawString(label,helperLength - textPadding - stringLength,(lineEnd.y + lineStart.y + stringHeight)/2 -3);
            }

            g2.setTransform(old);
        }

        //Generates an Affine Transformation to local runway coordinates, so that 0,0 refers to the center of the start of the runway.
        public AffineTransform genHorizontalInfoArrowTransform(String runwayId){
            Point pos = controller.getRunwayPos(runwayId);
            Dimension dim = controller.getRunwayDim(runwayId);
            AffineTransform tx = createRunwayTransform(pos,dim,runwayId);
            tx.translate(pos.x,pos.y);
            tx.translate(0,dim.height/2);
            tx.translate(globalXOffset, globalYOffset);
            return tx;
        }

        public AffineTransform genVerticalInfoArrowTransform(String runwayId){
            Point pos = controller.getRunwayPos(runwayId);
            Dimension dim = controller.getRunwayDim(runwayId);
            AffineTransform tx = createRunwayTransform(pos,dim,runwayId);
            tx.translate(pos.x,pos.y);
            tx.translate(globalXOffset, globalYOffset);
            return tx;
        }
    }
}
