package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class InteractivePanel extends JPanel {

    //Variable used to keep track of the current pan location.
    private Point globalPan;
    //Variable used to keep track of the current level of zoom.
    private Double globalZoom;

    InteractivePanel(Point globalPan, Double globalZoom){
        this.globalPan = globalPan;
        this.globalZoom = globalZoom;

        PanAndZoomListener panListener = new PanAndZoomListener();
        this.addMouseWheelListener(panListener);
        this.addMouseListener(panListener);
        this.addMouseMotionListener(panListener);
    }

    public abstract void paintView(Graphics2D g2);

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Generate a Buffered Image to draw on instead of using the g2d object.
        BufferedImage img = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        //Configure the global transform to match the pan and zoom settings.
        configureGlobalTransform(g2);
        paintView(g2);

        //Use the g2d object to paint the buffered image.
        g2d.drawImage(img,0,0,getWidth(),getHeight(),null);
    }

    private void configureGlobalTransform(Graphics2D g2){
        //Create a global affine transformation which pans and zooms the view accordingly.
        AffineTransform globalTransform = new AffineTransform();

        //Translate the view to account for the user's pan.
        globalTransform.translate(getPan().x*getZoom(), getPan().y*getZoom());

        //Scale the view to account for the user's zoom level. Translate such that it zoom to the center of the screen.
        globalTransform.translate(getWidth()/2, getHeight()/2);
        globalTransform.scale(getZoom(), getZoom());
        globalTransform.translate(-getWidth()/2, -getHeight()/2);

        //Set the transform to the one used by the graphics object.
        g2.setTransform(globalTransform);

    }

    public Point getPan(){
        return globalPan;
    }

    public Double getZoom(){
        return globalZoom;
    }

    public void setPan(Point newPan){
        this.globalPan = newPan;
    }

    public void setZoom(Double newZoom){
        this.globalZoom = newZoom;
    }

    //Inner class devoted to giving the view zoom and pan functionality.
    private class PanAndZoomListener extends MouseAdapter {

        Point startPoint;
        Point originalGlobalPan;

        PanAndZoomListener(){
            startPoint = new Point(0,0);
            originalGlobalPan = new Point(0,0);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            originalGlobalPan = (Point)getPan().clone();
            startPoint = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            getPan().x = (int)(originalGlobalPan.x + (e.getX() - startPoint.x)/getZoom());
            getPan().y = (int)(originalGlobalPan.y + (e.getY() - startPoint.y)/getZoom());
            InteractivePanel.this.repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Integer scaleFactor = e.getWheelRotation();
            Double maxZoom = Settings.TOP_DOWN_MAX_ZOOM;
            Double minZoom = Settings.TOP_DOWN_MIN_ZOOM;
            if(scaleFactor < 0 & getZoom() * 1/0.95 < maxZoom ){
                setZoom(getZoom() * 1/0.95);
            } else  if (scaleFactor > 0 & getZoom() * 0.95 > minZoom){
                setZoom(getZoom()*0.95);
            } else {
                return;
            }
            InteractivePanel.this.repaint();
        }
    }

}
