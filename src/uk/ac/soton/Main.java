package uk.ac.soton;

import uk.ac.soton.controller.AppController;
import uk.ac.soton.view.AppView;
import uk.ac.soton.view.DebugModel;

public class Main {

    public static void main(String[] args) {
        AppView appView = new AppView("Runway Re-declaration Tool");
        AppController appController = new AppController(appView);

        DebugModel debugModel = new DebugModel();
        debugModel.populateModel();

        appView.setController(appController);
        appView.init();
    }
}
