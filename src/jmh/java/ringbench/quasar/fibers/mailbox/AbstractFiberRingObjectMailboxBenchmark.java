package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.SuspendExecution;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractFiberRingObjectMailboxBenchmark extends AbstractFiberConfigurableMailboxRingBenchmark<ObjectMailboxFiberWorker> {
    @Override
    protected ObjectMailboxFiberWorker[] newFiberArray(final int size) {
        return new ObjectMailboxFiberWorker[size];
    }

    @Override
    protected void start(final ObjectMailboxFiberWorker fiber, final int ringSize) throws InterruptedException, SuspendExecution {
        fiber.getSelf().send(ringSize);
    }

    @Override
    protected ObjectMailboxFiberWorker newFiber(final int id, final int[] sequences, final CountDownLatch cdl) {
        return new ObjectMailboxFiberWorker(id, getFiberBaseName() + "-" + id, sequences, cdl, getMailboxSize(), getMailboxPolicy());
    }
}
