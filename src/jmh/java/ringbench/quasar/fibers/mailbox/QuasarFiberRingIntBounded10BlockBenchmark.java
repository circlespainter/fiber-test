package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.strands.channels.Channels;

/**
 * @author circlespainter
 */
public class QuasarFiberRingIntBounded10BlockBenchmark extends AbstractFiberRingIntMailboxBenchmark {
    @Override
    protected String getFiberBaseName() {
        return "IntBounded10Block";
    }

    @Override
    protected Channels.OverflowPolicy getMailboxPolicy() {
        return Channels.OverflowPolicy.BLOCK;
    }

    @Override
    protected int getMailboxSize() {
        return 10;
    }
}
