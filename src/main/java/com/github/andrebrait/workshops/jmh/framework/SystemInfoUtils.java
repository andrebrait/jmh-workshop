package com.github.andrebrait.workshops.jmh.framework;

import oshi.SystemInfo;

import java.util.stream.Collectors;

public final class SystemInfoUtils {

    /**
     * Prints some information about the JVM, CPU and OS
     */
    public static void printSystemInfo() {
        System.out.printf(
                "JVM info:%n"
                        + "\tName: %s%n"
                        + "\tVendor: %s%n"
                        + "\tVersion: %s%n"
                        + "\tArchitecture: %s%n"
                        + "%n",
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.vendor"),
                System.getProperty("java.vm.version"),
                System.getProperty("os.arch"));
        SystemInfo info = new SystemInfo();
        System.out.printf(
                "CPU info:%n%s%n%n",
                info.getHardware()
                        .getProcessor()
                        .toString()
                        .lines()
                        .filter(s -> !s.startsWith("ProcessorID:"))
                        .map("\t"::concat)
                        .collect(
                                Collectors.joining(System.lineSeparator())));
        System.out.printf(
                "OS info:%n%s%n%n",
                info.getOperatingSystem().toString().lines().map("\t"::concat).collect(
                        Collectors.joining(System.lineSeparator())));
    }

    private SystemInfoUtils() {
        // util class
    }
}
