package uk.ac.soton.common;

public class LogicalRunway{

    private class Parameter{

        private Integer original;
        private Integer redeclared;

        private Parameter(Integer original){
            this.original = original;
        }

        public Integer getOriginalValue() {
            return original;
        }

        public void setOriginalValue(Integer aDefault) {
            this.original = aDefault;
        }

        public Integer getRedeclaredValue() {
            return redeclared;
        }

        public void setRedeclaredValue(Integer redeclared) {
            this.redeclared = redeclared;
        }
    }


    private String name;

    private Parameter tora, toda, asda, lda;

    private Integer displacedThreshold;


    public LogicalRunway(String name, Integer tora, Integer toda, Integer asda, Integer lda, Integer displacedThreshold) {
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

}

