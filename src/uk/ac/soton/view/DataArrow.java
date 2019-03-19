package uk.ac.soton.view;

import java.awt.*;

/**
 * A class dedicated to representing and drawing arrows showing data.
 */
public class DataArrow {

    //The start point of the distance being displayed. This should always be less than the endpoint.
    Point startPoint;
    //The end point of the distance being displayed. This should always be greater than the startpoint.
    Point endPoint;
    //The length of the dotted line separating the arrow from the start and end point.
    Integer helperLength;
    //The string to appear in the center of the line.
    String label;

    /**
     * The constructor to the class.
     * @param startPoint The start point of the distance being displayed.
     * @param endPoint The end point of the distance being displayed.
     * @param helperLength The length of the dotted line separating the arrow from the start and end point.
     * @param label The string to appear in the center of the line.
     */
    DataArrow(Point startPoint, Point endPoint, Integer helperLength, String label){
        this.startPoint = startPoint;
        this.helperLength = helperLength;
        this.endPoint = endPoint;
        this.label = label;
    }

    /**
     * Identical in every way to drawHorizontalArrow but the arrow is drawn vertically, with the helper lines coming out
     * horizontally to the same x-coordinate.
     * @param g2
     */
    public void drawVerticalArrow(Graphics2D g2){
        Point arrowStart;
        Point arrowEnd;
        Integer padding = Settings.TOP_DOWN_INFO_ARROW_PADDING;

        /*
        Determine the position of the start and end of the arrow based on if the start point is above or
        below the end point, and if the helper distance is positive of negative.
         */
        if(helperLength >= 0){
            if (endPoint.x >= startPoint.x){
                arrowStart = new Point(endPoint.x + helperLength,startPoint.y - padding);
                arrowEnd = new Point(endPoint.x + helperLength, endPoint.y + padding);
            } else {
                arrowStart = new Point(startPoint.x + helperLength,startPoint.y - padding);
                arrowEnd = new Point(startPoint.x + helperLength, endPoint.y + padding);
            }
        } else {
            if (endPoint.x >= startPoint.x){
                arrowStart = new Point(startPoint.x + helperLength,startPoint.y - padding);
                arrowEnd = new Point(startPoint.x + helperLength, endPoint.y + padding);
            } else {
                arrowStart = new Point(endPoint.x + helperLength,startPoint.y - padding);
                arrowEnd = new Point(endPoint.x + helperLength, endPoint.y + padding);
            }
        }

        //Draw the dotted "helper" lines.
        g2.setColor(Settings.INFO_ARROW_COLOUR);
        g2.setStroke(Settings.INFO_ARROW_HELPER_STROKE);

        g2.drawLine(startPoint.x, startPoint.y, arrowStart.x, arrowStart.y + padding);
        g2.drawLine(endPoint.x, endPoint.y , arrowEnd.x, arrowEnd.y - padding);

        //Draw the solid line which will becomes the arrow.
        Integer arrowLength = Settings.TOP_DOWN_INFO_ARROW_LENGTH;
        Integer arrowHeight = Settings.TOP_DOWN_INFO_ARROW_HEIGHT;
        g2.setStroke(Settings.INFO_ARROW_STROKE);
        g2.drawLine(arrowStart.x , arrowStart.y - arrowLength, arrowEnd.x , arrowEnd.y + arrowLength);

        //Create two polygons for the arrow heads at each end of the line.
        Polygon arrowHeadStart = new Polygon(new int[] {arrowStart.x - arrowHeight, arrowStart.x , arrowStart.x + arrowHeight},
                new int[] {arrowStart.y -arrowLength, arrowStart.y , arrowStart.y - arrowLength}, 3);
        Polygon arrowHeadEnd = new Polygon(new int[] {arrowEnd.x - arrowHeight, arrowEnd.x , arrowEnd.x + arrowHeight},
                new int[] {arrowEnd.y +arrowLength, arrowEnd.y , arrowEnd.y + arrowLength}, 3);

        //Draw the two arrowheads
        g2.fillPolygon(arrowHeadStart);
        g2.fillPolygon(arrowHeadEnd);

        //Set the font to the correct font specified in the settings class.
        g2.setFont(Settings.INFO_TEXT_FONT);
        FontMetrics fontMetrics = g2.getFontMetrics();

        //Using a font metrics object gained from g2, determine the width and height of the label.
        Integer textPadding = Settings.TOP_DOWN_INFO_TEXT_PADDING;
        Integer stringLength = g2.getFontMetrics().stringWidth(label);
        Integer stringHeight = Settings.INFO_TEXT_FONT.getSize();

        g2.setColor(Settings.INFO_TEXT_COLOUR);
        g2.setFont(Settings.INFO_TEXT_FONT);

        if(helperLength > 0){
            g2.drawString(label,arrowStart.x + textPadding,(startPoint.y + endPoint.y + stringHeight)/2 -5);
        } else {
            g2.drawString(label,arrowStart.x - stringLength - textPadding,(startPoint.y + endPoint.y + stringHeight)/2 -5);
        }
    }

