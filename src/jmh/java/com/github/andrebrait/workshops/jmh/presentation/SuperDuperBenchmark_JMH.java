package com.github.andrebrait.workshops.jmh.presentation;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * A JMH equivalent of the Super Duper Benchmark!
 * <p>
 * Results:
 * <pre>
 * Benchmark                       Mode  Cnt        Score         Error   Units
 * SuperDuperBenchmark_JMH.allan  thrpt    3    28039.465 ±     119.873  ops/ms
 * SuperDuperBenchmark_JMH.bob    thrpt    3   996627.931 ± 1131515.265  ops/ms
 * SuperDuperBenchmark_JMH.joe    thrpt    3  1135069.323 ±  219898.808  ops/ms
 * SuperDuperBenchmark_JMH.steve  thrpt    3  1760845.935 ±   93624.394  ops/ms
 * </pre>
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class SuperDuperBenchmark_JMH {

    @State(Scope.Benchmark)
    public static class BenchmarkArguments {
        private Operands operands;

        // Using Level.Trial here causes bob and joe to be optimized away
        @Setup(Level.Iteration)
        public void setup() {
            operands = Operands.random();
        }
    }

    @Benchmark
    public void allan(Blackhole blackhole, BenchmarkArguments args) {
        Operands o = args.operands;
        blackhole.consume(Solutions.allan(o.x1(), o.y1(), o.x2(), o.y2()));
    }

    @Benchmark
    public void bob(Blackhole blackhole, BenchmarkArguments args) {
        Operands o = args.operands;
        blackhole.consume(Solutions.bob(o.x1(), o.y1(), o.x2(), o.y2()));
    }

    @Benchmark
    public void joe(Blackhole blackhole, BenchmarkArguments args) {
        Operands o = args.operands;
        blackhole.consume(Solutions.joe(o.x1(), o.y1(), o.x2(), o.y2()));
    }

    @Benchmark
    public double steve(BenchmarkArguments args) {
        Operands o = args.operands;
        return Solutions.steve(o.x1(), o.y1(), o.x2(), o.y2());
    }

    public static void main(String[] args) throws RunnerException {
        String regex = "^\\Q%s.\\E.*".formatted(SuperDuperBenchmark_JMH.class.getName());
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
