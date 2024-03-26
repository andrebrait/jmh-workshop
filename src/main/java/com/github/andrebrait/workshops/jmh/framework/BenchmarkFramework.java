package com.github.andrebrait.workshops.jmh.framework;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * "Naive" Benchmarking Framework used as an example on how <em>not</em> to do benchmarking in a JVM.
 *
 * <p>This just runs a provided {@link Runnable} and prints out the number of executions per millisecond.
 * The number of executions (loops) per time measurement can be tuned to emphasize certain aspects of the
 * benchmark, as the time measurement can be expensive compared to the benchmarked code and thus could mask
 * its performance characteristics.
 *
 * <p>Reference: <a href="https://www.oracle.com/technical-resources/articles/java/architect-benchmarking.html">Avoiding Benchmarking Pitfalls on the JVM</a>.
 */
public final class BenchmarkFramework {

    public static final long RUN_MILLIS = 2000;
    public static final int REPEAT = 3;
    public static final int WARMUP = 2;
    public static final int LOOP = 10_000;

    /**
     * Runs a given benchmark and print the statistics.
     *
     * @param name      the name of the benchmark (for display purposes)
     * @param runMillis the duration of the benchmark (in milliseconds)
     * @param loop      the number of times to execute the benchmarked code per time measurement
     * @param warmup    how many runs to execute before taking measurements
     * @param repeat    how many times to repeat the benchmark in total (after the warmup)
     * @param benchmark a {@link Runnable} which will contain the benchmarked code
     */
    public static void bench(
            String name, long runMillis, int loop, int warmup, int repeat, Runnable benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long average = 0L;
        for (int i = 0; i < max; i++) {
            long numOperations = 0L;
            long duration = 0L;
            long start = System.currentTimeMillis();
            while (duration < runMillis) {
                for (int j = 0; j < loop; j++) {
                    benchmark.run();
                    numOperations++;
                }
                duration = System.currentTimeMillis() - start;
            }
            long throughput = numOperations / duration;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                average = average + throughput;
            }
            System.out.printf("%d ops/ms %s%n", throughput, (!benchRun ? " (warmup) | " : " | "));
        }
        average = average / repeat;
        System.out.printf("[ ~%d ops/ms ]%n%n", average);
    }

    /**
     * Runs a given benchmark and print the statistics.
     *
     * @param name         the name of the benchmark (for display purposes)
     * @param runMillis    the duration of the benchmark (in milliseconds)
     * @param loop         the number of times to execute the benchmarked code per time measurement
     * @param warmup       how many runs to execute before taking measurements
     * @param repeat       how many times to repeat the benchmark in total (after the warmup)
     * @param argsSupplier supplier of arguments used for the benchmark, called once per trial
     * @param benchmark    a {@link Consumer<A>} which will contain the benchmarked code
     * @param <A>          the argument type taken by the benchmark
     */
    public static <A> void bench(
            String name,
            long runMillis,
            int loop,
            int warmup,
            int repeat,
            Supplier<A> argsSupplier,
            Consumer<A> benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long average = 0L;
        for (int i = 0; i < max; i++) {
            A args = argsSupplier.get();
            long numOperations = 0L;
            long duration = 0L;
            long start = System.currentTimeMillis();
            while (duration < runMillis) {
                for (int j = 0; j < loop; j++) {
                    benchmark.accept(args);
                    numOperations++;
                }
                duration = System.currentTimeMillis() - start;
            }
            long throughput = numOperations / duration;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                average = average + throughput;
            }
            System.out.printf("%d ops/ms %s%n", throughput, (!benchRun ? " (warmup) | " : " | "));
        }
        average = average / repeat;
        System.out.printf("[ ~%d ops/ms ]%n%n", average);
    }

    /**
     * Runs a given benchmark and print the statistics.
     *
     * @param name         the name of the benchmark (for display purposes)
     * @param runMillis    the duration of the benchmark (in milliseconds)
     * @param loop         the number of times to execute the benchmarked code per time measurement
     * @param warmup       how many runs to execute before taking measurements
     * @param repeat       how many times to repeat the benchmark in total (after the warmup)
     * @param argsSupplier supplier of arguments used for the benchmark, called once per trial
     * @param blackhole    a {@link Consumer} that will consume the values returned by the benchmark
     * @param benchmark    a {@link Function} which will contain the benchmarked code
     * @param <A>          the argument type taken by the benchmark
     * @param <R>          the type returned by the benchmark and consumed by the blackhole
     */
    public static <A, R> void bench(
            String name,
            long runMillis,
            int loop,
            int warmup,
            int repeat,
            Supplier<A> argsSupplier,
            Consumer<R> blackhole,
            Function<A, R> benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long average = 0L;
        for (int i = 0; i < max; i++) {
            A args = argsSupplier.get();
            long numOperations = 0L;
            long duration = 0L;
            long start = System.currentTimeMillis();
            while (duration < runMillis) {
                for (int j = 0; j < loop; j++) {
                    blackhole.accept(benchmark.apply(args));
                    numOperations++;
                }
                duration = System.currentTimeMillis() - start;
            }
            long throughput = numOperations / duration;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                average = average + throughput;
            }
            System.out.printf("%d ops/ms %s%n", throughput, (!benchRun ? " (warmup) | " : " | "));
        }
        average = average / repeat;
        System.out.printf("[ ~%d ops/ms ]%n%n", average);
    }

    private BenchmarkFramework() {
        // this is a utility class
    }
}
