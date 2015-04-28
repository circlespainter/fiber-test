package ringbench.quasar.actors;

import org.junit.Test;
import ringbench.Util;

/**
 * @author circlespainter
 */
public class QuasarActorRingBenchmarkTest extends QuasarActorRingBenchmark {
    @Test public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(rings, workerCount, ringSize, ringBenchmark(null));
    }
}
