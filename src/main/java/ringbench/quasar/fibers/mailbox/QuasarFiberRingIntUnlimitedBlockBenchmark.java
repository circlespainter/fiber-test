package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.strands.channels.Channels;

/**
 * @author circlespainter
 */
public class QuasarFiberRingIntUnlimitedBlockBenchmark extends AbstractFiberRingIntMailboxBenchmark {
    @Override
    protected String getFiberBaseName() {
        return "IntUnlimitedBlock";
    }

    @Override
    protected Channels.OverflowPolicy getMailboxPolicy() {
        return Channels.OverflowPolicy.BLOCK;
    }

    @Override
    protected int getMailboxSize() {
        return -1;
    }
}
