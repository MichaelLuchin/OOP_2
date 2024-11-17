import functions.*;
import functions.basic.*;
import functions.meta.Composition;

import java.io.*;

public class Main {
    public static void main(String[] args) throws InappropriateFunctionPointException, IOException {
        Exp exp = new Exp();
        TabulatedFunction lne = TabulatedFunctions.tabulate(new Composition(new Log(exp.getFunctionValue(1)),new Exp()), 0, 10, 11);
        TabulatedFunction lneInputed;

        try (Writer out = new FileWriter("out.txt")){
            TabulatedFunctions.writeTabulatedFunction(lne, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Reader in = new FileReader("out.txt")){
            lneInputed = TabulatedFunctions.readTabulatedFunction(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Writer out2 = new FileWriter("out2.txt")){
            TabulatedFunctions.writeTabulatedFunction(lneInputed, out2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}