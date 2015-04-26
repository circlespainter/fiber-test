package ringbench;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
@State(Scope.Benchmark)
public abstract class AbstractRingBenchmark<W> extends RingBenchmarkSupport {
    private CountDownLatch cdl;

    @Benchmark public int[][] ringBenchmark() throws Exception {
        cdl = new CountDownLatch(workerCount * rings);

        final int[][] sequences = new int[rings][workerCount];

        // Create and setup workers.
        final W[][] workers = setupWorkers(sequences, cdl);

        for (int i = 0; i < rings; i++)
            // Start workers.
            startWorkers(workers[i]);

        for (int i = 0; i < rings; i++)
            // Initiate the ring.
            startRing(workers[i][0]);

        // Wait for the latch.
        cdl.await();

        shutdown();

        return sequences;
    }

    protected void shutdown() {}
    protected void startWorkers(final W[] workers) {}

    protected abstract W[][] setupWorkers(final int[][] sequences, final CountDownLatch cdl);
    protected abstract void startRing(W first) throws SuspendExecution;
}
