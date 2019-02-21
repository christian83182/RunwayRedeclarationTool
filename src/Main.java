import Controller.AppController;
import View.AppView;

public class Main {

    public static void main(String[] args) {
        AppView appView = new AppView("Runway Re-declaration Tool");
        AppController appController = new AppController(appView);
        appView.init();
    }
}
