package uk.ac.soton.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.soton.common.Airfield;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Importer {

    AppController controller;
    Airfield airfield;

    Importer(AppController controller){
        this.controller = controller;
        this.airfield = new Airfield();
        this.controller.setAirfield(airfield);
    }

    public void importConfiguration(String absolutePath) throws ParserConfigurationException, IOException, SAXException, ImporterException{
        //Parse and normalize the file at the given path into a Document object.
        File targetFile = new File(absolutePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(targetFile);
        document.getDocumentElement().normalize();

        //Retrieve the root airfieldElement.
        Element airfieldElement = document.getDocumentElement();

        //Import airfield properties: name, min angle of decent, and blast distance policy.
        importAirfieldProperties(airfieldElement);

        //Import background image properties: x & y offset, scale and rotation.
        importBackgroundImageProperties(airfieldElement);

        //Import obstacle list properties: length, width, height and name for all obstacles.
        importObstacleProperties(airfieldElement);

    }

    //Imports the name, min angle of decent, and blast distance policy given the root node airfieldElement.
    private void importAirfieldProperties(Element airfieldElement){
        //Retrieve the RunwayName child from the airfieldElement node.
        Node airfieldNameNode = airfieldElement.getElementsByTagName("AirfieldName").item(0);
        String airfieldName = airfieldNameNode.getTextContent();
        airfield.setName(airfieldName);

        //Retrieve the MinAngleOfDecent from the airfieldElement node.
        Node minAngleOfDecentNode = airfieldElement.getElementsByTagName("MinimumAngleOfDecent").item(0);
        Integer minAngleOfDecent = Integer.parseInt(minAngleOfDecentNode.getTextContent());
        Airfield.setMinAngleOfDecent(minAngleOfDecent);

        //Retrieve the BlastDistancePolicy from the airfieldElement node.
        Node blastDistanceNode = airfieldElement.getElementsByTagName("BlastDistancePolicy").item(0);
        Integer blastDistance = Integer.parseInt(blastDistanceNode.getTextContent());
        Airfield.setBlastProtection(blastDistance);
    }

    //Imports the x & y offset, scale and rotation for a background image given the root node airfieldElement.
    private void importBackgroundImageProperties(Element airfieldElement){
        //Retrieve the BackgroundImage node from the airfieldElement node.
        Element bgImageElement = (Element) airfieldElement.getElementsByTagName("BackgroundImage").item(0);

        //Retrieve the xOffset node from the BackgroundImage node.
        Node xOffsetNode = bgImageElement.getElementsByTagName("X_Offset").item(0);
        Double xOffset = Double.parseDouble(xOffsetNode.getTextContent());

        //Retrieve the yOffset node from the BackgroundImage node.
        Node yOffsetNode = bgImageElement.getElementsByTagName("Y_Offset").item(0);
        Double yOffset = Double.parseDouble(yOffsetNode.getTextContent());

        //Set the controller's bgImage offset value.
        controller.setBackgroundImageOffset(new Point(xOffset.intValue(), yOffset.intValue()));

        //Retrieve the scale node from the BackgroundImage node.
        Node scaleNode = bgImageElement.getElementsByTagName("Scale").item(0);
        Double scale = Double.parseDouble(scaleNode.getTextContent());
        controller.setBackgroundImageScale(scale);

        //Retrieve the rotation node from the BackgroundImage node.
        Node rotationNode = bgImageElement.getElementsByTagName("Rotation").item(0);
        Double rotation = Double.parseDouble(rotationNode.getTextContent());
        controller.setBackgroundRotation(rotation);
    }

    //Imports the properties of all the defined obstacles given the root node airfieldElement.
    private void importObstacleProperties(Element airfieldElement){
        //Remove all entries from the list of predefined obstacles in the current airfield.
        airfield.getPredefinedObstacles().clear();

        //Retrieve the 'DefinedObstacles' element from the the airfieldElement node.
        Element predefinedObstaclesElement = (Element) airfieldElement.getElementsByTagName("DefinedObstacles").item(0);
        NodeList obstacleList = predefinedObstaclesElement.getElementsByTagName("Obstacle");

        //Iterate over all the child nodes (obstacles) of the predefinedObstaclesElement node.
        for(int i = 0; i < obstacleList.getLength(); i++){
            //Retrieve the obstacle element from the predefinedObstaclesElement.
            Element currentObstacle = (Element) obstacleList.item(i);

            //Retrieve the obstacle name from the currentObstacle node.
            Node obstacleIdNode = currentObstacle.getElementsByTagName("ObstacleName").item(0);
            String obstacleId = obstacleIdNode.getTextContent();

            //Retrieve the obstacle length from the currentObstacle node.
            Node obstacleLengthNode = currentObstacle.getElementsByTagName("ObstacleLength").item(0);
            Double obstacleLength = Double.parseDouble(obstacleLengthNode.getTextContent());

            //Retrieve the obstacle width from the currentObstacle node.
            Node obstacleWidthNode = currentObstacle.getElementsByTagName("ObstacleWidth").item(0);
            Double obstacleWidth = Double.parseDouble(obstacleWidthNode.getTextContent());

            //Retrieve the obstacle height from the currentObstacle node.
            Node obstacleHeightNode = currentObstacle.getElementsByTagName("ObstacleHeight").item(0);
            Double obstacleHeight = Double.parseDouble(obstacleHeightNode.getTextContent());

            //Defined a new obstacle in airfield using the data extracted from the currentObstacle node.
            airfield.defineNewObstacle(obstacleId, obstacleLength, obstacleWidth, obstacleHeight);
        }
    }
}
