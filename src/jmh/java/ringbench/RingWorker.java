package ringbench;

import org.openjdk.jmh.infra.Blackhole;

/**
 * @author circlespainter
 */
public interface RingWorker {
    default void doWork(final Blackhole bh) throws Exception {
        if (bh != null) BusinessLogic.getBusinessLogic().doIt(bh);
    }
}
