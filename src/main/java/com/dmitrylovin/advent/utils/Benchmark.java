package com.dmitrylovin.advent.utils;

public class Benchmark {
    public interface Benchmarkable{
        void run();
    }

    public static void measureMillis(Benchmarkable task){
        long start = System.nanoTime();
        task.run();
        double result = (System.nanoTime() - start) / 1000000d;

        System.out.printf("Completed in %.2f milliseconds.%n", result);
    }
}
