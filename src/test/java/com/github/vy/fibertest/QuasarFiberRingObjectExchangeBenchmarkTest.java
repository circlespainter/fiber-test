package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingObjectExchangeBenchmarkTest extends QuasarFiberRingObjectExchangeBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
