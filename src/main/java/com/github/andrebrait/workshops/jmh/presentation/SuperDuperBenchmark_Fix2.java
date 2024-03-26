package com.github.andrebrait.workshops.jmh.presentation;

import java.util.function.Consumer;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time and has random parameters.
 */
public final class SuperDuperBenchmark_Fix2 {

    enum Benchmark {
        bob, joe
    }

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        Benchmark benchmark = select("Select a benchmark to run:", Benchmark.class);
        Consumer<Operands> benchmarkMethod = switch (benchmark) {
            case bob -> o -> Solutions.bob(o.x1(), o.y1(), o.x2(), o.y2());
            case joe -> o -> Solutions.joe(o.x1(), o.y1(), o.x2(), o.y2());
        };
        bench(
                benchmark.name(),
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                Operands::random,
                benchmarkMethod);
    }

}
