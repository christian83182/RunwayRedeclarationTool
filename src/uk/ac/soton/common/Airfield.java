package uk.ac.soton.common;

import java.io.*;
import java.util.*;

public class Airfield implements Serializable {

    private ArrayList<Runway> runways = new ArrayList<>();
    private String airfieldName;
    private static Integer blastProtection = 300;
    private static Integer minAngleOfDecent = 50;

    public Airfield(String airfieldName){
        this.airfieldName = airfieldName;
    }

    public Airfield(){
        this("Unnamed Airfield");
    }

    private Map<String,Dimensions> predefinedObstacles = new HashMap<String,Dimensions>(){
        {
            // Airbus 320 series
            put("Airbus A318", new Dimensions(31,34,12));
            put("Airbus A319", new Dimensions(33,34,11));
            put("Airbus A320", new Dimensions(37,34,11));
            put("Airbus A321", new Dimensions(44,34,11));

            // Airbus 330 series
            put("Airbus A330-200ceo", new Dimensions(58,60,17));
            put("Airbus A330-200F", new Dimensions(58,60,16));
            put("Airbus A330-300ceo", new Dimensions(63,60,16));

            // Airbus 340 series
            put("Airbus A340-200", new Dimensions(59,60,16));
            put("Airbus A340-300", new Dimensions(63,60,16));
            put("Airbus A340-500", new Dimensions(67,63,17));
            put("Airbus A340-600", new Dimensions(75,63,17));

            // Airbus 350 series
            put("Airbus A350-800", new Dimensions(60,64,17));
            put("Airbus A350-900", new Dimensions(66,64,17));
            put("Airbus A350-1000", new Dimensions(73,64,17));

            // Airbus 380 series
            put("Airbus A380", new Dimensions(73,79,24));

            // ATR
            put("ATR-42", new Dimensions(22,24,7));
            put("ATR-72", new Dimensions(27,27,7));

            // Boeing 717
            put("Boeing 717", new Dimensions(37,28,8));

            // Boeing 727
            put("Boeing 727-100", new Dimensions(40,32,10));
            put("Boeing 727-200", new Dimensions(46,32,10));

            // Boeing 737
            put("Boeing 737-100", new Dimensions(28,28,11));
            put("Boeing 737-200", new Dimensions(30,28,11));
            put("Boeing 737-300", new Dimensions(33,28,11));
            put("Boeing 737-400", new Dimensions(36,28,11));
            put("Boeing 737-500", new Dimensions(31,28, 11));
            put("Boeing 737-600", new Dimensions(31,34,12));
            put("Boeing 737-700", new Dimensions(33,34,12));
            put("Boeing 737-800", new Dimensions(39,34,12));
            put("Boeing 737-900", new Dimensions(42,34,12));

            // Boeing 747
            put("Boeing 747-100", new Dimensions(70,59,19));
            put("Boeing 747SP", new Dimensions(56,59,20));
            put("Boeing 747-200", new Dimensions(70,59,19));
            put("Boeing 747-300", new Dimensions(70,59,19));
            put("Boeing 747-400", new Dimensions(70,64,19));

            // Boeing 757
            put("Boeing 757-200", new Dimensions(47,38,13));
            put("Boeing 757-300", new Dimensions(54,38,13));

            // Boeing 767
            put("Boeing 767-200", new Dimensions(47,47,15));
            put("Boeing 767-300", new Dimensions(54,47,16));
            put("Boeing 767-400", new Dimensions(61,51,16));

            // Boeing 777
            put("Boeing 777-200", new Dimensions(63,60,18));
            put("Boeing 777-200LR", new Dimensions(63,64,18));
            put("Boeing 777F", new Dimensions(63,64,18));
            put("Boeing 777-300", new Dimensions(73,60,18));
            put("Boeing 777-300ER", new Dimensions(73,64,18));
            put("Boeing 777X-8", new Dimensions(69,71,19));
            put("Boeing 777X-9", new Dimensions(76,71,19));

            // Boeing 787
            put("Boeing 787-8", new Dimensions(56,60,16));
            put("Boeing 787-9", new Dimensions(62,60,17));
            put("Boeing 787-10", new Dimensions(68,60,17));

            // Bombardier
            put("Bombardier Dash 8-100", new Dimensions(22,25,7));
            put("Bombardier Dash 8-200", new Dimensions(22,25,7));
            put("Bombardier Dash 8-300", new Dimensions(25,27,7));
            put("Bombardier Dash 8-400", new Dimensions(32,28,8));

            // Embraer E Jet
            put("ERJ 170", new Dimensions(29,26,9));
            put("ERJ 170-100", new Dimensions(29,26,9));
            put("ERJ 170-200", new Dimensions(31,26,9));
            put("ERJ 175", new Dimensions(31,26,9));
            put("ERJ 190", new Dimensions(36,28,10));
            put("ERJ 190-100", new Dimensions(36,28,10));
            put("ERJ 190-200", new Dimensions(38,28,10));
            put("ERJ 195", new Dimensions(38,28,10));

            // McDonnell Douglas
            put("DC-10-10", new Dimensions(55,47,17));
            put("DC-10-30", new Dimensions(55,50,17));
            put("DC-10-40", new Dimensions(55,50,17));

            // Misc
            put("Comac C919", new Dimensions(38,35,11));
            put("Concorde Plane", new Dimensions(61,25,12));

            // Ground support equipment
            put("Dolly without cargo", new Dimensions(3,2,2));
            put("Dolly with cargo", new Dimensions(3,2,2));
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

            String[] ids = runways.get(i).getId().split("/");

            if(ids[0].equals(name) || ids[1].equals(name)){

                return runways.get(i);
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

            // The if statement is for testing only, will be removed later when all runways have 2 log ones
            if(temp[1] != null){
                logRunways.add(temp[1]);
            }
        }

        return logRunways;
    }

    public LogicalRunway[] getLogicalRunwaysOf(Runway runway){ return runway.getLogicalRunways(); }

    public Map<String, Dimensions> getPredefinedObstacles() { return predefinedObstacles; }

    public void setPredefinedObstacles(Map<String, Dimensions> predefinedObstacles) { this.predefinedObstacles = predefinedObstacles; }

    public void defineNewObstacle(String type, Integer length, Integer width, Integer height){
        predefinedObstacles.put(type, new Dimensions(length, width, height));
    }

    public void removePredefinedObstacle(String type){ predefinedObstacles.remove(type); }

    public void redefineObstacle(String type, Integer length, Integer width, Integer height){
        Dimensions dimensions = predefinedObstacles.get(type);
        dimensions.setLength(length);
        dimensions.setWidth(width);
        dimensions.setHeight(height);
    }

    public String getName() {
        return airfieldName;
    }

    public void setName(String id) {
        this.airfieldName = id;
    }

    public static Integer getBlastProtection() {
        return blastProtection;
    }

    public static void setBlastProtection(Integer blast) {
        blastProtection = blast;
    }

    public static Integer getMinAngleOfDecent(){
        return minAngleOfDecent;
    }

    public static void setMinAngleOfDecent(Integer newMinAngle){
        Airfield.minAngleOfDecent = newMinAngle;
    }

    public Object deepClone() {
        try{
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(this);
            ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
            ObjectInputStream objectInput = new ObjectInputStream(byteInput);
            return objectInput.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static class Dimensions implements Serializable{

        private Integer length = 0;
        private Integer width = 0;
        private Integer height = 0;

        public Dimensions(Integer length, Integer width, Integer height) {
            this.length = length;
            this.width = width;
            this.height = height;
        }

        public Integer getLength() { return length; }

        public void setLength(Integer length) { this.length = length; }

        public Integer getWidth() { return width; }

        public void setWidth(Integer width) { this.width = width; }

        public Integer getHeight() { return height; }

        public void setHeight(Integer height) { this.height = height; }
    }
}
