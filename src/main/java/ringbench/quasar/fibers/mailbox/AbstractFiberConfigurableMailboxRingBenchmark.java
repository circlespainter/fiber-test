package ringbench.quasar.fibers.mailbox;

import co.paralleluniverse.strands.channels.Channels;
import ringbench.quasar.fibers.AbstractFiberRingBenchmark;

/**
 * Created by fabio on 4/19/15.
 */
public abstract class AbstractFiberConfigurableMailboxRingBenchmark<F extends AbstractMailboxFiberRingWorker> extends AbstractFiberRingBenchmark<F> {
    protected abstract String getFiberBaseName();
    protected abstract Channels.OverflowPolicy getMailboxPolicy();
    protected abstract int getMailboxSize();
}
