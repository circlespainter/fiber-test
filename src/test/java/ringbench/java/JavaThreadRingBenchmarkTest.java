package ringbench.java;

import org.junit.Test;
import ringbench.Util;

public class JavaThreadRingBenchmarkTest extends JavaThreadRingBenchmark {
    // @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(rings, workerCount, ringSize, ringBenchmark(null));
    }
}
