package threads;

public class Integrator extends Thread {
    private final Task task;
    private final ReaderWriterSemaphore semaphore;

    public Integrator(Task task, ReaderWriterSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run()
    {
        int i = 0;
        while ((task.getCounter() >= 0) && (!isInterrupted()))
        {
            try {
                semaphore.acquireRead();
                System.out.printf("Result(%d): %f %f %f %f\n",
                        i,
                        task.getMinX(),
                        task.getMaxX(),
                        task.getDClock(),
                        task.toWork());
                i++;
            } finally {
                semaphore.releaseRead();
            }
        }
    }
}