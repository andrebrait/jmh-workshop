package com.github.andrebrait.workshops.jmh.presentation;

import com.github.andrebrait.workshops.jmh.framework.BenchRunnable;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time.
 */
public final class SuperDuperBenchmark_Fix1 {

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        BenchmarkName benchmark = args.length == 1 ?
                BenchmarkName.valueOf(args[0]) :
                select("Select a benchmark to run:", BenchmarkName.class);
        BenchRunnable benchmarkMethod = switch (benchmark) {
            case allan -> () -> Solutions.allan(0.0d, 0.0d, 10.0d, 10.0d);
            case bob -> () -> Solutions.bob(0.0d, 0.0d, 10.0d, 10.0d);
            case joe -> () -> Solutions.joe(0.0d, 0.0d, 10.0d, 10.0d);
            case steve -> () -> Solutions.steve(0.0d, 0.0d, 10.0d, 10.0d);
        };
        bench(benchmark.name(), RUN_MILLIS, LOOP, WARMUP, REPEAT, benchmarkMethod);
    }
}
