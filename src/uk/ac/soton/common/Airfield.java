package uk.ac.soton.common;

import java.util.Set;

public class Airfield {

    private Set<Runway> runwaySet;

    public Set<Runway> getRunwaySet(){
        return  runwaySet;
    }

    public void addRunway(Runway newRunway){
        runwaySet.add(newRunway);
    }

    public void removeRunway(Runway oldRunway){
        runwaySet.remove(oldRunway);
    }
}
