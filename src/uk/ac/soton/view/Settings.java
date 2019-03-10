package uk.ac.soton.view;

import java.awt.*;

public final class Settings{

    final static Dimension TOP_DOWN_DEFAULT_SIZE = new Dimension(1600,900);
    final static Point TOP_DOWN_DEFAULT_PAN = new Point(0, 0);
    final static Double TOP_DOWN_DEFAULT_ZOOM = 1.0;
    final static Integer DEFAULT_ROTATION = 90;
    final static Integer CENTERLINE_PADDING = 20;
    final static Double TOP_DOWN_MAX_ZOOM = 5.0;
    final static Double TOP_DOWN_MIN_ZOOM = 0.05;
    final static Integer TOP_DOWN_INFO_ARROW_PADDING =5;
    final static Integer TOP_DOWN_INFO_ARROW_HEIGHT = 10;
    final static Integer TOP_DOWN_INFO_ARROW_LENGTH = 15;
    final static Integer TOP_DOWN_INFO_TEXT_PADDING = 15;

    final static Color AIRFIELD_COLOUR = new Color(44, 44, 44);
    final static Color RUNWAY_STRIP_COLOUR = new Color(61, 122, 61);
    final static Color CLEAR_AND_GRADED_COLOUR = new Color(80, 160, 79);
    final static Color RUNWAY_COLOUR = new Color(147, 147, 147);

    final static Color CENTERLINE_COLOUR = new Color(237, 237, 237);
    final static Stroke CENTERLINE_STROKE = new BasicStroke(2, 1, 0, 10, new float[] {15,10}, 1);

    final static Color SELECTED_RUNWAY_HIGHLIGHT = new Color(255, 151, 71);
    final static Stroke SELECTED_RUNWAY_STROKE = new BasicStroke(3);

    final static Color THRESHOLD_INDICATOR_COLOUR = new Color(255, 138, 42, 100);

    final static Color STOPWAY_STROKE_COLOUR = new Color(255, 0, 25);
    final static Stroke STOPWAY_STROKE = new BasicStroke(3);
    final static Color STOPWAY_FILL_COLOUR = new Color(STOPWAY_STROKE_COLOUR.getRed(), STOPWAY_STROKE_COLOUR.getGreen(), STOPWAY_STROKE_COLOUR.getBlue(),40);

    final static Color CLEARWAY_STROKE_COLOUR = new Color(23, 0, 255);
    final static Color CLEARWAY_FILL_COLOUR = new Color(CLEARWAY_STROKE_COLOUR.getRed(), CLEARWAY_STROKE_COLOUR.getGreen(), CLEARWAY_STROKE_COLOUR.getBlue(),20);
    final static Stroke CLEARWAY_STROKE = new BasicStroke(3);

    final static Color AXIS_COLOUR = new Color(226, 226, 226);
    final static Stroke AXIS_STROKE = new BasicStroke(1);

    final static Color INFO_ARROW_COLOUR = new Color(255,255,255);
    final static Stroke INFO_ARROW_STROKE = new BasicStroke(4);
    final static Stroke INFO_ARROW_HELPER_STROKE = new BasicStroke(2, 1, 0, 10, new float[] {1,10}, 1);
    final static Color INFO_TEXT_COLOUR = new Color(255,255,255);
    final static Font INFO_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 32);

    final static Color RUNWAY_NAME_COLOUR = new Color(255,255,255);
}