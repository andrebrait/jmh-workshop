package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.AccuratePoint;
import com.github.andrebrait.workshops.jmh.domain.FastPoint;
import com.github.andrebrait.workshops.jmh.domain.Point;
import com.github.andrebrait.workshops.jmh.domain.SuperFastPoint;
import com.github.andrebrait.workshops.jmh.framework.Benchmark;
import com.github.andrebrait.workshops.jmh.framework.SystemInfoUtils;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy
 * and direct object usage to avoid using {@link Runnable}.
 *
 * <p>Results:
 *
 * <pre>
 *  JVM info:
 *  	Name: OpenJDK 64-Bit Server VM
 *  	Vendor: Eclipse Adoptium
 *  	Version: 17.0.10+7
 *  	Architecture: amd64
 *
 *  CPU info:
 *  	AMD Ryzen 9 5900X 12-Core Processor
 *  	 1 physical CPU package(s)
 *  	 12 physical CPU core(s)
 *  	 24 logical CPU(s)
 *  	Identifier: AuthenticAMD Family 25 Model 33 Stepping 0
 *  	Microarchitecture: Zen 3
 *
 *  OS info:
 *  	Microsoft Windows 11 build 22631
 *
 *  Running: point_accurate
 *  125815442 ops/ms  (warmup) |
 *  127247990 ops/ms  (warmup) |
 *  127536500 ops/ms  |
 *  127459450 ops/ms  |
 *  127934105 ops/ms  |
 *  [ ~127643351 ops/ms ]
 *
 *  Running: point_fast
 *  2302165 ops/ms  (warmup) |
 *  2313612 ops/ms  (warmup) |
 *  2307690 ops/ms  |
 *  2313135 ops/ms  |
 *  2310977 ops/ms  |
 *  [ ~2310600 ops/ms ]
 *
 *  Running: point_super_fast
 *  577145 ops/ms  (warmup) |
 *  577717 ops/ms  (warmup) |
 *  576392 ops/ms  |
 *  579802 ops/ms  |
 *  577787 ops/ms  |
 *  [ ~577993 ops/ms ]
 * </pre>
 */
public final class D_PointDirectBenchmark {

    private static final long RUN_MILLIS = 4000;
    private static final int REPEAT = 3;
    private static final int WARMUP = 2;
    private static final int LOOP = 10_000;

    /**
     * A copy of {@link Benchmark#bench(String, long, int, int, int, Runnable)} that skips the creation of a Runnable.
     */
    private static void bench(String name, long runMillis, int loop, int warmup, int repeat, Point point) {
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
        SystemInfoUtils.printSystemInfo();
        bench("point_accurate", RUN_MILLIS, LOOP, WARMUP, REPEAT, new AccuratePoint(10, 10));
        bench("point_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, new FastPoint(10, 10));
        bench("point_super_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, new SuperFastPoint(10, 10));
    }
}
