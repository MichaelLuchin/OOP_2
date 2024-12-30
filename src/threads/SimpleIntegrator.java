package threads;

public class SimpleIntegrator implements Runnable{
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (task.getCounter() >= 0) {
                synchronized (task)
                {
                    System.out.printf("Result(%d): %f %f %f %f\n",
                            i,
                            task.getMinX(),
                            task.getMaxX(),
                            task.getDClock(),
                            task.toWork());
                    i++;
                    task.notify();
                    task.wait(10);
                }
            }
        } catch(IllegalMonitorStateException|InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
