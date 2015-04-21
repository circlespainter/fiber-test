package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class ObjectMailboxFiberWorker extends AbstractMailboxFiberRingWorker<Channel<Integer>> {
    public ObjectMailboxFiberWorker(final int id, final String name, final int[] seqs, final CountDownLatch cdl, final int mboxSize, final Channels.OverflowPolicy mboxPolicy) {
        super(id, name, seqs, cdl, Channels.newChannel(mboxSize, mboxPolicy));
    }

    @Override
    protected int receiveFromMailbox() throws InterruptedException, SuspendExecution {
        return self.receive().intValue();
    }

    @Override
    protected void closeMailbox() {
        self.close();
    }

    @Override
    protected void sendToNext(final int i) throws InterruptedException, SuspendExecution {
        next.send(new Integer(i));
    }
}
