package com.github.andrebrait.workshops.jmh.presentation;

public final class Solutions {

    public static double allan(double x1, double y1, double x2, double y2) {
        double result = 0;
        // looping 18 times prevents some optimizations
        // without the sqrt, this limit is about 20
        for (int i = 0; i < 10; i++) {
            double dx = x2 - x1 - result / 10.0d;
            double dy = y2 - y1 - result / 12.0d;
            result += Math.sqrt((dx * dx) + (dy * dy) + result);
        }
        return result;
    }

    public static double bob(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public static double joe(double x1, double y1, double x2, double y2) {
        return (x1 - x2) + (y1 - y2);
    }

    public static double steve(double x1, double y1, double x2, double y2) {
        return 0.0d;
    }

    private Solutions() {
        // util class
    }
}
