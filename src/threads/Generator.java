package threads;

public class Generator extends Thread {
    private final Task task;
    private final ReaderWriterSemaphore semaphore;

    public Generator(Task task, ReaderWriterSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }


    @Override
    public void run()
    {
        int i = 0;
        while ((task.getCounter() > 0) && (!isInterrupted()))
        {
            try {
                semaphore.acquireWrite();
                task.updateRand();
                System.out.printf("Source(%d): %f %f %f\n",
                        i,
                        task.getMinX(),
                        task.getMaxX(),
                        task.getDClock());
                i++;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.releaseWrite();
            }
        }
    }
}