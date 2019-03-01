package uk.ac.soton.controller;

import uk.ac.soton.common.Airfield;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

public class XMLSaver {
    private Airfield airfield;
    public XMLSaver (Airfield airfield){
        this.airfield = airfield;
    }

    public Airfield getAirfield() {
        return airfield;
    }

    public void setAirfield(Airfield airfield) {
        this.airfield = airfield;
    }

    public void saveToXMLFileAirfield(){
        try{
            FileOutputStream fosa = new FileOutputStream(new File("./Airfiled.xml"));
            XMLEncoder encoder = new XMLEncoder(fosa);
            for(Map.Entry<String, Airfield.Dimensions> pair : airfield.getPredefinedObstacles().entrySet()){
                encoder.writeObject("Object: " + pair.getValue() + "\n Height: " + pair.getValue().getHeight() + "\n Length: " + pair.getValue().getLength() + "\n Width: " +pair.getValue().getWidth());
            }
            encoder.close();
            fosa.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void saveToXMLFileRunways(){
        try{
            FileOutputStream fos = new FileOutputStream(new File("./Runways.xml"));
            XMLEncoder encoder = new XMLEncoder(fos);
            for(int i = 0; i<airfield.getRunways().size();i++){
                encoder.writeObject(airfield.getRunways().get(i));
              //  if(airfield.getRunways().get(i).obstaclePresent()){
               //     encoder.writeObject(airfield.getRunways().get(i).getObstacle());
            //    }
            }
            encoder.close();
            fos.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    }




