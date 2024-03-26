package com.github.andrebrait.workshops.jmh.framework;

/**
 * Equivalent to a {@link java.util.function.Consumer}.
 */
@FunctionalInterface
public interface BenchSupplier<R> {

    R get();
}
