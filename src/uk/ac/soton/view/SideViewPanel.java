package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class SideViewPanel extends InteractivePanel{

    private AppView appView;
    private MenuPanel menuPanel;
    private ViewController controller;

    //helper heights for drawing the info arrows of the obstacle parameters
    private final Integer distanceFromAlsHelperHeight = -100;
    private final Integer resaHelperHeight = -170;
    private final Integer newStripendHelperHeight = -170;
    private final Integer blastDistanceHelperHeight = -240;

    SideViewPanel(AppView appView) {
        super(new Point(-300, 150), 0.34);
        this.appView = appView;
        this.menuPanel = appView.getMenuPanel();
        this.controller = appView.getController();
    }

    @Override
    public void paintView(Graphics2D g2) {
        paintBackground(g2);

        //if a runway was selected, paint the runway
        if(!appView.getSelectedRunway().equals("")){
            paintRunway(g2);

            if(menuPanel.isSideViewShowRunwayParametersEnabled()){
                paintParameters(g2);
            }
            if(menuPanel.isSideViewShowOtherEnabled()){
                paintOtherDistances(g2);
            }
        }

        //if there is an obstacle on the runway, paint it
        if(!controller.getRunwayObstacle(appView.getSelectedRunway()).equals("")){
            paintObstacle(g2);

            if(menuPanel.isSideViewShowBreakdownEnabled()){
                paintBreakdownParameters(g2);
            }
        }

        //painting map
        g2.setTransform(new AffineTransform());
        if(menuPanel.isSideViewShowOverlay()) {
            paintLegend(g2);
            paintScale(g2);
        }
    }

    //painting the background
    private void paintBackground(Graphics2D g2){
        GradientPaint skyGradient = new GradientPaint(0,500,Settings.SIDEVIEW_SKY_COLOUR_BOTTOM, 0,-2000, Settings.SIDEVIEW_SKY_COLOUR_TOP);
        g2.setPaint(skyGradient);
        g2.fillRect(-100000,-100000,200000,100000);
        GradientPaint groundGradient = new GradientPaint(0,0,Settings.SIDEVIEW_GROUND_COLOUR_TOP, 0,2000, Settings.SIDEVIEW_GROUND_COLOUR_BOTTOM);
        g2.setPaint(groundGradient);
        g2.fillRect(-100000,0,200000,100000);
        Color horizonColour = Settings.SIDEVIEW_SKY_COLOUR_BOTTOM.darker();
        g2.setColor(horizonColour);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(-100000,0,100000,0);
    }

    //painting the legend
    private void paintLegend(Graphics2D g2){
        Legend legend = new Legend("Legend");
        legend.addToLegend("Runway Strip", Settings.RUNWAY_STRIP_COLOUR);
        legend.addToLegend("Runway", Settings.RUNWAY_COLOUR);
        legend.addToLegend("Displaced Threshold", Settings.SELECTED_RUNWAY_HIGHLIGHT);
        legend.addToLegend("Stopway", Settings.STOPWAY_STROKE_COLOUR);
        legend.addToLegend("Clearway", Settings.CLEARWAY_STROKE_COLOUR);
        legend.addToLegend("Obstacle", Settings.OBSTACLE_FILL_COLOUR);
        legend.drawLegend(g2, new Point(getWidth()-10,getHeight()-10));
    }

    private void paintThresholdDesignator(Graphics2D g2){
        String runwayId = appView.getSelectedRunway();

        g2.setColor(Settings.RUNWAY_COLOUR);
        g2.fillRect(0,0,100,50);

        g2.setPaint(Color.WHITE);
        if(runwayId.length() == 2){
            g2.setFont(new Font("SansSerif", Font.PLAIN, 40));
            g2.drawString(runwayId, 20, 40);
        } else {
            g2.setFont(new Font("SansSerif", Font.BOLD, 25));
            g2.drawString(runwayId.substring(0,2), 20, 25);
            g2.drawString(runwayId.substring(2), 25, 45);
        }

    }

    //painting the runway
    private void paintRunway(Graphics2D g2){
        String selectedRunway = appView.getSelectedRunway();
        Dimension runwayDim = controller.getRunwayDim(selectedRunway);

        //Painting the Runway Strip
        Integer stripEndSize = controller.getStripEndSize(selectedRunway);
        g2.setColor(Settings.RUNWAY_STRIP_COLOUR);
        g2.fillRect(-stripEndSize, 0,runwayDim.width+stripEndSize*2, 20);
        g2.fillRoundRect(-stripEndSize, 0,runwayDim.width+stripEndSize*2, 70,50,50);

        // Painting runway
        g2.setPaint(Settings.RUNWAY_COLOUR);
        g2.fillRect(0,0, runwayDim.width, 50);
        g2.setColor(Settings.RUNWAY_COLOUR.darker());
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(0,0, runwayDim.width, 50);

        // Paint the centerline
        g2.setColor(Settings.CENTERLINE_COLOUR);
        g2.setStroke(Settings.CENTERLINE_STROKE);
        g2.drawLine(Settings.CENTERLINE_PADDING, 20, runwayDim.width-Settings.CENTERLINE_PADDING-50, 20);

        // Painting the Threshold Designator
        paintThresholdDesignator(g2);

        //Painting the landing direction
        Point arrowStart = new Point((int)(runwayDim.width*0.2), 20);
        Polygon arrow = new Polygon(new int[] {arrowStart.x, arrowStart.x, arrowStart.x + 25},
                new int[] {arrowStart.y-8, arrowStart.y+13, arrowStart.y}, 3);
        g2.setColor(Settings.CENTERLINE_COLOUR);
        g2.fillPolygon(arrow);


        // Painting displaced threshold
        Integer threshold = controller.getRunwayThreshold(selectedRunway);
        g2.setColor(Settings.THRESHOLD_INDICATOR_COLOUR);
        g2.fillRect(0, 0, threshold, 50);
        g2.setColor(Settings.SELECTED_RUNWAY_HIGHLIGHT);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(0, 0, threshold, 50);

        // Painting clearway
        Dimension clearway = controller.getClearwayDim(selectedRunway);
        g2.setColor(Settings.CLEARWAY_FILL_COLOUR);
        g2.fillRect(runwayDim.width, 0, clearway.width, 50);
        g2.setColor(Settings.CLEARWAY_STROKE_COLOUR);
        g2.setStroke(Settings.CLEARWAY_STROKE);
        g2.drawRect(runwayDim.width, 0, clearway.width, 50);

        // Painting stopway
        Dimension stopway = controller.getStopwayDim(selectedRunway);
        g2.setColor(Settings.STOPWAY_FILL_COLOUR);
        g2.fillRect(runwayDim.width, 0, stopway.width, 50);
        g2.setColor(Settings.STOPWAY_STROKE_COLOUR);
        g2.setStroke(Settings.STOPWAY_STROKE);
        g2.drawRect(runwayDim.width, 0, stopway.width, 50);
    }

    //painting the obstacle
    private void paintObstacle(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();
        String obstacle = controller.getRunwayObstacle(selectedRunway);
        Integer distanceFromEdge = controller.getDistanceFromThreshold(selectedRunway) + controller.getObstacleOffset(selectedRunway);
        Integer obstacleLength = controller.getPredefinedObstacleLength(obstacle);
        Integer obstacleHeight = controller.getPredefinedObstacleHeight(obstacle);

        g2.setColor(Settings.SIDEVIEW_OBSTACLE_FILL_COLOR);
        g2.fillRect(distanceFromEdge, -obstacleHeight, obstacleLength, obstacleHeight);
        g2.setColor(Settings.OBSTACLE_STROKE_COLOUR);
        g2.setStroke(Settings.OBSTACLE_STROKE);
        g2.drawRect(distanceFromEdge, -obstacleHeight,obstacleLength, obstacleHeight);
    }

    // painting runway length, displaced threshold, clearway, stopway arrows
    private void paintOtherDistances(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();

        Integer length = controller.getRunwayDim(selectedRunway).width;
        String lengthLabel = length + "m";
        Point startLength = new Point(0,0);
        Point endLength = new Point(length,0);
        DataArrow lengthArrow = new DataArrow(startLength, endLength, -380, lengthLabel);
        lengthArrow.drawHorizontalArrow(g2);

        Integer threshold = controller.getRunwayThreshold(selectedRunway);
        String thresholdLabel = threshold + "m";
        Point startThreshold = new Point(0,0);
        Point endThreshold = new Point (threshold, 0);
        DataArrow thresholdArrow = new DataArrow(startThreshold, endThreshold, -310, thresholdLabel);
        thresholdArrow.drawHorizontalArrow(g2);

        Integer stopway = controller.getStopwayDim(selectedRunway).width;
        String stopwayLabel = stopway + "m";
        Point startStopway = new Point(length,0);
        Point endStopway = new Point(length+stopway,0);
        DataArrow stopwayArrow = new DataArrow(startStopway, endStopway, -380, stopwayLabel);
        stopwayArrow.drawHorizontalArrow(g2);

        Integer clearway = controller.getClearwayDim(selectedRunway).width;
        String clearwayLabel = clearway + "m";
        Point startClearway = new Point(length,0);
        Point endClearway = new Point(length+clearway,0);
        DataArrow clearwayArrow = new DataArrow(startClearway, endClearway, -310, clearwayLabel);
        clearwayArrow.drawHorizontalArrow(g2);
    }

    //painting parameters
    private void paintParameters(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();
        String obstacle = controller.getRunwayObstacle(selectedRunway);

        //displaying lda
        Integer ldaOffset = controller.getLDAOffset(selectedRunway);

        if(!obstacle.equals("")){
            if(controller.isRedeclared(selectedRunway)) {

                // If LDA will be drawn to the right of the obstacle
                if (controller.getLogicalRunwayCloserToObstacle(selectedRunway).getName().equals(selectedRunway)) {
                    ldaOffset = ldaOffset + controller.getRunwayThreshold(selectedRunway);
                }
            }
        }

        Integer lda = controller.getRunwayLDA(selectedRunway);
        String ldaLabel = "LDA: " + lda + "m";
        Point startLda = new Point(ldaOffset,0);
        Point endLda = new Point (lda+ldaOffset, 0);
        DataArrow ldaArrow = new DataArrow(startLda, endLda, 100, ldaLabel);
        ldaArrow.drawHorizontalArrow(g2);

        //displaying tora
        Integer toraOffset = controller.getTORAOffset(selectedRunway);
        Integer tora = controller.getRunwayTORA(selectedRunway);
        String toraLabel = "TORA: " + tora + "m";
        Point startTora = new Point(toraOffset,0);
        Point endTora = new Point (tora + toraOffset, 0);
        DataArrow toraArrow = new DataArrow(startTora, endTora, 200, toraLabel);
        toraArrow.drawHorizontalArrow(g2);

        //displaying asda
        Integer asdaOffset = controller.getASDAOffset(selectedRunway);
        Integer asda = controller.getRunwayASDA(selectedRunway);
        String asdaLabel = "ASDA: " + asda + "m";
        Point startAsda = new Point(asdaOffset,0);
        Point endAsda = new Point (asda + asdaOffset, 0);
        DataArrow asdaArrow = new DataArrow(startAsda, endAsda, 300, asdaLabel);
        asdaArrow.drawHorizontalArrow(g2);

        //displaying toda
        Integer todaOffset = controller.getTODAOffset(selectedRunway);
        Integer toda = controller.getRunwayTODA(selectedRunway);
        String todaLabel ="TODA: " + toda + "m";
        Point startToda = new Point(todaOffset,0);
        Point endToda = new Point (toda + todaOffset, 0);
        DataArrow todaArrow = new DataArrow(startToda, endToda, 400, todaLabel);
        todaArrow.drawHorizontalArrow(g2);
    }

    private void paintBreakdownParameters(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();
        String obstacle = controller.getRunwayObstacle(selectedRunway);

        //if the obstacle has triggered a redeclaration of the runway, draw the redeclared parameters
        if(controller.isRedeclared(selectedRunway)) {
            //invoke displaying distances relative to their direction in which they are drawn
            if (controller.getLogicalRunwayCloserToObstacle(selectedRunway).getName().equals(selectedRunway)) {
                displayDistancesToTheRight(g2, obstacle, selectedRunway);
            } else {
                displayDistancesToTheLeft(g2, obstacle, selectedRunway);
            }
        }
    }

    //all parameters drawn to the left of the obstacle are drawn from a specified distance
    //and the line of the parameter ends at the specified distance + the length of the parameter
    private void drawParameterToTheRight(Graphics2D g2, Integer helperLength, String label, Integer distanceLength, Integer startPointX){

        Point startDistance = new Point(startPointX, 0);
        Point endDistance = new Point(startPointX + distanceLength, 0);
        DataArrow distanceArrow = new DataArrow(startDistance, endDistance, helperLength, label);
        distanceArrow.drawHorizontalArrow(g2);
    }

    // displaying distances to the right of the obstacle
    // height*als: start point is obstacle distance from start of runway and it goes on for h*als
    // RESA is drawn to the right of the obstacle
    // the new strip end comes after the end of the resa or the end of the height*als distance, whichever is longer
    private void displayDistancesToTheRight(Graphics2D g2, String obstacle, String selectedRunway){

        Integer obstacleDistance = controller.getDistanceFromThreshold(selectedRunway) + controller.getObstacleOffset(selectedRunway);
        Integer obstacleLength = controller.getPredefinedObstacleLength(obstacle);

        Integer alsDistance = controller.getPredefinedObstacleHeight(obstacle)* controller.getALS(selectedRunway);
        Integer resa = controller.getRESADistance(selectedRunway);
        Integer blastingDistance = controller.getBlastingDistance();
        Integer newStripEnd = controller.getStripEndSize(selectedRunway);


        if(menuPanel.isSideViewShowRelevantDistOnlyEnabled()){

            if(alsDistance > resa && alsDistance > blastingDistance){
                drawParameterToTheRight(g2, distanceFromAlsHelperHeight, "h*" + controller.getALS(selectedRunway), alsDistance, obstacleDistance);
                drawParameterToTheRight(g2, newStripendHelperHeight, newStripEnd + "m", newStripEnd, obstacleDistance +  alsDistance);
                drawParameterToTheRight(g2, blastDistanceHelperHeight, "Blast Dist: " + blastingDistance + "m", blastingDistance, obstacleDistance);
            }else if (resa > alsDistance && resa > blastingDistance){
                drawParameterToTheRight(g2, resaHelperHeight, "RESA: " + resa, resa, obstacleDistance + obstacleLength);
                drawParameterToTheRight(g2, newStripendHelperHeight, newStripEnd + "m", newStripEnd, obstacleDistance + obstacleLength + resa);
                drawParameterToTheRight(g2, blastDistanceHelperHeight, "Blast Dist: " + blastingDistance + "m", blastingDistance, obstacleDistance);
            }else if(resa.equals(alsDistance) && resa > blastingDistance){
                drawParameterToTheRight(g2, distanceFromAlsHelperHeight, "h*" + controller.getALS(selectedRunway), alsDistance, obstacleDistance);
                drawParameterToTheRight(g2, resaHelperHeight, "RESA: " + resa + "m", resa, obstacleDistance + obstacleLength);
                drawParameterToTheRight(g2, blastDistanceHelperHeight, "Blast Dist: " + blastingDistance + "m", blastingDistance, obstacleDistance);
            }
            else{
                drawParameterToTheRight(g2, blastDistanceHelperHeight, "Blast Dist: " + blastingDistance + "m", blastingDistance, obstacleDistance);
            }
        }else{

            drawParameterToTheRight(g2, distanceFromAlsHelperHeight, "h*" + controller.getALS(selectedRunway), alsDistance, obstacleDistance);
            drawParameterToTheRight(g2, resaHelperHeight, "RESA: " + resa + "m", resa, obstacleDistance + obstacleLength);

            if(resa > alsDistance){
                drawParameterToTheRight(g2, newStripendHelperHeight, newStripEnd + "m", newStripEnd, obstacleDistance + obstacleLength + resa);
            }else{
                drawParameterToTheRight(g2, newStripendHelperHeight, newStripEnd + "m", newStripEnd, obstacleDistance +  alsDistance);
            }

            drawParameterToTheRight(g2, blastDistanceHelperHeight, "Blast Dist: " + blastingDistance + "m", blastingDistance, obstacleDistance);

        }

        if(resa > alsDistance){
            g2.setPaint(Settings.STOPWAY_FILL_COLOUR);
            Point startSlope = new Point(obstacleDistance, -controller.getPredefinedObstacleHeight(obstacle));
            Point endSlope = new Point(obstacleDistance + resa, 0);
            g2.drawLine(startSlope.x, startSlope.y, endSlope.x, endSlope.y);

        }else{
            g2.setPaint(Settings.STOPWAY_FILL_COLOUR);
            Point startSlope = new Point(obstacleDistance, -controller.getPredefinedObstacleHeight(obstacle));
            Point endSlope = new Point(obstacleDistance + alsDistance, 0);
            g2.drawLine(startSlope.x, startSlope.y, endSlope.x, endSlope.y);

        }
    }

    //all parameters drawn to the left of the obstacle are drawn from a specified distance - the length of the parameter
    //and the line of the parameter ends at the specified distance
    private void drawParameterToTheLeft(Graphics2D g2, Integer helperLength, String label, Integer distanceLength, Integer endPointX){

        Point startDistance = new Point(endPointX - distanceLength, 0);
        Point endDistance = new Point(endPointX, 0);
        DataArrow distanceArrow = new DataArrow(startDistance, endDistance, helperLength, label);
        distanceArrow.drawHorizontalArrow(g2);
    }

    // displaying distances to the right of the obstacle
    // height*als: start point is obstacle distance from start of runway and it goes on for h*als
    // RESA is drawn to the left of the obstacle
    // the new strip end comes before the end of the resa or the end of the height*als distance, whichever is longer
    private void displayDistancesToTheLeft(Graphics2D g2, String obstacle, String selectedRunway){

        Integer obstacleDistance = controller.getDistanceFromThreshold(selectedRunway) + controller.getObstacleOffset(selectedRunway);
        Integer obstacleLength = controller.getPredefinedObstacleLength(obstacle);

        Integer alsDistance = controller.getPredefinedObstacleHeight(obstacle)*controller.getALS(selectedRunway);
        Integer resa = controller.getRESADistance(selectedRunway);
        Integer newStripEnd = controller.getStripEndSize(selectedRunway);

        if(menuPanel.isSideViewShowRelevantDistOnlyEnabled()){

            if(alsDistance > resa){
                drawParameterToTheLeft(g2, distanceFromAlsHelperHeight, "h*" + controller.getALS(selectedRunway), alsDistance, obstacleDistance + obstacleLength);
                drawParameterToTheLeft(g2, newStripendHelperHeight,newStripEnd + " m", newStripEnd, obstacleDistance + obstacleLength - alsDistance);
            }else if (resa > alsDistance){
                drawParameterToTheLeft(g2, resaHelperHeight,"RESA: " + resa, resa, obstacleDistance);
                drawParameterToTheLeft(g2, newStripendHelperHeight,newStripEnd + " m", newStripEnd, obstacleDistance + obstacleLength - resa);
            }else if(resa.equals(alsDistance)){
                drawParameterToTheLeft(g2, distanceFromAlsHelperHeight, "h*" + controller.getALS(selectedRunway), alsDistance, obstacleDistance + obstacleLength);
                drawParameterToTheLeft(g2, resaHelperHeight,"RESA: " + resa, resa, obstacleDistance);
                drawParameterToTheLeft(g2, newStripendHelperHeight,newStripEnd + " m", newStripEnd, obstacleDistance + obstacleLength - alsDistance);
            }

        }else{

            drawParameterToTheLeft(g2, distanceFromAlsHelperHeight, "h*" + controller.getALS(selectedRunway), alsDistance, obstacleDistance + obstacleLength);
            drawParameterToTheLeft(g2, resaHelperHeight,"RESA: " + resa, resa, obstacleDistance);

            if(resa > alsDistance){
                drawParameterToTheLeft(g2, newStripendHelperHeight,newStripEnd + " m", newStripEnd, obstacleDistance + obstacleLength - resa);
            }else{
                drawParameterToTheLeft(g2, newStripendHelperHeight,newStripEnd + " m", newStripEnd, obstacleDistance + obstacleLength - alsDistance);
            }
        }

        if(resa > alsDistance){
            g2.setPaint(Settings.STOPWAY_FILL_COLOUR);
            Point startSlope = new Point(obstacleDistance + obstacleLength - resa, 0);
            Point endSlope = new Point (obstacleDistance + obstacleLength, -controller.getPredefinedObstacleHeight(obstacle));
            g2.drawLine(startSlope.x, startSlope.y, endSlope.x, endSlope.y);
        }else{
            g2.setPaint(Settings.STOPWAY_FILL_COLOUR);
            Point startSlope = new Point(obstacleDistance + obstacleLength - alsDistance, 0);
            Point endSlope = new Point (obstacleDistance + obstacleLength, -controller.getPredefinedObstacleHeight(obstacle));
            g2.drawLine(startSlope.x, startSlope.y, endSlope.x, endSlope.y);
        }

    }
}
