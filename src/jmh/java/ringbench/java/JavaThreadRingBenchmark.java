package ringbench.java;

import org.openjdk.jmh.infra.Blackhole;
import ringbench.AbstractRingBenchmark;
import ringbench.RingWorker;

import java.util.concurrent.CountDownLatch;
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
    protected ThreadWorker[][] setupWorkers(final int[][] sequences, final CountDownLatch cdl, final Blackhole bh) {
        final ThreadWorker[][] workers = new ThreadWorker[sequences.length][sequences.length >= 0 ? sequences[0].length : 0];

        for (int i = 0; i < sequences.length; i++) {
            final ThreadWorker[] seq = workers[i];
            final int len = seq.length;

            for (int j = 0; j < len; j++)
                seq[j] = new ThreadWorker(j, sequences[i], cdl, bh);

            // Set next worker pointers.
            for (int j = 0; j < len; j++)
                workers[i][j].next = workers[i][(j + 1) % workerCount];
        }

        return workers;
    }

    @Override
    protected void startWorkers(final ThreadWorker[] workers) {
        for (final ThreadWorker worker : workers) worker.start();
    }

    protected class ThreadWorker extends Thread implements RingWorker {
        private final int id;
        private final int[] sequences;
        private final Blackhole blackHole;
        private CountDownLatch cdl;

        private volatile boolean waiting = true;
        private volatile int sequence = Integer.MAX_VALUE;

        protected ThreadWorker next;

        public ThreadWorker(final int id, final int[] sequences, final CountDownLatch cdl, final Blackhole bh) {
            super(String.format("%s-%s-%d",
                    JavaThreadRingBenchmark.class.getSimpleName(),
                    ThreadWorker.class.getSimpleName(), id));
            this.id = id;
            this.sequences = sequences;
            this.cdl = cdl;
            this.blackHole = bh;
        }

        @Override
        public void run() {
            while (sequence > 0) {
                while (waiting) {
                    LockSupport.park();
                }
                try {
                    doWork(blackHole);
                } catch (final Exception e) {
                    throw new AssertionError(e);
                }
                waiting = true;
                next.sequence = sequence - 1;
                next.waiting = false;
                LockSupport.unpark(next);
            }
            cdl.countDown();
            sequences[id] = sequence;
        }
    }
}
