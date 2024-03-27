package com.github.andrebrait.workshops.jmh.presentation.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import oshi.annotation.concurrent.GuardedBy;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Benchmarking lock strategies with counter implementations.
 * <p>
 * Inspired by <a href="https://isuru-perera.blogspot.com/2016/05/benchmarking-java-locks-with-counters.html">Benchmarking Java Locks with Counters</a>
 * <p>
 * Results (macOS ARM64):
 * <pre>
 * Benchmark                                                    Mode  Cnt       Score        Error   Units
 * LockBenchmark.Adder                                         thrpt    3  121557.304 ±  25512.272  ops/ms
 * LockBenchmark.Adder:adder_get                               thrpt    3   30633.722 ±   8274.599  ops/ms
 * LockBenchmark.Adder:adder_inc                               thrpt    3   90923.582 ±  19563.731  ops/ms
 * LockBenchmark.Atomic                                        thrpt    3  612552.270 ± 556332.813  ops/ms
 * LockBenchmark.Atomic:atomic_get                             thrpt    3  595654.409 ± 564094.012  ops/ms
 * LockBenchmark.Atomic:atomic_inc                             thrpt    3   16897.861 ±  10273.992  ops/ms
 * LockBenchmark.RWLockFair                                    thrpt    3     515.374 ±    140.314  ops/ms
 * LockBenchmark.RWLockFair:rw_lock_fair_get                   thrpt    3     257.677 ±     69.997  ops/ms
 * LockBenchmark.RWLockFair:rw_lock_fair_inc                   thrpt    3     257.697 ±     70.317  ops/ms
 * LockBenchmark.RWLockNonFair                                 thrpt    3   32014.531 ±   9355.368  ops/ms
 * LockBenchmark.RWLockNonFair:rw_lock_get                     thrpt    3    8076.279 ±   2354.169  ops/ms
 * LockBenchmark.RWLockNonFair:rw_lock_inc                     thrpt    3   23938.252 ±   7066.754  ops/ms
 * LockBenchmark.StampedLock                                   thrpt    3   50515.021 ±  34515.501  ops/ms
 * LockBenchmark.StampedLock:stamped_get                       thrpt    3   11855.643 ±   7152.522  ops/ms
 * LockBenchmark.StampedLock:stamped_inc                       thrpt    3   38659.379 ±  32476.617  ops/ms
 * LockBenchmark.StampedLockOptimistic                         thrpt    3  386914.358 ± 196547.711  ops/ms
 * LockBenchmark.StampedLockOptimistic:stamped_optimistic_get  thrpt    3  359210.852 ± 193587.094  ops/ms
 * LockBenchmark.StampedLockOptimistic:stamped_optimistic_inc  thrpt    3   27703.506 ±   6640.446  ops/ms
 * LockBenchmark.Synchronized                                  thrpt    3   27420.127 ±   3851.113  ops/ms
 * LockBenchmark.Synchronized:synchronized_get                 thrpt    3   10818.018 ±   1123.696  ops/ms
 * LockBenchmark.Synchronized:synchronized_inc                 thrpt    3   16602.109 ±   4641.771  ops/ms
 * LockBenchmark.Volatile                                      thrpt    3  753812.569 ± 952572.577  ops/ms
 * LockBenchmark.Volatile:volatile_get                         thrpt    3  726596.444 ± 956115.823  ops/ms
 * LockBenchmark.Volatile:volatile_inc                         thrpt    3   27216.125 ±   9898.125  ops/ms
 * </pre>
 *
 * Results (Windows x86-64):
 * <pre>
 * Benchmark                                                    Mode  Cnt        Score        Error   Units
 * LockBenchmark.Adder                                         thrpt    3   111900.578 ± 204480.530  ops/ms
 * LockBenchmark.Adder:adder_get                               thrpt    3    24656.565 ±  34727.826  ops/ms
 * LockBenchmark.Adder:adder_inc                               thrpt    3    87244.012 ± 193287.788  ops/ms
 * LockBenchmark.Atomic                                        thrpt    3   661270.645 ± 172803.349  ops/ms
 * LockBenchmark.Atomic:atomic_get                             thrpt    3   617056.897 ± 179494.911  ops/ms
 * LockBenchmark.Atomic:atomic_inc                             thrpt    3    44213.748 ±   7122.411  ops/ms
 * LockBenchmark.RWLockFair                                    thrpt    3      102.075 ±     32.198  ops/ms
 * LockBenchmark.RWLockFair:rw_lock_fair_get                   thrpt    3       51.516 ±     31.089  ops/ms
 * LockBenchmark.RWLockFair:rw_lock_fair_inc                   thrpt    3       50.559 ±      1.627  ops/ms
 * LockBenchmark.RWLockNonFair                                 thrpt    3   123758.015 ±  16815.717  ops/ms
 * LockBenchmark.RWLockNonFair:rw_lock_get                     thrpt    3    14077.482 ±  21791.403  ops/ms
 * LockBenchmark.RWLockNonFair:rw_lock_inc                     thrpt    3   109680.533 ±   6136.945  ops/ms
 * LockBenchmark.StampedLock                                   thrpt    3   152385.403 ±  11421.495  ops/ms
 * LockBenchmark.StampedLock:stamped_get                       thrpt    3    32314.824 ±  27359.128  ops/ms
 * LockBenchmark.StampedLock:stamped_inc                       thrpt    3   120070.579 ±  33022.030  ops/ms
 * LockBenchmark.StampedLockOptimistic                         thrpt    3   530662.368 ±  16881.601  ops/ms
 * LockBenchmark.StampedLockOptimistic:stamped_optimistic_get  thrpt    3   417656.750 ±  27033.940  ops/ms
 * LockBenchmark.StampedLockOptimistic:stamped_optimistic_inc  thrpt    3   113005.617 ±  16793.148  ops/ms
 * LockBenchmark.Synchronized                                  thrpt    3    52357.460 ±   2098.374  ops/ms
 * LockBenchmark.Synchronized:synchronized_get                 thrpt    3    31291.342 ±   2717.365  ops/ms
 * LockBenchmark.Synchronized:synchronized_inc                 thrpt    3    21066.117 ±   2940.820  ops/ms
 * LockBenchmark.Volatile                                      thrpt    3  1459644.719 ± 772102.065  ops/ms
 * LockBenchmark.Volatile:volatile_get                         thrpt    3  1435795.718 ± 770952.513  ops/ms
 * LockBenchmark.Volatile:volatile_inc                         thrpt    3    23849.001 ±  28207.124  ops/ms
 * </pre>
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class LockBenchmark {

    @Benchmark
    @Group("Volatile")
    @GroupThreads(4)
    public void volatile_inc(VolatileCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("Volatile")
    @GroupThreads(4)
    public long volatile_get(VolatileCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("Synchronized")
    @GroupThreads(4)
    public void synchronized_inc(SynchronizedCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("Synchronized")
    @GroupThreads(4)
    public long synchronized_get(SynchronizedCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("Atomic")
    @GroupThreads(4)
    public void atomic_inc(AtomicCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("Atomic")
    @GroupThreads(4)
    public long atomic_get(AtomicCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("Adder")
    @GroupThreads(4)
    public void adder_inc(AdderCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("Adder")
    @GroupThreads(4)
    public long adder_get(AdderCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("RWLockNonFair")
    @GroupThreads(4)
    public void rw_lock_inc(ReadWriteLockCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("RWLockNonFair")
    @GroupThreads(4)
    public long rw_lock_get(ReadWriteLockCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("RWLockFair")
    @GroupThreads(4)
    public void rw_lock_fair_inc(FairReadWriteLockCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("RWLockFair")
    @GroupThreads(4)
    public long rw_lock_fair_get(FairReadWriteLockCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("StampedLock")
    @GroupThreads(4)
    public void stamped_inc(StampedLockCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("StampedLock")
    @GroupThreads(4)
    public long stamped_get(StampedLockCounterState state) {
        return state.get();
    }

    @Benchmark
    @Group("StampedLockOptimistic")
    @GroupThreads(4)
    public void stamped_optimistic_inc(OptimisticStampedLockCounterState state) {
        state.increment();
    }

    @Benchmark
    @Group("StampedLockOptimistic")
    @GroupThreads(4)
    public long stamped_optimistic_get(OptimisticStampedLockCounterState state) {
        return state.get();
    }

    public static void main(String[] args) throws RunnerException {
        String regex = "^\\Q%s.\\E.*".formatted(LockBenchmark.class.getName());
        Options options = new OptionsBuilder()
                // .threadGroups(5, 10) // 5 read groups, 10 write groups
                .include(regex).build();
        Runner runner = new Runner(options);
        runner.run();
    }

    // THIS IS INCORRECT AND SERVES ONLY AS A BASELINE
    @State(Scope.Group)
    public static class VolatileCounterState {
        private volatile long counter;

        public void increment() {
            counter += 1;
        }

        public long get() {
            return counter;
        }
    }

    @State(Scope.Group)
    public static class SynchronizedCounterState {
        private long counter;

        public synchronized void increment() {
            counter += 1;
        }

        public synchronized long get() {
            return counter;
        }
    }

    @State(Scope.Group)
    public static class AtomicCounterState {
        private final AtomicLong counter = new AtomicLong();

        public void increment() {
            counter.incrementAndGet();
        }

        public long get() {
            return counter.get();
        }
    }

    @State(Scope.Group)
    public static class AdderCounterState {
        private final LongAdder counter = new LongAdder();

        public void increment() {
            counter.increment();
        }

        public long get() {
            return counter.sum();
        }
    }

    @State(Scope.Group)
    public static class ReadWriteLockCounterState {

        @GuardedBy("lock")
        private long counter;

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        public void increment() {
            Lock writeLock = lock.writeLock();
            writeLock.lock();
            try {
                counter++;
            } finally {
                writeLock.unlock();
            }
        }

        public long get() {
            Lock readLock = lock.readLock();
            readLock.lock();
            try {
                return counter;
            } finally {
                readLock.unlock();
            }
        }
    }

    @State(Scope.Group)
    public static class FairReadWriteLockCounterState {

        @GuardedBy("lock")
        private long counter;

        private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

        public void increment() {
            Lock writeLock = lock.writeLock();
            writeLock.lock();
            try {
                counter++;
            } finally {
                writeLock.unlock();
            }
        }

        public long get() {
            Lock readLock = lock.readLock();
            readLock.lock();
            try {
                return counter;
            } finally {
                readLock.unlock();
            }
        }
    }

    @State(Scope.Group)
    public static class OptimisticStampedLockCounterState {

        @GuardedBy("lock")
        private long counter;

        private final StampedLock lock = new StampedLock();

        public void increment() {
            long stamp = lock.writeLock();
            try {
                counter += 1;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        public long get() {
            long stamp = lock.tryOptimisticRead();
            long currentCount;
            if (lock.validate(stamp)) {
                currentCount = counter;
            } else {
                stamp = lock.readLock();
                try {
                    currentCount = counter;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            return currentCount;
        }
    }

    @State(Scope.Group)
    public static class StampedLockCounterState {

        @GuardedBy("lock")
        private long counter;

        private final StampedLock lock = new StampedLock();

        public void increment() {
            long stamp = lock.writeLock();
            try {
                counter += 1;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        public long get() {
            long stamp = lock.readLock();
            try {
                return counter;
            } finally {
                lock.unlockRead(stamp);
            }
        }
    }
}
