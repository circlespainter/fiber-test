package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingIntExchangeBenchmarkTest extends QuasarFiberRingIntExchangeBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
