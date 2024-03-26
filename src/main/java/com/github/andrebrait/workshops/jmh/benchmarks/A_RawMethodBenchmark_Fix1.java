package com.github.andrebrait.workshops.jmh.benchmarks;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time.
 */
public final class A_RawMethodBenchmark_Fix1 {

    enum Benchmark {
        distance, constant, nothing
    }

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
        Benchmark benchmark = select("Select a benchmark to run:", Benchmark.class);
        Runnable benchmarkMethod = switch (benchmark) {
            case distance -> () -> distance(0, 0, 10, 10);
            case constant -> () -> constant(0, 0, 10, 10);
            case nothing -> A_RawMethodBenchmark_Fix1::nothing;
        };
        bench(benchmark.name(), RUN_MILLIS, LOOP, WARMUP, REPEAT, benchmarkMethod);
    }
}
