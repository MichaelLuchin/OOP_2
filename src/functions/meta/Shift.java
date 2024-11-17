package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function F1;
    private double ShiftX;
    private double ShiftY;

    public Shift(Function F1, double ScaleX, double ScaleY) {
        this.F1 = F1;
        this.ShiftX = ScaleX;
        this.ShiftY = ScaleY;
    }

    public double getFunctionValue(double x) {
        return ShiftY + getFunctionValue(F1.getFunctionValue(x - ShiftX));
    }

    public double getLeftDomainBorder() {
        return F1.getLeftDomainBorder() + ShiftX;
    }

    public double getRightDomainBorder() {
        return F1.getRightDomainBorder() + ShiftX;
    }
}