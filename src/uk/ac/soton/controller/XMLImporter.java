package uk.ac.soton.controller;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.Runway;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class XMLImporter {
    private Airfield airfield = new Airfield();

    public XMLImporter(Airfield airfield) {
        this.airfield = airfield;
    }

    public Airfield getAirfield() {
        return airfield;
    }

    public void setAirfield(Airfield airfield) {
        this.airfield = airfield;
    }

    public void importFromXMLFileObjectList() {
        try {
            FileInputStream fis = new FileInputStream(new File("./Objects.xml"));
            XMLDecoder decoder = new XMLDecoder(fis);
          /*  for(Map.Entry<String, Airfield.Dimensions> pair : airfield.getPredefinedObstacles().entrySet()){
                encoder.writeObject(pair.getKey());
                //length
                encoder.writeObject(pair.getValue().getLength());
                //height
                encoder.writeObject(pair.getValue().getHeight());
                //width
                encoder.writeObject(pair.getValue().getWidth());
            }
            encoder.close();
            fosa.close(); */
            ArrayList<Runway> newRunways = (ArrayList<Runway>) decoder.readObject();
            airfield.setRunways(newRunways);
            decoder.close();
            fis.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
