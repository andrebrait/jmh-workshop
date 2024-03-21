package com.github.andrebrait.workshops.jmh.domain;

/**
 * The standard Point interface, which is a Coordinate capable of
 * calculating the distance between itself and another coordinate.
 */
public interface Point extends Coordinate {

    /**
     * Calculates the distance between this point and another coordinate.
     *
     * @param coordinate the other coordinate
     * @return the calculated distance
     */
    double distance(Coordinate coordinate);
}
