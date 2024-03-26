package com.github.andrebrait.workshops.jmh.domain;

/**
 * A "super fast" fixed point implementation written by someone who
 * misunderstood Douglas Adam's The Hitchhiker's Guide to the Galaxy
 * and thinks 42 is the answer to everything.
 *
 * <p><em>42 is the Answer to the Ultimate Question of Life, The Universe, and Everything.</em>
 * That does not mean it's the answer to every question.
 */
public record FixedPoint(int x, int y) implements Point {
    @Override
    public double distance(Coordinate coordinate) {
        return 42;
    }
}
