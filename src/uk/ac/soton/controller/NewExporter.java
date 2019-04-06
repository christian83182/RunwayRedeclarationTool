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

    public void exportConfiguration(AppController controller, String absolutePath) throws ParserConfigurationException, TransformerException {
        Airfield airfield = controller.getAirfield();

        //Create the document object.
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        //Create the root Airfield node
        Element airfieldElement = document.createElement("Airfield");
        document.appendChild(airfieldElement);

        //Create and populate a BackgroundImage node if there is a background image
        if(controller.getBackgroundImage() != null){
            Point offset = controller.getBackgroundImageOffset();

            //Create the BackgroundImage node and add it to the Airfield node
            Element bgImageElement = document.createElement("Background_Image");
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
            xOffsetElement.appendChild(document.createTextNode(Double.toString(controller.getBackgroundRotation())));
            bgImageElement.appendChild(rotationElement);
        }

        //Add a node for the minimum angle of decent.
        //todo Implement MAoD into the application correctly.
        Element minAngOfDecElement = document.createElement("Minimum_Angle_Of_Decent");
        minAngOfDecElement.appendChild(document.createTextNode("50");
        airfieldElement.appendChild(minAngOfDecElement);

        //Add a node for the airport's blast distance policy.
        //todo correctly hook up the blast distance to this method.
        Element blastDistanceElement = document.createElement("Blast_Distance_Policy");
        blastDistanceElement.appendChild(document.createTextNode("300");
        airfieldElement.appendChild(blastDistanceElement);

        //Create a transformer which creates an output stream from the document. Use this to output the file to the given path.
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(absolutePath+".xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);

    }
}
