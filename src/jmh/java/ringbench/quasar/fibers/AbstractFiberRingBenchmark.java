package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.infra.Blackhole;
import ringbench.AbstractRingBenchmark;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractFiberRingBenchmark<F extends AbstractRingFiberWorker> extends AbstractRingBenchmark<F> {
    protected final FiberForkJoinScheduler fiberScheduler;

    public AbstractFiberRingBenchmark() {
        int parallelism;
        try {
            parallelism = Integer.parseInt(System.getProperty("fiberParallelism"));
        } catch (Throwable t) {
            parallelism = Runtime.getRuntime().availableProcessors();
        }
        this.fiberScheduler = new FiberForkJoinScheduler("fiber-scheduler-parallelism" + parallelism, parallelism);
    }

    @Override protected F[][] setupWorkers(final int[][] sequences, final CountDownLatch cdl, final Blackhole bh) {
        final F[][] fibers = newFiberArray(sequences.length, (sequences.length >= 0 ? sequences[0].length : 0));

        for (int i = 0; i < sequences.length; i++) {
            final F[] seq = fibers[i];
            final int len = seq.length;

            for (int j = 0; j < len; j++)
                seq[j] = newFiber(fiberScheduler, j, sequences[i], cdl, bh);

            // Set next worker pointers.
            for (int j = 0; j < len; j++)
                seq[j].next = seq[(j + 1) % workerCount].self;
        }

        return fibers;
    }

    @Override protected void startWorkers(final F[] workers) {
        for (final F fiber : workers) fiber.start();
    }

    @Override protected void startRing(final F first) {
        new Fiber<Void>() {
            @Override
            protected Void run() throws SuspendExecution, InterruptedException {
                AbstractFiberRingBenchmark.this.start(first, ringSize);
                return null;
            }
        }.start();
    }

    protected abstract F[][] newFiberArray(final int rings, final int size);
    protected abstract F newFiber(final FiberScheduler scheduler, final int id, final int[] sequences, final CountDownLatch cdl, final Blackhole bh);
    protected abstract void start(final F fiber, final int ringSize) throws InterruptedException, SuspendExecution;
}
