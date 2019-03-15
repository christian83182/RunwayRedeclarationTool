package uk.ac.soton.view;

import java.awt.*;

public class Settings{

    static Dimension TOP_DOWN_DEFAULT_SIZE = new Dimension(1600,900);
    static Point TOP_DOWN_DEFAULT_PAN = new Point(0, 0);
    static Double TOP_DOWN_DEFAULT_ZOOM = 1.0;
    static Integer DEFAULT_ROTATION = 90;
    static Integer CENTERLINE_PADDING = 20;
    static Double TOP_DOWN_MAX_ZOOM = 5.0;
    static Double TOP_DOWN_MIN_ZOOM = 0.05;
    static Integer TOP_DOWN_INFO_ARROW_PADDING =5;
    static Integer TOP_DOWN_INFO_ARROW_HEIGHT = 10;
    static Integer TOP_DOWN_INFO_ARROW_LENGTH = 15;
    static Integer TOP_DOWN_INFO_TEXT_PADDING = 15;

    static Color AIRFIELD_COLOUR = new Color(44, 44, 44);
    static Color RUNWAY_STRIP_COLOUR = new Color(61, 122, 61);
    static Color CLEAR_AND_GRADED_COLOUR = new Color(80, 160, 79);
    static Color RUNWAY_COLOUR = new Color(147, 147, 147);

    static Color CENTERLINE_COLOUR = new Color(237, 237, 237);
    static Stroke CENTERLINE_STROKE = new BasicStroke(2, 1, 0, 10, new float[] {15,10}, 1);

    static Color SELECTED_RUNWAY_HIGHLIGHT = new Color(255, 151, 71);
    static Stroke SELECTED_RUNWAY_STROKE = new BasicStroke(3);

    static Color THRESHOLD_INDICATOR_COLOUR = new Color(255, 138, 42, 100);

    static Stroke OBSTACLE_STROKE = new BasicStroke(2);
    static Color OBSTACLE_STROKE_COLOUR = new Color(139,0, 147);
    static Color OBSTACLE_FILL_COLOUR = new Color(171,0, 183);

    static Color STOPWAY_STROKE_COLOUR = new Color(255, 0, 25);
    static Stroke STOPWAY_STROKE = new BasicStroke(3);
    static Color STOPWAY_FILL_COLOUR = new Color(STOPWAY_STROKE_COLOUR.getRed(), STOPWAY_STROKE_COLOUR.getGreen(), STOPWAY_STROKE_COLOUR.getBlue(),40);

    static Color CLEARWAY_STROKE_COLOUR = new Color(23, 0, 255);
    static Color CLEARWAY_FILL_COLOUR = new Color(CLEARWAY_STROKE_COLOUR.getRed(), CLEARWAY_STROKE_COLOUR.getGreen(), CLEARWAY_STROKE_COLOUR.getBlue(),20);
    static Stroke CLEARWAY_STROKE = new BasicStroke(3);

    static Color AXIS_COLOUR = new Color(226, 226, 226,80);
    static Stroke AXIS_STROKE = new BasicStroke(1);

    static Color INFO_ARROW_COLOUR = new Color(255,255,255);
    static Stroke INFO_ARROW_STROKE = new BasicStroke(4);
    static Stroke INFO_ARROW_HELPER_STROKE = new BasicStroke(2, 1, 0, 10, new float[] {1,10}, 1);
    static Color INFO_TEXT_COLOUR = new Color(255,255,255);
    static Font INFO_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 32);

    static Color RUNWAY_NAME_COLOUR = new Color(255,255,255);

    static Font SIDE_MENU_DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    static Font MENU_BAR_DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public static void setRedGreenColourblindTheme(){
        //things like "AXIS_COLOUR = new Color(255,255,255)"
        //dont feel like you need to change every single setting. A lot of them will stay the same.
        //AIRFIELD_COLOUR = new Color(44, 44, 44);
        RUNWAY_STRIP_COLOUR = new Color(5, 7, 207);
        CLEAR_AND_GRADED_COLOUR = new Color(111, 111, 210);
        RUNWAY_COLOUR = new Color(147, 147, 147);
        SELECTED_RUNWAY_HIGHLIGHT = new Color(254, 255, 10);
        THRESHOLD_INDICATOR_COLOUR= new Color(226, 229, 0, 131);
        OBSTACLE_STROKE_COLOUR = new Color(30, 30, 61, 103);
        OBSTACLE_FILL_COLOUR = new Color(94, 94, 179, 250);
        STOPWAY_STROKE_COLOUR = new Color(255, 233, 0, 124);
        CLEARWAY_STROKE_COLOUR = new Color(0, 27, 255, 64);
    }

    public static void setBlueYellowColourblidTheme(){
        //things like "AXIS_COLOUR = new Color(255,255,255)"
        //dont feel like you need to change every single setting. A lot of them will stay the same.
        //AIRFIELD_COLOUR = new Color(44, 44, 44);
        RUNWAY_STRIP_COLOUR = new Color(0, 153, 153);
        CLEAR_AND_GRADED_COLOUR = new Color(0, 230, 230);
        RUNWAY_COLOUR = new Color(147, 147, 147);
        SELECTED_RUNWAY_HIGHLIGHT = new Color(255, 89, 142);
        THRESHOLD_INDICATOR_COLOUR= new Color(255, 0, 196, 180);
        OBSTACLE_STROKE_COLOUR = new Color(255,0, 108);
        OBSTACLE_FILL_COLOUR = new Color(255, 230, 242);
        STOPWAY_STROKE_COLOUR = new Color(0, 252, 255);
        CLEARWAY_STROKE_COLOUR = new Color(102, 0, 77);
    }

    public static void setDefaultTheme(){
        //dont worry about this, I'll populate this later.
    }
}