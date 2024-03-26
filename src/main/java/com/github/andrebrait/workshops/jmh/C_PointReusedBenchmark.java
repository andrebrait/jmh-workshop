package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.*;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy and object reuse
 */
public final class C_PointReusedBenchmark {

    private static void bench_accurate_reuse() {
        AccuratePoint a = new AccuratePoint(0, 0);
        AccuratePoint b = new AccuratePoint(10, 10);
        bench("point_accurate", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
    }

    private static void bench_accurate_raw_reuse() {
        AccuratePoint a = new AccuratePoint(0, 0);
        RawCoordinate b = new RawCoordinate(10, 10);
        bench("point_accurate_raw", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
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

    private static void bench_fixed_reuse() {
        FixedPoint a = new FixedPoint(0, 0);
        FixedPoint b = new FixedPoint(10, 10);
        bench("point_super_fast", RUN_MILLIS, LOOP, WARMUP, REPEAT, () -> a.distance(b));
    }

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        bench_accurate_reuse();
        bench_accurate_raw_reuse();
        bench_fast_reuse();
        bench_super_fast_reuse();
    }
}
