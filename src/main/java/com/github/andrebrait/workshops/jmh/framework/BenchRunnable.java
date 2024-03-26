package com.github.andrebrait.workshops.jmh.framework;


/**
 * Equivalent to a {@link java.lang.Runnable}.
 */
@FunctionalInterface
public interface BenchRunnable {

    void run();
}
