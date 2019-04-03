package uk.ac.soton.view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import uk.ac.soton.controller.ViewController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View3D extends JFrame{

    private AppView appView;
    private ViewController controller;

    private double dragStartX, dragStartY, anchorAngleX, anchorAngleY;
    private double xOffset, yOffset, zOffset;
    private DoubleProperty angleX, angleY;

    View3D(AppView appView){
        super("3D Visualization");
        this.appView = appView;
        this.controller = appView.getController();

        angleX = new SimpleDoubleProperty(60);
        angleY = new SimpleDoubleProperty(-45);

        initSwing();
    }

    //Initializes the Swing components.
    private void initSwing() {
        this.setPreferredSize(new Dimension(1600,900));
        this.setResizable(false);

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
        Group globalRoot = new Group();
        Scene scene = new Scene(globalRoot, getWidth(), getHeight());
        scene.setFill(Color.WHITE);

        Group root3D = new Group();
        SubScene scene3D = new SubScene(root3D, getWidth(), getHeight(), true, SceneAntialiasing.BALANCED);
        scene3D.setFill(convertToJFXColour(Settings.AIRFIELD_COLOUR));
        globalRoot.getChildren().add(scene3D);

        createScene(globalRoot, root3D);
        initCamera(scene3D, root3D);

        fxPanel.setScene(scene);
    }

    //Creates the JavaFx scene which should be contained within the JFXPanel.
    private void createScene(Group globalRoot, Group root3D) {
        if(appView.getSelectedRunway().equals("")){
            createGeneralScene(root3D);
        } else if(appView.getMenuPanel().isIsolateMode()){
            createIsolatedScene(globalRoot, root3D);
        } else {
            createSelectedScene(globalRoot, root3D);
        }
    }

    //Populates the root3D scene and manipulates the camera such that the camera orbits about the origin, with all runways visible.
    private void createGeneralScene(Group root3D){
        generateRunways(root3D);
        generateLighting(root3D);
        pointCameraAt(new Point3D(0,0,0),root3D);
    }

    //Restricts the camera to one runway and only renders that one runway.
    private void createIsolatedScene(Group globalRoot, Group root3D){
        String selectedRunway = appView.getSelectedRunway();
        Point pos = controller.getRunwayPos(selectedRunway);

        generateRunwayStrip(root3D, selectedRunway);
        generateClearAndGraded(root3D, selectedRunway);
        generateRunway(root3D, selectedRunway);
        generateLighting(root3D);
        pointCameraAt(new Point3D(pos.x,0, -pos.y),root3D);

        generateOverlay(globalRoot, root3D);
    }

    //Restricts the camera to one runway but still renders all other runways.
    private void createSelectedScene(Group globalRoot, Group root3D){
        String selectedRunway = appView.getSelectedRunway();
        Point pos = controller.getRunwayPos(selectedRunway);

        generateRunways(root3D);
        generateLighting(root3D);
        pointCameraAt(new Point3D(pos.x,0, -pos.y),root3D);

        generateOverlay(globalRoot, root3D);
    }

    //Initializes a perspective camera in the scene, enables rotation and zooming.
    private void initCamera(SubScene scene, Group root){
        //Create a perspective camera for 3D use and add it to the scene.
        PerspectiveCamera camera = new PerspectiveCamera(true);
        scene.setCamera(camera);

        //Set the camera's field of view, near and far pane, and starting zoom.
        camera.setFieldOfView(80);
        camera.setNearClip(1);
        camera.setFarClip(5000);
        camera.setTranslateZ(-1000);

        //Initiate the rotation objects used for rotating the camera about the world.
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(xRotate, yRotate);

        //Bind the property so it automatically updates.
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        //Add an event listener to track the start of a mouse drag when the mouse is clicked.
        scene.setOnMousePressed(event -> {
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        //Add an event listener to update the rotation objects based on the distance dragged.
        scene.setOnMouseDragged(event -> {
            //A modifier (0.2 in this case) is used to control the sensitivity.
            Double newAngleX = anchorAngleX - (dragStartY - event.getSceneY())*0.2;
            //Restrict the rotation to angles less than 90.
            if(newAngleX > 90){
                angleX.set(90);
            //Restrict the rotation to angles greater than 5.
            } else if (newAngleX < 5){
                angleX.set(5);
            } else {
                angleX.set(newAngleX);
            }
            angleY.set(anchorAngleY + (dragStartX - event.getSceneX())*0.2);
        });

        //Add an event listener to move the camera away or towards the scene using the mouse wheel.
        scene.setOnScroll(event -> {
            Double delta = event.getDeltaY();
            //If the mouse scrolls in, and the camera is not too close, then move the camera closer.
            if(delta > 0 && camera.getTranslateZ() <= -100){
                camera.setTranslateZ(camera.getTranslateZ()*0.975);
            //Else, if the mouse scrolls out and it's not too far away, then move the camera away.
            } else if(delta <0 && camera.getTranslateZ() >= -3000){
                camera.setTranslateZ(camera.getTranslateZ()* 1/0.975);
            }
        });
    }

    //Iterates over all physical runways given in the controller, and draws the strip, clear and graded area, and runway.
    private void generateRunways(Group root){
        //Use an array list to keep track of which runways have been generated
        ArrayList<Integer> generatedRunways = new ArrayList<>();
        for(String runwayId : controller.getRunways()){
            Integer currentRunwayBearing = controller.getBearing(runwayId);
            //If runwayId the the id of a runway that hasn't been generated then...
            if(!(generatedRunways.contains(currentRunwayBearing+180) || generatedRunways.contains(currentRunwayBearing-180))){

                generateRunway(root, runwayId);
                generateRunwayStrip(root, runwayId);
                generateClearAndGraded(root, runwayId);

                //Add the runway to the list of generated runways.
                generatedRunways.add(currentRunwayBearing);
            }
        }
    }

    //Creates a Box of the same dimensions and position as runwayId, with the correct material, and adds it to root.
    private void generateRunway(Group root, String runwayId){
        Point pos = controller.getRunwayPos(runwayId);
        Dimension dim = controller.getRunwayDim(runwayId);
        Integer height = 18; //The height of the runway box off the plane x=0, z=0.

        //Creates a box of the right size and material.
        Box runwayBox = new Box(dim.height,height,dim.width);
        PhongMaterial runwayMaterial = new PhongMaterial(convertToJFXColour(Settings.RUNWAY_COLOUR));
        runwayBox.setMaterial(runwayMaterial);

        //Moves the box to the correct position, and elevates it so it's entirely above the x=0, z=0 plane.
        runwayBox.setTranslateX(pos.x);
        runwayBox.setTranslateZ(-pos.y + dim.width/2.0);
        runwayBox.setTranslateY(-height/2);

        //Rotates the box to match the rotation of the runway.
        Rotate r = new Rotate(controller.getBearing(runwayId), 0,0,-dim.width/2.0, Rotate.Y_AXIS);
        runwayBox.getTransforms().add(r);

        //Add the box to the root group.
        root.getChildren().add(runwayBox);
    }

    //Generates a group containing meshes which simulate an extruded clear and graded area for the given runwayId.
    //NOTE: If both meshes are combined then the shading on the mesh acts up, hence why this approach is being used.
    private Group genClearAndGradedMesh(String runwayId, Integer height){
        Dimension dim = controller.getRunwayDim(runwayId);
        Group meshGroup = new Group();
        //Create now TriangleMeshes for the top and side meshes.
        TriangleMesh meshTop = new TriangleMesh();
        TriangleMesh meshSide = new TriangleMesh();

        //Add all the vertexes for the top of the mesh.
        meshTop.getPoints().addAll(
                -75, -height/2, dim.width/2 +60,
                75, -height/2, dim.width/2 +60,
                -75, -height/2, -dim.width/2 -60,
                75, -height/2, -dim.width/2 -60,
                -75, -height/2, -dim.width/2 +150,
                -105, -height/2, -dim.width/2 +300,
                -105, -height/2, dim.width/2 -300,
                -75, -height/2, dim.width/2 -150,
                75, -height/2, -dim.width/2 +150,
                105, -height/2, -dim.width/2 +300,
                105, -height/2, dim.width/2 -300,
                75, -height/2, dim.width/2 -150);

        //Define all the faces on the top of the shape.
        meshTop.getFaces().addAll(
                0,0,3,3,1,1,   0,0,2,2,3,3,
                6,6,5,5,4,4,   6,6,4,4,7,7,
                11,11,8,8,9,9, 11,11,9,9,10,10);

        //Since the object will not be UV unwrapped, we don't worry about the texture coordinates.
        for(int i =0; i < meshTop.getPoints().size()/3; i++){
            meshTop.getTexCoords().addAll(0,0);
        }
        //Create a MeshView for the mesh, set the material, and add it to the mesh group.
        MeshView topMeshView = new MeshView(meshTop);
        topMeshView.setMaterial(new PhongMaterial(convertToJFXColour(Settings.CLEAR_AND_GRADED_COLOUR)));
        meshGroup.getChildren().add(topMeshView);

        //Add all the vertices in topMesh to sideMesh.
        meshSide.getPoints().addAll(meshTop.getPoints());
        //Iterate over all the vertices and add a copy which is mirrored in the x=0, y=0 plane.
        for (int i = 0; i < meshTop.getPoints().size()/3; i++){
            meshSide.getPoints().addAll(meshTop.getPoints().get(i*3));
            meshSide.getPoints().addAll(-meshTop.getPoints().get(i*3+1));
            meshSide.getPoints().addAll(meshTop.getPoints().get(i*3+2));
        }

        //Define all the faces to connect the top and bottom faces.
        meshSide.getFaces().addAll(
                0,0,13,13,12,12,   0,0,1,1,13,13,
                2,2,14,14,15,15,   2,2,15,15,3,3,
                6,6,18,18,17,17,   6,6,17,17,5,5,
                9,9,21,21,22,22,   9,9,22,22,10,10,
                0,0,12,12,19,19,   0,0,19,19,7,7,
                11,11,23,23,13,13, 11,11,13,13,1,1,
                4,4,16,16,14,14,   4,4,14,14,2,2,
                3,3,15,15,20,20,   3,3,20,20,8,8,
                7,7,19,19,18,18,   7,7,18,18,6,6,
                10,10,22,22,23,23, 10,10,23,23,11,11,
                5,5,17,17,16,16,   5,5,16,16,4,4,
                8,8,20,20,21,21,  8,8,21,21,9,9
        );

        //Again, since the object will not be UV unwrapped, we don't worry about the texture coordinates.
        for(int i =0; i < meshSide.getPoints().size()/3; i++){
            meshSide.getTexCoords().addAll(0,0);
        }
        //Create a MeshView for the mesh, set the material, and add it to the mesh group.
        MeshView sideMeshView = new MeshView(meshSide);
        sideMeshView.setMaterial(new PhongMaterial(convertToJFXColour(Settings.CLEAR_AND_GRADED_COLOUR)));
        meshGroup.getChildren().add(sideMeshView);

        //return the group containing both meshes.
        return meshGroup;
    }

    //Creates a mesh of the correct dimensions for the given runway, in the correct position and rotation, and adds it to the given group.
    private void generateClearAndGraded(Group root, String runwayId){
        Point pos = controller.getRunwayPos(runwayId);
        Dimension dim = controller.getRunwayDim(runwayId);
        Integer height = 12; //The height of the runway box off the plane x=0, z=0.

        //Use the genClearAndGradedMesh to get a group containing meshes simulating a 3D clear and graded area.
        Group gradedMeshGroup = genClearAndGradedMesh(runwayId, height);

        //Moves the mesh to the correct position, and elevates it so it's entirely above the x=0, z=0 plane.
        gradedMeshGroup.setTranslateX(pos.x);
        gradedMeshGroup.setTranslateZ(-pos.y + dim.width/2.0);
        gradedMeshGroup.setTranslateY(-height/2);

        //Rotates the mesh to match the rotation of the runway.
        Rotate r = new Rotate(controller.getBearing(runwayId), 0,0,-dim.width/2.0, Rotate.Y_AXIS);
        gradedMeshGroup.getTransforms().add(r);

        //Add the mesh to the root group.
        root.getChildren().add(gradedMeshGroup);
    }

    //Creates a Box of the same dimensions and position as runwayId's runway strip, with the correct material, and adds it to root.
    private void generateRunwayStrip(Group root, String runwayId){
        Point pos = controller.getRunwayPos(runwayId);
        Dimension dim = controller.getRunwayDim(runwayId);
        Integer distanceFromCenterline = controller.getStripWidthFromCenterline(runwayId);
        Integer stripEndSize = controller.getStripEndSize(runwayId);
        Integer height = 4; //The height of the runway box off the plane x=0, z=0.

        //Creates a box of the right size and material.
        Box stripBox = new Box(distanceFromCenterline*2,height,dim.width+stripEndSize*2+1);
        PhongMaterial stripMaterial = new PhongMaterial(convertToJFXColour(Settings.RUNWAY_STRIP_COLOUR));
        stripBox.setMaterial(stripMaterial);

        //Moves the box to the correct position, and elevates it so it's entirely above the x=0, z=0 plane.
        stripBox.setTranslateX(pos.x);
        stripBox.setTranslateZ(-pos.y + dim.width/2.0);
        stripBox.setTranslateY(-height/2);

        //Rotates the box to match the rotation of the runway.
        Rotate r = new Rotate(controller.getBearing(runwayId), 0,0,-dim.width/2.0, Rotate.Y_AXIS);
        stripBox.getTransforms().add(r);

        //Add the box to the root group.
        root.getChildren().add(stripBox);
    }

    private void generateLighting(Group root){
        PointLight p1= new PointLight(Color.WHITE);
        p1.setTranslateY(-400);
        root.getChildren().add(p1);

        Double intensity = 0.6;
        AmbientLight ambientLight = new AmbientLight(new Color(intensity, intensity, intensity,1));
        root.getChildren().add(ambientLight);
    }

    private void generateOverlay(Group globalRoot, Group root3D){

        Double sliderLength = getWidth()*0.6;
        Double sliderStartX = getWidth()/2-sliderLength/2;

        Polygon poly = new Polygon(sliderStartX-80, getHeight(), sliderStartX-20, getHeight()-85,
               sliderStartX+sliderLength+20, getHeight()-85,  sliderStartX+sliderLength+80, getHeight());

        poly.setFill(convertToJFXColour(new java.awt.Color(72, 72, 72)));
        poly.setStroke(convertToJFXColour(new java.awt.Color(52, 52, 52)));
        poly.setStrokeWidth(3);
        globalRoot.getChildren().add(poly);

        Slider slider = prepareSlider(root3D, appView.getSelectedRunway());
        globalRoot.getChildren().add(slider);
    }

    private Slider prepareSlider(Group root3D, String runwayId){
        Point pos = controller.getRunwayPos(runwayId);
        Dimension dim = controller.getRunwayDim(runwayId);
        Integer bearing = controller.getBearing(runwayId)-90;

        Slider slider = new Slider();
        slider.setMin(5);
        slider.setMax(dim.width-5);
        slider.setPrefSize(getWidth()*0.6,1);
        slider.setLayoutX(getWidth()/2-slider.getPrefWidth()/2);
        slider.setLayoutY(getHeight()-70);

        slider.valueProperty().addListener(e -> {
            Double xComp = Math.cos(Math.toRadians(bearing))* slider.getValue();
            Double zComp = Math.sin(-Math.toRadians(bearing))* slider.getValue();
            pointCameraAt(new Point3D(pos.x + xComp,0, -pos.y + zComp),root3D);
        });

        return slider;
    }

    //Translates the world to simulate the camera pointing at a certain position.
    private void pointCameraAt(Point3D target,Group root){
        for(Node node: root.getChildren()){
            node.setTranslateX(node.getTranslateX()-xOffset);
            node.setTranslateY(node.getTranslateY()-yOffset);
            node.setTranslateZ(node.getTranslateZ()-zOffset);
        }

        xOffset = -target.getX(); yOffset = -target.getY(); zOffset = -target.getZ();

        for(Node node: root.getChildren()){
            node.setTranslateX(node.getTranslateX()+xOffset);
            node.setTranslateY(node.getTranslateY()+yOffset);
            node.setTranslateZ(node.getTranslateZ()+zOffset);
        }
    }

    // Converts a java AWT colour to a JavaFX colour.
    private Color convertToJFXColour(java.awt.Color swingColour){
        double red = swingColour.getRed()/255.0;
        double green = swingColour.getGreen()/255.0;
        double blue = swingColour.getBlue()/255.0;
        double opacity = swingColour.getAlpha() / 255.0;
        return new Color(red,green, blue,opacity);
    }
}

