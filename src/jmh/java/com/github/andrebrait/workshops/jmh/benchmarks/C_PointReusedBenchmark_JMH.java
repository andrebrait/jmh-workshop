package com.github.andrebrait.workshops.jmh.benchmarks;

import com.github.andrebrait.workshops.jmh.domain.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class C_PointReusedBenchmark_JMH {

    @State(Scope.Benchmark)
    public static class Points {

        @Param({"accurate", "fast", "super-fast", "fixed"})
        private String implementation;

        private Point a;
        private Point b;

        @Setup
        public void setup() {
            Random random = ThreadLocalRandom.current();
            int x1 = random.nextInt(100);
            int y1 = random.nextInt(100);
            int x2 = random.nextInt(100);
            int y2 = random.nextInt(100);
            a = newPointInstance(x1, y1);
            b = newPointInstance(x2, y2);
        }

        private Point newPointInstance(int x, int y) {
            return switch (implementation) {
                case "accurate" -> new AccuratePoint(x, y);
                case "fast" -> new FastPoint(x, y);
                case "super-fast" -> new SuperFastPoint(x, y);
                case "fixed" -> new FixedPoint(x, y);
                default ->
                        throw new IllegalArgumentException("Invalid: %s".formatted(implementation));
            };
        }
    }

    @Benchmark
    public void distance(Blackhole blackhole, Points points) {
        blackhole.consume(points.a.distance(points.b));
    }

    public static void main(String[] args) throws RunnerException {
        String regex = "^\\Q%s.\\E.*".formatted(C_PointReusedBenchmark_JMH.class.getName());
        Options options = new OptionsBuilder()
                //.jvmArgsAppend("-Djmh.blackhole.mode=COMPILER")
                //.addProfiler("gc")
                //.addProfiler("xperfasm") // for Windows
                //.addProfiler("perfasm") // for Linux
                //.addProfiler("dtraceasm") // for macOS (required root)
                //.jvmArgsAppend("-XX:LoopUnrollLimit=1") // simplify assembly, keep unrolling to a minimum
                //.jvmArgsAppend("-XX:-TieredCompilation") // enforce tiered compilation with the final optimizing compiler                .include(regex)
                .include(regex).build();
        Runner runner = new Runner(options);
        runner.run();
    }
}
