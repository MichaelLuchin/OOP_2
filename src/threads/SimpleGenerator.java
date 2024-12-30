package threads;

public class SimpleGenerator implements Runnable
{
    private final Task task;

    public SimpleGenerator(Task task)
    {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (task.getCounter() >= 0) {
                synchronized (task)
                {
                    task.updateRand();
                    System.out.printf("Source(%d): %f %f %f\n",
                            i,
                            task.getMinX(),
                            task.getMaxX(),
                            task.getDClock());
                    i++;
                    task.notify();
                    task.wait(10);
                }
            }
        }catch(IllegalMonitorStateException|InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }

}