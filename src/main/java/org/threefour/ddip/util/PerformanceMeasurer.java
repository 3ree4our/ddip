package org.threefour.ddip.util;

public class PerformanceMeasurer {
    public static long computeElapsedTime(long startedAt) {
        return System.currentTimeMillis() - startedAt;
    }

    public static long computeUsedMemory(long beforeMemory) {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - beforeMemory;
    }
}