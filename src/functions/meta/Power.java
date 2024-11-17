package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function F1;
    private double power;

    public Power (Function f1, double power) {
        F1 = f1;
        this.power = power;
    }

    public double getFunctionValue(double x) {
        return Math.pow(F1.getFunctionValue(x), power);
    }

    public double getRightDomainBorder() {
        return F1.getRightDomainBorder();
    }

    public double getLeftDomainBorder() {
        return F1.getLeftDomainBorder();
    }
}