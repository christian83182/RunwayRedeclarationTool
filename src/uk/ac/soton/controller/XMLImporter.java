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
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

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

    public Airfield importAirfieldInfo(String filename) throws ParserConfigurationException, IOException, SAXException, ImporterException {
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


    private void importRunways(String filename) throws ParserConfigurationException, IOException, SAXException, ImporterException {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();

        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xml);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("runway");
        for (int i = 0; i < nodeList.getLength(); i++) {
            runwayList.add(getRunway(nodeList.item(i),filename));
        }
    }

    private Runway getRunway(Node node, String filename) throws ImporterException {
        Runway runway = new Runway();
        importDimension(filename);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            Pattern pattern = Pattern.compile("([0-9]{2}[A-Z]?)/[0-9]{2}[A-Z]?");

            if (getTagValue("id",element) == null){
                throw new ImporterException("Error: Runway <id> is null");

            } else if (getTagValue("id",element).matches(String.valueOf(pattern)) == false){
                throw new ImporterException("Error: Runway <id> not the right format");

            } else { runway.setId(getTagValue("id", element)); }
              runway.setxPos(Integer.parseInt(getTagValue("xPos", element)));
              runway.setyPos(Integer.parseInt(getTagValue("yPos", element)));

            if (Integer.parseInt(getTagValue("length",element)) <= 0) {
                throw new ImporterException("Error: Runway <length> is '0' or negative");

            } else { length = Integer.parseInt(getTagValue("length", element));
                runway.setLength(length); }

            if (Integer.parseInt(getTagValue("width",element)) <= 0) {
                throw new ImporterException("Error: Runway <width> is '0' or negative");

            } else {runway.setWidth(Integer.parseInt(getTagValue("width", element)));}

            runway.setActive(Boolean.parseBoolean(getTagValue("isActive", element)));

            if(Integer.parseInt(getTagValue("als",element)) <= 0) {
                throw new ImporterException("Error: Runway <als> is '0' or negative");

            } else { runway.setAls(Integer.parseInt(getTagValue("als", element))); }

            if (Integer.parseInt(getTagValue("resa",element)) <= 0) {
                throw new ImporterException("Error: Runway <resa> is '0' or negative");

            } else { runway.setResa(Integer.parseInt(getTagValue("resa", element))); }

            if (Integer.parseInt(getTagValue("stripEnd",element)) <= 0){
                throw new ImporterException("Error: Runway <stripEnd> is '0' or negative");

            } else {runway.setStripEnd(Integer.parseInt(getTagValue("stripEnd",element)));}

            if (Integer.parseInt(getTagValue("stripWidth", element)) <= 0) {
                throw new ImporterException("Error: Runway <stripWidth> is '0' or negative");

            } else {runway.setStripWidth(Integer.parseInt(getTagValue("stripWidth",element))); }
        }
        return runway;
    }

    private void getLogicalRunwayData(Node node, String filename) {
        importDimension(filename);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            name = getTagValue("name",element);
            threshold = Integer.parseInt(getTagValue("treshold", element));
        }
    }

    private void importLogicalRunways(String filename) throws ImporterException, ParserConfigurationException, IOException, SAXException {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();
        importDimension(filename);

        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xml);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("logicalRunway");

        for (int i = 0; i < nodeList.getLength(); i++) {
            getLogicalRunwayData(nodeList.item(i), filename);
            Pattern pattern = Pattern.compile("[0-9]{2}[A-Z]?");
            if (name == null) {
                throw new ImporterException("Error: Logical Runway <name> is null");

            } else if (name.matches(String.valueOf(pattern)) == false){
                throw new ImporterException("Error: Logical Runway <name> is not the right format");

            } else if (length <= 0) {
                throw new ImporterException("Error: Logical Runway <length> is either '0' or negative");

            } else if (threshold <= 0) {
                throw new ImporterException("Error: Logical Runway <threshold> is either '0' or negative");

            } else {
                logicalRunways.add(new LogicalRunway(name, length, threshold, clearways.get(i), stopways.get(i)));
            }
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
        } catch (ImporterException importerException) {
            importerException.printStackTrace();
        }
    }

    private Dimension getDimension(Node node) throws ImporterException {
        Dimension dimension = null;

        if(node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;
            if (Double.parseDouble((getTagValue("width",element))) <= 0 || Double.parseDouble(getTagValue("height",element)) <= 0){
                throw new ImporterException("Error: <width> or <height> Dimensions are either '0' or negative");

            } else {
                dimension = new Dimension((int)Double.parseDouble(getTagValue("width",element)),
                        (int) Double.parseDouble(getTagValue("height",element)));
            }
        }
        return dimension;
    }

    private void importObstacles(String filename) throws ParserConfigurationException, IOException, SAXException, ImporterException {
        File xml = new File(filename);
        dbFactory = DocumentBuilderFactory.newInstance();

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
    }

    private PredefinedObstacle getObstacle(Node node) throws ImporterException {
        PredefinedObstacle obstacle = new PredefinedObstacle();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (obstacle.id == null){
                throw new ImporterException("Error: the obstacle <id> is null.");
            } else {
                obstacle.id = getTagValue("id", element);
            }

            if (obstacle.length <= 0) {
                throw new ImporterException("Error: the obstacle <length> is either '0' or negative.");
            } else {
                obstacle.length = Double.parseDouble(getTagValue("length", element));
            }

            if (obstacle.height <= 0) {
                throw new ImporterException("Error: the obstacle <height> is either '0' or negative.");
            } else {
                obstacle.height = Double.parseDouble(getTagValue("height", element));
            }

            if (obstacle.width <= 0) {
                throw new ImporterException("Error: the obstacle <width> is either '0' or negative.");
            } else {
                obstacle.height = Double.parseDouble(getTagValue("width", element));
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

        public String toString() {
            return "Obstacle :: id=" + this.id + " Height=" + this.height + " Width=" + this.width +
                    " Length=" + this.length;
        }
    }
}