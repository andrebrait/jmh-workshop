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
