package uk.ac.soton.controller;

import uk.ac.soton.common.Airfield;
import uk.ac.soton.common.LogicalRunway;
import uk.ac.soton.view.AppView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Saver {
    private AppView appView;
    private AppController appController;
    private Airfield airfield;

    public Saver(AppController controller, AppView appView){
        this.appController = controller;
        this.appView = appView;
        this.airfield = controller.getAirfield();
    }

    public void saveState(String filename) throws IOException {
        File file = new File(filename);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        if(appView.getSelectedRunway().equals("")){
            for(LogicalRunway runway : airfield.getAllLogicalRunways()){
                writer.println("Runway " + runway.getName() + "\n" +
                "ASDA: " + appController.getASDAOriginal(runway.getName()) + "\n" +
                "TORA: " + appController.getTORAOriginal(runway.getName()) + "\n" +
                "TODA: " + appController.getTODAOriginal(runway.getName()) + "\n" +
                "LDA: " + appController.getLDAOriginal(runway.getName()) + "\n" + "" + "\n" +
                "Redeclared ASDA: " + appController.getASDARedeclared(runway.getName()) + "\n" +
                "Redeclared TORA: " + appController.getTORARedeclared(runway.getName()) + "\n" +
                "Redeclared TODA: " + appController.getTODARedeclared(runway.getName()) + "\n" +
                "Redeclared LDA: " + appController.getLDARedeclared(runway.getName()) + "\n" + "");
            }
            writer.close();
        } else {
            String selected = appView.getSelectedRunway();
            writer.println("Runway " + selected + "\n" +
            "ASDA: " + appController.getASDAOriginal(selected) + "\n" +
            "TORA: " + appController.getTORAOriginal(selected) + "\n" +
            "TODA: " + appController.getTODAOriginal(selected) + "\n" +
            "LDA: " + appController.getLDAOriginal(selected) + "\n" + "" + "\n" +
            "Redeclared ASDA: " + appController.getASDARedeclared(selected) + "\n" +
            "Redeclared TORA: " + appController.getTORARedeclared(selected) + "\n" +
            "Redeclared TODA: " + appController.getTODARedeclared(selected) + "\n" +
            "Redeclared LDA: " + appController.getLDARedeclared(selected));
            writer.close();
        }
    }
}
