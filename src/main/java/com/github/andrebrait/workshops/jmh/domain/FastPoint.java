package com.github.andrebrait.workshops.jmh.domain;

/**
 * A point which skips the square root operation when calculating
 * the Euclidean distance to another coordinate.
 */
public record FastPoint(int x, int y) implements Point {
    @Override
    public double distance(Coordinate coordinate) {
        double dx = coordinate.x() - x;
        double dy = coordinate.y() - y;
        return (dx * dx) + (dy * dy);
    }
}
