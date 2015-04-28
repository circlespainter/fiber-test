package ringbench.quasar.actors;

import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class QuasarActorRingBenchmark extends AbstractQuasarActorRingBenchmark {
    @Override
    protected QuasarActor newActor(final int j, final int[] sequence, final CountDownLatch cdl, final Blackhole bh) {
        return new QuasarActor(j, sequence, cdl, bh);
    }
}
