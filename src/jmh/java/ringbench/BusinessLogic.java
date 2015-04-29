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
    private static final int arraySize = Integer.parseInt(System.getProperty("ringbench.BusinessLogic.arraySize", "32"));

    private static final Callable<Integer> RANDOM_INT_FUN = () -> ThreadLocalRandom.current().nextInt();

    private static final Function<Integer, int[]> RANDOM_INT_ARRAY_FUN = (final Integer size) -> {
        final int[] ret = new int[size];
        for (int i = 0; i < size; i++) {
            try {
                ret[i] = RANDOM_INT_FUN.call();
            } catch (final Exception e) {
                throw new AssertionError(e);
            }
        }
        return ret;
    };

    private static final Callable<Double> RANDOM_DOUBLE_FUN = () -> ThreadLocalRandom.current().nextDouble();

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

    private static final int[] ia = RANDOM_INT_ARRAY_FUN.apply(arraySize);
    private static final double[] da = RANDOM_DOUBLE_ARRAY_FUN.apply(arraySize);

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

    public static final Work NULL = (final Blackhole bh) -> {};

    public static final Work RANDOM_DOUBLE = (final Blackhole bh) -> {
        bh.consume(RANDOM_DOUBLE_FUN.call());
    };

    public static final Work RANDOM_SQRT = (final Blackhole bh) -> {
        bh.consume(Math.sqrt(RANDOM_DOUBLE_FUN.call()));
    };

    public static final Work RANDOM_DOUBLE_ARRAY = (final Blackhole bh) -> {
        bh.consume(RANDOM_DOUBLE_ARRAY_FUN.apply(arraySize));
    };

    public final Work INT_ARRAY_SORT() {
        return bh -> {
            int[] iac = Arrays.copyOf(ia, ia.length);
            Arrays.sort(iac);
            bh.consume(iac);
        };
    }

    public final Work DOUBLE_ARRAY_SORT() {
        return bh -> {
            double[] dac = Arrays.copyOf(da, da.length);
            Arrays.sort(dac);
            bh.consume(dac);
        };
    }

    public static final Work RANDOM_DOUBLE_ARRAY_SORT = (final Blackhole bh) -> {
        final double[] a = RANDOM_DOUBLE_ARRAY_FUN.apply(arraySize);
        Arrays.sort(a);
        bh.consume(a);
    };

    private ImmutableMap<String, Work> getWorkMap() {
        return new ImmutableMap.Builder<String, Work>()
            .put("null", NULL)
            .put("intArraySort", INT_ARRAY_SORT())
            .put("doubleArraySort", DOUBLE_ARRAY_SORT())
            .put("randomSqrt", RANDOM_SQRT)
            .put("randomDoubleArray", RANDOM_DOUBLE_ARRAY)
            .put("randomDoubleArraySort", RANDOM_DOUBLE_ARRAY_SORT).build();
    }

    private static final String defaultWorkKey = "randomSqrt";

    private static final String bl = System.getProperty("businessLogic", defaultWorkKey);

    public Work bl() {
        return getWorkMap().get(bl);
    }
}
