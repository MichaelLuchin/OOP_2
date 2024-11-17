package functions.basic;

import functions.Function;

public class Log implements Function {

    private double base;

    public Log (double base){
        this.base = base;
    }

    public double getFunctionValue(double x) {
        return Math.log(x)/Math.log(base);
    }

    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}