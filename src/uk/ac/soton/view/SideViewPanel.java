package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import java.awt.*;
import java.awt.geom.AffineTransform;

//TODO: implement functionality of repainting when adding/removing obstacle

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
        GradientPaint groundGradient = new GradientPaint(0,0,Settings.SIDEVIEW_GROUND_COLOUR_TOP, 0,2000, Settings.SIDEVIEW_GROUND_COLOUR_BOTTOM);
        g2.setPaint(groundGradient);
        g2.fillRect(-100000,0,200000,100000);
    }

    //painting the legend
    private void paintLegend(Graphics2D g2){
        Integer width = 200;
        Integer height = 150;
        Integer fontSize = 10;
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

        g2.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        g2.drawString("Obstacle", pos.x+40, pos.y + fontSize * 2 +verticalPadding*6);

        g2.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        g2.drawString("Clearway", pos.x+40, pos.y + fontSize * 3 +verticalPadding*7);

        g2.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        g2.drawString("Stopway", pos.x+40, pos.y + fontSize * 4 +verticalPadding*8);


        Integer iconSize = 16;
        g2.setColor(Settings.RUNWAY_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*5 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.OBSTACLE_FILL_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*7 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.CLEARWAY_STROKE_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*9 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.STOPWAY_STROKE_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*11 - iconSize+2, iconSize, iconSize);


    }

    //painting the runway
    public void paintRunway(Graphics2D g2){

        String selectedRunway = appView.getSelectedRunway();
        Dimension dimension = controller.getRunwayDim(selectedRunway);

        g2.setPaint(Settings.RUNWAY_COLOUR);
        g2.fillRect(0,0, dimension.width, dimension.height);
        g2.setPaint(Color.WHITE);
        g2.setFont(new Font("SansSerif", 0,  (dimension.height/2)));
        g2.drawString(selectedRunway, 0, (dimension.height)/2);

        //TODO: paint clearway

        //TODO: paint stopway
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

            //displaying the height * als distance
            //TODO: is this displayed correctly for sibling runway?
            Integer obstacleDistance = controller.getDistanceFromThreshold(selectedRunway);
            Integer value = controller.getPredefinedObstacleHeight(obstacle).intValue()*50;
            Point startDistance = new Point(0,0);
            Point endDistance = new Point (obstacleDistance + value, 0);
            DataArrow distanceArrow = new DataArrow(startDistance, endDistance, -100, "h * 50");
            distanceArrow.drawArrow(g2);

            //TODO: displaying height of the obstacle (problem: will be extremely small though, implement vertical arrow)

            //TODO: displaying resa
            Integer resa = 240;

            //TODO: displaying that 60 m area that comes with resa

            //TODO: displaying blasting distance

            //TODO: displaying slope

        }
    }
}
