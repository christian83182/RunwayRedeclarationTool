package uk.ac.soton;

import javafx.application.Platform;
import uk.ac.soton.view.StartupScreen;

public class Main {

    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        new StartupScreen();
    }
}
