package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.AccuratePoint;
import com.github.andrebrait.workshops.jmh.domain.FastPoint;
import com.github.andrebrait.workshops.jmh.domain.Point;
import com.github.andrebrait.workshops.jmh.domain.SuperFastPoint;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy
 * and direct object usage to avoid using {@link Runnable}
 */
public final class D_PointDirectBenchmark {

    private static final long RUN_MILLIS = 4000;
    private static final int REPEAT = 3;
    private static final int WARMUP = 2;
    private static final int LOOP = 10_000;

    private static void bench(
            String name, long runMillis, int loop, int warmup, int repeat, Point point) {
        System.out.printf("Running: %s%n", name);
        int max = repeat + warmup;
        long average = 0L;
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
            boolean benchRun = i >= warmup;
            if (benchRun) {
                average = average + throughput;
            }
            System.out.printf("%d ops/ms %s%n", throughput, (!benchRun ? " (warmup) | " : " | "));
        }
        average = average / repeat;
        System.out.printf("[ ~%d ops/ms ]%n%n", average);
    }

    public static void main(String[] args) {
        bench("point_accurate", RUN_MILLIS, LOOP, WARMUP, REPEAT, new AccuratePoint(10, 10));
        bench("point_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, new FastPoint(10, 10));
        bench("point_super_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, new SuperFastPoint(10, 10));
    }
}
