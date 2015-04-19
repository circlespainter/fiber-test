package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.strands.channels.Channels;

/**
 * @author circlespainter
 */
public class QuasarFiberRingObjectTransferBenchmark extends AbstractFiberRingObjectMailboxBenchmark {
    @Override
    protected String getFiberBaseName() {
        return "ObjectTransfer";
    }

    @Override
    protected Channels.OverflowPolicy getMailboxPolicy() {
        return Channels.OverflowPolicy.BLOCK;
    }

    @Override
    protected int getMailboxSize() {
        return 0;
    }
}