    /**
     * Takes some Graphics2D object and draws a data arrow according to the properties of the object.
     * Dotted lines are drawn vertically down from the start and end point, and terminate at the same y-level.
     * A solid line with arrow heads at each end is drawn horizontally between the two dotted lines.
     * The label is drawn at the center of the solid line.
     *
     * If the helperDistance is positive, then the dotted
     * lines will be drawn down from the start and end point and the text is displayed under the line.
     * If the helperDistance is negative, then the dotted lines will be drawn upwards and the text will appear
     * above the line.
     *
     * As of right now, the class makes no guarantees that the arrow will be displayed properly when the arrow is
     * quite small. Generally thing is when the distance between startPoint.x and endPoint.x is less than:
     * Settings.TOP_DOWN_INFO_ARROW_PADDING * 2 + Settings.INFO_ARROW_LENGTH * 2.
     *
     * @param g2 The graphics object used to draw the arrow. Note that the graphics object is not modifed in any way,
     * and is used as is.
     */
    public void drawHorizontalArrow(Graphics2D g2){
        Point arrowStart;
        Point arrowEnd;
        Integer padding = Settings.TOP_DOWN_INFO_ARROW_PADDING;

        /*
        Determine the position of the start and end of the arrow based on if the start point is above or
        below the end point, and if the helper distance is positive of negative.
         */
        if(helperLength >= 0){
            if (endPoint.y >= startPoint.y){
                arrowStart = new Point(startPoint.x + padding,endPoint.y + helperLength);
                arrowEnd = new Point(endPoint.x - padding, endPoint.y + helperLength);
            } else {
                arrowStart = new Point(startPoint.x + padding,startPoint.y + helperLength);
                arrowEnd = new Point(endPoint.x - padding, startPoint.y + helperLength);
            }
        } else {
            if (endPoint.y >= startPoint.y){
                arrowStart = new Point(startPoint.x + padding,startPoint.y + helperLength);
                arrowEnd = new Point(endPoint.x - padding, startPoint.y + helperLength);
            } else {
                arrowStart = new Point(startPoint.x + padding,endPoint.y + helperLength);
                arrowEnd = new Point(endPoint.x - padding, endPoint.y + helperLength);
            }
        }

        //Draw the dotted "helper" lines.
        g2.setColor(Settings.INFO_ARROW_COLOUR);
        g2.setStroke(Settings.INFO_ARROW_HELPER_STROKE);
        g2.drawLine(startPoint.x, startPoint.y, arrowStart.x - padding, arrowStart.y);
        g2.drawLine(endPoint.x, endPoint.y, arrowEnd.x + padding, arrowEnd.y);

        //Draw the solid line which will becomes the arrow.
        Integer arrowLength = Settings.TOP_DOWN_INFO_ARROW_LENGTH;
        Integer arrowHeight = Settings.TOP_DOWN_INFO_ARROW_HEIGHT;
        g2.setStroke(Settings.INFO_ARROW_STROKE);
        g2.drawLine(arrowStart.x + arrowLength, arrowStart.y, arrowEnd.x - arrowLength, arrowEnd.y);

        //Create two polygons for the arrow heads at each end of the line.
        Polygon arrowHeadStart = new Polygon(new int[] {arrowStart.x + arrowLength, arrowStart.x + arrowLength, arrowStart.x},
                new int[] {arrowStart.y - arrowHeight, arrowStart.y + arrowHeight, arrowStart.y}, 3);
        Polygon arrowHeadEnd = new Polygon(new int[] {arrowEnd.x - arrowLength, arrowEnd.x - arrowLength, arrowEnd.x},
                new int[] {arrowEnd.y - arrowHeight, arrowEnd.y + arrowHeight, arrowEnd.y}, 3);

        //Draw the two arrowheads
        g2.fillPolygon(arrowHeadStart);
        g2.fillPolygon(arrowHeadEnd);

        g2.setFont(Settings.INFO_TEXT_FONT);
        FontMetrics fontMetrics = g2.getFontMetrics();

        //Using a font metrics object gained from g2, determine the width and height of the label.
        Integer labelLength = fontMetrics.stringWidth(label);
        Integer labelHeight = fontMetrics.getHeight();
        Integer arrowMidpoint = (endPoint.x + startPoint.x)/2;

        //Draw the text below if helperLength is positive, and draw the text above if it is not.
        if(helperLength >= 0){
            g2.drawString(label,arrowMidpoint - labelLength/2, arrowStart.y + labelHeight);
        } else {
            g2.drawString(label,arrowMidpoint - labelLength/2, arrowStart.y - Settings.TOP_DOWN_INFO_TEXT_PADDING);
        }
    }
}
