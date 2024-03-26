package com.github.andrebrait.workshops.jmh.framework;

/**
 * Equivalent to a {@link java.util.function.DoubleConsumer}.
 */
@FunctionalInterface
public interface BenchDoubleConsumer {

    void accept(double value);
}
