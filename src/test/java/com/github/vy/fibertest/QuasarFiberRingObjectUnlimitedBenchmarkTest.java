package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingObjectUnlimitedBenchmarkTest extends QuasarFiberRingObjectUnlimitedBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
