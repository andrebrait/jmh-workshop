package com.github.andrebrait.workshops.jmh.presentation;

public class Solutions {

    /*
    1. Without arguments
     */

    public static double bob() {
        return distance(0, 0, 10, 10);
    }

    public static double joe() {
        return constant(0, 0, 10, 10);
    }

    public static void steve() {
        nothing();
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    private static double constant(double x1, double y1, double x2, double y2) {
        return 0.0d;
    }

    private static void nothing() {
    }
}
