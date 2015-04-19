package ringbench;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static void testRingBenchmark(
            final int workerCount,
            final int ringSize,
            final int[] sequences) {
        final int offset = workerCount - ringSize % workerCount;
        for (int i = 0; i < workerCount; i++)
            try {
                assertEquals(
                        "sequence returned by Worker#" + i,
                        -((offset + i) % workerCount), sequences[i]);
            } catch (AssertionError ae) {
                log.trace("sequences[] = {}", Arrays.toString(sequences));
                throw ae;
            }
    }

}
