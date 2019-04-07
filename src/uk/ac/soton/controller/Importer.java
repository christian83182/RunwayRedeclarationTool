package uk.ac.soton.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;
import uk.ac.soton.common.Runway;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A class created to import an XML file containing a configuration of the application created by Exporter.
 */
public class Importer {

    //An instance of the controller used to access the model.
    private AppController controller;
    //An instance of the airfield which acts as the model.
    private Airfield airfield;

    //Initialize member variables.
    Importer(AppController controller){
        this.controller = controller;
        this.airfield = new Airfield();
        this.controller.setAirfield(airfield);
    }

    public void importConfiguration(String absolutePath) throws ParserConfigurationException, IOException, SAXException{
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

        //Import runway properties
        importRunwayProperties(airfieldElement);
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
        NodeList bgImageNodeList = airfieldElement.getElementsByTagName("BackgroundImage");
        if(bgImageNodeList.getLength()>0){
            //Retrieve the BackgroundImage node from the airfieldElement node.
            Element bgImageElement = (Element)bgImageNodeList.item(0);

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
            //Retrieve the obstacle element from the predefinedObstaclesElement at the index position.
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

    //Imports the properties of all defined runways given the root node airfieldElement.
    private void importRunwayProperties(Element airfieldElement){
        //Retrieve the physicalRunwaysElement node from the root airfieldElement node.
        Element physicalRunwaysElement = (Element) airfieldElement.getElementsByTagName("PhysicalRunways").item(0);
        NodeList runwayList = physicalRunwaysElement.getElementsByTagName("Runway");

        //Iterate over all the child nodes (runways) of physicalRunwayElement.
        for(int i = 0; i < runwayList.getLength(); i++){
            //Retrieve the runway at the index position from runwayList.
            Element currentRunway = (Element) runwayList.item(i);

            //Retrieve the firstLogicalRunway's name from the currentRunway node.
            Node firstLogicalRunwayNode = currentRunway.getElementsByTagName("FirstLogicalRunwayName").item(0);
            String firstLogicalRunwayName = firstLogicalRunwayNode.getTextContent();

            //Retrieve the secondLogicalRunway's name from the currentRunway node.
            Node secondLogicalRunwayNode = currentRunway.getElementsByTagName("SecondLogicalRunwayName").item(0);
            String secondLogicalRunwayName = secondLogicalRunwayNode.getTextContent();

            //Retrieve the xPosition from the currentRunway node.
            Node xPositionNode = currentRunway.getElementsByTagName("XPosition").item(0);
            Integer xPosition = Integer.parseInt(xPositionNode.getTextContent());

            //Retrieve the yPosition from the currentRunway node.
            Node yPositionNode = currentRunway.getElementsByTagName("YPosition").item(0);
            Integer yPosition = Integer.parseInt(yPositionNode.getTextContent());

            //Retrieve the length from the currentRunway node.
            Node lengthNode = currentRunway.getElementsByTagName("Length").item(0);
            Integer length = Integer.parseInt(lengthNode.getTextContent());

            //Retrieve the width from the currentRunway node.
            Node widthNode = currentRunway.getElementsByTagName("Width").item(0);
            Integer width = Integer.parseInt(widthNode.getTextContent());

            //Retrieve the stripEndSize from the currentRunway node.
            Node stripEndNode = currentRunway.getElementsByTagName("StripEndLength").item(0);
            Integer stripEndSize = Integer.parseInt(stripEndNode.getTextContent());

            //Retrieve the stripWidth from the currentRunway node.
            Node stripWidthNode = currentRunway.getElementsByTagName("stripWidthElement").item(0);
            Integer stripWidth = Integer.parseInt(stripWidthNode.getTextContent());

            //Retrieve the resaSize from the currentRunway node.
            Node resaSizeNode = currentRunway.getElementsByTagName("RESALength").item(0);
            Integer resaSize = Integer.parseInt(resaSizeNode.getTextContent());

            //Construct a new Runway object using the data extracted from currentObject.
            String runwayName = firstLogicalRunwayName + "/" + secondLogicalRunwayName;
            Runway physicalRunway = new Runway(runwayName, xPosition, yPosition, length, width, stripWidth, stripEndSize);
            physicalRunway.setResa(resaSize);
            airfield.addRunway(physicalRunway);

            //Import the logical runways associated with the newly imported physical runway.
            importLogicalRunway(currentRunway, physicalRunway);

            importRunwayObstacle(currentRunway, firstLogicalRunwayName);
        }
    }

    //Imports the properties of both logical runways given an Element runwayElement corresponding to a physical runway.
    //The imported logical runways are imported into the given physicalRunway.
    private void importLogicalRunway(Element runwayElement, Runway physicalRunway){
        //Extract the firstLogicalRunwayElement and secondLogicalRunwayElement nodes from the runwayElement node.
        Element firstLogicalRunwayElement = (Element)runwayElement.getElementsByTagName("FirstLogicalRunway").item(0);
        Element secondLogicalRunwayElement = (Element)runwayElement.getElementsByTagName("SecondLogicalRunway").item(0);

        //Extract the threshold for the first logical runway.
        Node firstThresholdNode = firstLogicalRunwayElement.getElementsByTagName("DisplacedThreshold").item(0);
        Integer firstThreshold = Integer.parseInt(firstThresholdNode.getTextContent());
        //Extract the threshold for the second logical runway.
        Node secondThresholdNode = secondLogicalRunwayElement.getElementsByTagName("DisplacedThreshold").item(0);
        Integer secondThreshold = Integer.parseInt(secondThresholdNode.getTextContent());

        //Extract the stopway length for the first logical runway.
        Node firstStopwayLengthNode = firstLogicalRunwayElement.getElementsByTagName("StopwayLength").item(0);
        Integer firstStopwayLength = Integer.parseInt(firstStopwayLengthNode.getTextContent());
        //Extract the stopway length for the second logical runway.
        Node secondStopwayLengthNode = secondLogicalRunwayElement.getElementsByTagName("StopwayLength").item(0);
        Integer secondStopwayLength = Integer.parseInt(secondStopwayLengthNode.getTextContent());

        //Extract the stopway width for the first logical runway.
        Node firstStopwayWidthNode = firstLogicalRunwayElement.getElementsByTagName("StopwayWidth").item(0);
        Integer firstStopwayWidth = Integer.parseInt(firstStopwayWidthNode.getTextContent());
        //Extract the stopway width for the second logical runway.
        Node secondStopwayWidthNode = secondLogicalRunwayElement.getElementsByTagName("StopwayWidth").item(0);
        Integer secondtStopwayWidth = Integer.parseInt(secondStopwayWidthNode.getTextContent());

        //Extract the clearway length for the first logical runway.
        Node firstClearwayLengthNode = firstLogicalRunwayElement.getElementsByTagName("ClearwayLength").item(0);
        Integer firstClearwayLength = Integer.parseInt(firstClearwayLengthNode.getTextContent());
        //Extract the clearway length for the second logical runway.
        Node secondClearwayLengthNode = secondLogicalRunwayElement.getElementsByTagName("ClearwayLength").item(0);
        Integer secondClearwayLength = Integer.parseInt(secondClearwayLengthNode.getTextContent());

        //Extract the clearway width for the first logical runway.
        Node firstClearwayWidthNode = firstLogicalRunwayElement.getElementsByTagName("ClearwayWidth").item(0);
        Integer firstClearwayWidth = Integer.parseInt(firstClearwayWidthNode.getTextContent());
        //Extract the clearway width for the second logical runway.
        Node secondClearwayWidthNode = secondLogicalRunwayElement.getElementsByTagName("ClearwayWidth").item(0);
        Integer secondClearwayWidth = Integer.parseInt(secondClearwayWidthNode.getTextContent());

        //Use the retrieved data to construct the first logical runway.
        String firstName = physicalRunway.getId().split("/")[0];
        Dimension firstStopwayDim = new Dimension(firstStopwayLength,firstStopwayWidth);
        Dimension firstClearwayDim = new Dimension(firstClearwayLength,firstClearwayWidth);
        LogicalRunway firstLogicalRunway = new LogicalRunway(firstName, physicalRunway.getLength(), firstThreshold, firstClearwayDim, firstStopwayDim);

        //Use the retrieved data to construct the second logical runway.
        String secondName = physicalRunway.getId().split("/")[1];
        Dimension secondStopwayDim = new Dimension(secondStopwayLength,secondtStopwayWidth);
        Dimension secondClearwayDim = new Dimension(secondClearwayLength,secondClearwayWidth);
        LogicalRunway secondLogicalRunway = new LogicalRunway(secondName, physicalRunway.getLength(), secondThreshold, secondClearwayDim, secondStopwayDim);

        //Add the newly created logical runways to the physical runway.
        physicalRunway.setLogicalRunways(firstLogicalRunway, secondLogicalRunway);
    }

    //Imports an obstacle onto a runway if the relevant tags indicate so in teh runwayElement node.
    private void importRunwayObstacle(Element runwayElement, String firstLogicalRunwayName){
        //Check to see if there is a tag implying there's an obstacle on the runway.
        NodeList runwayObstacleNodeList = runwayElement.getElementsByTagName("RunwayObstacle");
        //If the size of the NodeList is not greater than 0 then there's no obstacle on the runway.
        if(runwayObstacleNodeList.getLength()>0){
            //Extract the RunwayObstacleElement node from the runwayElement node.
            Element runwayObstacleElement = (Element) runwayObstacleNodeList.item(0);

            //Extract the name of the obstacle placed on the runway.
            Node obstacleIdNode = runwayObstacleElement.getElementsByTagName("ObstacleName").item(0);
            String obstacleId = obstacleIdNode.getTextContent();

            //Extract the distance from the start to the obstacle on the runway.
            Node distanceFromStartNode = runwayObstacleElement.getElementsByTagName("DistanceFromRunwayStart").item(0);
            Integer distanceFromStart = Integer.parseInt(distanceFromStartNode.getTextContent());

            //Extract the distance from the centerline to the obstacle on the runway.
            Node distanceFromCenterlineNode = runwayObstacleElement.getElementsByTagName("DistanceFromRunwayCenterline").item(0);
            Integer distanceFromCenterline = Integer.parseInt(distanceFromCenterlineNode.getTextContent());

            //Use the controller's addObstacleToRunway method to place an obstacle on the runway.
            controller.addObstacleToRunway(firstLogicalRunwayName,obstacleId, distanceFromCenterline, distanceFromStart);
        }
    }
}
