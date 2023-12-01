package com.dmitrylovin.advent.days;

import com.dmitrylovin.advent.parsers.InputParser;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 implements Day {
    private final Pattern PATTERN = Pattern.compile("[\\D]*(\\d)");
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

    List<Function<String, String>> formatters = new ArrayList<>();

    private final String[] inputData;

    public Day1() {
        this.inputData = InputParser.parseInput(1);
        formatters.add(this::simpleFormat);
        formatters.add(this::notSimpleFormat);
    }

    public String calculate(int part) {
        return String.format("Result %d: %d",
                part,
                Arrays.stream(inputData)
                        .map(formatters.get(part - 1))
                        .mapToInt(Integer::parseInt).sum());
    }

    private String simpleFormat(String input) {
        Matcher matcher = PATTERN.matcher(input);

        String firstMatch = null;
        String lastMatch = null;

        while (matcher.find()) {
            String match = matcher.group(1);

            firstMatch = Objects.requireNonNullElse(firstMatch, match);
            lastMatch = match;
        }

        return String.format("%s%s", firstMatch, lastMatch);
    }

    private String notSimpleFormat(String input) {
        String output = "";

        for(int i = 0; i < input.length(); i++){
            for(Map.Entry<String, Integer> entry : LETTERS.entrySet()){
                if(input.startsWith(entry.getKey(), i) || input.startsWith(entry.getValue().toString(), i)){
                   output = String.format("%s%d",output, entry.getValue());
                   break;
                }
            }
        }

        return simpleFormat(output);
    }
}
