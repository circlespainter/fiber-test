package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import org.openjdk.jmh.infra.Blackhole;
import ringbench.quasar.fibers.AbstractRingFiberWorker;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractMailboxFiberRingWorker<H> extends AbstractRingFiberWorker<H> {
    private final CountDownLatch latch;
    private final int id;
    private final int[] sequences;

    public AbstractMailboxFiberRingWorker(final FiberScheduler scheduler, final int id, final String name, final int[] seqs, final CountDownLatch cdl, final H handle, final Blackhole bh) {
        super(scheduler, name, handle, bh);
        this.latch = cdl;
        this.id = id;
        this.sequences = seqs;
    }

    @Override
    @Suspendable
    public Integer run() throws SuspendExecution, InterruptedException {
        int sequence = Integer.MAX_VALUE;
        while (sequence > 0) {
            sequence = receiveFromMailbox();
            try {
                doWork(blackHole);
            } catch (final Exception e) {
                throw new AssertionError(e);
            }
            sendToNext(sequence - 1);
        }
        closeMailbox();
        sequences[id] = sequence;
        latch.countDown();
        return sequence;
    }

    protected abstract Integer receiveFromMailbox() throws InterruptedException, SuspendExecution;

    protected abstract void sendToNext(final Integer i) throws InterruptedException, SuspendExecution;

    protected abstract void closeMailbox();
}
