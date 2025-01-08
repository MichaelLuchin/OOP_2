package functions;

import java.io.*;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {

    private static final TabulatedFunctions INSTANCE = new TabulatedFunctions();
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {} // Приватный конструктор, чтобы не создавать обьекты данного класса вне класа

    public static TabulatedFunctions getInstance() {
        return INSTANCE;
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException("The specified boundaries for tabulation exceed the domain of the function definition");
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX)/pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            values[i] = function.getFunctionValue(leftX + step * i);
        }
        return createTabulatedFunction(leftX,rightX,pointsCount);
    }
    public static TabulatedFunction tabulate(Class<?extends TabulatedFunction> TBClass, Function function, double leftX, double rightX, int pointsCount)
    {
        if(leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
        {
            throw new IllegalArgumentException("Cannot create TabulatedFunction: Left or right border out of the function's domain of definition");
        }
        double stepX = (rightX - leftX)/(pointsCount - 1);
        double curX = leftX;
        FunctionPoint[] arr = new FunctionPoint[pointsCount];

        for(int i = 0; i < pointsCount; i++)
        {
            curX = leftX + stepX*i;
            arr[i] = new FunctionPoint(curX, function.getFunctionValue(curX)) ;
        }
        return createTabulatedFunction(TBClass, arr);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException { //  Используем конструкцию try-with-resources, чтобы автоматически закрыть потоки и избежать утечек ресурсов.
        try(DataOutputStream outstr = new DataOutputStream(out)) {

            int PoitsCount = function.getPointsCount();

            outstr.write(PoitsCount);
            outstr.writeDouble(function.getLeftDomainBorder());
            outstr.writeDouble(function.getRightDomainBorder());
            for (int i = 0; i < PoitsCount; i++) {
                outstr.writeDouble(function.getPointX(i));
                outstr.writeDouble(function.getPointY(i)); //Возможно нужно выводить между точками перевод на новую строку, а между координатами нужно ставить пробел
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in){
        TabulatedFunction InpTabFunc = null;

        try(DataInputStream instr = new DataInputStream(in)) { //Возможно нужно выводить между точками перевод на новую строку, а между координатами нужно ставить пробел
            int PointsCount = instr.readInt();
            double leftX = instr.readDouble();
            double rightX = instr.readDouble();
            InpTabFunc = createTabulatedFunction(leftX,rightX,PointsCount);
            for (int i = 0; i < PointsCount; i++) {
                InpTabFunc.setPointX(i, instr.readDouble());
                InpTabFunc.setPointY(i, instr.readDouble());
            }
        }
        catch (IOException | InappropriateFunctionPointException e){
            e.printStackTrace();
        }

        return InpTabFunc;
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        try(PrintWriter outstr = new PrintWriter(new BufferedWriter(out))) {

            int PoitsCount = function.getPointsCount();

            outstr.println(PoitsCount);
            outstr.println(function.getLeftDomainBorder());
            outstr.println(function.getRightDomainBorder());
            for (int i = 0; i < PoitsCount; i++) {
                outstr.println(function.getPointX(i));
                outstr.println(function.getPointY(i));
            }
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) {
        TabulatedFunction InpTabFunc = null;

        try{
            StreamTokenizer instr = new StreamTokenizer(in);
            instr.nextToken();
            int PointsCount = (int) instr.nval;
            instr.nextToken();
            double leftX = instr.nval;
            instr.nextToken();
            double rightX = instr.nval;
            InpTabFunc = createTabulatedFunction(leftX,rightX,PointsCount);
            for (int i = 0; instr.nextToken() != StreamTokenizer.TT_EOF; i++) {
                InpTabFunc.setPointX(i,instr.nval);
                instr.nextToken();
                InpTabFunc.setPointY(i,instr.nval);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException(e);
        }
        return InpTabFunc;
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory f)
    {
        factory = f;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount)
    {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values)
    {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] arr)
    {
        return factory.createTabulatedFunction(arr);
    }

    public static TabulatedFunction createTabulatedFunction(Class<?extends TabulatedFunction> TBClass, double leftX, double rightX, int pointsCount)
    {
        try
        {
            Constructor<?> constr=TBClass.getConstructor(double.class, double.class, int.class);
            return (TabulatedFunction) constr.newInstance(leftX, rightX, pointsCount);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?extends TabulatedFunction> TBClass, double leftX, double rightX, double[] arr)
    {
        try
        {
            Constructor<?extends TabulatedFunction> constr = TBClass.getConstructor(double.class, double.class, double[].class);
            return (TabulatedFunction) constr.newInstance(leftX, rightX, arr);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?extends TabulatedFunction> TBClass, FunctionPoint[] arr)
    {
        try
        {
            Constructor<?extends TabulatedFunction> constr = TBClass.getConstructor(FunctionPoint[].class);
            return constr.newInstance((Object) arr);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
