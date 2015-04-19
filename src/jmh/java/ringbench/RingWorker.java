package ringbench;

/**
 * Created by fabio on 4/19/15.
 */
public interface RingWorker {
    RingWorker getNext();
    void setNext(final RingWorker rw);
}
