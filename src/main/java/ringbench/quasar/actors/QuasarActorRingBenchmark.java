package ringbench.quasar.actors;

import co.paralleluniverse.actors.Actor;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.annotations.Benchmark;
import ringbench.RingBenchmarkSupport;

import java.util.concurrent.CountDownLatch;

/**
 * @author circlespainter
 */
public class QuasarActorRingBenchmark extends RingBenchmarkSupport {
    protected static class InternalActor extends Actor {
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

        @Override protected Object doRun() throws InterruptedException, SuspendExecution {
            int sequence = Integer.MAX_VALUE;
            while (sequence > 0) {
                Object message = receive();
                if (message instanceof Integer) {
                    sequence = (Integer) message;
                    next.send(sequence - 1);
                }
            }
            sequences[id] = sequence;
            cdl.countDown();
            return sequence;
        }
    }

    @Benchmark public int[] ringBenchmark() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(workerCount);

        int[] sequences = new int[workerCount];
        // Create and start actors.
        final InternalActor[] actors = new InternalActor[workerCount];
        final ActorRef[] actorRefs = new ActorRef[workerCount];
        for (int i = 0; i < workerCount; i++) {
            InternalActor actor = new InternalActor(i, sequences, cdl);
            actors[i] = actor;
            actorRefs[i] = actor.spawn();
        }

        // Set next actor pointers.
        for (int i = 0; i < workerCount; i++)
            actors[i].next = actorRefs[(i+1) % workerCount];

        // Initiate the ring.
        actorRefs[0].send(ringSize);

        // Wait for actors to finish and collect the results.
        cdl.await();
        return sequences;
    }

    public static void main(final String[] args) throws Exception {
        new QuasarActorRingBenchmark().ringBenchmark();
    }
}
