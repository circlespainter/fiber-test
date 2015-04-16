package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingObjectBoundedBenchmarkTest extends QuasarFiberRingObjectBoundedBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
