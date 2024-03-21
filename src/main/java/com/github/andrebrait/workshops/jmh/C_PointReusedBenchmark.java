package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.AccuratePoint;
import com.github.andrebrait.workshops.jmh.domain.FastPoint;
import com.github.andrebrait.workshops.jmh.domain.RawCoordinate;
import com.github.andrebrait.workshops.jmh.domain.SuperFastPoint;
import com.github.andrebrait.workshops.jmh.framework.SystemInfoUtils;

import static com.github.andrebrait.workshops.jmh.framework.Benchmark.*;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy and object reuse
 *
 * <p>Results:
 *
 * <pre>
 *  JVM info:
 *  	Name: OpenJDK 64-Bit Server VM
 *  	Vendor: Eclipse Adoptium
 *  	Version: 17.0.10+7
 *  	Architecture: amd64
 *
 *  CPU info:
 *  	AMD Ryzen 9 5900X 12-Core Processor
 *  	 1 physical CPU package(s)
 *  	 12 physical CPU core(s)
 *  	 24 logical CPU(s)
 *  	Identifier: AuthenticAMD Family 25 Model 33 Stepping 0
 *  	Microarchitecture: Zen 3
 *
 *  OS info:
 *  	Microsoft Windows 11 build 22631
 *
 *  Running: point_accurate_reuse
 *  127224817 ops/ms  (warmup) |
 *  124505207 ops/ms  (warmup) |
 *  123661992 ops/ms  |
 *  123809707 ops/ms  |
 *  123770960 ops/ms  |
 *  [ ~123747553 ops/ms ]
 *
 *  Running: point_accurate_raw_reuse
 *  1536907 ops/ms  (warmup) |
 *  1543572 ops/ms  (warmup) |
 *  1529615 ops/ms  |
 *  1537882 ops/ms  |
 *  1540712 ops/ms  |
 *  [ ~1536069 ops/ms ]
 *
 *  Running: point_fast
 *  575632 ops/ms  (warmup) |
 *  575950 ops/ms  (warmup) |
 *  571535 ops/ms  |
 *  576847 ops/ms  |
 *  576680 ops/ms  |
 *  [ ~575020 ops/ms ]
 *
 *  Running: point_super_fast
 *  576427 ops/ms  (warmup) |
 *  578050 ops/ms  (warmup) |
 *  578807 ops/ms  |
 *  578795 ops/ms  |
 *  576712 ops/ms  |
 *  [ ~578104 ops/ms ]
 * </pre>
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

    public static void main(String[] args) {
        SystemInfoUtils.printSystemInfo();
        bench_accurate_reuse();
        bench_accurate_raw_reuse();
        bench_fast_reuse();
        bench_super_fast_reuse();
    }
}
