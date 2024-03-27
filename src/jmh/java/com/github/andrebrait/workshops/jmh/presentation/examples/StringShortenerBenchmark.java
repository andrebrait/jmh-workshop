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
 * <p>
 * Results (macOS ARM64):
 * <pre>
 * Benchmark                                                                           (className)  (separators)  Mode  Cnt       Score      Error   Units
 * StringShortenerBenchmark.shorten_loop                                                   MyClass             0  avgt    3     372.239 ±   21.760   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate                                     MyClass             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm                                MyClass             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                                          MyClass             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop                                                   MyClass             5  avgt    3    8189.558 ±  272.391   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate                                     MyClass             5  avgt    3    1769.921 ±   58.599  MB/sec
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm                                MyClass             5  avgt    3   15200.002 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                                          MyClass             5  avgt    3      36.000             counts
 * StringShortenerBenchmark.shorten_loop:gc.time                                           MyClass             5  avgt    3      17.000                 ms
 * StringShortenerBenchmark.shorten_loop                                                   MyClass            10  avgt    3   11944.981 ±  232.920   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate                                     MyClass            10  avgt    3    1277.330 ±   24.917  MB/sec
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm                                MyClass            10  avgt    3   16000.003 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                                          MyClass            10  avgt    3      26.000             counts
 * StringShortenerBenchmark.shorten_loop:gc.time                                           MyClass            10  avgt    3      13.000                 ms
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory             0  avgt    3     861.343 ±   16.420   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate               MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                    MyClassListenerBuilderFactory             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory             5  avgt    3    8353.022 ± 2274.996   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate               MyClassListenerBuilderFactory             5  avgt    3    1918.228 ±  526.368  MB/sec
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory             5  avgt    3   16800.002 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                    MyClassListenerBuilderFactory             5  avgt    3      38.000             counts
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory             5  avgt    3      18.000                 ms
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory            10  avgt    3   12434.710 ±  424.511   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate               MyClassListenerBuilderFactory            10  avgt    3    2085.938 ±   71.379  MB/sec
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory            10  avgt    3   27200.003 ±    0.002    B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                    MyClassListenerBuilderFactory            10  avgt    3      42.000             counts
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory            10  avgt    3      21.000                 ms
 * StringShortenerBenchmark.shorten_loop_cached                                            MyClass             0  avgt    3     739.094 ±   41.232   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate                              MyClass             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm                         MyClass             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count                                   MyClass             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                                            MyClass             5  avgt    3     646.182 ±   14.272   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate                              MyClass             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm                         MyClass             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count                                   MyClass             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                                            MyClass            10  avgt    3     671.679 ±    7.534   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate                              MyClass            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm                         MyClass            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count                                   MyClass            10  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                      MyClassListenerBuilderFactory             0  avgt    3     778.096 ±   17.187   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate        MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm   MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count             MyClassListenerBuilderFactory             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                      MyClassListenerBuilderFactory             5  avgt    3     679.669 ±    1.842   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate        MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm   MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count             MyClassListenerBuilderFactory             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                      MyClassListenerBuilderFactory            10  avgt    3     632.287 ±   17.204   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate        MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm   MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count             MyClassListenerBuilderFactory            10  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split                                                  MyClass             0  avgt    3     353.924 ±   38.473   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate                                    MyClass             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm                               MyClass             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split:gc.count                                         MyClass             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split                                                  MyClass             5  avgt    3   17446.902 ±  795.459   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate                                    MyClass             5  avgt    3    3109.814 ±  141.084  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm                               MyClass             5  avgt    3   56896.005 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                                         MyClass             5  avgt    3      63.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                                          MyClass             5  avgt    3      29.000                 ms
 * StringShortenerBenchmark.shorten_split                                                  MyClass            10  avgt    3   23950.830 ± 1998.584   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate                                    MyClass            10  avgt    3    4192.751 ±  349.631  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm                               MyClass            10  avgt    3  105304.006 ±    0.004    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                                         MyClass            10  avgt    3      84.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                                          MyClass            10  avgt    3      38.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory             0  avgt    3     416.021 ±   12.993   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate              MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split:gc.count                   MyClassListenerBuilderFactory             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory             5  avgt    3   16504.737 ± 1096.788   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate              MyClassListenerBuilderFactory             5  avgt    3    3906.254 ±  259.422  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory             5  avgt    3   67608.004 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                   MyClassListenerBuilderFactory             5  avgt    3      78.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory             5  avgt    3      36.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory            10  avgt    3   24344.259 ±  827.828   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate              MyClassListenerBuilderFactory            10  avgt    3    4292.267 ±  145.892  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory            10  avgt    3  109576.006 ±    0.004    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                   MyClassListenerBuilderFactory            10  avgt    3      86.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory            10  avgt    3      40.000                 ms
 * StringShortenerBenchmark.shorten_split_cached                                           MyClass             0  avgt    3     746.016 ±   33.250   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate                             MyClass             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm                        MyClass             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count                                  MyClass             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                                           MyClass             5  avgt    3     671.060 ±   27.148   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate                             MyClass             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm                        MyClass             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count                                  MyClass             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                                           MyClass            10  avgt    3     727.664 ±   53.778   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate                             MyClass            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm                        MyClass            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count                                  MyClass            10  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                     MyClassListenerBuilderFactory             0  avgt    3     779.869 ±   14.366   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate       MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm  MyClassListenerBuilderFactory             0  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count            MyClassListenerBuilderFactory             0  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                     MyClassListenerBuilderFactory             5  avgt    3     646.220 ±   74.912   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate       MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm  MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count            MyClassListenerBuilderFactory             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                     MyClassListenerBuilderFactory            10  avgt    3     700.265 ±  104.482   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate       MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm  MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count            MyClassListenerBuilderFactory            10  avgt    3         ≈ 0             counts
 * </pre>
 * <p>
 * Important parts (macOS ARM64):
 * <pre>
 * Benchmark                                                                           (className)  (separators)  Mode  Cnt       Score      Error   Units
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory             5  avgt    3    8353.022 ± 2274.996   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory             5  avgt    3   16800.002 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory             5  avgt    3      18.000                 ms
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory            10  avgt    3   12434.710 ±  424.511   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory            10  avgt    3   27200.003 ±    0.002    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory            10  avgt    3      21.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory             5  avgt    3   16504.737 ± 1096.788   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory             5  avgt    3   67608.004 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory             5  avgt    3      36.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory            10  avgt    3   24344.259 ±  827.828   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory            10  avgt    3  109576.006 ±    0.004    B/op
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory            10  avgt    3      40.000                 ms
 * </pre>
 * <p>
 * Results (Windows x86-64):
 * <pre>
 * Benchmark                                                                           (className)  (separators)  Mode  Cnt       Score      Error   Units
 * StringShortenerBenchmark.shorten_loop                                                   MyClass             5  avgt    3    6636.842 ±  367.491   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm                                MyClass             5  avgt    3   15200.002 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                                           MyClass             5  avgt    3      22.000                 ms
 * StringShortenerBenchmark.shorten_loop                                                   MyClass            10  avgt    3   10191.214 ± 3062.475   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm                                MyClass            10  avgt    3   16000.003 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                                           MyClass            10  avgt    3      15.000                 ms
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory             5  avgt    3    7921.299 ±   62.748   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory             5  avgt    3   16800.002 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory             5  avgt    3      19.000                 ms
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory            10  avgt    3   12035.973 ± 1406.314   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory            10  avgt    3   27200.003 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.count                    MyClassListenerBuilderFactory            10  avgt    3      43.000             counts
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory            10  avgt    3      21.000                 ms
 * StringShortenerBenchmark.shorten_loop_cached                                            MyClass             5  avgt    3     639.384 ±  111.619   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate                              MyClass             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm                         MyClass             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count                                   MyClass             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                                            MyClass            10  avgt    3     775.031 ±   55.464   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate                              MyClass            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm                         MyClass            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count                                   MyClass            10  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                      MyClassListenerBuilderFactory             5  avgt    3     663.235 ±  187.044   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate        MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm   MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count             MyClassListenerBuilderFactory             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_loop_cached                      MyClassListenerBuilderFactory            10  avgt    3     673.293 ±  257.577   ns/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate        MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_loop_cached:gc.alloc.rate.norm   MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_loop_cached:gc.count             MyClassListenerBuilderFactory            10  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split                                                  MyClass             5  avgt    3   12613.700 ± 3843.884   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate                                    MyClass             5  avgt    3    4327.481 ± 1327.302  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm                               MyClass             5  avgt    3   57232.003 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                                         MyClass             5  avgt    3      87.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                                          MyClass             5  avgt    3      41.000                 ms
 * StringShortenerBenchmark.shorten_split                                                  MyClass            10  avgt    3   24656.259 ± 4311.001   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate                                    MyClass            10  avgt    3    4082.486 ±  711.997  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm                               MyClass            10  avgt    3  105552.007 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                                         MyClass            10  avgt    3      82.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                                          MyClass            10  avgt    3      39.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory             5  avgt    3   14295.017 ± 4301.033   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate              MyClassListenerBuilderFactory             5  avgt    3    4481.893 ± 1337.793  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory             5  avgt    3   67176.004 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                   MyClassListenerBuilderFactory             5  avgt    3      89.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory             5  avgt    3      43.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory            10  avgt    3   24703.653 ± 5660.994   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate              MyClassListenerBuilderFactory            10  avgt    3    4238.080 ±  964.537  MB/sec
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory            10  avgt    3  109784.007 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.count                   MyClassListenerBuilderFactory            10  avgt    3      85.000             counts
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory            10  avgt    3      41.000                 ms
 * StringShortenerBenchmark.shorten_split_cached                                           MyClass             5  avgt    3     678.447 ±  108.492   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate                             MyClass             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm                        MyClass             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count                                  MyClass             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                                           MyClass            10  avgt    3     688.905 ±   21.732   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate                             MyClass            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm                        MyClass            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count                                  MyClass            10  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                     MyClassListenerBuilderFactory             5  avgt    3     649.272 ±  203.328   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate       MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm  MyClassListenerBuilderFactory             5  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count            MyClassListenerBuilderFactory             5  avgt    3         ≈ 0             counts
 * StringShortenerBenchmark.shorten_split_cached                     MyClassListenerBuilderFactory            10  avgt    3     672.192 ±  152.266   ns/op
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate       MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴             MB/sec
 * StringShortenerBenchmark.shorten_split_cached:gc.alloc.rate.norm  MyClassListenerBuilderFactory            10  avgt    3      ≈ 10⁻⁴               B/op
 * StringShortenerBenchmark.shorten_split_cached:gc.count            MyClassListenerBuilderFactory            10  avgt    3         ≈ 0             counts
 * </pre>
 * <p>
 * Important parts (Windows x86-64):
 * <pre>
 * Benchmark                                                                           (className)  (separators)  Mode  Cnt       Score      Error   Units
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory             5  avgt    3    7921.299 ±   62.748   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory             5  avgt    3   16800.002 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory             5  avgt    3      19.000                 ms
 * StringShortenerBenchmark.shorten_loop                             MyClassListenerBuilderFactory            10  avgt    3   12035.973 ± 1406.314   ns/op
 * StringShortenerBenchmark.shorten_loop:gc.alloc.rate.norm          MyClassListenerBuilderFactory            10  avgt    3   27200.003 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_loop:gc.time                     MyClassListenerBuilderFactory            10  avgt    3      21.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory             5  avgt    3   14295.017 ± 4301.033   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory             5  avgt    3   67176.004 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory             5  avgt    3      43.000                 ms
 * StringShortenerBenchmark.shorten_split                            MyClassListenerBuilderFactory            10  avgt    3   24703.653 ± 5660.994   ns/op
 * StringShortenerBenchmark.shorten_split:gc.alloc.rate.norm         MyClassListenerBuilderFactory            10  avgt    3  109784.007 ±    0.001    B/op
 * StringShortenerBenchmark.shorten_split:gc.time                    MyClassListenerBuilderFactory            10  avgt    3      41.000                 ms
 * </pre>
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class StringShortenerBenchmark {

    @State(Scope.Benchmark)
    public static class ShortenerState {

        @Param({"MyClass", "MyClassListenerBuilderFactory"})
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
