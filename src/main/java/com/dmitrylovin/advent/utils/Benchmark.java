package com.dmitrylovin.advent.utils;

import java.util.Arrays;
import java.util.function.Supplier;

public class Benchmark {
    public interface Benchmarkable{
        void run();
    }

    public interface Returnable {
        int run();
    }

    public static long measure(Supplier<Long> supplier, int count) {
        long result = 0;
        double minTime = Double.MAX_VALUE;
        double[] times = new double[count];
        int counter = count;

        while(counter > 0) {
            long start = System.nanoTime();

            result = supplier.get();

            double finish = (System.nanoTime() - start) / 1000000d;
            times[counter - 1] = finish;
            minTime = Math.min(minTime, finish);
            counter--;
        }

        double avg = Arrays.stream(times).average().getAsDouble();

        System.out.printf("%d attempts, fastest: %.5fms, avg: %.5fms%n", count, minTime, avg);
        return result;
    }

    public static int measureAndReturn(String message, Returnable task) {
        long start = System.nanoTime();
        int result = task.run();
        double time = (System.nanoTime() - start) / 1000000d;

        System.out.printf("%s in %.2f milliseconds.%n", message, time);
        return result;
    }

    public static void measureMillis(String message, Benchmarkable task){
        long start = System.nanoTime();
        task.run();
        double result = (System.nanoTime() - start) / 1000000d;

        System.out.printf("%s in %.2f milliseconds.%n", message, result);
    }
}
