package com.github.vy.fibertest;

import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;

public class QuasarFiberRingIntExchangeBenchmark extends AbstractFiberRingIntMailboxBenchmark<QuasarFiberRingIntExchangeBenchmark.IntExchangeFiber> {

    @Override
    IntExchangeFiber[] newFiberArray(int workerCount) {
        return new IntExchangeFiber[workerCount];
    }

    @Override
    IntExchangeFiber newFiber(int id) {
        return new IntExchangeFiber(id);
    }

    protected static class IntExchangeFiber extends IntMailboxInternalFiber {
        private IntChannel mailbox = Channels.newIntChannel(0);

        public IntExchangeFiber(int id) {
            super(id, IntExchangeFiber.class.getSimpleName());
        }

        @Override
        protected IntChannel getMailbox() {
            return mailbox;
        }
    }

    public static void main(String[] args) throws Exception {
        new QuasarFiberRingIntExchangeBenchmark().ringBenchmark();
    }
}
