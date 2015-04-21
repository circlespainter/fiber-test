package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import ringbench.AbstractRingBenchmark;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractFiberRingBenchmark<F extends AbstractRingFiberWorker> extends AbstractRingBenchmark<F> {
    @Override protected F[] setupWorkers(final int[] sequences) {
        final F[] fibers = newFiberArray(workerCount);
        for (int i = 0; i < workerCount; i++)
            fibers[i] = newFiber(i, sequences, cdl);
        // Set next worker pointers.
        for (int i = 0; i < workerCount; i++)
            fibers[i].next = fibers[(i+1) % workerCount].self;

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

    protected abstract F[] newFiberArray(final int size);
    protected abstract F newFiber(final int id, final int[] sequences, final CountDownLatch cdl);
    protected abstract void start(final F fiber, final int ringSize) throws InterruptedException, SuspendExecution;
}
