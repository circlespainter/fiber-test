package ringbench.java;

import ringbench.AbstractRingBenchmark;
import ringbench.RingWorker;

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
    protected ThreadWorker[] createWorkers(int[] sequences) {
        final ThreadWorker[] workers = new ThreadWorker[workerCount];
        for (int i = 0; i < workerCount; i++)
            workers[i] = new ThreadWorker(i, sequences);
        return workers;
    }

    @Override
    protected void startWorkers(ThreadWorker[] workers) {
        for (final ThreadWorker worker : workers) worker.start();
    }

    protected class ThreadWorker extends Thread implements RingWorker {
        protected final int id;
        protected final int[] sequences;
        protected ThreadWorker next = null;
        protected volatile boolean waiting = true;
        protected int sequence = Integer.MAX_VALUE;

        public ThreadWorker(final int id, final int[] sequences) {
            super(String.format("%s-%s-%d",
                    JavaThreadRingBenchmark.class.getSimpleName(),
                    ThreadWorker.class.getSimpleName(), id));
            this.id = id;
            this.sequences = sequences;
        }

        @Override
        public void run() {
            while (sequence > 0) {
                while (waiting) { LockSupport.park(); }
                waiting = true;
                next.sequence = sequence - 1;
                next.waiting = false;
                LockSupport.unpark(next);
            }
            cdl.countDown();
            sequences[id] = sequence;
        }

        @Override
        public RingWorker getNext() {
            return next;
        }

        @Override
        public void setNext(RingWorker rw) {
            this.next = (ThreadWorker) rw;
        }
    }
}
