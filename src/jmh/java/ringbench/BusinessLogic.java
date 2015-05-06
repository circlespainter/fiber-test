package ringbench;

import com.google.common.collect.ImmutableMap;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author circlespainter
 */
final public class BusinessLogic {
    private static final int arraySize = Integer.parseInt(System.getProperty("ringbench.BusinessLogic.arraySize", "224" /* 128 */));

    private static final int s1Size = Integer.parseInt(System.getProperty("ringbench.BusinessLogic.string1Size", "6"));
    private static final int s2Size = Integer.parseInt(System.getProperty("ringbench.BusinessLogic.string2Size", "4096" /* 2048 */));

    private static char[] CHARSET_AZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static char[] CHARSET_09 = "0123456789".toCharArray();

    public static String randomString(final int length, final char[] charset) {
        Random random = ThreadLocalRandom.current();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(charset.length);
            result[i] = charset[randomCharIndex];
        }
        return new String(result);
    }

    private static final Callable<Integer> RANDOM_INT_FUN = () -> ThreadLocalRandom.current().nextInt();

    private static int[] randomIntArray(final Integer size) {
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

    private static final double randomDouble() { return ThreadLocalRandom.current().nextDouble(); }

    private static final double[] randomDoubleArray(final Integer size) {
        final double[] ret = new double[size];
        for (int i = 0; i < size; i++) {
            try {
                ret[i] = randomDouble();
            } catch (final Exception e) {
                throw new AssertionError(e);
            }
        }
        return ret;
    };

    private static final int[] ia = randomIntArray(arraySize);
    private static final double[] da = randomDoubleArray(arraySize);
    private static final String s1 = randomString(s1Size, CHARSET_09);
    private static final String s2 = randomString(s2Size, CHARSET_AZ);
    private static final Matcher m = Pattern.compile(s1).matcher(s2);

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
        bh.consume(randomDouble());
    };

    public static final Work RANDOM_SQRT = (final Blackhole bh) -> {
        bh.consume(Math.sqrt(randomDouble()));
    };

    public static final Work RANDOM_DOUBLE_ARRAY = (final Blackhole bh) -> {
        bh.consume(randomDoubleArray(arraySize));
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

    public final Work STRING_MATCH = (final Blackhole bh) -> {
        bh.consume(m.find());
    };

    public static final Work RANDOM_DOUBLE_ARRAY_SORT = (final Blackhole bh) -> {
        final double[] a = randomDoubleArray(arraySize);
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
            .put("randomDoubleArraySort", RANDOM_DOUBLE_ARRAY_SORT)
            .put("stringMatch", STRING_MATCH).build();
    }

    private static final String defaultWorkKey = "randomSqrt";

    private static final String bl = System.getProperty("businessLogic", defaultWorkKey);

    public Work get() {
        return getWorkMap().get(bl);
    }
}
