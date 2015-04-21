package ringbench.java;

import ringbench.AbstractRingBenchmark;

import java.util.concurrent.locks.LockSupport;

/**
 * Ring benchmark using Java threads.
 */
public class JavaThreadRingBenchmark extends AbstractRingBenchmark<JavaThreadRingBenchmark.ThreadWorker> {
    @Override
    protected void startRing(ThreadWorker first) {
        first.sequence = ringSize;
        first.waiting = false;
        LockSupport.unpark(first);
    }

    @Override
    protected ThreadWorker[] setupWorkers(int[] sequences) {
        final ThreadWorker[] workers = new ThreadWorker[workerCount];
        for (int i = 0; i < workerCount; i++)
            workers[i] = new ThreadWorker(i, sequences);
        for (int i = 0; i < workerCount; i++) {
            workers[i].next = workers[(i + 1) % workerCount];
            workers[i].prev = workers[(i - 1 >= 0 ? i - 1 : workerCount - 1)];
        }
        return workers;
    }

    @Override
    protected void startWorkers(ThreadWorker[] workers) {
        for (final ThreadWorker worker : workers) worker.start();
    }

    protected class ThreadWorker extends Thread {
        protected final int id;
        protected final int[] sequences;
        protected volatile boolean waiting = true;
        protected int sequence = Integer.MAX_VALUE;
        protected volatile ThreadWorker next;
        protected ThreadWorker prev;

        public ThreadWorker(final int id, final int[] sequences) {
            super(String.format("%s-%s-%d",
                    JavaThreadRingBenchmark.class.getSimpleName(),
                    ThreadWorker.class.getSimpleName(), id));
            this.id = id;
            this.sequences = sequences;
        }

        @Override
        public void run() {
            System.out.println("Thread " + Thread.currentThread().getName() +" starting");
            while (sequence > 0) {
                System.out.println("Thread " + Thread.currentThread().getName() +" received " + sequence);
                while (waiting) {
                    System.out.println("Thread " + Thread.currentThread().getName() +" parking");
                    LockSupport.park();
                }
                System.out.println("Thread " + Thread.currentThread().getName() +" setting " + sequence + " and unparking next " + next.getName());
                waiting = true;
                next.sequence = sequence - 1;
                next.waiting = false;
                LockSupport.unpark(next);
            }
            System.out.println("Thread " + Thread.currentThread().getName() +" finishing, exiting ring and unparking next " + next.getName());
            prev.next = next;
            next.waiting = false;
            LockSupport.unpark(next);
            cdl.countDown();
            System.out.println("Thread " + Thread.currentThread().getName() + " count is " + cdl.getCount());
            sequences[id] = sequence;
        }
    }
}
