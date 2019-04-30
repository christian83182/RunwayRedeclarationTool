package uk.ac.soton;

import javafx.application.Platform;
import uk.ac.soton.controller.AppController;
import uk.ac.soton.view.AppView;

public class Main {

    public static void main(String[] args) {
        Platform.setImplicitExit(false);

        AppView appView = new AppView("Runway Re-declaration Tool");
        AppController appController = new AppController(appView);

        appView.setController(appController);
        appView.init();
    }
}
