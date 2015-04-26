package ringbench.quasar.actors;

import co.paralleluniverse.actors.Actor;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.annotations.Benchmark;
import ringbench.AbstractRingBenchmark;
import ringbench.RingBenchmarkSupport;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class QuasarActorRingBenchmark extends AbstractRingBenchmark<ActorRef> {
    protected static class InternalActor extends Actor<Integer, Void> {
        private final int id;
        private final int[] sequences;
        private final CountDownLatch cdl;

        protected ActorRef next = null;

        public InternalActor(final int id, final int[] seqs, final CountDownLatch latch) {
            super(String.format("%s-%s-%d",
                    QuasarActorRingBenchmark.class.getSimpleName(),
                    InternalActor.class.getSimpleName(), id), null);
            this.id = id;
            this.sequences = seqs;
            this.cdl = latch;
        }

        @Override protected Void doRun() throws InterruptedException, SuspendExecution {
            Integer sequence = Integer.MAX_VALUE;
            while (sequence > 0) {
                Integer message = receive();
                sequence = message;
                next.send(sequence - 1);
            }
            sequences[id] = sequence;
            cdl.countDown();
            return null;
        }
    }

    @Override
    protected ActorRef[][] setupWorkers(int[][] sequences, CountDownLatch cdl) {
        final ActorRef[][] actorRefs = new ActorRef[sequences.length][sequences.length >= 0 ? sequences[0].length : 0];

        for (int i = 0; i < sequences.length; i++) {
            final ActorRef[] refs = actorRefs[i];
            final int len = refs.length;
            final InternalActor[] actors = new InternalActor[len];

            for (int j = 0; j < len; j++) {
                final InternalActor actor = new InternalActor(j, sequences[i], cdl);
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
    protected void startWorkers(ActorRef[] workers) {
        // NOP, already started
    }

    @Override
    protected void startRing(ActorRef first) throws SuspendExecution {
        first.send(ringSize);
    }
}
