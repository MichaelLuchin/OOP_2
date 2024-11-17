package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function F1;
    private Function F2;

    public Sum (Function f1, Function f2) {
        F1 = f1;
        F2 = f2;
    }

    public double getFunctionValue(double x) {
        return F1.getFunctionValue(x) + F2.getFunctionValue(x);
    }

    public double getRightDomainBorder() {
        return Double.min(F2.getRightDomainBorder(), F1.getRightDomainBorder());
    }

    public double getLeftDomainBorder() {
        return Double.max(F2.getLeftDomainBorder(), F1.getLeftDomainBorder());
    }
}