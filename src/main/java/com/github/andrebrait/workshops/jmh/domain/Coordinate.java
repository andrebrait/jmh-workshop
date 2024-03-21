package com.github.andrebrait.workshops.jmh.domain;

/**
 * A set of coordinates in a plane.
 */
public interface Coordinate {
    /**
     * The value of this coordinate in the x-axis.
     */
    int x();

    /**
     * The value of this coordinate in the y-axis.
     */
    int y();
}
