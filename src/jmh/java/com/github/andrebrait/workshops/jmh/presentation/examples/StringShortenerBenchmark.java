package com.github.andrebrait.workshops.jmh.presentation.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Benchmarking a string shortener implementation and its memory allocation/GC characteristics
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class StringShortenerBenchmark {

    @State(Scope.Benchmark)
    public static class ShortenerState {

        @Param(
                {
                        "MyClass", "MyClassListenerBuilderFactory",
                }
        )
        private String className;

        @Param({"0", "5", "10"})
        private int separators;

        private String[] strings;

        private Map<String, String> cache;

        @Setup(Level.Trial)
        public void setup() {
            cache = new ConcurrentHashMap<>();
            strings = new String[100];
            Random random = ThreadLocalRandom.current();
            for (int i = 0; i < 100; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < separators; j++) {
                    String string = UUID.randomUUID().toString();
                    sb.append(string, 0, random.nextInt(3, string.length() + 1));
                    sb.append('.');
                }
                sb.append(className);
                strings[i] = sb.toString();
            }
        }
    }

    private static String shorten_split(String s) {
        if (s == null || !s.contains(".")) {
            return s;
        }
        String[] split = s.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            sb.append(split[i], 0, 1);
        }
        sb.append(split[split.length - 1]);
        return sb.toString();
    }

    private static String shorten_loop(String s) {
        int lastIndex;
        if (s == null || s.isEmpty() || (lastIndex = s.lastIndexOf('.')) == -1) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        char c;
        if ((c = s.charAt(0)) != '.') {
            sb.append(c);
            i += 1;
        }
        for (; (i = s.indexOf('.', i)) > -1 && i < lastIndex; i++) {
            if ((c = s.charAt(i + 1)) != '.') {
                if (!sb.isEmpty()) {
                    sb.append('.');
                }
                sb.append(c);
            }
        }
        if (lastIndex < s.length() - 1) {
            if (!sb.isEmpty()) {
                sb.append('.');
            }
            sb.append(s, lastIndex + 1, s.length());
        }
        return sb.toString();
    }

    @Benchmark
    public void shorten_split(ShortenerState state, Blackhole blackhole) {
        for (String s : state.strings) {
            blackhole.consume(shorten_split(s));
        }
    }

    @Benchmark
    public void shorten_loop(ShortenerState state, Blackhole blackhole) {
        for (String s : state.strings) {
            blackhole.consume(shorten_loop(s));
        }
    }

    @Benchmark
    public void shorten_split_cached(ShortenerState state, Blackhole blackhole) {
        for (String s : state.strings) {
            String result = state.cache.computeIfAbsent(s, StringShortenerBenchmark::shorten_split);
            blackhole.consume(result);
        }
    }

    @Benchmark
    public void shorten_loop_cached(ShortenerState state, Blackhole blackhole) {
        for (String s : state.strings) {
            String result = state.cache.computeIfAbsent(s, StringShortenerBenchmark::shorten_loop);
            blackhole.consume(result);
        }
    }

    public static void main(String[] args) throws RunnerException {
        String regex = "^\\Q%s.\\E.*".formatted(StringShortenerBenchmark.class.getName());
        // add 'gc' profiler to get memory allocation/GC metrics
        Options options = new OptionsBuilder().addProfiler("gc").include(regex).build();
        Runner runner = new Runner(options);
        runner.run();
    }
}
