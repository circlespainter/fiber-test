package ringbench;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * @author circlespainter
 */
@State(Scope.Benchmark)
public class RingBenchmarkSupport {
    protected final int workerCount;
    protected final int ringSize;

    public RingBenchmarkSupport() {
        this.workerCount = Integer.parseInt(System.getProperty("workerCount"));
        this.ringSize = Integer.parseInt(System.getProperty("ringSize"));
    }
}
