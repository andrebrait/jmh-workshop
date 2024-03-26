package com.github.andrebrait.workshops.jmh.framework;

/**
 * Equivalent to a {@link java.util.function.ToDoubleFunction}.
 */
@FunctionalInterface
public interface BenchToDoubleFunction<T> {

    double applyAsDouble(T t);
}
