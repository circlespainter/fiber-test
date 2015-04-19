package ringbench.akka;

import org.junit.Test;
import ringbench.Util;

public class AkkaActorRingBenchmarkTest extends AkkaActorRingBenchmark {
    @Test public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
