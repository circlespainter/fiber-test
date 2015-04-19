package ringbench.quasar.fibers.mailbox;

import org.junit.Test;
import ringbench.Util;

/**
 * @author circlespainter
 */
public class QuasarFiberRingIntUnlimitedBlockBenchmarkTest extends QuasarFiberRingIntUnlimitedBlockBenchmark {
    @Test public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
