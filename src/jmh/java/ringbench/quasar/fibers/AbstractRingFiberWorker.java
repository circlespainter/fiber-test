package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;

/**
 * @author ci
 */
public abstract class AbstractRingFiberWorker<WorkerHandle> extends Fiber<Integer> {
    protected WorkerHandle self;
    protected WorkerHandle next;

    public AbstractRingFiberWorker(final FiberScheduler scheduler, final String name) {
        super(name, scheduler);
    }

    public AbstractRingFiberWorker(final FiberScheduler scheduler, final String name, final WorkerHandle self) {
        this(scheduler, name);
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
