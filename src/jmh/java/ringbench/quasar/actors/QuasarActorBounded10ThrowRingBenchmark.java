package ringbench.quasar.actors;

import co.paralleluniverse.actors.MailboxConfig;
import co.paralleluniverse.strands.channels.Channels;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class QuasarActorBounded10ThrowRingBenchmark extends AbstractQuasarActorRingBenchmark {
    @Override
    protected QuasarActor newActor(final int j, final int[] sequence, final CountDownLatch cdl, final Blackhole bh) {
        return new QuasarActor(j, sequence, cdl, new MailboxConfig(10, Channels.OverflowPolicy.THROW), bh);
    }
}
