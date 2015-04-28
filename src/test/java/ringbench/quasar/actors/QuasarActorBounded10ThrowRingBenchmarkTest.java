package ringbench.quasar.actors;

import org.junit.Test;
import ringbench.Util;

/**
 * @author circlespainter
 */
public class QuasarActorBounded10ThrowRingBenchmarkTest extends QuasarActorBounded10ThrowRingBenchmark {
    @Test public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(rings, workerCount, ringSize, ringBenchmark(null));
    }
}
