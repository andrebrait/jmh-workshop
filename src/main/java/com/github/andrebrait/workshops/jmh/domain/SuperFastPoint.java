package com.github.andrebrait.workshops.jmh.domain;

/**
 * A point which calculates its distance as the sum of the differences of each coordinate.
 */
public record SuperFastPoint(int x, int y) implements Point {
    @Override
    public double distance(Coordinate coordinate) {
        double dx = coordinate.x() - x;
        double dy = coordinate.y() - y;
        return dx + dy;
    }
}
