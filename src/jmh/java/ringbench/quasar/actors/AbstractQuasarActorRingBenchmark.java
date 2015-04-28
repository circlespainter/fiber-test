package ringbench.quasar.actors;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.infra.Blackhole;
import ringbench.AbstractRingBenchmark;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public abstract class AbstractQuasarActorRingBenchmark extends AbstractRingBenchmark<ActorRef> {

    @Override
    protected ActorRef[][] setupWorkers(final int[][] sequences, CountDownLatch cdl, final Blackhole bh) {
        final ActorRef[][] actorRefs = new ActorRef[sequences.length][sequences.length >= 0 ? sequences[0].length : 0];

        for (int i = 0; i < sequences.length; i++) {
            final ActorRef[] refs = actorRefs[i];
            final int len = refs.length;
            final QuasarActor[] actors = new QuasarActor[len];

            for (int j = 0; j < len; j++) {
                final QuasarActor actor = newActor(j, sequences[i], cdl, bh);
                actors[j] = actor;
                refs[j] = actor.spawn();
            }

            // Set next actor pointers.
            for (int j = 0; j < len; j++)
                actors[j].next = refs[(j+1) % workerCount];
        }

        return actorRefs;
    }

    @Override
    protected void startRing(ActorRef first) throws SuspendExecution {
        first.send(ringSize);
    }

    protected abstract QuasarActor newActor(final int j, final int[] sequence, final CountDownLatch cdl, final Blackhole bh);
}
