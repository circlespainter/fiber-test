package ringbench;

import org.openjdk.jmh.infra.Blackhole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static void testRingBenchmark(
            final int rings,
            final int workerCount,
            final int ringSize,
            final int[][] sequences) {
        final int offset = workerCount - ringSize % workerCount;

        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < workerCount; j++)
                try {
                    assertEquals(
                            "sequence returned by Worker#" + j,
                            -((offset + j) % workerCount), sequences[i][j]);
                } catch (AssertionError ae) {
                    log.trace("sequences[] = {}", Arrays.toString(sequences[i]));
                    throw ae;
                }
        }
    }
}
