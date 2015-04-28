package ringbench.quasar.fibers;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import org.openjdk.jmh.infra.Blackhole;
import ringbench.BusinessLogic;
import ringbench.RingWorker;

/**
 * @author circlespainter
 */
public abstract class AbstractRingFiberWorker<WorkerHandle> extends Fiber<Integer> implements RingWorker {
    protected WorkerHandle self;
    protected WorkerHandle next;
    public Blackhole blackHole;

    public AbstractRingFiberWorker(final FiberScheduler scheduler, final String name, final Blackhole bh) {
        super(name, scheduler);
        blackHole = bh;
    }

    public AbstractRingFiberWorker(final FiberScheduler scheduler, final String name, final WorkerHandle self, final Blackhole bh) {
        this(scheduler, name, bh);
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
