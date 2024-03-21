package com.github.andrebrait.workshops.jmh.domain;

/**
 * A point implementing the standard way of calculating the
 * Euclidean distance to another coordinate.
 */
public record AccuratePoint(int x, int y) implements Point {
    @Override
    public double distance(Coordinate coordinate) {
        double dx = coordinate.x() - x;
        double dy = coordinate.y() - y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }
}
