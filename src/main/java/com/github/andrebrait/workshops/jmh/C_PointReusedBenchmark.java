package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.AccuratePoint;
import com.github.andrebrait.workshops.jmh.domain.FastPoint;
import com.github.andrebrait.workshops.jmh.domain.RawCoordinate;
import com.github.andrebrait.workshops.jmh.domain.SuperFastPoint;

import static com.github.andrebrait.workshops.jmh.framework.Benchmark.*;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy and object reuse
 */
public final class C_PointReusedBenchmark {

    public static void main(String[] args) {
        bench_accurate_reuse();
        bench_accurate_raw_reuse();
        bench_fast_reuse();
        bench_super_fast_reuse();
    }

    private static void bench_accurate_reuse() {
        AccuratePoint a = new AccuratePoint(0, 0);
        AccuratePoint b = new AccuratePoint(10, 10);
        bench("point_accurate_reuse", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
    }

    private static void bench_accurate_raw_reuse() {
        AccuratePoint a = new AccuratePoint(0, 0);
        RawCoordinate b = new RawCoordinate(10, 10);
        bench("point_accurate_raw_reuse", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
    }

    private static void bench_fast_reuse() {
        FastPoint a = new FastPoint(0, 0);
        FastPoint b = new FastPoint(10, 10);
        bench("point_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
    }

    private static void bench_super_fast_reuse() {
        SuperFastPoint a = new SuperFastPoint(0, 0);
        SuperFastPoint b = new SuperFastPoint(10, 10);
        bench("point_super_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
    }
}
