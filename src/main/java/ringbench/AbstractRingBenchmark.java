package ringbench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
@State(Scope.Benchmark)
public abstract class AbstractRingBenchmark<W extends RingWorker> extends RingBenchmarkSupport {
    protected CountDownLatch cdl;

    @Benchmark public int[] ringBenchmark() throws Exception {
        cdl = new CountDownLatch(workerCount);

        // Create workers.
        final int[] sequences = new int[workerCount];
        final W[] workers = createWorkers(sequences);

        // Set next worker pointers.
        for (int i = 0; i < workerCount; i++)
            workers[i].setNext(workers[(i+1) % workerCount]);

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

    protected abstract W[] createWorkers(final int[] sequences);
    protected abstract void startWorkers(final W[] workers);
    protected abstract void startRing(W first);
}
