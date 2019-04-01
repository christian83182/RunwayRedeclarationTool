package uk.ac.soton.view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View3D extends JFrame{

    private AppView appView;
    private ViewController controller;
    private double startX, startY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(60);
    private final DoubleProperty angleY = new SimpleDoubleProperty(45);

    View3D(AppView appView){
        super("3D Visualization");
        this.appView = appView;
        this.controller = appView.getController();
        initSwing();
    }

    //Initializes the Swing components.
    private void initSwing() {
        this.setPreferredSize(new Dimension(1400,900));

        //Create a JFXPanel for the 3D content.
        JFXPanel fxPanel = new JFXPanel();
        this.add(fxPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //Runs the method 'initJFX' in a new thread, which then becomes the JFX thread.
        Platform.runLater(() -> initJFX(fxPanel));
    }

    //Initializes the JavaFX content for the JFXPanel. Should run in a separate thread from the EDT.
    private void initJFX(JFXPanel fxPanel) {
        Group root = new Group();
        Scene scene = new Scene(root, getWidth(), getHeight(), true);
        scene.setFill(convertToJFXColour(Settings.AIRFIELD_COLOUR));
        initCamera(scene, root);
        createScene(scene, root);
        fxPanel.setScene(scene);
    }

    //Creates the JavaFx scene which should be contained within the JFXPanel.
    private void createScene(Scene scene, Group root) {
        if(appView.getSelectedRunway().equals("")){
            generateRunways(root);
        } else if(appView.getMenuPanel().isIsolateMode()){
            //draw only one one runway, focus on one.
        } else {
            //draw all runways, focus on one.
        }
    }

    private void initCamera(Scene scene, Group root){
        PerspectiveCamera camera = new PerspectiveCamera(true);
        scene.setCamera(camera);

        camera.setFieldOfView(80);
        camera.setNearClip(1);
        camera.setFarClip(5000);
        camera.setTranslateZ(-200);

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(xRotate, yRotate);

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            startX = event.getSceneX();
            startY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            Double newAngleX = anchorAngleX - (startY - event.getSceneY())*0.2;
            if(newAngleX > 90){
                angleX.set(90);
            } else if (newAngleX < 5){
                angleX.set(5);
            } else {
                angleX.set(newAngleX);
            }
            angleY.set(anchorAngleY + (startX - event.getSceneX())*0.2);
        });

        scene.setOnScroll(event -> {
            Double delta = event.getDeltaY();
            if(delta > 0 && camera.getTranslateZ() <= -100){
                camera.setTranslateZ(camera.getTranslateZ()*0.975);
            } else if(delta <0 && camera.getTranslateZ() >= -3000){
                camera.setTranslateZ(camera.getTranslateZ()* 1/0.975);
            }
        });
    }

    private Color convertToJFXColour(java.awt.Color swingColour){
        double red = swingColour.getRed()/255.0;
        double green = swingColour.getGreen()/255.0;
        double blue = swingColour.getBlue()/255.0;
        double opacity = swingColour.getAlpha() / 255.0;
        return new Color(red,green, blue,opacity);
    }

    private void generateRunwayStrip(Group root, String runwayId){
        Point pos = controller.getRunwayPos(runwayId);
        Dimension dim = controller.getRunwayDim(runwayId);
        Integer distanceFromCenterline = controller.getStripWidthFromCenterline(runwayId);
        Integer stripEndSize = controller.getStripEndSize(runwayId);

        Box stripBox = new Box(distanceFromCenterline*2,12,dim.width+stripEndSize*2);
        PhongMaterial stripMaterial = new PhongMaterial(convertToJFXColour(Settings.RUNWAY_STRIP_COLOUR));
        stripBox.setMaterial(stripMaterial);

        stripBox.setTranslateX(pos.x);
        stripBox.setTranslateZ(-pos.y + dim.width/2.0);

        Rotate r = new Rotate(controller.getBearing(runwayId), 0,0,-dim.width/2.0, Rotate.Y_AXIS);
        stripBox.getTransforms().add(r);

        root.getChildren().add(stripBox);
    }

    private void generateRunway(Group root, String runwayId){
        Point pos = controller.getRunwayPos(runwayId);
        Dimension dim = controller.getRunwayDim(runwayId);

        Box runwayBox = new Box(dim.height,20,dim.width);
        PhongMaterial runwayMaterial = new PhongMaterial(convertToJFXColour(Settings.RUNWAY_COLOUR));
        runwayBox.setMaterial(runwayMaterial);

        runwayBox.setTranslateX(pos.x);
        runwayBox.setTranslateZ(-pos.y + dim.width/2.0);

        Rotate r = new Rotate(controller.getBearing(runwayId), 0,0,-dim.width/2.0, Rotate.Y_AXIS);
        runwayBox.getTransforms().add(r);

        root.getChildren().add(runwayBox);
    }

    private void generateRunways(Group root){
        ArrayList<Integer> drawnRunways = new ArrayList<>();

        for(String runwayId : controller.getRunways()){
            Integer currentRunwayBearing = controller.getBearing(runwayId);
            if(!(drawnRunways.contains(currentRunwayBearing+180) || drawnRunways.contains(currentRunwayBearing-180))){

                generateRunway(root, runwayId);
                generateRunwayStrip(root, runwayId);
                drawnRunways.add(currentRunwayBearing);
            }
        }
    }
}

