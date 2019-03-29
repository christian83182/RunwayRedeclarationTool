package uk.ac.soton.view;

import javax.swing.*;

/*
 * A class designed to act as a logger for notifications given by the program. It uses the singleton design pattern: the constructor
 * is private so an instance of the class cannot be created, but it contains a static member variable which is an instance of itself,
 * which can be accessed from a static context.
 */
public class NotificationLogger {

    //An instance of the class is created from a static context.
    public static NotificationLogger logger = new NotificationLogger();
    private StringBuilder log;
    private JLabel logLabel;

    //The constructor is private so an instance of the class cannot be made.
    private NotificationLogger(){
        this.log = new StringBuilder();
        this.logLabel = new JLabel("");
    }

    /**
     * Adds an entry to the log. Will automatically repaint any labels provided by this class.
     * @param notification
     */
    public void addToLog(String notification){
        log.append("- "+ notification + "\n");
        logLabel.setText(notification);
        logLabel.repaint();
    }

    /**
     * Returns the entire log as a string where each entry is separated by a newline character.
     * @return A string containing the entire log.
     */
    public String getAllLog(){
        return log.toString();
    }

    /**
     * @return The most recent entry in the log.
     */
    public String getLastLog(){
        return logLabel.getText();
    }

    /**
     * Returns an instance of an JLabel containing the most recent entry to the log. Every call to this method is guaranteed to return the
     * same object. The label will be edited and repainted by the class in the case that a new entry is made in the log.
     * @return A JLabel containing the most recent entry into the log.
     */
    public JLabel getLabel(){
        return logLabel;
    }


}
