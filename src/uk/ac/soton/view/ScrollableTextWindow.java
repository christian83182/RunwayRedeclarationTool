package uk.ac.soton.view;

import javax.swing.*;
import java.awt.*;

public class ScrollableTextWindow extends JFrame {

    ScrollableTextWindow(String windowContent, Dimension size){
        init(windowContent, size);
    }

    private void init(String windowContent, Dimension size){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setPreferredSize(size);
        this.setLayout(new GridBagLayout());

        JTextArea textArea = new JTextArea(windowContent);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollArea = new JScrollPane(textArea);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.fill = GridBagConstraints.BOTH; c.weightx = 1; c.weighty = 1;
        c.insets = new Insets(10,10,10,10);
        this.add(scrollArea,c);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose());
        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,10,10,10);
        this.add(closeButton,c);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
