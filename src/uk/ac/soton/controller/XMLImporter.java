package uk.ac.soton.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.soton.common.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLImporter {
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Map<String, Airfield.Dimensions> predefinedObstacles = new HashMap<>();
    private ArrayList<Runway> runwayList = new ArrayList<>();
    private List<Dimension> clearways = new ArrayList<>();
    private List<Dimension> stopways = new ArrayList<>();
    private List<LogicalRunway> logicalRunways = new ArrayList<>();
    private String name = null;
    private Integer threshold = null;
    private Integer length;
    private Airfield airfield;

    public Airfield importAirfieldInfo(String filename) throws ParserConfigurationException, IOException, SAXException {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();
        airfield = new Airfield();

        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xml);
        doc.getDocumentElement().normalize();

        importObstacles(filename);
        importRunways(filename);
        importLogicalRunways(filename);

        airfield.setPredefinedObstacles(predefinedObstacles);
        for (Runway runway : runwayList) {
            airfield.addRunway(runway);
        }

        for (Runway currentRunway : airfield.getRunways()){
            String firstLogicalRunwayID = currentRunway.getId().split("/")[0];
            LogicalRunway firstLogicalRunway = null;
            String secondLogicalRunwayID = currentRunway.getId().split("/")[1];
            LogicalRunway secondLogicalRunway = null;
            for(LogicalRunway currentLogicalRunway : logicalRunways){
                if(currentLogicalRunway.getName().equals(firstLogicalRunwayID)){
                    firstLogicalRunway = currentLogicalRunway;
                } else if (currentLogicalRunway.getName().equals(secondLogicalRunwayID)){
                    secondLogicalRunway = currentLogicalRunway;
                }
            }
            currentRunway.setLogicalRunways(firstLogicalRunway, secondLogicalRunway);
        }
        return airfield;
    }

    private void importRunways(String filename) {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("runway");

            for (int i = 0; i < nodeList.getLength(); i++) {
                runwayList.add(getRunway(nodeList.item(i),filename));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private Runway getRunway(Node node, String filename){
        Runway runway = new Runway();
        LogicalRunway logicalRunway1;
        LogicalRunway logicalRunway2;
        importDimension(filename);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            int width = Integer.parseInt(getTagValue("width", element));
            int als = Integer.parseInt(getTagValue("als", element));
            int resa = Integer.parseInt(getTagValue("resa", element));
            int stripEnd = Integer.parseInt(getTagValue("stripEnd", element));
            int stripWidth = Integer.parseInt(getTagValue("stripWidth", element));
            length = Integer.parseInt(getTagValue("length", element));

            if (length > 0 && width>0 && als>0 && resa>0 && stripEnd>0 && stripWidth>0) {
                runway.setId(getTagValue("id", element));
                runway.setxPos(Integer.parseInt(getTagValue("xPos", element)));
                runway.setyPos(Integer.parseInt(getTagValue("yPos", element)));
                runway.setLength(length);
                runway.setWidth(width);
                runway.setActive(Boolean.parseBoolean(getTagValue("isActive", element)));
                runway.setAls(als);
                runway.setResa(resa);
                runway.setStripEnd(stripEnd);
                runway.setStripWidth(stripWidth);
            } else {
                throw new IllegalArgumentException("Runway measurements can not be negative");
            }

        }
        return runway;
    }

    private void getLogicalRunwayData(Node node, String filename){
        importDimension(filename);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            name = getTagValue("name",element);
            threshold = Integer.parseInt(getTagValue("treshold", element));
        }
    }

    private void importLogicalRunways(String filename) {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();
        importDimension(filename);

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("logicalRunway");

            for (int i = 0; i < nodeList.getLength(); i++) {
                getLogicalRunwayData(nodeList.item(i), filename);
                if(threshold > 0 && length > 0) {
                    logicalRunways.add(new LogicalRunway(name, length, threshold, clearways.get(i), stopways.get(i)));
                }else {
                    throw new IllegalArgumentException("Logical Runway measurements can not be negative");
                }
            }


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void importDimension(String filename) {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList clearwayNodeList = doc.getElementsByTagName("clearway");
            NodeList stopwayNodeList = doc.getElementsByTagName("stopway");

            for (int i = 0; i < clearwayNodeList.getLength(); i++) {
                clearways.add(getDimension(clearwayNodeList.item(i)));
                stopways.add(getDimension(stopwayNodeList.item(i)));
            }

        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private Dimension getDimension(Node node){
        Dimension dimension = null;

        if(node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;
            double width = Double.parseDouble(getTagValue("width",element));
            double height = Double.parseDouble(getTagValue("height",element));

            if(width > 0 && height > 0) {
                dimension = new Dimension((int) width, (int) height);
            }else {
                throw new IllegalArgumentException("Clearway and Stopway measurements can not be negative");
            }

        }
        return dimension;
    }

    private void importObstacles(String filename) {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("obstacle");

            List<PredefinedObstacle> obstacleList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                obstacleList.add(getObstacle(nodeList.item(i)));
            }

            for(PredefinedObstacle obstacle : obstacleList){
                predefinedObstacles.put(obstacle.id, new Airfield.Dimensions(obstacle.length,obstacle.width,obstacle.height));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private PredefinedObstacle getObstacle(Node node) {
        PredefinedObstacle obstacle = new PredefinedObstacle();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String id = getTagValue("id", element);
            Double length = Double.parseDouble(getTagValue("length", element));
            Double height = Double.parseDouble(getTagValue("height", element));
            Double width = Double.parseDouble(getTagValue("width", element));

            if(length > 0 && height > 0 && width > 0){
                obstacle.id = id;
                obstacle.length = length;
                obstacle.height = height;
                obstacle.width = width;
            } else {
                throw new IllegalArgumentException("Obstacle measurements can not be negative");
            }
        }
        return obstacle;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    class PredefinedObstacle {
        public String id;
        public Double length = 0.0;
        public Double width = 0.0;
        public Double height = 0.0;

       /* public String toString() {
            return "Obstacle :: id=" + this.id + " Height=" + this.height + " Width=" + this.width +
                    " Length=" + this.length;
        }
        */
    }
}