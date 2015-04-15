package com.github.vy.fibertest;

import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;

public class QuasarFiberRingIntUnlimitedBenchmark extends AbstractFiberRingIntMailboxBenchmark<QuasarFiberRingIntUnlimitedBenchmark.IntUnlimitedFiber> {

    @Override
    IntUnlimitedFiber[] newFiberArray(int workerCount) {
        return new IntUnlimitedFiber[workerCount];
    }

    @Override
    IntUnlimitedFiber newFiber(int id) {
        return new IntUnlimitedFiber(id);
    }

    protected static class IntUnlimitedFiber extends IntMailboxInternalFiber {
        private IntChannel mailbox = Channels.newIntChannel(-1);

        public IntUnlimitedFiber(int id) {
            super(id, IntUnlimitedFiber.class.getSimpleName());
        }

        @Override
        protected IntChannel getMailbox() {
            return mailbox;
        }
    }

    public static void main(String[] args) throws Exception {
        new QuasarFiberRingIntUnlimitedBenchmark().ringBenchmark();
    }
}
