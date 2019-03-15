package uk.ac.soton.view;
import uk.ac.soton.controller.ViewController;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class SideViewPanel extends InteractivePanel{

    AppView appView;
    MenuPanel menuPanel;
    ViewController controller;

    SideViewPanel(AppView appView){
        super(new Point(400,200), 1.0);
        this.appView = appView;
        this.menuPanel = appView.getMenuPanel();
        this.controller = appView.getController();
    }

    @Override
    public void paintView(Graphics2D g2) {
        paintBackground(g2);

        paintRunway(g2);

        paintParameters(g2);

        if(!controller.getRunwayObstacle(appView.getSelectedRunway()).equals("")){
            paintObstacle(g2);
        }

        g2.setTransform(new AffineTransform());

        if(menuPanel.isSideViewShowOverlay()) {
            paintLegend(g2);
        }
    }

    public void paintBackground(Graphics2D g2){
        GradientPaint skyGradient = new GradientPaint(0,0,Settings.SIDEVIEW_SKY_COLOUR_BOTTOM, 0,-2000, Settings.SIDEVIEW_SKY_COLOUR_TOP);
        g2.setPaint(skyGradient);
        g2.fillRect(-100000,-100000,200000,100000);
        GradientPaint groundGradient = new GradientPaint(0,0,Settings.SIDEVIEW_GROUND_COLOUR_TOP, 0,2000, Settings.SIDEVIEW_GROUND_COLOUR_BOTTOM);
        g2.setPaint(groundGradient);
        g2.fillRect(-100000,0,200000,100000);
    }

    private void paintLegend(Graphics2D g2){
        Integer width = 200;
        Integer height = 100;
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


        Integer iconSize = 16;
        g2.setColor(Settings.RUNWAY_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*5 - iconSize+2, iconSize, iconSize);

        g2.setColor(Settings.OBSTACLE_FILL_COLOUR);
        g2.fillRect(pos.x +18, pos.y + fontSize*1 + verticalPadding*7 - iconSize+2, iconSize, iconSize);
    }

    public void paintRunway(Graphics2D g2){
        if(appView.getSelectedRunway().equals("")){
            return;
        }

        String selectedRunway = appView.getSelectedRunway();
        Integer width = (int)controller.getRunwayDim(selectedRunway).getWidth();
        Integer height = (int)controller.getRunwayDim(selectedRunway).getHeight();
        g2.setPaint(Settings.RUNWAY_COLOUR);
        g2.fillRect(0,0, width, height);
        g2.setPaint(Color.WHITE);
        g2.setFont(new Font("SansSerif", 0,  height/2));
        g2.drawString(selectedRunway, 10, height/2);
    }

    public void paintObstacle(Graphics2D g2){
        if(appView.getSelectedRunway().equals("")){
            return;
        }
        String selectedRunway = appView.getSelectedRunway();
        String obstacle = controller.getRunwayObstacle(selectedRunway);
        Integer distanceFromEdge = controller.getDistanceFromThreshold(selectedRunway);
        Integer obstacleLength = controller.getPredefinedObstacleLength(obstacle).intValue() * 3;
        Integer obstacleHeight = controller.getPredefinedObstacleHeight(obstacle).intValue() * 3;

        g2.setColor(Settings.SIDEVIEW_OBSTACLE_FILL_COLOR);
        g2.fillRect(distanceFromEdge, -obstacleHeight, obstacleLength, obstacleHeight);
        g2.setColor(Settings.OBSTACLE_STROKE_COLOUR);
        g2.setStroke(Settings.OBSTACLE_STROKE);
        g2.drawRect(distanceFromEdge, -obstacleHeight,obstacleLength, obstacleHeight);
    }

    public void paintParameters(Graphics2D g2){
        if(appView.getSelectedRunway().equals("")){
            return;
        }

        String selectedRunway = appView.getSelectedRunway();
        Integer tora = controller.getRunwayTORA(selectedRunway);


        Point start = new Point(0,0);
        Point end = new Point (tora, 0);
        DataArrow toraArrow = new DataArrow(start, end, 100, "TORA");
        toraArrow.drawArrow(g2);

    }
}
