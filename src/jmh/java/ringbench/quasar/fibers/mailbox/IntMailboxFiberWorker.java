package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class IntMailboxFiberWorker extends AbstractMailboxFiberRingWorker<IntChannel> {
    public IntMailboxFiberWorker(final int id, final String name, final int[] seqs, final CountDownLatch cdl, final int mboxSize, final Channels.OverflowPolicy mboxPolicy) {
        super(id, name, seqs, cdl, Channels.newIntChannel(mboxSize, mboxPolicy));
    }

    @Override
    protected int receiveFromMailbox() throws InterruptedException, SuspendExecution {
        return self.receive();
    }

    @Override
    protected void closeMailbox() {
        self.close();
    }

    @Override
    protected void sendToNext(final int i) throws InterruptedException, SuspendExecution {
        next.send(i);
    }
}
