package threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReaderWriterSemaphore {
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Condition noWriters = readWriteLock.writeLock().newCondition();
    private int activeWriters = 0;
    private int waitingWriters = 0;

    public void acquireRead() {
        readWriteLock.readLock().lock();
    }

    public void releaseRead() {
        readWriteLock.readLock().unlock();
    }

    public void acquireWrite() throws InterruptedException {
        readWriteLock.writeLock().lock();
        try {
            while (activeWriters > 0 || waitingWriters > 0) {
                noWriters.await();
            }
            ++activeWriters;
        } finally {
            if (activeWriters == 1 && waitingWriters > 0) {
                noWriters.signalAll();
            }
        }
    }

    public void releaseWrite() {
        --activeWriters;
        if (waitingWriters > 0) {
            noWriters.signal();
        }
        readWriteLock.writeLock().unlock();
    }
}
