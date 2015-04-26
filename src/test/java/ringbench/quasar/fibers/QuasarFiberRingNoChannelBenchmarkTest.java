package ringbench.quasar.fibers;

import org.junit.Test;
import ringbench.Util;

/**
 * @author circlespainter
 */
public class QuasarFiberRingNoChannelBenchmarkTest extends QuasarFiberRingNoChannelBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(rings, workerCount, ringSize, ringBenchmark());
    }
}
