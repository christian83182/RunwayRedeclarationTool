package uk.ac.soton.view;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An class dedicated to representing and drawing Legends/Keys. It is assumed that all entries will be a colour, and an
 * associated text.
 */
public class Legend {

    Map<String, Color> legendMap;
    String title;

    /**
     * The constructor takes a title which is displayed as the title in the box when drawLegend is called.
     * @param title the title which should be displayed.
     */
    Legend(String title){
        this.legendMap = new LinkedHashMap<>();
        this.title = title;
    }

    /**
     * Adds an entry to the legend.
     * @param key The string which corresponds to the colour.
     * @param value The colour associated with the string.
     */
    public void addToLegend(String key, Color value){
        legendMap.put(key, value);
    }

    /**
     * Draws the legend on the specified graphics2D object. It is drawn such that the specified point ends up
     * and the bottom right of the key. This is because it is intended to be placed in the bottom-right
     * of the screen and this make its placement the most convenient.
     * @param g2 The Graphics object on which it should be drawn.
     * @param bottomLeft The point which should end up at the bottom right of the drawn legend.
     */
    public void drawLegend(Graphics2D g2, Point bottomLeft){

        //Defines various variables used throughout the methods.
        Integer fontSize = Settings.SIDE_MENU_DEFAULT_FONT.getSize();
        Integer titleFontSize = (int)(fontSize*1.5);
        Integer offset = titleFontSize + 20;
        Integer heightAllowed = fontSize+10;
        Integer width = 0;
        Integer height = offset + heightAllowed*legendMap.size() +20;

        //Determines the width of the panel based on the width of the text in it.
        g2.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        FontMetrics fontMetrics = g2.getFontMetrics();
        for(String key : legendMap.keySet()){
            width = Math.max(width, fontMetrics.stringWidth(key));
        }
        width += 80;

        //Draw the background.
        Point pos = new Point(bottomLeft.x-width, bottomLeft.y-height);
        g2.setColor(new Color(45, 45, 45, 150));
        g2.fillRect(pos.x, pos.y, width, height);
        g2.setColor(new Color(39, 39, 39));
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(pos.x, pos.y, width, height);

        //Draw the title.
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, titleFontSize));
        fontMetrics = g2.getFontMetrics();
        g2.drawString(title, pos.x + (width - fontMetrics.stringWidth(title))/2, pos.y + titleFontSize +10);

        //Draw the boxes and the associated text.
        Integer counter = 0;
        g2.setFont(Settings.SIDE_MENU_DEFAULT_FONT);
        for(String key: legendMap.keySet()){
            g2.setColor(legendMap.get(key));
            g2.fillRect(pos.x+18, pos.y + offset + counter*heightAllowed + 2, heightAllowed-4, heightAllowed-4);
            g2.setColor(Color.WHITE);
            g2.drawString(key, pos.x + 18 + heightAllowed-4 +10 , pos.y + offset + counter*heightAllowed + (int)(fontSize*1.25));
            counter++;
        }
    }
}
