package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.regex.MatchResult;
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
        formatters.add(this::partTwo);
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
        return DigitsCombiner.combine(PATTERN.matcher(input));
    }

    private int partTwo(String input) {
        DigitsCombiner combiner = new DigitsCombiner();

        int j = 0;
        while (j < input.length()) {
            boolean found = false;
            for (Map.Entry<String, Integer> entry : LETTERS.entrySet()) {
                if (input.startsWith(entry.getKey(), j) || input.startsWith(entry.getValue().toString(), j)) {
                    combiner.put(entry.getValue());
                    found = true;
                    break;
                }
            }
            if (found) break;
            j++;
        }

        for (int i = input.length() - 1; i >= j; i--) {
            boolean found = false;
            for (Map.Entry<String, Integer> entry : LETTERS.entrySet()) {
                if (input.startsWith(entry.getKey(), i) || input.startsWith(entry.getValue().toString(), i)) {
                    combiner.put(entry.getValue());
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        return combiner.combine();
    }

    static class DigitsCombiner {
        int first = 0;
        int last = 0;

        void put(int value) {
            if (this.first == 0)
                this.first = 10 * value;
            last = value;
        }

        int combine() {
            return this.first + this.last;
        }

        static int combine(Matcher matcher) {
            String[] numbers = matcher.results().map(MatchResult::group).toArray(String[]::new);

            return Integer.parseInt(numbers[0] + numbers[numbers.length - 1]);
        }
    }
}
