package ringbench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

/**
 * author @circlespainter
 */
public class WorkloadBench {
    @Benchmark
    public void benchWorkload(final Blackhole bh) throws Exception {
        (new RingWorker() {}).doWork(bh);
    }
}
