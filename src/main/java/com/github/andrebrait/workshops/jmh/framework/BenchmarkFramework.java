package com.github.andrebrait.workshops.jmh.framework;

import java.util.function.Consumer;

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
    public static final int REPEAT = 2;
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
            String name,
            long runMillis,
            int loop,
            int warmup,
            int repeat,
            BenchRunnable benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long averageThroughput = 0L;
        double averageTimePerOp = 0.0d;
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
            double timePerOp = (duration * 1E6) / numOperations;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                averageThroughput += throughput;
                averageTimePerOp += timePerOp;
            }
            System.out.printf(
                    "%d ops/ms (%.2f ns/op)%s%n",
                    throughput,
                    timePerOp,
                    (!benchRun ? " (warmup) | " : " | "));
        }
        averageThroughput /= repeat;
        averageTimePerOp /= repeat;
        System.out.printf("[ ~%d ops/ms (%.2f ns/op)]%n%n", averageThroughput, averageTimePerOp);
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
            BenchSupplier<A> argsSupplier,
            BenchConsumer<A> benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long averageThroughput = 0L;
        double averageTimePerOp = 0.0d;
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
            double timePerOp = (duration * 1E6) / numOperations;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                averageThroughput += throughput;
                averageTimePerOp += timePerOp;
            }
            System.out.printf(
                    "%d ops/ms (%.2f ns/op)%s%n",
                    throughput,
                    timePerOp,
                    (!benchRun ? " (warmup) | " : " | "));
        }
        averageThroughput /= repeat;
        averageTimePerOp /= repeat;
        System.out.printf("[ ~%d ops/ms (%.2f ns/op)]%n%n", averageThroughput, averageTimePerOp);
    }

    /**
     * Runs a given benchmark and print the statistics.
     *
     * @param name           the name of the benchmark (for display purposes)
     * @param runMillis      the duration of the benchmark (in milliseconds)
     * @param loop           the number of times to execute the benchmarked code per time measurement
     * @param warmup         how many runs to execute before taking measurements
     * @param repeat         how many times to repeat the benchmark in total (after the warmup)
     * @param argsSupplier   supplier of arguments used for the benchmark, called once per trial
     * @param resultConsumer a consumer that will consume the values returned by the benchmark
     * @param benchmark      a function which will contain the benchmarked code
     * @param <A>            the argument type taken by the benchmark
     * @param <R>            the type returned by the benchmark and consumed by the resultConsumer
     */
    public static <A, R> void bench(
            String name,
            long runMillis,
            int loop,
            int warmup,
            int repeat,
            BenchSupplier<A> argsSupplier,
            BenchConsumer<R> resultConsumer,
            BenchFunction<A, R> benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long averageThroughput = 0L;
        double averageTimePerOp = 0.0d;
        for (int i = 0; i < max; i++) {
            A args = argsSupplier.get();
            long numOperations = 0L;
            long duration = 0L;
            long start = System.currentTimeMillis();
            while (duration < runMillis) {
                for (int j = 0; j < loop; j++) {
                    resultConsumer.accept(benchmark.apply(args));
                    numOperations++;
                }
                duration = System.currentTimeMillis() - start;
            }
            long throughput = numOperations / duration;
            double timePerOp = (duration * 1E6) / numOperations;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                averageThroughput += throughput;
                averageTimePerOp += timePerOp;
            }
            System.out.printf(
                    "%d ops/ms (%.2f ns/op)%s%n",
                    throughput,
                    timePerOp,
                    (!benchRun ? " (warmup) | " : " | "));
        }
        averageThroughput /= repeat;
        averageTimePerOp /= repeat;
        System.out.printf("[ ~%d ops/ms (%.2f ns/op)]%n%n", averageThroughput, averageTimePerOp);
    }

    /**
     * Runs a given benchmark and print the statistics, avoiding boxing/unboxing costs.
     *
     * @param name           the name of the benchmark (for display purposes)
     * @param runMillis      the duration of the benchmark (in milliseconds)
     * @param loop           the number of times to execute the benchmarked code per time measurement
     * @param warmup         how many runs to execute before taking measurements
     * @param repeat         how many times to repeat the benchmark in total (after the warmup)
     * @param argsSupplier   supplier of arguments used for the benchmark, called once per trial
     * @param resultConsumer a consumer that will consume the values returned by the benchmark
     * @param benchmark      a function which will contain the benchmarked code
     * @param <A>            the argument type taken by the benchmark
     */
    public static <A> void benchDouble(
            String name,
            long runMillis,
            int loop,
            int warmup,
            int repeat,
            BenchSupplier<A> argsSupplier,
            BenchDoubleConsumer resultConsumer,
            BenchToDoubleFunction<A> benchmark) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long averageThroughput = 0L;
        double averageTimePerOp = 0.0d;
        for (int i = 0; i < max; i++) {
            A args = argsSupplier.get();
            long numOperations = 0L;
            long duration = 0L;
            long start = System.currentTimeMillis();
            while (duration < runMillis) {
                for (int j = 0; j < loop; j++) {
                    resultConsumer.accept(benchmark.applyAsDouble(args));
                    numOperations++;
                }
                duration = System.currentTimeMillis() - start;
            }
            long throughput = numOperations / duration;
            double timePerOp = (duration * 1E6) / numOperations;
            boolean benchRun = i >= warmup;
            if (benchRun) {
                averageThroughput += throughput;
                averageTimePerOp += timePerOp;
            }
            System.out.printf(
                    "%d ops/ms (%.2f ns/op)%s%n",
                    throughput,
                    timePerOp,
                    (!benchRun ? " (warmup) | " : " | "));
        }
        averageThroughput /= repeat;
        averageTimePerOp /= repeat;
        System.out.printf("[ ~%d ops/ms (%.2f ns/op)]%n%n", averageThroughput, averageTimePerOp);
    }

    private BenchmarkFramework() {
        // this is a utility class
    }
}
