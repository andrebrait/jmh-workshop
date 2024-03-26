package com.github.andrebrait.workshops.jmh.presentation;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * Allan's Super Duper Benchmark
 *
 * <p>My solution is by far the best!
 */
public final class A_AllanSuperDuperBenchmark {

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench(
                "allan",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> Solutions.allan(0.0d, 0.0d, 10.0d, 10.0d));
        bench(
                "bob",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> Solutions.bob(0.0d, 0.0d, 10.0d, 10.0d));
        bench(
                "joe",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> Solutions.joe(0.0d, 0.0d, 10.0d, 10.0d));
        bench(
                "steve",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> Solutions.steve(0.0d, 0.0d, 10.0d, 10.0d));
    }
}
