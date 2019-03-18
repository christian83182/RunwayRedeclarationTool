package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class SideViewPanel extends InteractivePanel{

    AppView appView;
    MenuPanel menuPanel;
    ViewController controller;
    final Integer OBSTACLE_RESCALE_VALUE = 1;

    SideViewPanel(AppView appView){
        super(new Point(400,200), 1.0);
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

            paintParameters(g2);
        }

        //if there is an obstacle on the runway, paint it
        if(!controller.getRunwayObstacle(appView.getSelectedRunway()).equals("")){
            paintObstacle(g2);
            paintParameters(g2);
        }

        //painting map
        g2.setTransform(new AffineTransform());
        if(menuPanel.isSideViewShowOverlay()) {
            paintLegend(g2);
        }
    }

    //painting the background
    public void paintBackground(Graphics2D g2){
        GradientPaint skyGradient = new GradientPaint(0,0,Settings.SIDEVIEW_SKY_COLOUR_BOTTOM, 0,-2000, Settings.SIDEVIEW_SKY_COLOUR_TOP);
        g2.setPaint(skyGradient);
        g2.fillRect(-100000,-100000,200000,100000);
        GradientPaint groundGradient = new GradientPaint(0,0,Settings.RUNWAY_STRIP_COLOUR, 0,2000, Settings.CLEAR_AND_GRADED_COLOUR);
        g2.setPaint(groundGradient);
        g2.fillRect(-100000,0,200000,100000);
    }

    //painting the legend
    private void paintLegend(Graphics2D g2){
        Integer width = 210;
        Integer height = 170;
        Integer fontSize = 14;
        Integer verticalPadding = 9;
        Point pos = new Point(getWidth()-width-10, getHeight()-height-10);

        g2.setColor(new Color(45, 45, 45, 150));
        g2.fillRect(pos.x, pos.y, width, height);
        g2.setColor(new Color(39, 39, 39));
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(pos.x, pos.y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, (int)(fontSize*1.5)));
        g2.drawString("KEY", pos.x+85, pos.y + fontSize*1 + verticalPadding*2);

        g2.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        g2.drawString("Runway", pos.x+40, pos.y + fontSize * 1 +verticalPadding*5);
        g2.drawString("Obstacle", pos.x+40, pos.y + fontSize * 2 +verticalPadding*6);
        g2.drawString("Displaced Threshold", pos.x+40, pos.y + fontSize * 3 +verticalPadding*7);
        g2.drawString("Clearway", pos.x+40, pos.y + fontSize * 4 +verticalPadding*8);
        g2.drawString("Stopway", pos.x+40, pos.y + fontSize * 5 +verticalPadding*9);


        Integer iconSize = 16;
        g2.setColor(Settings.RUNWAY_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*5 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.OBSTACLE_FILL_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*2 + verticalPadding*6 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.SELECTED_RUNWAY_HIGHLIGHT);
        g2.fillRect(pos.x +18, pos.y + fontSize*3 + verticalPadding*7 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.CLEARWAY_STROKE_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*4 + verticalPadding*8 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.STOPWAY_STROKE_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*5 + verticalPadding*9 - iconSize+2, iconSize, iconSize);
    }

    //painting the runway
    public void paintRunway(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();
        Dimension runway = controller.getRunwayDim(selectedRunway);

        // Painting runway
        g2.setPaint(Settings.RUNWAY_COLOUR);
        g2.fillRect(0,0, runway.width, 50);
        g2.setPaint(Color.WHITE);
        g2.setFont(new Font("SansSerif", 0,  (runway.height/2)));
        g2.drawString(selectedRunway, 0, (runway.height)/2);

        // Painting displaced threshold
        Integer threshold = controller.getRunwayThreshold(selectedRunway);
        g2.setColor(Settings.THRESHOLD_INDICATOR_COLOUR);
        g2.fillRect(0, 0, threshold, 50);

        // Painting clearway
        Dimension clearway = controller.getClearwayDim(selectedRunway);
        g2.setColor(Settings.CLEARWAY_FILL_COLOUR);
        g2.fillRect(runway.width, 0, clearway.width, 50);
        g2.setColor(Settings.CLEARWAY_STROKE_COLOUR);
        g2.setStroke(Settings.CLEARWAY_STROKE);
        g2.drawRect(runway.width, 0, clearway.width, 50);

        // Painting stopway
        Dimension stopway = controller.getStopwayDim(selectedRunway);
        g2.setColor(Settings.STOPWAY_FILL_COLOUR);
        g2.fillRect(runway.width, 0, stopway.width, 50);
        g2.setColor(Settings.STOPWAY_STROKE_COLOUR);
        g2.setStroke(Settings.STOPWAY_STROKE);
        g2.drawRect(runway.width, 0, stopway.width, 50);
    }

    //painting the obstacle
    public void paintObstacle(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();
        String obstacle = controller.getRunwayObstacle(selectedRunway);
        Integer distanceFromEdge = controller.getDistanceFromThreshold(selectedRunway);
        Integer obstacleLength = controller.getPredefinedObstacleLength(obstacle).intValue() * OBSTACLE_RESCALE_VALUE;
        Integer obstacleHeight = controller.getPredefinedObstacleHeight(obstacle).intValue() * OBSTACLE_RESCALE_VALUE;

        g2.setColor(Settings.SIDEVIEW_OBSTACLE_FILL_COLOR);
        g2.fillRect(distanceFromEdge, -obstacleHeight, obstacleLength, obstacleHeight);
        g2.setColor(Settings.OBSTACLE_STROKE_COLOUR);
        g2.setStroke(Settings.OBSTACLE_STROKE);
        g2.drawRect(distanceFromEdge, -obstacleHeight,obstacleLength, obstacleHeight);
    }

    //painting parameters
    public void paintParameters(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();

        //displaying lda
        Integer ldaOffset = controller.getLDAOffset(selectedRunway);
        Integer lda = controller.getRunwayLDA(selectedRunway);
        String ldaLabel = new String("LDA: " + lda);
        Point startLda = new Point(ldaOffset,0);
        Point endLda = new Point (lda+ldaOffset, 0);
        DataArrow ldaArrow = new DataArrow(startLda, endLda, 100, ldaLabel);
        ldaArrow.drawArrow(g2);

        //displaying tora
        Integer toraOffset = controller.getTORAOffset(selectedRunway);
        Integer tora = controller.getRunwayTORA(selectedRunway);
        String toraLabel = new String("TORA: " + tora);
        Point startTora = new Point(toraOffset,0);
        Point endTora = new Point (tora + toraOffset, 0);
        DataArrow toraArrow = new DataArrow(startTora, endTora, 200, toraLabel);
        toraArrow.drawArrow(g2);

        //displaying toda
        Integer todaOffset = controller.getTODAOffset(selectedRunway);
        Integer toda = controller.getRunwayTODA(selectedRunway);
        String todaLabel = new String("TODA: " + toda);
        Point startToda = new Point(todaOffset,0);
        Point endToda = new Point (toda + todaOffset, 0);
        DataArrow todaArrow = new DataArrow(startToda, endToda, 300, todaLabel);
        todaArrow.drawArrow(g2);

        //displaying asda
        Integer asdaOffset = controller.getASDAOffset(selectedRunway);
        Integer asda = controller.getRunwayASDA(selectedRunway);
        String asdaLabel = new String("ASDA: " + asda);
        Point startAsda = new Point(asdaOffset,0);
        Point endAsda = new Point (asda + asdaOffset, 0);
        DataArrow asdaArrow = new DataArrow(startAsda, endAsda, 400, asdaLabel);
        asdaArrow.drawArrow(g2);

        String obstacle = controller.getRunwayObstacle(selectedRunway);

        //if obstacle exists, display obstacle related distances
        if(!obstacle.equals("")){


            //invoke displaying distances relative to their direction in which they are drawn
            //displayDistancesToTheLeft(g2,obstacle,selectedRunway);
            //displayDistancesToTheRight(g2,obstacle,selectedRunway);

            //TODO: displaying height of the obstacle (problem: will be extremely small though, implement vertical arrow)
            //TODO: displaying blasting distance

        }
    }


    // displaying distances to the right of the obstacle
    // height*als: start point is obstaccle distance from start of runway and it goes on for h*als
    // resa is just before the end of the above distance
    // the new strip end comes after the end of the resa
    
    public void displayDistancesToTheRight(Graphics2D g2, String obstacle, String selectedRunway){

        Integer obstacleDistance = controller.getDistanceFromThreshold(selectedRunway);
        Integer value = controller.getPredefinedObstacleHeight(obstacle).intValue()*50;
        Point startDistance = new Point(obstacleDistance, 0);
        Point endDistance = new Point(obstacleDistance + value, 0);
        DataArrow distanceArrow = new DataArrow(startDistance, endDistance, -100, "h*50");
        distanceArrow.drawArrow(g2);

        Integer resa = 240;
        String resaLabel = new String("RESA: " + resa);
        Point startResa = new Point(endDistance.x - resa,0);
        Point endResa = new Point (endDistance.x, 0);
        DataArrow resaArrow = new DataArrow(startResa, endResa, -300, resaLabel);
        resaArrow.drawArrow(g2);

        Integer newStripend = controller.getStripEndSize(selectedRunway);
        String newStripEndLabel = new String (newStripend + " m");
        Point endStripend = new Point (endResa.x + newStripend, 0);
        DataArrow stripEndArrow = new DataArrow(endResa, endStripend, -300, newStripEndLabel);
        stripEndArrow.drawArrow(g2);

        g2.setPaint(Settings.STOPWAY_FILL_COLOUR);
        Point startSlope = new Point(obstacleDistance, -controller.getPredefinedObstacleHeight(obstacle).intValue());
        Point endSlope = new Point(endResa.x, 0);
        g2.drawLine(startSlope.x, startSlope.y, endSlope.x, endSlope.y);

    }

    // displaying distances to the left of the obstacle
    // height*als: start point is (obstacle distance from start - h*als) of runway and it goes on for h*als
    // resa is just before the start of the above distance
    // the new strip end comes before the start of the resa

    public void displayDistancesToTheLeft(Graphics2D g2, String obstacle, String selectedRunway){

        Integer obstacleDistance = controller.getDistanceFromThreshold(selectedRunway);

        Integer obstacleLength = controller.getPredefinedObstacleLength(obstacle).intValue();
        Integer value = controller.getPredefinedObstacleHeight(obstacle).intValue()*50;
        Point startDistance = new Point(obstacleDistance + obstacleLength - value, 0);
        Point endDistance = new Point(obstacleDistance + obstacleLength, 0);
        DataArrow distanceArrow = new DataArrow(startDistance, endDistance, -100, "h*50");
        distanceArrow.drawArrow(g2);

        Integer resa = 240;
        String resaLabel = new String("RESA: " + resa);
        Point startResa = new Point(startDistance.x,0);
        Point endResa = new Point (startDistance.x + resa, 0);
        DataArrow resaArrow = new DataArrow(startResa, endResa, -300, resaLabel);
        resaArrow.drawArrow(g2);

        Integer newStripend = controller.getStripEndSize(selectedRunway);
        String newStripEndLabel = new String (newStripend + " m");
        Point startStripEnd = new Point(startResa.x - newStripend, 0);
        Point endStripEnd = new Point (startResa.x, 0);
        DataArrow stripEndArrow = new DataArrow(startStripEnd , endStripEnd, -300, newStripEndLabel);
        stripEndArrow.drawArrow(g2);

        g2.setPaint(Settings.STOPWAY_FILL_COLOUR);
        Point startSlope = new Point(startResa.x, 0);
        Point endSlope = new Point (endDistance.x, -controller.getPredefinedObstacleHeight(obstacle).intValue());
        g2.drawLine(startSlope.x, startSlope.y, endSlope.x, endSlope.y);

    }
}
