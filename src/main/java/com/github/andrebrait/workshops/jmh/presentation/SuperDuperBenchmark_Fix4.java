package com.github.andrebrait.workshops.jmh.presentation;

import com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework;

import java.util.function.Function;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time and
 * has "random" parameters, with the result being consumed in a "blackhole" so the compiler
 * can't remove the invocation altogether.
 */
public final class SuperDuperBenchmark_Fix4 {

    enum Benchmark {
        bob, joe
    }

    private static long cumulativeResult = 0;

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        Benchmark benchmark = select("Select a benchmark to run:", Benchmark.class);
        Function<Operands, Double> benchmarkMethod = switch (benchmark) {
            case bob -> o -> Solutions.bob(o.x1(), o.y1(), o.x2(), o.y2());
            case joe -> o -> Solutions.joe(o.x1(), o.y1(), o.x2(), o.y2());
        };
        BenchmarkFramework.bench(
                benchmark.name(),
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                Operands::random,
                r -> cumulativeResult += r.longValue(),
                benchmarkMethod);
        System.out.printf("Cumulative result: %d%n", cumulativeResult);
    }
}
