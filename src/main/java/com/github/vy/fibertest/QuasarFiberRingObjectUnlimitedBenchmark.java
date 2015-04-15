package com.github.vy.fibertest;

import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

public class QuasarFiberRingObjectUnlimitedBenchmark extends AbstractFiberRingObjectMailboxBenchmark<QuasarFiberRingObjectUnlimitedBenchmark.ObjectUnlimitedFiber> {

    @Override
    ObjectUnlimitedFiber[] newFiberArray(int workerCount) {
        return new ObjectUnlimitedFiber[workerCount];
    }

    @Override
    ObjectUnlimitedFiber newFiber(int id) {
        return new ObjectUnlimitedFiber(id);
    }

    protected static class ObjectUnlimitedFiber extends ObjectMailboxInternalFiber {
        private Channel<Object> mailbox = Channels.newChannel(-1);

        public ObjectUnlimitedFiber(int id) {
            super(id, ObjectUnlimitedFiber.class.getSimpleName());
        }

        @Override
        protected Channel<Object> getMailbox() {
            return mailbox;
        }
    }

    public static void main(String[] args) throws Exception {
        new QuasarFiberRingObjectUnlimitedBenchmark().ringBenchmark();
    }
}
