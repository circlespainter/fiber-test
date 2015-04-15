package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingIntUnlimitedBenchmarkTest extends QuasarFiberRingIntUnlimitedBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
