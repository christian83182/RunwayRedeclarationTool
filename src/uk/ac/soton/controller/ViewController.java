package uk.ac.soton.controller;

import org.xml.sax.SAXException;
import uk.ac.soton.common.LogicalRunway;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
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
     * Returns a String explaining the calculation process of the current TORA value.
     * @param runwayId the runway's name.
     * @return the TORA breakdown.
     */
    String getTORABreakdown(String runwayId);

    /**
     * Returns a String explaining the calculation process of the current TODA value.
     * @param runwayId the runway's name.
     * @return the TODA breakdown.
     */
    String getTODABreakdown(String runwayId);

    /**
     * Returns a String explaining the calculation process of the current ASDA value.
     * @param runwayId the runway's name.
     * @return the ASDA breakdown.
     */
    String getASDABreakdown(String runwayId);

    /**
     * Returns a String explaining the calculation process of the current LDA value.
     * @param runwayId the runway's name.
     * @return the LDA breakdown.
     */
    String getLDABreakdown(String runwayId);

    /**
     * Returns the original TORA value.
     * @param runwayId the runway's name.
     * @return original TORA.
     */
    Integer getTORAOriginal(String runwayId);

    /**
     * Returns the redeclared TORA value.
     * @param runwayId the runway's name.
     * @return redeclared TORA.
     */
    Integer getTORARedeclared(String runwayId);

    /**
     * Returns the original TODA value.
     * @param runwayId the runway's name.
     * @return original TODA.
     */
    Integer getTODAOriginal(String runwayId);

    /**
     * Returns the redeclared TODA value.
     * @param runwayId the runway's name.
     * @return redeclared TODA.
     */
    Integer getTODARedeclared(String runwayId);

    /**
     * Returns the original ASDA value.
     * @param runwayId the runway's name.
     * @return original ASDA.
     */
    Integer getASDAOriginal(String runwayId);

    /**
     * Returns the redeclared ASDA value.
     * @param runwayId the runway's name.
     * @return redeclared ASDA.
     */
    Integer getASDARedeclared(String runwayId);

    /**
     * Returns the original LDA value.
     * @param runwayId the runway's name.
     * @return original LDA.
     */
    Integer getLDAOriginal(String runwayId);

    /**
     * Returns the redeclared LDA value.
     * @param runwayId the runway's name.
     * @return redeclared LDA.
     */
    Integer getLDARedeclared(String runwayId);

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
    void addObstacleToList(String id, Double length, Double width, Double height);

    /**
     * Deletes a specified object from the list of predefined obstacles.
     * @param obstacleId the obstacle to be deleted from the list.
     */
    void deleteObstacleFromList(String obstacleId);

    /**
     * Places an obstacle on a runway. This is more complicated than it might originally seem. RunwayId specifies the ID of the
     * logical runway on which to place the obstacle. The distance from the centerline and distance from edge are measured in respect to
     * the logical runway given. The distance from the centerline refers to the offset from the centerline to the obstacle where a
     * positive number will imply the object is above the centerline, and vice versa. The distance from the start of the given logical runway
     * to the obstacle. A small number implies it is near the start and hence were planes will land, and vice versa.
     *
     * This method will need to find the instance of Runway to which the specified logical runway is part of, and add the obstacle to both
     * in physically the same place. Hence the distance from the centerline will invert and the distance from the edge be (width - distance
     * from the edge) for the second logical runway.
     *
     * @param runwayId the name of the logical runway to which to add the obstacle.
     * @param obstacleId the name of the obstacle to be added.
     * @param distanceFromCenterline the distance from the centerline to where the obstacle will be.
     * @param distanceFromEdge the distance from the start of the runway to where the obstacle will be.
     */
    void addObstacleToRunway(String runwayId, String obstacleId, Integer distanceFromCenterline, Integer distanceFromEdge);

    /**
     * Removes any obstacles which are currently on the specified runway. This method must also remove the obstacle from the other logical
     * runway belonging to the same physical runway.
     * @param runwayId the logical runway from which to remove the obstacle.
     */
    void removeObstacleFromRunway(String runwayId);

    /**
     * Returns the name of any obstacle on the specified runway.
     * @param runwayId The name of the runway.
     * @return The name of the obstacle on the runway. Return the empty string if no obstacle has been placed on it.
     */
    String getRunwayObstacle(String runwayId);

    /**
     * Returns the distance from the centerline of the object on the given runway.
     * @param runwayId the runway on which the object is placed.
     * @return The distance of the object from the centerline.
     */
    Integer getDistanceFromCenterline(String runwayId);

    /**
     * Returns the distance from the threshold to the object on the given runway.
     * @param runwayId The runway on which the object is placed.
     * @return The distance from the threshold to the obstacle.
     */
    Integer getDistanceFromThreshold(String runwayId);

    /**
     * Exports the current model as an XML file to the location specified.
     * @param absolutePath The full path of the file being exported, including the name of the file.
     */
    void exportAirfieldConfiguration(String absolutePath);

    /**
     * Imports an XML file describing a configuration of the airfield and sets it as the current model.
     * @param path The location of the XML file specified by the user.
     */
    void importAirfieldConfiguration(String path) throws ImporterException, ParserConfigurationException, SAXException, IOException;

    /**
     * Getting the logical runway on which the obstacle (if present) is closer to its respective threshold.
     * Returns the same logical runway if that is the one on which the obstacle is closer to its threshold and
     * or the sibling logical runway if the reverse is true. It returns null if there is no obstacle on the specified
     * logical runway.
     * @param runwayID The logicalRunway on which the obstacle is present.
     */
    LogicalRunway getLogicalRunwayCloserToObstacle(String runwayID);

    /**
     * @return The name which should be displayed as the airport name.
     */
    String getAirfieldName();

}

