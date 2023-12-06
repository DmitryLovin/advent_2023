package com.dmitrylovin.advent.days;

import com.dmitrylovin.advent.utils.Benchmark;

import java.util.Arrays;

public class Day6 extends Day {
    public Day6() {
        super(6);
    }

    @Override
    public void calculate() {
        long result1 = Benchmark.measure(this::partOne, 10000);
        System.out.printf("p%d. %d%n", 1, result1);
        long result2 = Benchmark.measure(this::partTwo, 10000);

        System.out.printf("p%d. %d%n", 2, result2);
    }

    private long partOne() {
        int[] times = Arrays.stream(parseLine(inputData[0])).mapToInt(Integer::parseInt).toArray();
        int[] distances = Arrays.stream(parseLine(inputData[1])).mapToInt(Integer::parseInt).toArray();


        long result = 1;

        for (int i = 0; i < times.length; i++) {
            result *= calculate(times[i], distances[i]);
        }

        return result;
    }

    private long partTwo() {
        long time = Long.parseLong(String.join("", parseLine(inputData[0])));
        long distance = Long.parseLong(String.join("", parseLine(inputData[1])));

        return calculate(time, distance);
    }

    private long calculate(long time, long distance) {
        double d = Math.sqrt((time * time) - (4 * distance));
        long x1 = (long) (0.5d * (time - d));
        long x2 = (long) Math.ceil(0.5 * (time + d) - 1);

        return x2 - x1;
    }

    private String[] parseLine(String line) {
        return line.replaceFirst("\\w+: +", "").split(" +");
    }
}
