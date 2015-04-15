package com.github.vy.fibertest;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.IntChannel;

/**
 * Created by fabio on 4/15/15.
 */
abstract class ObjectMailboxInternalFiber extends Fiber<Integer> {

    protected ObjectMailboxInternalFiber next;

    public ObjectMailboxInternalFiber(final int id, String name) {
        super(String.format("%s-%s-%d",
                QuasarFiberRingIntExchangeBenchmark.class.getSimpleName(),
                name, id));
    }


    @Override
    @Suspendable
    public Integer run() throws SuspendExecution, InterruptedException {
        Integer sequence = new Integer(Integer.MAX_VALUE);
        while (sequence.intValue() > 0) {
            Object message = getMailbox().receive();
            if (message instanceof Integer) {
                sequence = (Integer) message;
                next.getMailbox().send(sequence - 1);
            }
        }
        getMailbox().close();
        return sequence;
    }

    protected abstract Channel<Object> getMailbox();
}
