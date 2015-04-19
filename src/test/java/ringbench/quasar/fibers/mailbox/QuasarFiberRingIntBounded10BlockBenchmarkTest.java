package ringbench.quasar.fibers.mailbox;

import org.junit.Test;
import ringbench.Util;

/**
 * @author circlespainter
 */
public class QuasarFiberRingIntBounded10BlockBenchmarkTest extends QuasarFiberRingIntBounded10BlockBenchmark {
    @Test public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
