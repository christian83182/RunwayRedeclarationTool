package uk.ac.soton.controller;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.Runway;

public class TestXML {
    public static void main(String[] args) {
        Airfield a1 = new Airfield();
        XMLSaver xmlSaver = new XMLSaver(a1);
        xmlSaver.saveToXMLFileObjectList();
        Runway run1 = new Runway("Runway",12,13,5,2);
        run1.setName("Runwaylol");
        Runway run2 = new Runway("Runway2",13,14,6,3);
        a1.addRunway(run1);
        a1.addRunway(run2);
        Airfield.Dimensions dimensions = a1.new Dimensions(30.0,20.0,10.0);
        a1.getRunway("Runwaylol").placeObstacle("Boeing 717",20,10,20,dimensions );
        //a1.addRunway(run1);
        xmlSaver.saveToXMLFileRunways();

    }
}
