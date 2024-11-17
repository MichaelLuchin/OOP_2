package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function F1;
    private double ScaleX;
    private double ScaleY;

    public Scale(Function F1, double ScaleX, double ScaleY) {
        this.F1 = F1;
        this.ScaleX = ScaleX;
        this.ScaleY = ScaleY;
    }

    public double getFunctionValue(double x) {
        return ScaleY * getFunctionValue(F1.getFunctionValue(x / ScaleX));
    }

    public double getLeftDomainBorder() {
        return F1.getLeftDomainBorder() - Math.abs(F1.getLeftDomainBorder()) * (ScaleX - 1);
    }

    public double getRightDomainBorder() {
        return F1.getRightDomainBorder() + Math.abs(F1.getRightDomainBorder()) * (ScaleX - 1);
    }
}