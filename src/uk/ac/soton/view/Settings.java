package uk.ac.soton.view;

import java.awt.*;

public final class Settings{

    final static Dimension TOP_DOWN_DEFAULT_SIZE = new Dimension(800,800);
    final static Point TOP_DOWN_DEFAULT_PAN = new Point(TOP_DOWN_DEFAULT_SIZE.width/2, TOP_DOWN_DEFAULT_SIZE.height/2);
    final static Double TOP_DOWN_DEFAULT_ZOOM = 0.4;
    final static Integer CENTERLINE_PADDING = 20;
    final static Double TOP_DOWN_MAX_ZOOM = 5.0;
    final static Double TOP_DOWN_MIN_ZOOM = 0.05;

    final static Color AIRFIELD_COLOUR = new Color(66, 66, 66);
    final static Color RUNWAY_STRIP_COLOUR = new Color(0,0,0);
    final static Color CLEAR_AND_GRADED_COLOUR = new Color(80, 160, 79);
    final static Color RUNWAY_COLOUR = new Color(147, 147, 147);
    final static Color CENTERLINE_COLOUR = new Color(237, 237, 237);
    final static Stroke CENTERLINE_STROKE = new BasicStroke(3, 1, 0, 10, new float[] {15,10}, 1);
    final static Color SELECTED_RUNWAY_HIGHLIGHT = new Color(255, 165, 83);
    final static Stroke SELECTED_RUNWAY_STROKE = new BasicStroke(3);
    final static Color STOPWAY_COLOUR = new Color(255, 95, 93);
    final static Stroke STOPWAY_STROKE = new BasicStroke(2);
    final static Color CLEARWAY_COLOUR = new Color(238, 124, 255);
    final static Stroke CLEARWAY_STROKE = new BasicStroke(2);
    final static Color AXIS_COLOUR = new Color(226, 226, 226);
    final static Stroke AXIS_STROKE = new BasicStroke(1);


}
