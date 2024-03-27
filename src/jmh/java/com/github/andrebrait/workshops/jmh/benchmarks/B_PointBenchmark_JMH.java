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
public class B_PointBenchmark_JMH {

    @State(Scope.Benchmark)
    public static class Operands {
        private int x1, y1, x2, y2;

        @Setup
        public void setup() {
            Random random = ThreadLocalRandom.current();
            x1 = random.nextInt(200);
            y1 = random.nextInt(200);
            x2 = random.nextInt(200);
            y2 = random.nextInt(200);
        }
    }

    @Benchmark
    public void accuratePoint(Blackhole blackhole, Operands operands) {
        Point a = new AccuratePoint(operands.x1, operands.y1);
        Point b = new AccuratePoint(operands.x2, operands.y2);
        blackhole.consume(a.distance(b));
    }

    @Benchmark
    public void fastPoint(Blackhole blackhole, Operands operands) {
        Point a = new FastPoint(operands.x1, operands.y1);
        Point b = new FastPoint(operands.x2, operands.y2);
        blackhole.consume(a.distance(b));
    }

    @Benchmark
    public void superFastPoint(Blackhole blackhole, Operands operands) {
        Point a = new SuperFastPoint(operands.x1, operands.y1);
        Point b = new SuperFastPoint(operands.x2, operands.y2);
        blackhole.consume(a.distance(b));
    }

    @Benchmark
    public void fixedPoint(Blackhole blackhole, Operands operands) {
        Point a = new FixedPoint(operands.x1, operands.y1);
        Point b = new FixedPoint(operands.x2, operands.y2);
        blackhole.consume(a.distance(b));
    }

    public static void main(String[] args) throws RunnerException {
        String regex = "^\\Q%s.\\E.*".formatted(B_PointBenchmark_JMH.class.getName());
        Options options = new OptionsBuilder()
                //.jvmArgsAppend("-Djmh.blackhole.mode=COMPILER")
                //.addProfiler("gc")
                //.addProfiler("xperfasm") // for Windows
                //.addProfiler("perfasm") // for Linux
                //.addProfiler("dtraceasm") // for macOS (required root)
                //.jvmArgsAppend("-XX:LoopUnrollLimit=1") // simplify assembly, keep unrolling to a minimum
                //.jvmArgsAppend("-XX:-TieredCompilation") // enforce tiered compilation with the final optimizing compiler
                .include(regex).build();
        Runner runner = new Runner(options);
        runner.run();
    }
}
