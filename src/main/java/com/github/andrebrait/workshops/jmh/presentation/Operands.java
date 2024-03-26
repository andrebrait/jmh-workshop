package com.github.andrebrait.workshops.jmh.presentation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

record Operands(double x1, double y1, double x2, double y2) {
    static Operands random() {
        Random random = ThreadLocalRandom.current();
        return new Operands(
                random.nextDouble(100),
                random.nextDouble(100),
                random.nextDouble(100),
                random.nextDouble(100));
    }
}
