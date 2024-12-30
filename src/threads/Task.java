package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Task
{
    private Function f;
    private double minX, maxX, dClock;
    int counter;
    public boolean flag = false;

    public Task(){this.counter = ThreadLocalRandom.current().nextInt(100,500);}

    public Task(Function f, double minX, double maxX, double dClock){
        this.f = f;
        this.minX = minX;
        this.maxX = maxX;
        this.dClock = dClock;
        this.counter = ThreadLocalRandom.current().nextInt(100,500);
    }


    public void setFunction(Function f)
    {
        this.f=f;
    }

    public void setMinX(double minX)
    {
        this.minX = minX;
    }

    public void setMaxX(double maxX)
    {
        this.maxX = maxX;
    }

    public void setDClock(double dClock)
    {
        this.dClock = dClock;
    }

    public Function getFunction()
    {
        return f;
    }

    public double getMinX()
    {
        return minX;
    }

    public double getMaxX()
    {
        return maxX;
    }

    public double getDClock()
    {
        return dClock;
    }

    public int getCounter()
    {
        return counter;
    }

    public double toWork()
    {
        counter--;
        return Functions.integral(f, minX, maxX, dClock);
    }

    public void updateRand()
    {
        this.f=new Log(ThreadLocalRandom.current().nextDouble(1,10));
        this.minX=ThreadLocalRandom.current().nextDouble(Math.nextUp(0),100);
        this.maxX=ThreadLocalRandom.current().nextDouble(100,200);
        this.dClock=ThreadLocalRandom.current().nextDouble(Math.nextUp(0),1);
    }

    /*public static Task getRandomTask()
    {
        double base = ThreadLocalRandom.current().nextDouble(1,10);
        double minX = ThreadLocalRandom.current().nextDouble(Math.nextUp(0),100);
        double maxX = ThreadLocalRandom.current().nextDouble(100,200);
        double dClock = ThreadLocalRandom.current().nextDouble(Math.nextUp(0),1);
        return new Task(new Log(base), minX, maxX, dClock);
    }*/

}