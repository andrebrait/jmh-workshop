package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.framework.SystemInfoUtils;

import static com.github.andrebrait.workshops.jmh.framework.Benchmark.*;

/**
 * "Naive" benchmark with just static methods.
 *
 * <p>Results:
 *
 * <pre>
 *  JVM info:
 *      Name: OpenJDK 64-Bit Server VM
 *      Vendor: Eclipse Adoptium
 *      Version: 17.0.10+7
 *      Architecture: amd64
 *
 *  CPU info:
 *      AMD Ryzen 9 5900X 12-Core Processor
 *       1 physical CPU package(s)
 *       12 physical CPU core(s)
 *       24 logical CPU(s)
 *      Identifier: AuthenticAMD Family 25 Model 33 Stepping 0
 *      Microarchitecture: Zen 3
 *
 *  OS info:
 *      Microsoft Windows 11 build 22631
 *
 *  Running: distance
 *  124859242 ops/ms  (warmup) |
 *  127379222 ops/ms  (warmup) |
 *  127837400 ops/ms  |
 *  127466947 ops/ms  |
 *  127366825 ops/ms  |
 *  [ ~127557057 ops/ms ]
 *
 *  Running: constant
 *  2301262 ops/ms  (warmup) |
 *  2295597 ops/ms  (warmup) |
 *  2287942 ops/ms  |
 *  2300107 ops/ms  |
 *  2304935 ops/ms  |
 *  [ ~2297661 ops/ms ]
 *
 *  Running: nothing
 *  662167 ops/ms  (warmup) |
 *  661840 ops/ms  (warmup) |
 *  661307 ops/ms  |
 *  663707 ops/ms  |
 *  663715 ops/ms  |
 *  [ ~662909 ops/ms ]
 * </pre>
 */
public final class A_RawMethodBenchmark {

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    private static double constant(double x1, double y1, double x2, double y2) {
        return 0.0d;
    }

    private static void nothing() {
    }

    public static void main(String[] args) {
        SystemInfoUtils.printSystemInfo();
        bench("distance", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> distance(0, 0, 10, 10));
        bench("constant", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> constant(0, 0, 10, 10));
        bench("nothing", RUN_MILLIS, LOOP, WARMUP, REPEAT, A_RawMethodBenchmark::nothing);
    }
}
