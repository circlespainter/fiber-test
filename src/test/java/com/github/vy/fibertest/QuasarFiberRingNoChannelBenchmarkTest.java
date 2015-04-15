package com.github.vy.fibertest;

import org.junit.Test;

public class QuasarFiberRingNoChannelBenchmarkTest extends QuasarFiberRingNoChannelBenchmark {
    @Test
    public void testRingBenchmark() throws Exception {
        Util.testRingBenchmark(workerCount, ringSize, ringBenchmark());
    }
}
