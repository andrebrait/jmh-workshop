package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.AccuratePoint;
import com.github.andrebrait.workshops.jmh.domain.FastPoint;
import com.github.andrebrait.workshops.jmh.domain.RawCoordinate;
import com.github.andrebrait.workshops.jmh.domain.SuperFastPoint;

import static com.github.andrebrait.workshops.jmh.framework.Benchmark.*;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy
 */
public final class B_PointBenchmark {

    public static void main(String[] args) {
        bench_accurate();
        bench_accurate_raw();
        bench_fast();
        bench_super_fast();
    }

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
}
