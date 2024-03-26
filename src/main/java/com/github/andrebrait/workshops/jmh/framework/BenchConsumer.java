package com.github.andrebrait.workshops.jmh.framework;

/**
 * Equivalent to a {@link java.util.function.Consumer}.
 */
@FunctionalInterface
public interface BenchConsumer<T> {

    void accept(T t);
}
