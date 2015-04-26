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
    protected final int rings;

    public RingBenchmarkSupport() {
        workerCount = Integer.parseInt(System.getProperty("workerCount"));
        ringSize = Integer.parseInt(System.getProperty("ringSize"));
        int rings;
        try {
            rings = Integer.parseInt(System.getProperty("rings"));
        } catch (Throwable t) {
            rings = Runtime.getRuntime().availableProcessors();
        }
        this.rings = rings;
    }
}
