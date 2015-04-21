package ringbench;

import co.paralleluniverse.fibers.Fiber;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
@State(Scope.Benchmark)
public abstract class AbstractRingBenchmark<W> extends RingBenchmarkSupport {
    protected CountDownLatch cdl;

    @Benchmark public int[] ringBenchmark() throws Exception {
        cdl = new CountDownLatch(workerCount);

        // Create workers.
        final int[] sequences = new int[workerCount];
        final W[] workers = setupWorkers(sequences);

        // Start workers.
        startWorkers(workers);

        // Initiate the ring.
        final W first = workers[0];
        startRing(first);

        // Wait for the latch.
        cdl.await();

        // Return result.
        return sequences;
    }

    protected abstract W[] setupWorkers(final int[] sequences);
    protected abstract void startWorkers(final W[] workers);
    protected abstract void startRing(W first);
}
