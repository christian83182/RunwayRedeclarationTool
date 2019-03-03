package uk.ac.soton.common;

import java.awt.*;

public class LogicalRunway{

    public class Parameter{

        private Number original = null;
        private Number redeclared = null;

        private Parameter(Number original){
            this.original = original;
        }

        public Number getOriginalValue() {
            return original;
        }

        public void setOriginalValue(Number aDefault) {
            this.original = aDefault;
        }

        public Number getRedeclaredValue() {
            return redeclared;
        }

        public void setRedeclaredValue(Number redeclared) {
            this.redeclared = redeclared;
        }

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

    private Parameter tora, toda, asda, lda;

    private Number threshold = 0;
    private Dimension stopway = new Dimension(0,0);
    private Dimension clearway = new Dimension(0,0);

    private Obstacle obstacle = null; // no obstacle present initially

    /**
     * Constructor for the logical runway associated with a physical one.
     * @param name Identifier of the logical runway, for example "09" or "09R".
     * @param length Length of the runway.
     * @param threshold Threshold of the logical runway.
     * @param clearway The width parameter of the dimension if the length of the clearway and the height is the width.
     * @param stopway The width parameter of the dimension if the length of the clearway and the height is the width
     *                (the width of the stopway can be assumed to be the same as the width of the runway).
     */
    public LogicalRunway(String name, Number length, Integer threshold, Dimension clearway, Dimension stopway) {
        this.name = name;
        this.threshold = threshold;
        this.clearway = clearway;
        this.stopway = stopway;
        this.tora = new Parameter(length);
        this.toda = new Parameter(length.intValue() + clearway.width);
        this.asda = new Parameter(length.intValue() + stopway.width);
        this.lda = new Parameter(length.intValue() - threshold.intValue());
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

    public void revertParameters() {
        this.tora.setRedeclaredValue(null);
        this.toda.setRedeclaredValue(null);
        this.asda.setRedeclaredValue(null);
        this.lda.setRedeclaredValue(null);
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

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public void clearObstacle(){
        this.obstacle = null;
        this.revertParameters();
    }

}

