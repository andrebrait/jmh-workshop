package com.github.andrebrait.workshops.jmh.presentation;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * Joe's Super Duper Benchmark
 *
 * <p>Bob and Steve know nothing. My solution is obviously superior!
 */
public final class A_JoeSuperDuperBenchmark {

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench("joe", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::joe);
        bench("bob", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::bob);
        bench("steve", RUN_MILLIS, LOOP, WARMUP, REPEAT, Solutions::steve);
    }
}
