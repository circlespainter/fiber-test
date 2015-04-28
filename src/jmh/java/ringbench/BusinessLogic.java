package ringbench;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author circlespainter
 */
final public class BusinessLogic {
    private static final int arraySize = Integer.parseInt(System.getProperty("ringbench.BusinessLogic.arraySize", "128"));

    public interface Work {
        void doIt(final Blackhole bh) throws Exception;
    }

    public class Seq implements Work {
        private final Work[] tasks;

        public Seq(final Work... tasks) {
            this.tasks = tasks;
        }

        @Override
        public void doIt(Blackhole bh) throws Exception {
            for (final Work w : tasks) {
                w.doIt(bh);
            }
        }
    }

    private static final Callable<Double> RANDOM_DOUBLE_FUN = () -> Math.sqrt(ThreadLocalRandom.current().nextDouble());

    public static final Work RANDOM_DOUBLE = (final Blackhole bh) -> {
        bh.consume(RANDOM_DOUBLE_FUN.call());
    };

    private static final Function<Integer, double[]> RANDOM_DOUBLE_ARRAY_FUN = (final Integer size) -> {
        final double[] ret = new double[size];
        for (int i = 0; i < size; i++) {
            try {
                ret[i] = RANDOM_DOUBLE_FUN.call();
            } catch (final Exception e) {
                throw new AssertionError(e);
            }
        }
        return ret;
    };

    public static final Work RANDOM_SQRT = (final Blackhole bh) -> {
        bh.consume(Math.sqrt(RANDOM_DOUBLE_FUN.call()));
    };

    public static final Work RANDOM_DOUBLE_ARRAY = (final Blackhole bh) -> {
        bh.consume(RANDOM_DOUBLE_ARRAY_FUN.apply(arraySize));
    };

    public static final Work RANDOM_DOUBLE_ARRAY_SORT = (final Blackhole bh) -> {
        final double[] a = RANDOM_DOUBLE_ARRAY_FUN.apply(arraySize);
        Arrays.sort(a);
        bh.consume(a);
    };

    private static final ImmutableMap<String, Work> workMap =
        new ImmutableMap.Builder<String, Work>()
            .put("randomSqrt", RANDOM_SQRT)
            .put("randomDoubleArray", RANDOM_DOUBLE_ARRAY)
            .put("randomDoubleArraySort", RANDOM_DOUBLE_ARRAY_SORT).build();

    private static String defaultWorkKey = "randomSqrt";

    public static Work getBusinessLogic() {
        return workMap.get(System.getProperty("businessLogic", defaultWorkKey));
    }
}
