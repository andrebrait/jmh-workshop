package com.github.andrebrait.workshops.jmh;

import com.github.andrebrait.workshops.jmh.domain.AccuratePoint;
import com.github.andrebrait.workshops.jmh.domain.FastPoint;
import com.github.andrebrait.workshops.jmh.domain.RawCoordinate;
import com.github.andrebrait.workshops.jmh.domain.SuperFastPoint;
import com.github.andrebrait.workshops.jmh.framework.SystemInfoUtils;

import static com.github.andrebrait.workshops.jmh.framework.Benchmark.*;

/**
 * "Naive" benchmark with a slightly more complex class hierarchy.
 *
 * <p><em>Note: it seems something breaks some runtime method dispatch optimizations
 * that happen for the first loaded class in this benchmark.</em>
 *
 * <p>Results:
 *
 * <pre>
 *  JVM info:
 *  	Name: OpenJDK 64-Bit Server VM
 *  	Vendor: Eclipse Adoptium
 *  	Version: 17.0.9+9
 *  	Architecture: aarch64
 *
 *  CPU info:
 *  	Apple M1 Pro
 *  	 1 physical CPU package(s)
 *  	 10 physical CPU core(s) (8 performance + 2 efficiency)
 *  	 10 logical CPU(s)
 *  	Identifier: Apple Inc. Family 0x1b588bb3 Model 0 Stepping 0
 *  	Microarchitecture: ARM64 SoC: Firestorm + Icestorm
 *
 *  OS info:
 *  	Apple macOS 14.4 (Sonoma) build 23E214
 *
 *  Running: point_accurate
 *  3137035 ops/ms  (warmup) |
 *  2121400 ops/ms  (warmup) |
 *  2125937 ops/ms  |
 *  2118642 ops/ms  |
 *  2123067 ops/ms  |
 *  [ ~2122548 ops/ms ]
 *
 *  Running: point_accurate_raw
 *  1590732 ops/ms  (warmup) |
 *  1594567 ops/ms  (warmup) |
 *  1593067 ops/ms  |
 *  1590417 ops/ms  |
 *  1594737 ops/ms  |
 *  [ ~1592740 ops/ms ]
 *
 *  Running: point_fast
 *  462610 ops/ms  (warmup) |
 *  460715 ops/ms  (warmup) |
 *  463565 ops/ms  |
 *  464972 ops/ms  |
 *  464762 ops/ms  |
 *  [ ~464433 ops/ms ]
 *
 *  Running: point_super_fast
 *  465952 ops/ms  (warmup) |
 *  464727 ops/ms  (warmup) |
 *  464432 ops/ms  |
 *  465102 ops/ms  |
 *  462227 ops/ms  |
 *  [ ~463920 ops/ms ]
 * </pre>
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

    public static void main(String[] args) {
        SystemInfoUtils.printSystemInfo();
        bench_accurate();
        bench_accurate_raw();
        bench_fast();
        bench_super_fast();
    }
}
