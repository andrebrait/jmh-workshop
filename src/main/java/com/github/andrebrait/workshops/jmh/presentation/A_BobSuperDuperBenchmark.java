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
        bench("bob", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::bob);
        bench("joe", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::joe);
        bench("steve", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::steve);
    }
}
