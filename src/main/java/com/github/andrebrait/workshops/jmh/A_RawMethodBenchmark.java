package com.github.andrebrait.workshops.jmh;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * "Naive" benchmark with just static methods.
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
        //SystemInfoUtils.printSystemInfo();
        bench("distance", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> distance(0, 0, 10, 10));
        bench("constant", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> constant(0, 0, 10, 10));
        bench("nothing", RUN_MILLIS, LOOP, WARMUP, REPEAT, A_RawMethodBenchmark::nothing);
    }
}
