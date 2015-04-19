package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class ObjectMailboxFiberWorker extends AbstractMailboxFiberRingWorker {
    private final Channel<Object> mailbox;

    public ObjectMailboxFiberWorker(final int id, final String name, final int[] seqs, final CountDownLatch cdl, final int mboxSize, final Channels.OverflowPolicy mboxPolicy) {
        super(id, name, seqs, cdl);
        mailbox = Channels.newChannel(mboxSize, mboxPolicy);
    }

    @Override
    protected int receiveFromMailbox() throws InterruptedException, SuspendExecution {
        return ((Integer) getMailbox().receive()).intValue();
    }

    @Override
    protected void closeMailbox() {
        getMailbox().close();
    }

    @Override
    protected void sendToNext(final int i) throws InterruptedException, SuspendExecution {
        ((ObjectMailboxFiberWorker) getNext()).getMailbox().send(i);
    }

    protected Channel<Object> getMailbox() {
        return mailbox;
    }
}
