package com.github.andrebrait.workshops.jmh.benchmarks;

import com.github.andrebrait.workshops.jmh.framework.BenchToDoubleFunction;
import com.github.andrebrait.workshops.jmh.framework.BenchmarkFramework;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
public final class A_RawMethodBenchmark_Fix5 {

    enum Benchmark {
        distance, constant
    }

    // if this is unboxed, it'll be optimized away
    private static Double last = 0.0d;

    private record Operands(double x1, double y1, double x2, double y2) {
        static Operands random() {
            Random random = ThreadLocalRandom.current();
            return new Operands(
                    random.nextDouble(1000.0d),
                    random.nextDouble(1000.0d),
                    random.nextDouble(1000.0d),
                    random.nextDouble(1000.0d));
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

    public static void main(String[] args) {
        //SystemInfoUtils.printSystemInfo();
        Benchmark benchmark = select("Select a benchmark to run:", Benchmark.class);
        BenchToDoubleFunction<Operands> benchmarkMethod = switch (benchmark) {
            case distance -> o -> distance(o.x1(), o.y1(), o.x2(), o.y2());
            case constant -> o -> constant(o.x1(), o.y1(), o.x2(), o.y2());
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
        System.out.printf("Last result: %f%n", last);
    }
}
