package com.github.vy.fibertest;

import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

public class QuasarFiberRingObjectExchangeBenchmark extends AbstractFiberRingObjectMailboxBenchmark<QuasarFiberRingObjectExchangeBenchmark.ObjectExchangeFiber> {

    @Override
    ObjectExchangeFiber[] newFiberArray(int workerCount) {
        return new ObjectExchangeFiber[workerCount];
    }

    @Override
    ObjectExchangeFiber newFiber(int id) {
        return new ObjectExchangeFiber(id);
    }

    protected static class ObjectExchangeFiber extends ObjectMailboxInternalFiber {
        private Channel<Object> mailbox = Channels.newChannel(0);

        public ObjectExchangeFiber(int id) {
            super(id, ObjectExchangeFiber.class.getSimpleName());
        }

        @Override
        protected Channel<Object> getMailbox() {
            return mailbox;
        }
    }

    public static void main(String[] args) throws Exception {
        new QuasarFiberRingObjectExchangeBenchmark().ringBenchmark();
    }
}
