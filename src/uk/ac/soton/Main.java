package uk.ac.soton;

import uk.ac.soton.controller.AppController;
import uk.ac.soton.view.AppView;

public class Main {

    public static void main(String[] args) {
        AppView appView = new AppView("Runway Re-declaration Tool");
        AppController appController = new AppController(appView);
        appView.init();
    }
}
