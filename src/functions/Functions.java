package functions;

import functions.meta.*;

public class Functions {
    private static final Functions INSTANCE = new Functions();

    private Functions() {} // Приватный конструктор, чтобы не создавать обьекты данного класса вне класа

    public static Functions getInstance() {
        return INSTANCE;
    }

    public static Function shift(Function f, double shiftX, double shiftY){
        Shift F = new Shift(f, shiftX, shiftY);
        return F;
    }
    public static Function scale(Function f, double scaleX, double scaleY){
        Scale F = new Scale(f, scaleX, scaleY);
        return F;
    }
    public static Function power(Function f, double power){
        Power F = new Power(f, power);
        return F;
    }
    public static Function sum(Function f1, Function f2){
        Sum F = new Sum(f1, f2);
        return F;
    }
    public static Function mult(Function f1, Function f2){
        Mult F = new Mult(f1, f2);
        return F;
    }
    public static Function composition(Function f1, Function f2){
        Composition F = new Composition(f1, f2);
        return F;
    }
    public static double integral(Function f, double minX, double maxX, double dClock)
    {
        if(minX < f.getLeftDomainBorder() || maxX > f.getRightDomainBorder())
        {
            throw new IllegalArgumentException("Cannot create TabulatedFunction: Left or right border out of the function's domain of definition");
        }
        double summ = 0;
        double curX = minX;
        while (curX+dClock <= maxX)
        {
            summ += dClock * ((f.getFunctionValue(curX) + f.getFunctionValue(curX+=dClock)) / 2);
        }
        summ+=(maxX - curX) * ((f.getFunctionValue(curX) + f.getFunctionValue(maxX)) / 2);
        return summ;
    }
}
