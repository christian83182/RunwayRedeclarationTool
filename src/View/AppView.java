package View;

import javax.swing.*;
import java.awt.*;

//View portion of the Model-View-Controller design pattern.
public class AppView extends JFrame{

    public AppView(String title){
        super(title);
    }

    //Properly initializes and displays the window.
    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topView = new JPanel();
        this.setPreferredSize(new Dimension(1280,720));
        this.pack();

        this.setVisible(true);
    }

}