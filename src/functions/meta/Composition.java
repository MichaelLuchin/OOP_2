package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function F1;
    private Function F2;

    public Composition(Function f1, Function f2) {
        F1 = f1;
        F2 = f2;
    }

    public double getFunctionValue(double x) {
        return F1.getFunctionValue(F2.getFunctionValue(x));
    }

    public double getRightDomainBorder() {
        return F1.getRightDomainBorder();
    }

    public double getLeftDomainBorder() {
        return F1.getLeftDomainBorder();
    }
}