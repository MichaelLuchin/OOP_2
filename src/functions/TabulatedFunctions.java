package functions;

import java.io.*;
import java.io.DataOutputStream;

public class TabulatedFunctions {

    private static final TabulatedFunctions INSTANCE = new TabulatedFunctions();

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
        TabulatedFunction NewTabFunc = new LinkedListTabulatedFunction(leftX, rightX, values); // Позже нужно будет сделать универсальный тип, а не только лист
        return NewTabFunc;
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
            int PoitsCount = instr.readInt();
            double leftX = instr.readDouble();
            double rightX = instr.readDouble();
            InpTabFunc = new LinkedListTabulatedFunction(leftX,rightX,PoitsCount);
            for (int i = 0; i < PoitsCount; i++) {
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
            InpTabFunc = new LinkedListTabulatedFunction(leftX,rightX,PointsCount);
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
}
