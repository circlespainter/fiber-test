package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingIntBoundedBenchmarkTest extends QuasarFiberRingIntBoundedBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
