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

            // Boeing 737
            put("Boeing 737-100", new Dimensions(28.65,28.35,11.23));
            put("Boeing 737-200", new Dimensions(30.53,28.35,11.23));
            put("Boeing 737-300", new Dimensions(33.40,28.88,11.13));
            put("Boeing 737-400", new Dimensions(36.45,28.88,11.13));
            put("Boeing 737-500", new Dimensions(31.01,28.88,11.13));
            put("Boeing 737-600", new Dimensions(31.20,34.32,12.6));
            put("Boeing 737-700", new Dimensions(33.6,34.32,12.6));
            put("Boeing 737-800", new Dimensions(39.5,34.32,12.6));
            put("Boeing 737-900", new Dimensions(42.1,34.32,12.6));

            // Boeing 747
            put("Boeing 747-100", new Dimensions(70.6,59.6,19.3));
            put("Boeing 747SP", new Dimensions(56.31,59.6,20.06));
            put("Boeing 747-200", new Dimensions(70.6,59.6,19.3));
            put("Boeing 747-300", new Dimensions(70.6,59.6,19.3));
            put("Boeing 747-400", new Dimensions(70.6,64.4,19.4));

            // Boeing 757
            put("Boeing 757-200", new Dimensions(47.3,38.05,13.56));
            put("Boeing 757-300", new Dimensions(54.47,38.05,13.56));

            // Boeing 767

            // Boeing 777
            put("Boeing 777-200", new Dimensions(63.7,60.9,18.5));
            put("Boeing 777-200LR", new Dimensions(63.7,64.8,18.6));
            put("Boeing 777F", new Dimensions(63.7,64.8,18.6));
            put("Boeing 777-300", new Dimensions(73.9,60.9,18.5));
            put("Boeing 777-300ER", new Dimensions(73.9,64.8,18.5));
            put("Boeing 777X-8", new Dimensions(69.8,71.8,19.5));
            put("Boeing 777X-9", new Dimensions(76.7,71.8,19.7));

            // Boeing 787
            put("Boeing 787-8", new Dimensions(56.7,60.1,16.9));
            put("Boeing 787-9", new Dimensions(62.8,60.1,17.02));
            put("Boeing 787-10", new Dimensions(68.3,60.1,17.02));

            // Bombardier
            put("Bombardier Dash 8-100", new Dimensions(22.25,25.89,7.49));
            put("Bombardier Dash 8-200", new Dimensions(22.25,25.89,7.49));
            put("Bombardier Dash 8-300", new Dimensions(25.68,27.43,7.49));
            put("Bombardier Dash 8-400", new Dimensions(32.81,28.4,8.3));

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

        // Can't add a runway with an already existing ID
        // The ID represents both directions, for example "09/27"
        for(int i = 0; i < runways.size(); i++){
            if(runways.get(i).getId().equals(newRunway.getId())){
                return false;
            }
        }

        runways.add(newRunway);
        return true;
    }

    // Returns the physical runway that contains the specified logical runway
    public Runway getRunway(String name){

        for(int i = 0; i < runways.size(); i++){

            if(runways.get(i).getId().contains(name)){

                LogicalRunway[] logicalRunways = runways.get(i).getLogicalRunways();

                for(int j = 0; j < 2; j++){

                    if(logicalRunways[j].getName().equals(name)){
                        return runways.get(i);
                    }
                }
            }
        }

        return null;
    }

    public void removeRunway(Runway oldRunway){
        runways.remove(oldRunway);
    }

    public ArrayList<LogicalRunway> getAllLogicalRunways(){

        ArrayList<LogicalRunway> logRunways = new ArrayList<LogicalRunway>();

        for(int i = 0; i < runways.size(); i++){
            LogicalRunway[] temp = runways.get(i).getLogicalRunways();
            logRunways.add(temp[0]);
            logRunways.add(temp[1]);
        }

        return logRunways;
    }

    public LogicalRunway[] getLogicalRunwaysOf(Runway runway){ return runway.getLogicalRunways(); }

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
