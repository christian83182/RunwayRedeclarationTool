package uk.ac.soton.common;

public class LogicalRunway{

    public class Parameter{

        private Number original;
        private Number redeclared;

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

    private String name;

    private Parameter tora, toda, asda, lda;

    private Integer threshold = 0;
    private Number stopway;
    private Number clearway;

    private Obstacle obstacle = null; // no obstacle present initially

    public LogicalRunway(String name, Number length, Integer threshold, Number clearway, Number stopway) {
        this.name = name;
        this.threshold = threshold;
        this.clearway = clearway;
        this.stopway = stopway;
        this.tora = new Parameter(length);
        this.toda = new Parameter(length.intValue() + clearway.intValue());
        this.asda = new Parameter(length.intValue() + stopway.intValue());
        this.lda = new Parameter(length.intValue() - threshold.intValue());
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Number getStopway() {
        return stopway;
    }

    public Number getClearway() {
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

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer displacedThreshold) { this.threshold = displacedThreshold; }

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

