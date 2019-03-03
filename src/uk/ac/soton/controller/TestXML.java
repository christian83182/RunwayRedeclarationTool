package uk.ac.soton.controller;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.Runway;

public class TestXML {
    public static void main(String[] args) {
        Airfield a1 = new Airfield();
        XMLSave xmlSave = new XMLSave(a1);
        xmlSave.saveObjectList();
        Runway test = new Runway("Test",12,13,14,15);
        a1.addRunway(test);
        xmlSave.saveRunways();

    }
}
