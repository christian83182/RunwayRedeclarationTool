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
    }


    private String name;

    private Parameter tora, toda, asda, lda;

    private Integer displacedThreshold;

    private Obstacle obstacle = null; // no obstacle present initially


    public LogicalRunway(String name, Number tora, Number toda, Number asda, Number lda, Integer displacedThreshold) {
        this.name = name;
        this.tora = new Parameter(tora);
        this.toda = new Parameter(toda);
        this.asda = new Parameter(asda);
        this.lda = new Parameter(lda);
        this.displacedThreshold = displacedThreshold;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
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

    public Integer getDisplacedThreshold() {
        return displacedThreshold;
    }

    public void setDisplacedThreshold(Integer displacedThreshold) {
        this.displacedThreshold = displacedThreshold;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

}

