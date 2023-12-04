package com.dmitrylovin.advent.utils;

public class Benchmark {
    public interface Benchmarkable{
        void run();
    }

    public interface Returnable {
        int run();
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
