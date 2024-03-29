package com.github.andrebrait.workshops.jmh.benchmarks;

import com.github.andrebrait.workshops.jmh.domain.*;
import com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy
 * and direct object usage to avoid using {@link Runnable}.
 *
 * <p>This demonstrates the problem does not lie in using Runnables or other functional interfaces.
 */
public final class D_PointDirectBenchmark {

    private static final long RUN_MILLIS = 2000;
    private static final int REPEAT = 3;
    private static final int WARMUP = 2;
    private static final int LOOP = 10_000;

    /**
     * A copy of {@link BenchmarkFramework#bench(String, long, int, int, int, com.github.andrebrait.workshops.jmh.framework.BenchRunnable)}
     * that skips the creation of a Runnable.
     */
    private static void bench(
            String name, long runMillis, int loop, int warmup, int repeat, Point point) {
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
                    point.distance(point);
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

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench("point_accurate", RUN_MILLIS, LOOP, WARMUP, REPEAT, new AccuratePoint(10, 10));
        bench("point_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, new FastPoint(10, 10));
        bench("point_super_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, new SuperFastPoint(10, 10));
        bench("point_fixed", RUN_MILLIS, LOOP, WARMUP, REPEAT, new FixedPoint(10, 10));
    }
}
