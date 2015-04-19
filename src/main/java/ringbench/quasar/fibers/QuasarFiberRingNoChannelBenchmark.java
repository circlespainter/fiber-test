package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import ringbench.RingWorker;

import java.util.concurrent.CountDownLatch;

public class QuasarFiberRingNoChannelBenchmark extends AbstractFiberRingBenchmark<QuasarFiberRingNoChannelBenchmark.ParkUnparkFiber> {
    protected static class ParkUnparkFiber extends AbstractRingFiberWorker {
        protected final CountDownLatch latch;
        private final int[] sequences;
        private final int id;

        protected ParkUnparkFiber next;
        protected volatile boolean waiting = true;
        protected int sequence = Integer.MAX_VALUE;

        public ParkUnparkFiber(final int id, final int[] seqs, final CountDownLatch cdl) {
            super("ParkUnpark");
            this.latch = cdl;
            this.id = id;
            this.sequences = seqs;
        }

        @Override
        @Suspendable
        public Integer run() throws SuspendExecution, InterruptedException {
            while (sequence > 0) {
                while (waiting) { Strand.park(); }
                waiting = true;
                next.sequence = sequence - 1;
                next.waiting = false;
                Strand.unpark(next);
            }
            sequences[id] = sequence;
            latch.countDown();
            return null;
        }

        @Override
        public RingWorker getNext() {
            return next;
        }

        @Override
        public void setNext(RingWorker rw) {
            this.next = (ParkUnparkFiber) rw;
        }
    }

    @Override
    protected ParkUnparkFiber[] newFiberArray(final int size) {
        return new ParkUnparkFiber[size];
    }

    @Override
    protected ParkUnparkFiber newFiber(final int id, final int[] sequences, final CountDownLatch cdl) {
        return new ParkUnparkFiber(id, sequences, cdl);
    }

    @Override
    protected void start(ParkUnparkFiber fiber, int ringSize) throws InterruptedException, SuspendExecution {
        fiber.sequence = ringSize;
        fiber.waiting = false;
        Strand.unpark(fiber);
    }
}
