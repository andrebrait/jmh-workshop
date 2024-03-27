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
 * <strong><em>Something seems to be off with compiler blackholes on macOS ARM64.</em></strong>
 *
 * <p>
 * Results (macOS ARM64, Compiler Blackholes):
 * <pre>
 * Benchmark                       Mode  Cnt        Score        Error   Units
 * SuperDuperBenchmark_JMH.allan  thrpt    6    27857.906 ±    840.894  ops/ms
 * SuperDuperBenchmark_JMH.bob    thrpt    6  1107158.120 ± 108122.770  ops/ms
 * SuperDuperBenchmark_JMH.joe    thrpt    6  1132149.755 ±  33530.737  ops/ms
 * SuperDuperBenchmark_JMH.steve  thrpt    6  1673249.128 ± 343043.657  ops/ms
 * </pre>
 * <p>
 * Results (macOS ARM64, full + no-inline Blackholes):
 * <pre>
 * Benchmark                       Mode  Cnt       Score       Error   Units
 * SuperDuperBenchmark_JMH.allan  thrpt    6   25144.941 ±  1801.490  ops/ms
 * SuperDuperBenchmark_JMH.bob    thrpt    6  372951.106 ± 11054.834  ops/ms
 * SuperDuperBenchmark_JMH.joe    thrpt    6  460825.021 ± 16687.557  ops/ms
 * SuperDuperBenchmark_JMH.steve  thrpt    6  471225.059 ±  4406.345  ops/ms
 * </pre>
 *
 * <p>
 * Results (Windows x86-64, Compiler Blackholes):
 * <pre>
 * Benchmark                       Mode  Cnt        Score        Error   Units
 * SuperDuperBenchmark_JMH.allan  thrpt    3    19556.936 ±   2852.904  ops/ms
 * SuperDuperBenchmark_JMH.bob    thrpt    3   525314.720 ± 135762.584  ops/ms
 * SuperDuperBenchmark_JMH.joe    thrpt    3  1350302.778 ± 136637.116  ops/ms
 * SuperDuperBenchmark_JMH.steve  thrpt    3  2983898.124 ±   8680.265  ops/ms
 * </pre>
 * <p>
 * Results (Windows x86-64, full + no-inline Blackholes)
 * <pre>
 * Benchmark                       Mode  Cnt       Score       Error   Units
 * SuperDuperBenchmark_JMH.allan  thrpt    3   18916.013 ±  2258.116  ops/ms
 * SuperDuperBenchmark_JMH.bob    thrpt    3  468360.786 ± 61138.030  ops/ms
 * SuperDuperBenchmark_JMH.joe    thrpt    3  565224.721 ± 41273.525  ops/ms
 * SuperDuperBenchmark_JMH.steve  thrpt    3  655535.352 ± 51378.771  ops/ms
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

        @Setup(Level.Trial)
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
                //.jvmArgsPrepend("-Djmh.blackhole.mode=COMPILER")
                //.addProfiler("gc")
                //.addProfiler("xperfasm") // for Windows
                //.addProfiler("perfasm") // for Linux
                //.addProfiler("dtraceasm") // for macOS (required root)
                //.jvmArgsAppend("-XX:LoopUnrollLimit=1", "-XX:-TieredCompilation") // simplify assembly, keep unrolling to a minimum and enforce tiered compilation with the final optimizing compiler
                .include(regex).build();
        Runner runner = new Runner(options);
        runner.run();
    }
}
