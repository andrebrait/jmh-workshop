package com.github.andrebrait.workshops.jmh.presentation;

public final class Solutions {

    public static double bob(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public static double joe(double x1, double y1, double x2, double y2) {
        return 0.0d;
    }

    public static void steve(double x1, double y1, double x2, double y2) {
        // do nothing
    }

    private Solutions() {
        // util class
    }
}
