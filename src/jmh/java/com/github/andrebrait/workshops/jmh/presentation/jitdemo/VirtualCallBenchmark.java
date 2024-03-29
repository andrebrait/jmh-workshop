package com.github.andrebrait.workshops.jmh.presentation.jitdemo;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * A benchmark to illustrate the effects of megamorphic method dispatching.
 * <p>
 * Reference: <a href="https://shipilev.net/jvm/anatomy-quarks/16-megamorphic-virtual-calls/">JVM Anatomy Quark #16</a>
 * <p>
 * Results (macOS):
 * <pre>
 * Benchmark                              (mode)  Mode  Cnt     Score     Error  Units
 * VirtualCallBenchmark.test_compiled       mono  avgt    3   121.812 ±   6.124  ns/op
 * VirtualCallBenchmark.test_compiled         bi  avgt    3   177.673 ±  35.827  ns/op
 * VirtualCallBenchmark.test_compiled       mega  avgt    3   862.371 ± 398.556  ns/op
 * VirtualCallBenchmark.test_interpreted    mono  avgt    3  5918.139 ±  11.641  ns/op
 * VirtualCallBenchmark.test_interpreted      bi  avgt    3  5601.312 ±  18.450  ns/op
 * VirtualCallBenchmark.test_interpreted    mega  avgt    3  5230.826 ±  83.587  ns/op
 * </pre>
 * <p>
 * Results (Windows x86-64):
 * <pre>
 * Benchmark                              (mode)  Mode  Cnt     Score      Error  Units
 * VirtualCallBenchmark.test_compiled       mono  avgt    3   100.138 ±    0.340  ns/op
 * VirtualCallBenchmark.test_compiled         bi  avgt    3   111.245 ±    5.697  ns/op
 * VirtualCallBenchmark.test_compiled       mega  avgt    3  2622.706 ±  381.581  ns/op
 * VirtualCallBenchmark.test_interpreted    mono  avgt    3  5925.842 ±  729.405  ns/op
 * VirtualCallBenchmark.test_interpreted      bi  avgt    3  5865.523 ±  192.276  ns/op
 * VirtualCallBenchmark.test_interpreted    mega  avgt    3  5863.516 ± 2017.571  ns/op
 * </pre>
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class VirtualCallBenchmark {

    private static abstract class A {
        protected long c1, c2, c3;

        public abstract void m();
    }

    private static class C1 extends A {

        public void m() {
            c1++;
        }
    }

    private static class C2 extends A {

        public void m() {
            c2++;
        }
    }

    private static class C3 extends A {

        public void m() {
            c3++;
        }
    }

    private A[] as;

    // monomorphic, bimorphic and megamorphic method dispatching
    @Param({"mono", "bi", "mega"})
    private String mode;

    @Setup(Level.Trial)
    public void setup() {
        as = new A[300];
        boolean bi = "bi".equals(mode);
        boolean mega = "mega".equals(mode);
        for (int c = 0; c < 300; c += 3) {
            as[c] = new C1();
            as[c + 1] = mega ? new C2() : new C1();
            as[c + 2] = mega || bi ? new C3() : new C1();
        }
    }

    // suppress compilation for this method, always interpret the bytecode
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    @Benchmark
    public void test_interpreted() {
        for (A a : as) {
            a.m();
        }
    }

    @Benchmark
    public void test_compiled() {
        for (A a : as) {
            a.m();
        }
    }

    public static void main(String[] args) throws RunnerException {
        String regex = "^\\Q%s.\\E.*".formatted(VirtualCallBenchmark.class.getName());
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
