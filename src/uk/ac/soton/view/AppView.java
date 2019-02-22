package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame{

    public AppView(String title){
        super(title);
    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(1280,720));
        this.pack();

        this.setVisible(true);
    }

}