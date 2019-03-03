package uk.ac.soton.common;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Airfield {

    private ArrayList<Runway> runways = new ArrayList<Runway>();
    private Map<String,Dimensions> predefinedObstacles = new HashMap<String,Dimensions>(){
        {
            // Airbus 320 series
            put("Airbus A318", new Dimensions(31.44,34.1,12.51));
            put("Airbus A319", new Dimensions(33.84,34.1,11.76));
            put("Airbus A320", new Dimensions(37.57,34.1,11.76));
            put("Airbus A321", new Dimensions(44.51,34.1,11.76));

            // Airbus 330 series
            put("Airbus A330-200ceo", new Dimensions(58.82,60.3,17.39));
            put("Airbus A330-200F", new Dimensions(58.82,60.3,16.90));
            put("Airbus A330-300ceo", new Dimensions(63.69,60.3,16.83));

            // Airbus 340 series
            put("Airbus A340-200", new Dimensions(59.39,60.3,16.7));
            put("Airbus A340-300", new Dimensions(63.6,60.3,16.85));
            put("Airbus A340-500", new Dimensions(67.9,63.45,17.1));
            put("Airbus A340-600", new Dimensions(75.3,63.45,17.3));

            // Airbus 350 series
            put("Airbus A350-800", new Dimensions(60.54,64.75,17.1));
            put("Airbus A350-900", new Dimensions(66.89,64.75,17.1));
            put("Airbus A350-1000", new Dimensions(73.88,64.75,17.1));

            // Airbus 380 series
            put("Airbus A380", new Dimensions(73.0,79.8,24.1));

            // ATR
            put("ATR-42", new Dimensions(22.67,24.57,7.59));
            put("ATR-72", new Dimensions(27.17,27.05,7.65));

            // Boeing 717
            put("Boeing 717", new Dimensions(37.8,28.47,8.92));

            // Boeing 727
            put("Boeing 727-100", new Dimensions(40.59,32.92,10.52));
            put("Boeing 727-200", new Dimensions(46.69,32.92,10.52));

            //TODO put other aircraft models

            // Ground support equipment
            put("Dolly without cargo", new Dimensions(3.18,2.44,0.5));
            put("Dolly with cargo", new Dimensions(3.18,2.44,2.5));
            //put("Hydrant pit cleaner");
            //put("Hydrant dispenser");
            //put("45,000 litre refueller");
            //put("18,000 litre refueller");
            //put("12,000 litre refueller");
            //put("4,500 litre refueller");
        }
    };

    public ArrayList<Runway> getRunways(){
        return runways;
    }
    public void setRunways(ArrayList<Runway> newRunways){
        this.runways = newRunways;
    }

    public Boolean addRunway(Runway newRunway){

        // Can't add a runway with an already existing name
        for(Runway r : runways){
            if(r.getName().equals(newRunway.getName())){
                return false;
            }
        }

        runways.add(newRunway);
        return true;
    }

    public Runway getRunway(String name){

        for(Runway runway : runways){

            if(runway.getName().equals(name)){
                return runway;
            }
        }

        return null;
    }

    public void removeRunway(Runway oldRunway){
        runways.remove(oldRunway);
    }

    public Map<String, Dimensions> getPredefinedObstacles() { return predefinedObstacles; }

    public void setPredefinedObstacles(Map<String, Dimensions> predefinedObstacles) { this.predefinedObstacles = predefinedObstacles; }

    public void defineNewObstacle(String type, Double length, Double width, Double height){
        predefinedObstacles.put(type, new Dimensions(length, width, height));
    }

    public void removePredefinedObstacle(String type){ predefinedObstacles.remove(type); }

    public void redefineObstacle(String type, Double length, Double width, Double height){
        Dimensions dimensions = predefinedObstacles.get(type);
        dimensions.setLength(length);
        dimensions.setWidth(width);
        dimensions.setHeight(height);
    }

    public class Dimensions {

        private Double length = 0.0;
        private Double width = 0.0;
        private Double height = 0.0;

        public Dimensions(Double length, Double width, Double height) {
            this.length = length;
            this.width = width;
            this.height = height;
        }

        public Double getLength() { return length; }

        public void setLength(Double length) { this.length = length; }

        public Double getWidth() { return width; }

        public void setWidth(Double width) { this.width = width; }

        public Double getHeight() { return height; }

        public void setHeight(Double height) { this.height = height; }
    }
}
