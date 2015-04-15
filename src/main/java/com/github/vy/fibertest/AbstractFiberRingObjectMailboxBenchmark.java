package com.github.vy.fibertest;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * Created by fabio on 4/15/15.
 */
public abstract class AbstractFiberRingObjectMailboxBenchmark<F extends ObjectMailboxInternalFiber> extends AbstractRingBenchmark {

    @Override
    @Benchmark
    public int[] ringBenchmark() throws Exception {
        // Create fibers.
        final F[] fibers = newFiberArray(workerCount);
        for (int i = 0; i < workerCount; i++)
            fibers[i] = newFiber(i);

        // Set next fiber pointers.
        for (int i = 0; i < workerCount; i++)
            fibers[i].next = fibers[(i+1) % workerCount];

        // Start fibers.
        for (final F fiber : fibers) fiber.start();

        // Initiate the ring.
        final F first = fibers[0];
        new Fiber<Void>() {
            @Override
            protected Void run() throws SuspendExecution, InterruptedException {
                first.getMailbox().send(ringSize);
                return null;
            }
        }.start();

        // Wait for fibers to complete.
        final int[] sequences = new int[workerCount];
        for (int i = 0; i < workerCount; i++)
            sequences[i] = fibers[i].get();
        return sequences;
    }

    abstract F[] newFiberArray(int workerCount);
    abstract F newFiber(int id);
}
