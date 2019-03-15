package uk.ac.soton.controller;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;
import uk.ac.soton.common.Runway;

import javax.sql.rowset.spi.XmlReader;
import java.awt.*;

public class TestXML {

    public static void main(String[] args) {
      /*  XMLImporter xmlImporter = new XMLImporter();
        xmlImporter.importAirfieldInfo("AirfieldInfo.xml");

        Airfield airfield;
        airfield = xmlImporter.getAirfield();

        XMLSave xmlSave = new XMLSave(airfield);
        xmlSave.saveAirfieldInfo();

        /*
        Airfield a1 = new Airfield();
        XMLSave xmlSave = new XMLSave(a1);
        xmlSave.saveObjectList();
        Runway test = new Runway("Test",12,13,14,15);
        a1.addRunway(test);
        xmlSave.saveRunways();

      Airfield airfieldBristol = new Airfield();
      XMLExporter xmlBristol = new XMLExporter();

      Runway runwayBristol = new Runway("27/06",-10,5,2011,45,45,27);

      Dimension clearway27 = new Dimension(45,89);
      Dimension stopway27 = new Dimension(45,608);
      LogicalRunway takeoff = new LogicalRunway("27",2011, 27,clearway27,stopway27);

      Dimension clearway06 = new Dimension(45,608);
      Dimension stopway06 = new Dimension(45,89);
      LogicalRunway landing = new LogicalRunway("06",2011,06,clearway06,stopway06);

      runwayBristol.setLogicalRunways(takeoff,landing);

      airfieldBristol.addRunway(runwayBristol);
      xmlBristol.saveAirfieldInfo(airfieldBristol,"./Bristol");

      Airfield airfieldHeathrow = new Airfield();
      Runway southernRunway = new Runway("09R/27L",0,0,3658,45,45,50);
      Runway northernRunway = new Runway("09L/27R",0,0,3902,45,45,50);

      LogicalRunway southTakeoff = new LogicalRunway("09R",3658,3658,clearway27,stopway27);
      LogicalRunway southLanding = new LogicalRunway("27L",3658,0,clearway06,stopway06);
      southernRunway.setLogicalRunways(southTakeoff,southLanding);

      LogicalRunway northTakeoff = new LogicalRunway("09L",3902,3902,clearway27,stopway27);
      LogicalRunway northLanding = new LogicalRunway("27R",3902,0,clearway06,stopway06);
      northernRunway.setLogicalRunways(northTakeoff,northLanding);

      airfieldHeathrow.addRunway(northernRunway);
      airfieldBristol.addRunway(southernRunway);
      xmlBristol.saveAirfieldInfo(airfieldHeathrow,"./Heathrow");
 */

    }
}
