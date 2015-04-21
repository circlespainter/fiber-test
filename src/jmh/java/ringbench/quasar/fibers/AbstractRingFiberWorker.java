package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.Fiber;

/**
 * @author ci
 */
public abstract class AbstractRingFiberWorker<WorkerHandle> extends Fiber<Integer> {
    protected WorkerHandle self;
    protected WorkerHandle next;

    public AbstractRingFiberWorker(final String name) {
        super(name);
    }

    public AbstractRingFiberWorker(final String name, final WorkerHandle self) {
        this(name);
        this.self = self;
    }

    public void setNext(final WorkerHandle rw) {
        this.next = rw;
    }

    public WorkerHandle getNext() {
        return next;
    }

    public WorkerHandle getSelf() {
        return self;
    }
}
