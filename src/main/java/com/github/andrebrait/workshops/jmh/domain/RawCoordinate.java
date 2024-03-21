package com.github.andrebrait.workshops.jmh.domain;

/**
 * A raw coordinate which is not a point
 */
public record RawCoordinate(int x, int y) implements Coordinate {
}
