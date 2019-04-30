package uk.ac.soton.common;

import java.awt.*;
import java.io.Serializable;

public class LogicalRunway implements Serializable {

    /**
     * A parameter of a logical runway that can change due to re-declaration.
     */
    public class Parameter implements Serializable{

        private Number original = null;
        private Number redeclared = null;
        private String definition = "";
        private String breakdown = "";

        /**
         * Constructor for a re-declarable parameter.
         * @param original The original/default value.
         * @param definition Breakdown of the original value according to definition.
         */
        private Parameter(Number original, String definition){
            this.original = original;
            this.definition = definition;
            this.breakdown = definition;
        }

        public Number getOriginalValue() {
            return original;
        }

        public void setOriginalValue(Number aDefault) {
            this.original = aDefault;
        }

        public String getDefinition() {
            return definition;
        }

        public Number getRedeclaredValue() {
            return redeclared;
        }

        public void setRedeclaredValue(Number redeclared) {

            // Resets the breakdown to the definition.
            if(redeclared == null){
                breakdown = definition;
            }

            this.redeclared = redeclared;
        }

        public String getBreakdown() {
            return breakdown;
        }

        public void setBreakdown(String breakdown) {
            this.breakdown = breakdown;
        }

        /**
         * Gets the currently active value for the parameter (redeclared or original).
         * @return Current value.
         */
        public Number getCurrentValue() {

            if(redeclared != null){
                return getRedeclaredValue();
            }
            else{
                return getOriginalValue();
            }
        }
    }

    private String name = "";

    // Take-Off Run Available
    private Parameter tora = null;
    // Take-Off Distance Available
    private Parameter toda = null;
    // Accelerate-Stop Distance Available
    private Parameter asda = null;
    // Landing Distance Available
    private Parameter lda = null;
    private Number threshold = 0;
    private Dimension stopway = new Dimension(0,0);
    private Dimension clearway = new Dimension(0,0);
    private Number objectDistanceFromThreshold = null;
    private Number getObjectDistanceFromStart = null;
    private Number objectDistanceFromCentreline = null;

    private Integer obstacleOffset = 0;


    /**
     * Constructor for the logical runway associated with a physical one.
     * @param name Identifier of the logical runway, for example "09" or "09R".
     * @param tora Usually the length of the runway, can be different due to taxiways.
     * @param threshold Threshold of the logical runway.
     * @param clearway The width parameter of the dimension if the length of the clearway and the height is the width.
     * @param stopway The width parameter of the dimension if the length of the clearway and the height is the width
     *                (the width of the stopway can be assumed to be the same as the width of the runway).
     */
    public LogicalRunway(String name, Number tora, Integer threshold, Dimension clearway, Dimension stopway) {
        this.name = name;
        this.threshold = threshold;
        this.clearway = clearway;
        this.stopway = stopway;
        initParams(tora);
    }

    public void initParams(Number tora){

        String toraDef = "TORA = " + tora.intValue();
        this.tora = new Parameter(tora, toraDef);

        Integer toda = tora.intValue() + clearway.width;
        String todaDef = "TODA = TORA + Clearway\n\n" + "TODA = " + tora.intValue() + " + " + clearway.width +
                " = " + toda;
        this.toda = new Parameter(toda, todaDef);

        Integer asda = tora.intValue() + stopway.width;
        String asdaDef = "ASDA = TORA + Stopway\n\n" + "ASDA = " + tora.intValue() + " + " + stopway.width +
                " = " + asda;
        this.asda = new Parameter(asda, asdaDef);

        Integer lda = tora.intValue() - threshold.intValue();
        String ldaDef = "LDA = TORA - Displaced Threshold\n\n" + "LDA = " + tora.intValue() + " - " + threshold +
                " = " + lda;
        this.lda = new Parameter(lda, ldaDef);
    }

    public Integer getObstacleOffset() {
        return obstacleOffset;
    }

    public void setObstacleOffset(Integer obstacleOffset) {
        this.obstacleOffset = obstacleOffset;
    }

    public void setObjectDistances(Integer distanceFromEdge, Integer distanceFromCentreline, Integer originalDistance){
        this.objectDistanceFromCentreline = distanceFromCentreline;
        this.getObjectDistanceFromStart = distanceFromEdge;
        this.objectDistanceFromThreshold = originalDistance - getThreshold().intValue();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Dimension getStopway() {
        return stopway;
    }

    public Dimension getClearway() {
        return clearway;
    }

    public void redeclareTora(Number tora) {
        this.tora.setRedeclaredValue(tora);
    }

    public void redeclareToda(Number toda) {
        this.toda.setRedeclaredValue(toda);
    }

    public void redeclareAsda(Number asda) {
        this.asda.setRedeclaredValue(asda);
    }

    public void redeclareLda(Number lda) {
        this.lda.setRedeclaredValue(lda);
    }

    /**
     * Resets all the parameters to their default values.
     */
    public void revertParameters() {
        tora.setRedeclaredValue(null);
        toda.setRedeclaredValue(null);
        asda.setRedeclaredValue(null);
        lda.setRedeclaredValue(null);
    }

    public Parameter getTora() {
        return tora;
    }

    public Parameter getToda() {
        return toda;
    }

    public Parameter getAsda() {
        return asda;
    }

    public Parameter getLda() {
        return lda;
    }

    public Number getThreshold() {
        return threshold;
    }

    public void setThreshold(Number displacedThreshold) { this.threshold = displacedThreshold; }

    public Number getObjectThresholdDistance(){
        return objectDistanceFromThreshold;
    }

    public Number getGetObjectDistanceFromStart() {
        return getObjectDistanceFromStart;
    }

    public Number getObjectDistanceFromCentreline() {
        return objectDistanceFromCentreline;
    }

}

