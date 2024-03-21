package com.github.andrebrait.workshops.jmh.domain;

public record SuperFastPoint(int x, int y) implements Point {
    @Override
    public double distance(Coordinate coordinate) {
        return 42;
    }
}
