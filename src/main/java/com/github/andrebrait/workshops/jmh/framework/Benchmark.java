package com.github.andrebrait.workshops.jmh.framework;

/**
 * "Naive" Benchmarking Framework used as an example on how <em>not</em> to do benchmarking in a JVM
 *
 * <p><a href=https://www.oracle.com/technical-resources/articles/java/architect-benchmarking.html>Reference</a>
 */
public final class Benchmark {

    public static final long RUN_MILLIS = 4000;
    public static final int REPEAT = 3;
    public static final int WARMUP = 2;
    public static final int LOOP = 10_000;

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

    private Benchmark() {
        // this is a utility class
    }
}
