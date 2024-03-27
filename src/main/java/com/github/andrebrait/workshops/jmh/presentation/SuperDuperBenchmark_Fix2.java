package com.github.andrebrait.workshops.jmh.presentation;

import com.github.andrebrait.workshops.jmh.framework.BenchConsumer;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time and has random parameters.
 */
public final class SuperDuperBenchmark_Fix2 {

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        BenchmarkName benchmark = args.length == 1 ?
                BenchmarkName.valueOf(args[0]) :
                select("Select a benchmark to run:", BenchmarkName.class);
        BenchConsumer<Operands> benchmarkMethod = switch (benchmark) {
            case allan -> o -> Solutions.allan(o.x1(), o.y1(), o.x2(), o.y2());
            case bob -> o -> Solutions.bob(o.x1(), o.y1(), o.x2(), o.y2());
            case joe -> o -> Solutions.joe(o.x1(), o.y1(), o.x2(), o.y2());
            case steve -> o -> Solutions.steve(o.x1(), o.y1(), o.x2(), o.y2());
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
