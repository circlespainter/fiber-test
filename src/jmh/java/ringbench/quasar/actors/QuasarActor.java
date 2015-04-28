package ringbench.quasar.actors;

import co.paralleluniverse.actors.Actor;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.MailboxConfig;
import co.paralleluniverse.fibers.SuspendExecution;
import org.openjdk.jmh.infra.Blackhole;
import ringbench.RingWorker;

import java.util.concurrent.CountDownLatch;

/**
 * @author riclespainter
 */
class QuasarActor extends Actor<Integer, Void> implements RingWorker {
    public static final MailboxConfig defaultMailboxConf = new MailboxConfig(-1, null);

    private final int id;
    private final int[] sequences;
    private final CountDownLatch cdl;
    private final Blackhole blackHole;

    protected ActorRef next = null;

    public QuasarActor(final int id, final int[] seqs, final CountDownLatch latch, final MailboxConfig mailboxConf, final Blackhole bh) {
        super(String.format("%s-%s-%d",
                AbstractQuasarActorRingBenchmark.class.getSimpleName(),
                QuasarActor.class.getSimpleName(), id), mailboxConf != null ? mailboxConf : defaultMailboxConf);
        this.id = id;
        this.sequences = seqs;
        this.cdl = latch;
        this.blackHole = bh;
    }

    public QuasarActor(final int id, final int[] seqs, final CountDownLatch latch, final Blackhole bh) {
        this(id, seqs, latch, defaultMailboxConf, bh);
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        Integer sequence = Integer.MAX_VALUE;
        while (sequence > 0) {
            Integer message = receive();
            sequence = message;
            try {
                doWork(blackHole);
            } catch (final Exception e) {
                throw new AssertionError(e);
            }
            next.send(sequence - 1);
        }
        sequences[id] = sequence;
        cdl.countDown();
        return null;
    }
}
