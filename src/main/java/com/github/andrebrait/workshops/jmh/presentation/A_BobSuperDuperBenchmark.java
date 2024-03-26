package com.github.andrebrait.workshops.jmh.presentation;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * Bob's Super Duper Benchmark
 *
 * <p>I'm gonna show Joe and Steve who's the boss!
 */
public final class A_BobSuperDuperBenchmark {

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench("bob", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> Solutions.bob(0, 0, 10, 10));
        bench("joe", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> Solutions.joe(0, 0, 10, 10));
        bench("steve", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> Solutions.steve(0, 0, 10, 10));
    }
}
