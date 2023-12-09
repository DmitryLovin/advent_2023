package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 extends Day<ToIntFunction<String>> {
    private final Pattern PATTERN = Pattern.compile("(\\d)");
    private final Map<String, Integer> LETTERS = new HashMap<>() {
        {
            put("one", 1);
            put("two", 2);
            put("three", 3);
            put("four", 4);
            put("five", 5);
            put("six", 6);
            put("seven", 7);
            put("eight", 8);
            put("nine", 9);
        }
    };

    public Day1() {
        super(1, 142, 281);
        formatters.add(this::partOne);
        formatters.add(this::notSimpleFormat);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String[] input) {
        return Arrays.stream(input).parallel()
                .mapToInt(formatters.get(part))
                .sum();
    }

    private int partOne(String input) {
        Matcher matcher = PATTERN.matcher(input);

        DigitsCombiner combiner = new DigitsCombiner();

        while (matcher.find()) {
            combiner.put(matcher.group());
        }

        return combiner.combine();
    }

    private int notSimpleFormat(String input) {
        DigitsCombiner combiner = new DigitsCombiner();

        for (int i = 0; i < input.length(); i++) {
            for (Map.Entry<String, Integer> entry : LETTERS.entrySet()) {
                if (input.startsWith(entry.getKey(), i) || input.startsWith(entry.getValue().toString(), i)) {
                    combiner.put(entry.getValue());
                    break;
                }
            }
        }

        return combiner.combine();
    }

    static class DigitsCombiner {
        Integer first = null;
        Integer last = null;

        void put(String value) {
            put(Integer.parseInt(value));
        }

        void put(int value) {
            this.first = Objects.requireNonNullElse(this.first, value);
            last = value;
        }

        int combine() {
            return Integer.parseInt(String.format("%s%s", this.first, this.last));
        }
    }
}
