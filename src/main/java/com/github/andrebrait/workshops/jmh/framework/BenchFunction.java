package com.github.andrebrait.workshops.jmh.framework;

/**
 * Equivalent to a {@link java.util.function.Function}.
 */
@FunctionalInterface
public interface BenchFunction<T, R> {

    R apply(T t);
}
