package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractFiberRingObjectMailboxBenchmark extends AbstractFiberConfigurableMailboxRingBenchmark<ObjectMailboxFiberWorker> {
    @Override
    protected ObjectMailboxFiberWorker[][] newFiberArray(final int rings, final int size) {
        return new ObjectMailboxFiberWorker[rings][size];
    }

    @Override
    protected void start(final ObjectMailboxFiberWorker fiber, final int ringSize) throws InterruptedException, SuspendExecution {
        fiber.getSelf().send(ringSize);
    }

    @Override
    protected ObjectMailboxFiberWorker newFiber(final FiberScheduler scheduler, final int id, final int[] sequences, final CountDownLatch cdl, final Blackhole bh) {
        return new ObjectMailboxFiberWorker(scheduler, id, getFiberBaseName() + "-" + id, sequences, cdl, getMailboxSize(), getMailboxPolicy(), bh);
    }
}
