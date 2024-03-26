package com.github.andrebrait.workshops.jmh.benchmarks;

import com.github.andrebrait.workshops.jmh.domain.*;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy.
 *
 * <p><em>Note: it seems something breaks some runtime method dispatch optimizations
 * that happen for the first loaded class in this benchmark.</em>
 */
public final class B_PointBenchmark {

    private static void bench_accurate() {
        bench(
                "point_accurate",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> new AccuratePoint(0, 0).distance(new AccuratePoint(10, 10)));
    }

    private static void bench_accurate_raw() {
        bench(
                "point_accurate_raw",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> new AccuratePoint(0, 0).distance(new RawCoordinate(10, 10)));
    }

    private static void bench_fast() {
        bench(
                "point_fast",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> new FastPoint(0, 0).distance(new FastPoint(10, 10)));
    }

    private static void bench_super_fast() {
        bench(
                "point_super_fast",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> new SuperFastPoint(0, 0).distance(new SuperFastPoint(10, 10)));
    }

    private static void bench_fixed() {
        bench(
                "point_fixed",
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                () -> new FixedPoint(0, 0).distance(new FixedPoint(10, 10)));
    }

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench_accurate();
        bench_accurate_raw();
        bench_fast();
        bench_super_fast();
        bench_fixed();
    }
}
