package com.github.andrebrait.workshops.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class A_RawMethodBenchmark_JMH {

    @State(Scope.Benchmark)
    public static class Operands {
        private double x1, y1, x2, y2;

        @Setup
        public void setup() {
            Random random = ThreadLocalRandom.current();
            x1 = random.nextDouble(100);
            y1 = random.nextDouble(100);
            x2 = random.nextDouble(100);
            y2 = random.nextDouble(100);
        }
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    private static double constant(double x1, double y1, double x2, double y2) {
        return 0.0d;
    }

    @Benchmark
    public void distance(Blackhole blackhole, Operands operands) {
        blackhole.consume(distance(operands.x1, operands.y1, operands.x2, operands.y2));
    }


    @Benchmark
    public void constant(Blackhole blackhole, Operands operands) {
        blackhole.consume(constant(operands.x1, operands.y1, operands.x2, operands.y2));
    }

    public static void main(String[] args) throws RunnerException {
        String regex =
                "^%s\\..*".formatted(Pattern.quote(A_RawMethodBenchmark_JMH.class.getName()));
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
