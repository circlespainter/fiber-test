package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class IntMailboxFiberWorker extends AbstractMailboxFiberRingWorker {
    private final IntChannel mailbox;

    public IntMailboxFiberWorker(final int id, final String name, final int[] seqs, final CountDownLatch cdl, final int mboxSize, final Channels.OverflowPolicy mboxPolicy) {
        super(id, name, seqs, cdl);
        mailbox = Channels.newIntChannel(mboxSize, mboxPolicy);
    }

    @Override
    protected int receiveFromMailbox() throws InterruptedException, SuspendExecution {
        return getMailbox().receive();
    }

    @Override
    protected void closeMailbox() {
        getMailbox().close();
    }

    @Override
    protected void sendToNext(final int i) throws InterruptedException, SuspendExecution {
        ((IntMailboxFiberWorker) getNext()).getMailbox().send(i);
    }

    protected IntChannel getMailbox() {
        return mailbox;
    }
}
