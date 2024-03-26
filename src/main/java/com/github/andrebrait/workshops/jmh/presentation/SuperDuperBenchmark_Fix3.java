package com.github.andrebrait.workshops.jmh.presentation;

import java.util.function.Consumer;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time and
 * has "random" parameters and some added state which may mutate the class but does not
 * consume the result of the operation.
 */
public final class SuperDuperBenchmark_Fix3 {
    enum Benchmark {
        bob, joe
    }

    private static long executions;

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        Benchmark benchmark = select("Select a benchmark to run:", Benchmark.class);
        Consumer<Operands> benchmarkMethod = switch (benchmark) {
            case bob -> o -> {
                Solutions.bob(o.x1(), o.y1(), o.x2(), o.y2());
                executions++;
            };
            case joe -> o -> {
                Solutions.joe(o.x1(), o.y1(), o.x2(), o.y2());
                executions++;
            };
        };
        bench(
                benchmark.name(),
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                Operands::random,
                benchmarkMethod);
        System.out.printf("Executions: %df%n", executions);
    }
}
