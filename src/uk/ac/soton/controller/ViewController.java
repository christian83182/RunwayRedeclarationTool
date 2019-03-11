package uk.ac.soton.controller;

import java.awt.*;
import java.util.Set;

public interface ViewController {

    /**
     * Returns a set of Strings where each string is the name of a runway. Such as 07, or 11L. The first two digits are
     * always assumed to be the bearing.
     * @return A set of Strings.
     */
    Set<String> getRunways();

    /**
     * Returns the position of a runway asa Point object where the x position refers to the leftmost side of the runway,
     * and the y position refers to the center of the runway.
     * @param runwayId The name of the runway for which the position should be returned.
     * @return The position of the specified runway.
     */
    Point getRunwayPos(String runwayId);

    /**
     * Returns the size of the runway as a Dimension object where the width property describes the length of the runway,
     * and the height property describes the width of the runway.
     * @param runwayId The name of the runway for which the Dimension should be returned.
     * @return The dimensions of the specified runway.
     */
    Dimension getRunwayDim(String runwayId);

    /**
     * Returns the bearing in degrees for a given runway's name. It should be noted that a runway with bearing 00 points North.
     * Hence runway 09 would point East, and runway 27 would point West.
     * @param runwayId The runway's name.
     * @return The bearing of the runway in degrees.
     */
    Integer getBearing(String runwayId);

    /**
     * Returns a Dimension object representing the dimensions of the stopway for a given runway. The width property
     * describes the length of the stopway, and the height property describes the width of the stopway. It can be assumed
     * that the stopway will have the same width as the specified runway.
     * @param runwayId The runway's name.
     * @return The Dimensions of the runway's stopway.
     */
    Dimension getStopwayDim(String runwayId);

    /**
     * Returns a Dimension object representing the dimensions of the clearway for a given runway. The width property
     * describes the length of the stopway, and the height property describes the width of the stopway. It should not be assumed that the
     * clearway is the same width as the runway's width.
     * @param runwayId The runway's name.
     * @return The Dimensions of the runway's clearway.
     */
    Dimension getClearwayDim(String runwayId);

    /**
     *Returns the perpendicular distance from the centerline to the edge of the runway strip.
     * This will be half of the runway strip's total width.
     * @param runwayId The runway's name.
     * @return The distance from the centerline to the edge.
     */
    Integer getStripWidthFromCenterline(String runwayId);

    /**
     *Returns the size of the strip end for a given runway. This is the distance from the leftmost side of the runway
     * strip to the start of the runway, or from the end of the runway to the rightmost side of the runway strip.
     * @param runwayId The runway's name.
     * @return The size of the strip end.
     */
    Integer getStripEndSize(String runwayId);

    /**
     * Returns the Take-Off Run Available for a given runway.
     * @param runwayId the runway's name.
     * @return the runway's TORA.
     */
    Integer getRunwayTORA(String runwayId);

    /**
     * Returns the Take-Off Distance Available for a given runway.
     * @param runwayId the runway's name.
     * @return the runway's TODA.
     */
    Integer getRunwayTODA(String runwayId);

    /**
     * Returns the Accelerate-Stop Distance Available for a given runway.
     * @param runwayId the runway's name.
     * @return the runway's ASDA.
     */
    Integer getRunwayASDA(String runwayId);

    /**
     * Returns the Landing Distance Available for a given runway.
     * @param runwayId the runway's name.
     * @return the runway's LDA.
     */
    Integer getRunwayLDA(String runwayId);

    /**
     * Returns the Threshold for a given runway. The threshold should be 0 unless it is displaced.
     * @param runwayId The runway's name.
     * @return the runway's threshold.
     */
    Integer getRunwayThreshold(String runwayId);

    /**
     * Returns the distance from the start of the the runway to the start of the TORA.
     * @param runwayId the runway's name.
     * @return the TORA offset.
     */
    Integer getTORAOffset(String runwayId);

    /**
     * Returns the distance from the start of the the runway to the start of the TODA.
     * @param runwayId the runway's name.
     * @return the TODA offset.
     */
    Integer getTODAOffset(String runwayId);

    /**
     * Returns the distance from the start of the the runway to the start of the ASDA.
     * @param runwayId the runway's name.
     * @return the ASDA offset.
     */
    Integer getASDAOffset(String runwayId);

    /**
     * Returns the distance from the start of the the runway to the start of the LDA.
     * @param runwayId the runway's name.
     * @return the LDA offset.
     */
    Integer getLDAOffset(String runwayId);

    /**
     * Returns a set of strings where each string is the unique ID of a predefined obstacle in the application.
     * @return The set of Obstacle IDs
     */
    Set<String> getPredefinedObstacleIds();

    /**
     * Returns the width of a predefined obstacle given its ID.
     * @param obstacleId the ID of the obstacle.
     * @return the width of the obstacle.
     */
    Double getPredefinedObstacleWidth(String obstacleId);

    /**
     * Returns the height of a predefined obstacle given its ID.
     * @param obstacleId the ID of the obstacle.
     * @return the height of the obstacle.
     */
    Double getPredefinedObstacleHeight(String obstacleId);

    /**
     * Returns the width of a predefined obstacle given its ID.
     * @param obstacleId the ID of the obstacle.
     * @return the height of the obstacle.
     */
    Double getPredefinedObstacleLength(String obstacleId);


    /**
     * Adds an obstacle to the list of predefined obstacles
     * @param id the name of the new obstacle.
     * @param length the length of the new obstacle.
     * @param width the width of the new obstacle.
     * @param height the height of the new obstacle.
     */
    //void addObstacleToList(String id, Double length, Double width, Double height);

    /**
     * Deletes a specified object from the list of predefined obstacles.
     * @param obstacleId the obstacle to be deleted from the list.
     */
    void deleteObstacleFromList(String obstacleId);

    /**
     * Exports the current model as an XML file to the location specified.
     * @param absolutePath The full path of the file being exported, including the name of the file.
     */
    void exportAirfieldConfiguration(String absolutePath);

    /**
     * Imports an XML file describing a configuration of the airfield and sets it as the current model.
     * @param path The location of the XML file specified by the user.
     */
    void importAirfieldConfiguration(String path);
}

