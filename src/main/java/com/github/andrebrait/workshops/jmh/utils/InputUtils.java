package com.github.andrebrait.workshops.jmh.utils;

import java.util.Scanner;

public final class InputUtils {

    public static <E extends Enum<E>> E select(String message, Class<E> enumType) {
        System.out.println(message);
        E[] enumConstants = enumType.getEnumConstants();
        if (enumConstants.length == 0) {
            throw new IllegalArgumentException("Enumeration must contain at least one element");
        }
        for (E e : enumConstants) {
            System.out.printf("%d. %s%n", e.ordinal() + 1, e.name());
        }
        int choice = new Scanner(System.in).nextInt();
        if (choice <= 0 || choice > enumConstants.length) {
            throw new IllegalArgumentException("Invalid choice: %d. Must be between 1 and %d".formatted(choice,
                    enumConstants.length));
        }
        return enumConstants[choice - 1];
    }

    private InputUtils() {
        // util class
    }
}
