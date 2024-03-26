package com.github.andrebrait.workshops.jmh.presentation;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time.
 */
public final class SuperDuperBenchmark_Fix1 {

    enum Benchmark {
        bob, joe, steve
    }

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        Benchmark benchmark = select("Select a benchmark to run:", Benchmark.class);
        Runnable benchmarkMethod = switch (benchmark) {
            case bob -> () -> Solutions.bob(0, 0, 10, 10);
            case joe -> () -> Solutions.joe(0, 0, 10, 10);
            case steve -> () -> Solutions.steve(0, 0, 10, 10);
        };
        bench(benchmark.name(), RUN_MILLIS, LOOP, WARMUP, REPEAT, benchmarkMethod);
    }
}
