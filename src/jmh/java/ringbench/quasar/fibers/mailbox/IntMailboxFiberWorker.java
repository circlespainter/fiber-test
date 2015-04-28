package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class IntMailboxFiberWorker extends AbstractMailboxFiberRingWorker<IntChannel> {
    public IntMailboxFiberWorker(final FiberScheduler scheduler, final int id, final String name, final int[] seqs, final CountDownLatch cdl, final int mboxSize, final Channels.OverflowPolicy mboxPolicy, final Blackhole bh) {
        super(scheduler, id, name, seqs, cdl, Channels.newIntChannel(mboxSize, mboxPolicy), bh);
    }

    @Override
    protected Integer receiveFromMailbox() throws InterruptedException, SuspendExecution {
        return self.receive();
    }

    @Override
    protected void closeMailbox() {
        self.close();
    }

    @Override
    protected void sendToNext(final Integer i) throws InterruptedException, SuspendExecution {
        next.send(i);
    }
}
