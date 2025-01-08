import functions.*;
import functions.basic.*;
import threads.*;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args)
    {
//        TabulatedFunction f = new LinkedListTabulatedFunction(10,20,11);
//        for (FunctionPoint p : f) {
//            System.out.println(p);
//        }
//        Function f = new Cos();
//        TabulatedFunction tf;
//        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
//        System.out.println(tf.getClass());
//
//        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
//        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
//        System.out.println(tf.getClass());
//
//        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
//        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
//        System.out.println(tf.getClass());

        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {new FunctionPoint(0, 0), new FunctionPoint(10, 10)});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);

    }

    private static void nonThread()
    {
        Task task = new Task();
        while (task.getCounter()>0)
        {
            task.updateRand();
            System.out.printf("Source: %f %f %f\n",
                    task.getMinX(),
                    task.getMaxX(),
                    task.getDClock());
            System.out.printf("Result: %f %f %f %f\n",
                    task.getMinX(),
                    task.getMaxX(),
                    task.getDClock(),
                    task.toWork());
        }
    }

    private static void simpleThread()
    {
        Task task = new Task();
        Thread generatorT = new Thread(new SimpleGenerator(task));
        Thread integratorT = new Thread(new SimpleIntegrator(task));
        generatorT.start();
        integratorT.start();
    }

    private static void complicatedThread()
    {
        Task task=new Task();
        ReaderWriterSemaphore semaphore =new ReaderWriterSemaphore();
        Thread generatorT = new Generator(task,semaphore);
        Thread integratorT = new Integrator(task,semaphore);
        generatorT.start();
        integratorT.start();

        try
        {
            sleep(50);
            generatorT.interrupt();
            integratorT.interrupt();
        }
        catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}