package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import ringbench.RingWorker;
import ringbench.quasar.fibers.AbstractRingFiberWorker;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractMailboxFiberRingWorker extends AbstractRingFiberWorker implements RingWorker {
    private final CountDownLatch latch;
    private final int id;
    private final int[] sequences;

    public AbstractMailboxFiberRingWorker(final int id, final String name, final int[] seqs, final CountDownLatch cdl) {
        super(name);
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
            sendToNext(sequence - 1);
        }
        closeMailbox();
        sequences[id] = sequence;
        latch.countDown();
        return sequence;
    }

    protected abstract int receiveFromMailbox() throws InterruptedException, SuspendExecution;

    protected abstract void sendToNext(final int i) throws InterruptedException, SuspendExecution;

    protected abstract void closeMailbox();
}
