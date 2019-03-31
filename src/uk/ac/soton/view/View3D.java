package uk.ac.soton.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class View3D extends JFrame{

    private AppView appView;
    private ViewController controller;

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
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    //Creates the JavaFx scene which should be contained within the JFXPanel.
    private Scene createScene() {
        Group root = new  Group();

        //Create some generic content to demo the functionality.
        Text text = new  Text();
        text.setX(600);
        text.setY(400);
        text.setFont(new Font(100));
        text.setText(appView.getSelectedRunway());
        root.getChildren().add(text);

        //Creates a scene based on the given group and returns a scene where this is the group.
        Scene scene = new Scene(root, Color.WHITE);
        return scene;
    }
}
