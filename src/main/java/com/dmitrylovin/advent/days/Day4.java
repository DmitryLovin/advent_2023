package com.dmitrylovin.advent.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;

public class Day4 extends Day<ToIntFunction<String[]>> {
    public Day4() {
        super(4, 13, 30);

        formatters.add(this::partOne);
        formatters.add(this::partTwo);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        return formatters.get(part).applyAsInt(inputData);
    }

    private int partOne(String... input) {
        return Arrays.stream(input).mapToInt(this::partOne).sum();
    }

    private int partOne(String line) {
        String[] split = line.replaceFirst("Card (\\d+): +", "").split(" +\\| +");
        Set<String> winnings = Set.of(split[0].split(" +"));

        int count = (int) Arrays.stream(split[1].split(" +")).filter(winnings::contains).count();
        return count == 0 ? 0 : (int) Math.pow(2, count - 1);
    }

    private int partTwo(String... input) {
        Map<Integer, Integer> instances = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            String[] split = input[i].replaceFirst("Card (\\d+): +", "").split(" +\\| +");
            Set<String> winnings = Set.of(split[0].split(" +"));
            int c = instances.merge(i, 1, Integer::sum);

            int count = (int) Arrays.stream(split[1].split(" +")).filter(winnings::contains).count();
            for (int w = 1; w <= count; w++) {
                instances.merge(i + w, c, Integer::sum);
            }
        }

        return instances.values().stream().mapToInt((s) -> s).sum();
    }
}
