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
import java.io.File;
import java.util.Map;


public class XMLSave {
    private Airfield airfield;

    public XMLSave(Airfield airfield) {
        this.airfield = airfield;
    }

    public static final String obstacleList = "./ObstacleList.xml";
    public static final String runwayList = "./RunwayList.xml";

    public Airfield getAirfield() {
        return airfield;
    }

    public void setAirfield(Airfield newAirfield) {
        this.airfield = newAirfield;
    }

    public void saveObjectList() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element obstacles = document.createElement("obstacles");
            document.appendChild(obstacles);
            for (Map.Entry<String, Airfield.Dimensions> pair : airfield.getPredefinedObstacles().entrySet()) {
                Element obstacle = document.createElement("obstacle");
                obstacles.appendChild(obstacle);
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(pair.getKey()));
                obstacle.appendChild(id);
                Element dimensions = document.createElement("dimensions");
                obstacle.appendChild(dimensions);
                Element length = document.createElement("length");
                Element width = document.createElement("width");
                Element height = document.createElement("height");
                length.appendChild(document.createTextNode(pair.getValue().getLength().toString()));
                dimensions.appendChild(length);
                width.appendChild(document.createTextNode(pair.getValue().getWidth().toString()));
                dimensions.appendChild(width);
                height.appendChild(document.createTextNode(pair.getValue().getHeight().toString()));
                dimensions.appendChild(height);
            }


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(obstacleList));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveRunways(){
        try{
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element runways = document.createElement("runways");
            document.appendChild(runways);
            for(int i = 0; i<airfield.getRunways().size(); i++){
                Element runway = document.createElement("runway");
                runways.appendChild(runway);
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(airfield.getRunways().get(i).getId()));
                runway.appendChild(id);
                Element xPos = document.createElement("xPos");
                xPos.appendChild(document.createTextNode(airfield.getRunways().get(i).getxPos().toString()));
                runway.appendChild(xPos);
                Element yPos = document.createElement("yPos");
                yPos.appendChild(document.createTextNode(airfield.getRunways().get(i).getyPos().toString()));
                runway.appendChild(yPos);
                Element length = document.createElement("length");
                length.appendChild(document.createTextNode(airfield.getRunways().get(i).getLength().toString()));
                runway.appendChild(length);
                Element width = document.createElement("width");
                width.appendChild(document.createTextNode(airfield.getRunways().get(i).getWidth().toString()));
                runway.appendChild(width);
                Element isActive = document.createElement("isActive");
                isActive.appendChild(document.createTextNode(airfield.getRunways().get(i).isActive().toString()));
                runway.appendChild(isActive);
                Element status = document.createElement("status");
                status.appendChild(document.createTextNode(airfield.getRunways().get(i).getStatus()));
                runway.appendChild(status);
                Element als = document.createElement("als");
                als.appendChild(document.createTextNode(airfield.getRunways().get(i).getAls().toString()));
                runway.appendChild(als);
                Element resa = document.createElement("resa");
                resa.appendChild(document.createTextNode(airfield.getRunways().get(i).getResa().toString()));
                runway.appendChild(resa);
                Element stripEnd = document.createElement("stripEnd");
                stripEnd.appendChild(document.createTextNode(airfield.getRunways().get(i).getStripEnd().toString()));
                runway.appendChild(stripEnd);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(runwayList));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}
