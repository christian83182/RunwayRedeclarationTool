package uk.ac.soton.controller;


import uk.ac.soton.common.*;
import uk.ac.soton.view.AppView;

public class AppController {

    //The controller's instance of the application's view.
    private AppView appView;
    //The configurer used to change the configuration of the model.
    private Configurer appConfigurer;
    //The model which the controller would interact with
    private Airfield airfield;

    public AppController(AppView appView){
        this.appView = appView;
        this.appConfigurer = new Configurer();
        this.airfield = new Airfield();

        appView.init();
    }
}
