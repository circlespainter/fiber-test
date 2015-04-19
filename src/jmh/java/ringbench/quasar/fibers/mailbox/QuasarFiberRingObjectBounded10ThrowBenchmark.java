package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.strands.channels.Channels;

/**
 * @author circlespainter
 */
public class QuasarFiberRingObjectBounded10ThrowBenchmark extends AbstractFiberRingObjectMailboxBenchmark {
    @Override
    protected String getFiberBaseName() {
        return "ObjectBounded10Throw";
    }

    @Override
    protected Channels.OverflowPolicy getMailboxPolicy() {
        return Channels.OverflowPolicy.THROW;
    }

    @Override
    protected int getMailboxSize() {
        return 10;
    }
}
