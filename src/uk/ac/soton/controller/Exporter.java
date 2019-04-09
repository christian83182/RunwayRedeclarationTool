package uk.ac.soton.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;
import uk.ac.soton.common.Runway;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;

/**
 * A class created to export the current airfield configuration as an XML file.
 */
public class Exporter {

    //An instance of the controller used to access the model.
    private AppController controller;
    //An instance of the airfield which acts as the model.
    private Airfield airfield;

    //Initialize member variables.
    Exporter(AppController controller){
        this.controller = controller;
        this.airfield = controller.getAirfield();
    }

    public void exportConfiguration(String absolutePath) throws ParserConfigurationException, TransformerException {
        //Create the document object.
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        //Create the root Airfield node
        Element airfieldElement = document.createElement("Airfield");
        document.appendChild(airfieldElement);

        //Using relevant methods, append the airfield properties, background image properties, runway configurations, and predefined obstacles.
        appendAirfieldProperties(document, airfieldElement);
        appendBackgroundImageElements(document, airfieldElement);
        appendRunwayElements(document, airfieldElement);
        appendPredefinedObstacleElements(document, airfieldElement);

        //Create a transformer which creates an output stream from the document. Use this to output the file to the given path.
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(absolutePath+".xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);
    }

    //Appends AirfieldName, MinimumAngleOfDecent, and BlastDistancePolicy to the airfieldElement.
    private void appendAirfieldProperties(Document document, Element airfieldElement){
        //Add a node for the airport's name.
        Element airfieldNameElement = document.createElement("AirfieldName");
        airfieldNameElement.appendChild(document.createTextNode(airfield.getName()));
        airfieldElement.appendChild(airfieldNameElement);

        //Add a node for the minimum angle of decent.
        Element minAngOfDecElement = document.createElement("MinimumAngleOfDecent");
        minAngOfDecElement.appendChild(document.createTextNode(Airfield.getMinAngleOfDecent().toString()));
        airfieldElement.appendChild(minAngOfDecElement);

        //Add a node for the airport's blast distance policy.
        Element blastDistanceElement = document.createElement("BlastDistancePolicy");
        blastDistanceElement.appendChild(document.createTextNode(Airfield.getBlastProtection().toString()));
        airfieldElement.appendChild(blastDistanceElement);
    }

    //Appends a new node 'BackgroundImage' containing data about the current background image, to the airfieldElement node.
    //Returns the BackgroundImage element.
    private Element appendBackgroundImageElements(Document document, Element airfieldElement){
        //If the application is used a background image, then populate the node, otherwise leave it empty.
        if(controller.getBackgroundImage() != null){
            Point offset = controller.getBackgroundImageOffset();

            //Create the BackgroundImage node and add it to the Airfield node
            Element bgImageElement = document.createElement("BackgroundImage");
            airfieldElement.appendChild(bgImageElement);

            //Create and add an xOffset node.
            Element xOffsetElement = document.createElement("X_Offset");
            xOffsetElement.appendChild(document.createTextNode(Double.toString(offset.x)));
            bgImageElement.appendChild(xOffsetElement);

            //Create and add a yOffset node.
            Element yOffsetElement = document.createElement("Y_Offset");
            yOffsetElement.appendChild(document.createTextNode(Double.toString(offset.y)));
            bgImageElement.appendChild(yOffsetElement);

            //Create and add a scale node.
            Element scaleElement = document.createElement("Scale");
            scaleElement.appendChild(document.createTextNode(Double.toString(controller.getBackgroundImageScale())));
            bgImageElement.appendChild(scaleElement);

            //Create and add a rotation node.
            Element rotationElement = document.createElement("Rotation");
            rotationElement.appendChild(document.createTextNode(Double.toString(controller.getBackgroundRotation())));
            bgImageElement.appendChild(rotationElement);

            return bgImageElement;
        }
        return null;
    }

    //Appends a new node 'DefinedObstacles' containing data about all defined obstacles, to the airfieldElement node.
    //Returns the DefinedObstacles element.
    private Element appendPredefinedObstacleElements(Document document, Element airfieldElement){
        if(controller.getPredefinedObstacleIds().size() > 0){
            //Create a DefinedObstacles node and add it to airfieldElement.
            Element predefinedObstaclesElement = document.createElement("DefinedObstacles");
            airfieldElement.appendChild(predefinedObstaclesElement);

            for(String obstacleId : controller.getPredefinedObstacleIds()){
                //Create a obstacleElement node and add it to the predefinedObstaclesElement node.
                Element obstacleElement = document.createElement("Obstacle");
                predefinedObstaclesElement.appendChild(obstacleElement);

                //Add the obstacle's id to the obstacleElement node.
                Element obstacleIdElement = document.createElement("ObstacleName");
                obstacleElement.appendChild(obstacleIdElement);
                obstacleIdElement.appendChild(document.createTextNode(obstacleId));

                //Add the obstacle's length to the obstacleElement node.
                Element obstacleLengthElement = document.createElement("ObstacleLength");
                obstacleElement.appendChild(obstacleLengthElement);
                obstacleLengthElement.appendChild(document.createTextNode(controller.getPredefinedObstacleLength(obstacleId).toString()));

                //Add the obstacle's width to the obstacleElement node.
                Element obstacleWidthElement = document.createElement("ObstacleWidth");
                obstacleElement.appendChild(obstacleWidthElement);
                obstacleWidthElement.appendChild(document.createTextNode(controller.getPredefinedObstacleWidth(obstacleId).toString()));

                //Add the obstacle's height to the obstacleElement node.
                Element obstacleHeightElement = document.createElement("ObstacleHeight");
                obstacleElement.appendChild(obstacleHeightElement);
                obstacleHeightElement.appendChild(document.createTextNode(controller.getPredefinedObstacleHeight(obstacleId).toString()));
            }
            return predefinedObstaclesElement;
        }
        return null;
    }

    //Appends a new node 'PhysicalRunways' containing data about all physical runways, to the airfieldElement node.
    //Returns the allRunwaysElement node.
    private Element appendRunwayElements(Document document, Element airfieldElement){
        if(airfield.getRunways().size() > 0){
            //Create a new PhysicalRunways node and add it to airfieldElement
            Element allRunwaysElement = document.createElement("PhysicalRunways");
            airfieldElement.appendChild(allRunwaysElement);

            //Iterate over all physical runways in the model.
            for(Runway currentRunway : airfield.getRunways()){
                //Create a new node Runway, to contain data about the current runway, and add it to allRunwaysElement.
                Element runwayElement = document.createElement("Runway");
                allRunwaysElement.appendChild(runwayElement);

                //Create and add a firstRunwayNameElement to runwayElement
                Element firstRunwayNameElement = document.createElement("FirstLogicalRunwayName");
                runwayElement.appendChild(firstRunwayNameElement);
                firstRunwayNameElement.appendChild(document.createTextNode(currentRunway.getLogicalRunways()[0].getName()));

                //Create and add a secondRunwayNameElement to runwayElement
                Element secondRunwayNameElement = document.createElement("SecondLogicalRunwayName");
                runwayElement.appendChild(secondRunwayNameElement);
                secondRunwayNameElement.appendChild(document.createTextNode(currentRunway.getLogicalRunways()[1].getName()));

                //Create and add an xPositionElement to runwayElement. Defined from the perspective of the first logical runway.
                Element xPositionElement = document.createElement("XPosition");
                runwayElement.appendChild(xPositionElement);
                xPositionElement.appendChild(document.createTextNode(currentRunway.getxPos().toString()));

                //Create and add a yPositionElement to runwayElement. Defined from the perspective of the first logical runway.
                Element yPositionElement = document.createElement("YPosition");
                runwayElement.appendChild(yPositionElement);
                yPositionElement.appendChild(document.createTextNode(currentRunway.getyPos().toString()));

                //Create and add a lengthElement to runwayElement
                Element lengthElement = document.createElement("Length");
                runwayElement.appendChild(lengthElement);
                lengthElement.appendChild(document.createTextNode(currentRunway.getLength().toString()));

                //Create and add a widthElement to runwayElement
                Element widthElement = document.createElement("Width");
                runwayElement.appendChild(widthElement);
                widthElement.appendChild(document.createTextNode(currentRunway.getWidth().toString()));

                //Create and add a stripEndElement to runwayElement
                Element stripEndElement = document.createElement("StripEndLength");
                runwayElement.appendChild(stripEndElement);
                stripEndElement.appendChild(document.createTextNode(currentRunway.getStripEnd().toString()));

                //Create and add a stripWidthElement to runwayElement
                Element stripWidthElement = document.createElement("stripWidthElement");
                runwayElement.appendChild(stripWidthElement);
                stripWidthElement.appendChild(document.createTextNode(currentRunway.getStripWidth().toString()));

                //Create and add a resaElement to runwayElement
                Element resaElement = document.createElement("RESALength");
                runwayElement.appendChild(resaElement);
                resaElement.appendChild(document.createTextNode(currentRunway.getResa().toString()));

                //Use the appendRunwayObstacleElements to add relevant nodes if an obstacle is present.
                //The obstacle is defined in terms of the first runway, as with everything else.
                appendRunwayObstacleElements(document, runwayElement, currentRunway.getLogicalRunways()[0]);

                //Create a firstLogicalRunwayElement, and use the appendLogicalRunwayElements method to add relevant nodes.
                Element firstLogicalRunwayElement = document.createElement("FirstLogicalRunway");
                runwayElement.appendChild(firstLogicalRunwayElement);
                appendLogicalRunwayElements(document, firstLogicalRunwayElement, currentRunway.getLogicalRunways()[0]);

                //Create a secondLogicalRunwayElement, and use the appendLogicalRunwayElements method to add relevant nodes.
                Element secondLogicalRunwayElement = document.createElement("SecondLogicalRunway");
                runwayElement.appendChild(secondLogicalRunwayElement);
                appendLogicalRunwayElements(document, secondLogicalRunwayElement, currentRunway.getLogicalRunways()[1]);
            }
            return allRunwaysElement;
        }
        return null;
    }

    //Creates a 'RunwayObstacle' node containing the id, distance from centerline, and distance from edge for an obstacle on a given runway.
    //This is appended to runwayElement. Returns the obstacleElement node.
    //It's not necessary to save any information about the obstacle other than the name, as it can be looked up in the obstacle list.
    private Element appendRunwayObstacleElements(Document document, Element runwayElement, LogicalRunway logicalRunway){
        //Check if there's an obstacle present on the runway.
        if(controller.getRunwayObstacle(logicalRunway.getName()) != ""){
            //Create an obstacleElement and add it to the given runwayElement.
            Element obstacleElement = document.createElement("RunwayObstacle");
            runwayElement.appendChild(obstacleElement);

            //Create an obstacleIdElement and add it to obstacleElement.
            Element obstacleIdElement = document.createElement("ObstacleName");
            obstacleElement.appendChild(obstacleIdElement);
            obstacleIdElement.appendChild(document.createTextNode(controller.getRunwayObstacle(logicalRunway.getName())));

            //Create an distanceFromEdgeElement and add it to obstacleElement.
            Element distanceFromEdgeElement = document.createElement("DistanceFromRunwayStart");
            obstacleElement.appendChild(distanceFromEdgeElement);
            distanceFromEdgeElement.appendChild(document.createTextNode(controller.getDistanceFromThreshold(logicalRunway.getName()).toString()));

            //Create an distanceFromCenterlineElement and add it to obstacleElement.
            Element distanceFromCenterlineElement = document.createElement("DistanceFromRunwayCenterline");
            obstacleElement.appendChild(distanceFromCenterlineElement);
            distanceFromCenterlineElement.appendChild(document.createTextNode(controller.getDistanceFromCenterline(logicalRunway.getName()).toString()));

            return obstacleElement;
        }
        return null;
    }

    //Adds relevant logical runway nodes to a given logicalRunwayElement. Returns nothing since the parent node must be given as a parameter...
    //... and so must already be created. It is done this way so the method can be reused for both logical runways.
    private void appendLogicalRunwayElements(Document document, Element logicalRunwayElement, LogicalRunway logicalRunway){
        //Create a displacedThresholdElement and add it to the given logicalRunwayElement
        Element displacedThresholdElement = document.createElement("DisplacedThreshold");
        logicalRunwayElement.appendChild(displacedThresholdElement);
        displacedThresholdElement.appendChild(document.createTextNode(logicalRunway.getThreshold().toString()));

        //Create a stopwayLength and add it to the given logicalRunwayElement.
        //Note, logicalRunway.getStopway().width refers to the length of the stopway.
        Element stopwayLength = document.createElement("StopwayLength");
        logicalRunwayElement.appendChild(stopwayLength);
        stopwayLength.appendChild(document.createTextNode(Integer.toString(logicalRunway.getStopway().width)));

        //Create a stopwayWidth and add it to the given logicalRunwayElement.
        //Note, logicalRunway.getStopway().height refers to the width of the stopway.
        Element stopwayWidth = document.createElement("StopwayWidth");
        logicalRunwayElement.appendChild(stopwayWidth);
        stopwayWidth.appendChild(document.createTextNode(Integer.toString(logicalRunway.getStopway().height)));

        //Create a clearwayLength and add it to the given logicalRunwayElement.
        //Note, logicalRunway.getClearway().width refers to the length of the clearway.
        Element clearwayLengthElement = document.createElement("ClearwayLength");
        logicalRunwayElement.appendChild(clearwayLengthElement);
        clearwayLengthElement.appendChild(document.createTextNode(Integer.toString(logicalRunway.getClearway().width)));

        //Create a clearwayWidth and add it to the given logicalRunwayElement.
        //Note, logicalRunway.getClearway().height refers to the width of the clearway.
        Element clearwayWidthElement = document.createElement("ClearwayWidth");
        logicalRunwayElement.appendChild(clearwayWidthElement);
        clearwayWidthElement.appendChild(document.createTextNode(Integer.toString(logicalRunway.getClearway().height)));
    }
}
