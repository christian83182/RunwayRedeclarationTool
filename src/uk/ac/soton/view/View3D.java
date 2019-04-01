package uk.ac.soton.view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class View3D extends JFrame{

    private AppView appView;
    private ViewController controller;
    private double startX, startY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

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
        GroupRotate root = new GroupRotate();
        Scene scene = new Scene(root, getWidth(), getHeight(), true);
        scene.setFill(Color.SILVER);
        initCamera(scene, root);
        createScene(scene, root);
        fxPanel.setScene(scene);
    }

    //Creates the JavaFx scene which should be contained within the JFXPanel.
    private void createScene(Scene scene, Group root) {

        Box box = new Box(100,100,100);
        box.setTranslateY(-50);

        Box plane = new Box(5000,1,5000);
        plane.setMaterial(new PhongMaterial(Color.LIMEGREEN));

        root.getChildren().addAll(box, plane);

    }

    private void initCamera(Scene scene, GroupRotate root){
        PerspectiveCamera camera = new PerspectiveCamera(true);
        scene.setCamera(camera);

        camera.setFieldOfView(80);
        camera.setNearClip(1);
        camera.setFarClip(5000);
        camera.setTranslateZ(-200);

        root.rotateByX(90);

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
            if(newAngleX < -85){
                angleX.set(-85);
            } else if (newAngleX > 0){
                angleX.set(0);
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

    class GroupRotate extends Group {
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }

}

