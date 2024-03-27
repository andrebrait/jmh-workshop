package com.github.andrebrait.workshops.jmh.presentation;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class SuperDuperBenchmark_JMH {

    @State(Scope.Benchmark)
    public static class BenchmarkArguments {
        private Operands operands;

        @Setup
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
    public void steve(Blackhole blackhole, BenchmarkArguments args) {
        Operands o = args.operands;
        blackhole.consume(Solutions.steve(o.x1(), o.y1(), o.x2(), o.y2()));
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
