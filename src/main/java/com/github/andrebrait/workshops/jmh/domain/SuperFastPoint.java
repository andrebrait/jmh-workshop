package com.github.andrebrait.workshops.jmh.domain;

/**
 * A "super fast" point implementation written by someone who misunderstood
 * Douglas Adam's The Hitchhiker's Guide to the Galaxy and thinks 42 is the
 * answer to everything.
 */
public record SuperFastPoint(int x, int y) implements Point {
    @Override
    public double distance(Coordinate coordinate) {
        return 42;
    }
}
