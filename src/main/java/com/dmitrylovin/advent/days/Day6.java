package com.dmitrylovin.advent.days;

import java.util.Arrays;
import java.util.function.Function;

public class Day6 extends Day<Function<String[], Long>> {
    public Day6() {
        super(6, 288, 71503);
        formatters.add(this::partOne);
        formatters.add(this::partTwo);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(10000);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        return formatters.get(part).apply(inputData);
    }

    private long partOne(String... input) {
        int[] times = Arrays.stream(parseLine(input[0])).mapToInt(Integer::parseInt).toArray();
        int[] distances = Arrays.stream(parseLine(input[1])).mapToInt(Integer::parseInt).toArray();

        long result = 1;

        for (int i = 0; i < times.length; i++) {
            result *= calculate(times[i], distances[i]);
        }

        return result;
    }

    private long partTwo(String... input) {
        long time = Long.parseLong(String.join("", parseLine(input[0])));
        long distance = Long.parseLong(String.join("", parseLine(input[1])));

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
