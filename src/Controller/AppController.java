package Controller;

import View.AppView;

//Controller portion of the Model-View-Controller design pattern.
public class AppController {

    //The controller's instance of the application's view.
    private AppView appView;

    public AppController(AppView appView){
        this.appView = appView;
    }
}
