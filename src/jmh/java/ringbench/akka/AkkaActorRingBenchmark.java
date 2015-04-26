package ringbench.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.openjdk.jmh.annotations.Benchmark;
import ringbench.RingBenchmarkSupport;

import java.util.concurrent.CountDownLatch;

/**
 * Ring benchmark using Akka actors.
 *
 * Internally actors use a {@link java.util.concurrent.CountDownLatch} to
 * notify the completion of the ring.
 */
public class AkkaActorRingBenchmark extends RingBenchmarkSupport {
    protected static class InternalActor extends UntypedActor {
        protected final int id;
        protected final int[] sequences;
        protected final CountDownLatch latch;
        protected ActorRef next = null;

        public InternalActor(final int id, final int[] sequences, final CountDownLatch latch) {
            this.id = id;
            this.sequences = sequences;
            this.latch = latch;
        }

        @Override public void onReceive(final Object message) throws Exception {
            if (message instanceof Integer) {
                final int sequence = (Integer) message;
                if (sequence < 1) {
                    sequences[id] = sequence;
                    latch.countDown();
                    getContext().stop(getSelf());
                }
                next.tell(sequence - 1, getSelf());
            }
            else if (message instanceof ActorRef) next = (ActorRef) message;
            else unhandled(message);
        }
    }

    @Benchmark public int[][] ringBenchmark() throws Exception {
        // Create an actor system and a shutdown latch.
        final ActorSystem system = ActorSystem.create(AkkaActorRingBenchmark.class.getSimpleName() + "System");
        final CountDownLatch latch = new CountDownLatch(workerCount);

        // Create actors.
        final int[][] sequences = new int[rings][workerCount];
        final ActorRef[][] actors = new ActorRef[rings][workerCount];

        for(int i = 0; i < rings; i++) {
            for (int j = 0; j < workerCount; j++)
                actors[i][j] = system.actorOf(
                        Props.create(InternalActor.class, j, sequences[i], latch),
                        String.format("%s-%d-%d", AkkaActorRingBenchmark.class.getSimpleName(), i, j));

            // Set next actor pointers.
            for (int j = 0; j < workerCount; j++)
                actors[i][j].tell(actors[i][(j + 1) % workerCount], null);
        }

        for(int i = 0; i < rings; i++) {
            // Initiate the rings.
            actors[i][0].tell(ringSize, null);
        }

        // Wait for the latch.
        latch.await();

        system.shutdown();

        return sequences;
    }

    public static void main(String[] args) throws Exception {
        new AkkaActorRingBenchmark().ringBenchmark();
    }
}
