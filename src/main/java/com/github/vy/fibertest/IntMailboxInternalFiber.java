package com.github.vy.fibertest;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.IntChannel;

/**
 * Created by fabio on 4/15/15.
 */
abstract class IntMailboxInternalFiber extends Fiber<Integer> {

    protected IntMailboxInternalFiber next;

    public IntMailboxInternalFiber(final int id, String name) {
        super(String.format("%s-%s-%d",
                QuasarFiberRingIntExchangeBenchmark.class.getSimpleName(),
                name, id));
    }

    @Override
    @Suspendable
    public Integer run() throws SuspendExecution, InterruptedException {
        int sequence = Integer.MAX_VALUE;
        while (sequence > 0) {
            sequence = getMailbox().receive();
            next.getMailbox().send(sequence - 1);
        }
        getMailbox().close();
        return sequence;
    }

    protected abstract IntChannel getMailbox();
}
