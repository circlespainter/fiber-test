package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.Fiber;
import ringbench.RingWorker;

/**
 * @author ci
 */
public abstract class AbstractRingFiberWorker extends Fiber<Integer> implements RingWorker {
    protected RingWorker next;

    public AbstractRingFiberWorker(String name) {
        super(name);
    }

    @Override
    public void setNext(final RingWorker rw) {
        this.next = rw;
    }

    @Override
    public RingWorker getNext() {
        return next;
    }
}
