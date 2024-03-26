package com.github.andrebrait.workshops.jmh.presentation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public record Operands(double x1, double y1, double x2, double y2) {
    public static Operands random() {
        Random random = ThreadLocalRandom.current();
        return new Operands(
                random.nextDouble(1000.0),
                random.nextDouble(1000.0),
                random.nextDouble(1000.0),
                random.nextDouble(1000.0));
    }
}
