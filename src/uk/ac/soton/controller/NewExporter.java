package uk.ac.soton.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.soton.common.Airfield;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;

public class NewExporter {

    AppController controller;
    Airfield airfield;

    NewExporter(AppController controller){
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

        appendAirfieldProperties(document, airfieldElement);
        appendBackgroundImageElements(document, airfieldElement);
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
        //Create the BackgroundImage node and add it to the Airfield node
        Element bgImageElement = document.createElement("BackgroundImage");
        airfieldElement.appendChild(bgImageElement);

        //If the application is used a background image, then populate the node, otherwise leave it empty.
        if(controller.getBackgroundImage() != null){
            Point offset = controller.getBackgroundImageOffset();

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
        }
        return bgImageElement;
    }

    //Appends a new node 'DefinedObstacles' containing data about all defined obstacles, to the airfieldElementNode.
    //Returns the DefinedObstacles element.
    private Element appendPredefinedObstacleElements(Document document, Element airfieldElement){
        //Create a DefinedObstacles node and add it to airfieldElement.
        Element predefinedObstaclesElement = document.createElement("DefinedObstacles");
        airfieldElement.appendChild(predefinedObstaclesElement);

        //Iterate over all defined obstacles and add their data to the predefinedObstaclesElement node.
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
}
