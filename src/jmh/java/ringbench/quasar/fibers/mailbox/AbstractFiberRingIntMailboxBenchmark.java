package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractFiberRingIntMailboxBenchmark extends AbstractFiberConfigurableMailboxRingBenchmark<IntMailboxFiberWorker> {
    @Override
    protected IntMailboxFiberWorker[][] newFiberArray(final int rings, final int size) {
        return new IntMailboxFiberWorker[rings][size];
    }

    @Override
    protected void start(final IntMailboxFiberWorker fiber, final int ringSize) throws InterruptedException, SuspendExecution {
        fiber.getSelf().send(ringSize);
    }

    @Override
    protected IntMailboxFiberWorker newFiber(final FiberScheduler scheduler, final int id, final int[] sequences, final CountDownLatch cdl) {
        return new IntMailboxFiberWorker(scheduler, id, getFiberBaseName() + "-" + id, sequences, cdl, getMailboxSize(), getMailboxPolicy());
    }
}
