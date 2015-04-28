package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

public class QuasarFiberRingNoChannelBenchmark extends AbstractFiberRingBenchmark<QuasarFiberRingNoChannelBenchmark.ParkUnparkFiber> {
    protected static class ParkUnparkFiber extends AbstractRingFiberWorker<ParkUnparkFiber> {
        protected final CountDownLatch latch;
        private final int[] sequences;
        private final int id;

        protected volatile boolean waiting = true;
        protected volatile int sequence = Integer.MAX_VALUE;

        public ParkUnparkFiber(final FiberScheduler scheduler, final Blackhole bh, final int id, final int[] seqs, final CountDownLatch cdl) {
            super(scheduler, "ParkUnpark", bh);
            this.self = this;
            this.latch = cdl;
            this.id = id;
            this.sequences = seqs;
        }

        @Override
        @Suspendable
        public Integer run() throws SuspendExecution, InterruptedException {
            while (sequence > 0) {
                while (waiting) { Strand.park(); }
                try {
                    doWork(blackHole);
                } catch (final Exception e) {
                    throw new AssertionError(e);
                }
                waiting = true;
                next.sequence = sequence - 1;
                next.waiting = false;
                Strand.unpark(next);
            }
            sequences[id] = sequence;
            latch.countDown();
            return sequence;
        }
    }

    @Override
    protected ParkUnparkFiber[][] newFiberArray(final int rings, final int size) {
        return new ParkUnparkFiber[rings][size];
    }

    @Override
    protected ParkUnparkFiber newFiber(final FiberScheduler scheduler, final int id, final int[] sequences, final CountDownLatch cdl, final Blackhole bl) {
        return new ParkUnparkFiber(scheduler, bl, id, sequences, cdl);
    }

    @Override
    protected void start(ParkUnparkFiber fiber, int ringSize) throws InterruptedException, SuspendExecution {
        fiber.sequence = ringSize;
        fiber.waiting = false;
        Strand.unpark(fiber);
    }
}
