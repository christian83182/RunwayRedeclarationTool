package uk.ac.soton.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;


public class XMLExporter {

    public XMLExporter() {
    }

    public void saveAirfieldInfo(Airfield airfield, String absolutePath) {
        try {
            //Create the document object.
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //Create the airfield tag.
            Element airfields = document.createElement("airfield");
            document.appendChild(airfields);

            //Add airfield attributes.
            Element mad = document.createElement("maximumAngleOfDescent");
            mad.appendChild(document.createTextNode("50"));
            airfields.appendChild(mad);

            //Add all obstacles and relevant data.
            Element obstacles = document.createElement("obstacles");
            airfields.appendChild(obstacles);
            for (Map.Entry<String, Airfield.Dimensions> pair : airfield.getPredefinedObstacles().entrySet()) {

                Element obstacle = document.createElement("obstacle");
                obstacles.appendChild(obstacle);

                //Add the obstacle's name.
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(pair.getKey()));
                obstacle.appendChild(id);

                Element dimensions = document.createElement("dimensions");
                obstacle.appendChild(dimensions);

                Element length = document.createElement("length");
                Element width = document.createElement("width");
                Element height = document.createElement("height");

                //Add length, width and height
                length.appendChild(document.createTextNode(pair.getValue().getLength().toString()));
                dimensions.appendChild(length);
                width.appendChild(document.createTextNode(pair.getValue().getWidth().toString()));
                dimensions.appendChild(width);
                height.appendChild(document.createTextNode(pair.getValue().getHeight().toString()));
                dimensions.appendChild(height);
            }

            //Add runways and relevant data.
            Element runways = document.createElement("runways");
            airfields.appendChild(runways);
            for (int i = 0; i < airfield.getRunways().size(); i++) {
                Element runway = document.createElement("runway");
                runways.appendChild(runway);

                //Add the runway's id.
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(airfield.getRunways().get(i).getId()));
                runway.appendChild(id);

                //Add the runway's xPos.
                Element xPos = document.createElement("xPos");
                xPos.appendChild(document.createTextNode(airfield.getRunways().get(i).getxPos().toString()));
                runway.appendChild(xPos);

                //Add the yPos.
                Element yPos = document.createElement("yPos");
                yPos.appendChild(document.createTextNode(airfield.getRunways().get(i).getyPos().toString()));
                runway.appendChild(yPos);

                //Add the length.
                Element length = document.createElement("length");
                length.appendChild(document.createTextNode(airfield.getRunways().get(i).getLength().toString()));
                runway.appendChild(length);

                //Add the width
                Element width = document.createElement("width");
                width.appendChild(document.createTextNode(airfield.getRunways().get(i).getWidth().toString()));
                runway.appendChild(width);

                //Add the isActive property.
                Element isActive = document.createElement("isActive");
                isActive.appendChild(document.createTextNode(airfield.getRunways().get(i).isActive().toString()));
                runway.appendChild(isActive);

                //Add the status property
                Element status = document.createElement("status");
                status.appendChild(document.createTextNode(airfield.getRunways().get(i).getStatus()));
                runway.appendChild(status);

                //Add the als.
                Element als = document.createElement("als");
                als.appendChild(document.createTextNode(airfield.getRunways().get(i).getAls().toString()));
                runway.appendChild(als);

                //Add the resa
                Element resa = document.createElement("resa");
                resa.appendChild(document.createTextNode(airfield.getRunways().get(i).getResa().toString()));
                runway.appendChild(resa);

                //Add ths tripEnd.
                Element stripEnd = document.createElement("stripEnd");
                stripEnd.appendChild(document.createTextNode(airfield.getRunways().get(i).getStripEnd().toString()));
                runway.appendChild(stripEnd);

                //Add the stripWidth.
                Element stripWidth = document.createElement("stripWidth");
                stripWidth.appendChild(document.createTextNode(airfield.getRunways().get(i).getStripWidth().toString()));
                runway.appendChild(stripWidth);

                //Add the obstacle.
                Element obstacle = document.createElement("obstacle");
                obstacle.appendChild(document.createTextNode(airfield.getRunways().get(i).getObstacle().getId()));


                Element startDistance = document.createElement("startDistance");
                startDistance.appendChild(document.createTextNode(airfield.getRunways().get(i).getObstacle().getStartDistance().toString()));
                obstacle.appendChild(startDistance);

                Element centreLineDistance = document.createElement("centreLineDistance");
                centreLineDistance.appendChild(document.createTextNode(airfield.getRunways().get(i).getObstacle().getCentrelineDistance().toString()));
                obstacle.appendChild(centreLineDistance);

              /*  Element obstacleLength = document.createElement("length");
                obstacleLength.appendChild(document.createTextNode(airfield.getRunways().get(i).getObstacle().getLength().toString()));
                obstacle.appendChild(obstacleLength);

                Element obstacleWidth = document.createElement("width");
                obstacleWidth.appendChild(document.createTextNode(airfield.getRunways().get(i).getObstacle().getWidth().toString()));
                obstacle.appendChild(obstacleWidth);

                Element obstacleHeight = document.createElement("height");
                obstacleHeight.appendChild(document.createTextNode(airfield.getRunways().get(i).getObstacle().getHeight().toString()));
                obstacle.appendChild(obstacleHeight); */
                runway.appendChild(obstacle);








                //Add logical runways.
                LogicalRunway[] logicalRunways = airfield.getLogicalRunwaysOf(airfield.getRunways().get(i));
                if (logicalRunways != null) {
                    Element logicalRunwaysOf = document.createElement("LogicalRunways");
                    runway.appendChild(logicalRunwaysOf);

                    for (int j = 0; j < logicalRunways.length; j++) {
                        Element logicalRunway = document.createElement("logicalRunway");
                        Element name = document.createElement("name");
                        name.appendChild(document.createTextNode(logicalRunways[j].getName()));
                        logicalRunway.appendChild(name);

                        Element threshold = document.createElement("treshold");
                        threshold.appendChild(document.createTextNode(logicalRunways[j].getThreshold().toString()));
                        logicalRunway.appendChild(threshold);

                        Element clearway = document.createElement("clearway");
                        Element clearwayWidth = document.createElement("width");
                        double cWidth = logicalRunways[j].getClearway().getWidth();
                        clearwayWidth.appendChild(document.createTextNode(String.valueOf(cWidth)));
                        clearway.appendChild(clearwayWidth);

                        Element clearwayHeight = document.createElement("height");
                        double cHeight = logicalRunways[j].getClearway().getHeight();
                        clearwayHeight.appendChild(document.createTextNode(String.valueOf(cHeight)));
                        clearway.appendChild(clearwayHeight);
                        logicalRunway.appendChild(clearway);

                        Element stopway = document.createElement("stopway");
                        Element stopwayWidth = document.createElement("width");
                        double sWidth = logicalRunways[j].getStopway().getWidth();
                        stopwayWidth.appendChild(document.createTextNode(String.valueOf(sWidth)));
                        stopway.appendChild(stopwayWidth);

                        Element stopwayHeight = document.createElement("height");
                        double sHeight = logicalRunways[j].getStopway().getHeight();
                        stopwayHeight.appendChild(document.createTextNode(String.valueOf(sHeight)));
                        stopway.appendChild(stopwayHeight);
                        logicalRunway.appendChild(stopway);
                        runway.appendChild(logicalRunway);

                        Element object = document.createElement("object");

                        Element objectDistanceFromStart = document.createElement("objectDistanceFromStart");
                        objectDistanceFromStart.appendChild(document.createTextNode(logicalRunways[j].getGetObjectDistanceFromStart().toString()));
                        object.appendChild(objectDistanceFromStart);

                        Element objectDistanceFromCentre = document.createElement("objectDistanceCentreline");
                        objectDistanceFromCentre.appendChild(document.createTextNode(logicalRunways[j].getObjectDistanceFromCentreline().toString()));
                        object.appendChild(objectDistanceFromCentre);

                        logicalRunway.appendChild(object);
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(absolutePath+".xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}