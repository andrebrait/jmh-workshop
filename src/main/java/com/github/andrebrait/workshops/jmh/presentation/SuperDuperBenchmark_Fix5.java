package com.github.andrebrait.workshops.jmh.presentation;

import com.github.andrebrait.workshops.jmh.framework.BenchToDoubleFunction;
import com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework;

import static com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework.*;
import static com.github.andrebrait.workshops.jmh.utils.InputUtils.select;

/**
 * "Naive" benchmark with just static methods which can only execute one test at a time and
 * has "random" parameters, with the result being consumed in a "blackhole" so the compiler
 * can't remove the invocation altogether. This version also avoids the costs of boxing/unboxing,
 * but it runs into a combination of inlining, unrolling and dead-code elimination with some escape
 * analysis around the fact it's a primitive instead of an object.
 * <p>
 * Honestly, I have no idea.
 * One more reason not to code your own benchmarks.
 */
public final class SuperDuperBenchmark_Fix5 {

    /*
     * If we make this a primitive, some crazy optimization is gonna kick in!
     * For allan, this gets reverted if the loop unrolling is prevented.
     *
     * This behavior is observed both on macOS ARM64 and on Windows x86-64.
     */
    private static Double last = 0.0d;

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        BenchmarkName benchmark = args.length == 1 ?
                BenchmarkName.valueOf(args[0]) :
                select("Select a benchmark to run:", BenchmarkName.class);
        BenchToDoubleFunction<Operands> benchmarkMethod = switch (benchmark) {
            case allan -> o -> Solutions.allan(o.x1(), o.y1(), o.x2(), o.y2());
            case bob -> o -> Solutions.bob(o.x1(), o.y1(), o.x2(), o.y2());
            case joe -> o -> Solutions.joe(o.x1(), o.y1(), o.x2(), o.y2());
            case steve -> o -> Solutions.steve(o.x1(), o.y1(), o.x2(), o.y2());
        };
        BenchmarkFramework.benchDouble(
                benchmark.name(),
                RUN_MILLIS,
                LOOP,
                WARMUP,
                REPEAT,
                Operands::random,
                r -> last = r,
                benchmarkMethod);
        System.out.printf("Last result: %f%n%n", last);
    }
}
