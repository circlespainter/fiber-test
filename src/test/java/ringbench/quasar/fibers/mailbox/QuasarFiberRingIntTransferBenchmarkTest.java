package ringbench.quasar.fibers.mailbox;

import org.junit.Test;
import ringbench.Util;

/**
 * @author circlespainter
 */
public class QuasarFiberRingIntTransferBenchmarkTest extends QuasarFiberRingIntTransferBenchmark {
    @Test public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(rings, workerCount, ringSize, ringBenchmark(null));
    }
}
