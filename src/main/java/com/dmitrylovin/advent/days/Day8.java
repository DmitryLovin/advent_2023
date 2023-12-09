package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 extends Day<ToLongFunction<Day8.Calculator>> {
    static Pattern sequencePattern = Pattern.compile("[LR]");
    static Map<String, Integer> sequenceMap;

    static {
        sequenceMap = new HashMap<>();
        sequenceMap.put("L", 0);
        sequenceMap.put("R", 1);
    }

    public Day8() {
        super(8, 6, 6);
        formatters.add(this::partOne);
        formatters.add(this::partTwo);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        return formatters.get(part).applyAsLong(Calculator.build(inputData));
    }

    private int partOne(Calculator calculator) {
        return calculator.getCount();
    }

    private long partTwo(Calculator calculator) {
        int[] counters = Arrays.stream(calculator.startKeys()).parallel().mapToInt(calculator::getCountWithSuffix).toArray();

        Set<Integer> commonDividers = getDividers(counters[0]);

        for (int i = 1; i < counters.length; i++) {
            commonDividers.retainAll(getDividers(counters[i]));
        }
        int maxDivider = commonDividers.stream().max(Integer::compareTo).orElse(1);

        long result = counters[0];
        for (int i = 1; i < counters.length; i++) {
            result *= (Long.divideUnsigned(counters[i], maxDivider));
        }
        return result;
    }

    private Set<Integer> getDividers(int number) {
        Set<Integer> dividers = new HashSet<>();

        for (int i = 2; i < number / 2; i++) {
            if ((number % i) == 0) {
                number /= i;
                dividers.add(number);
                i = 2;
            }
        }
        return dividers;
    }

    record Calculator(int[] sequence, Map<String, String[]> map) {
        static Calculator build(String... input) {
            return new Calculator(
                    sequencePattern.matcher(input[0]).results().mapToInt((r) -> sequenceMap.get(r.group())).toArray(),
                    Arrays.stream(Arrays.copyOfRange(input, 2, input.length))
                            .parallel().map((line) -> line.split(" = "))
                            .collect(
                                    Collectors.toMap(
                                            (line) -> line[0],
                                            (line) -> line[1].replaceAll("[()]", "").split(", "))));
        }

        String[] startKeys() {
            return map.keySet().stream().parallel().filter((s) -> s.endsWith("A")).toArray(String[]::new);
        }

        int getCount() {
            String key = "AAA";
            int counter = 0;
            while (true) {
                for (int action : this.sequence) {
                    counter += 1;
                    String newKey = this.map.get(key)[action];

                    if (newKey.equals("ZZZ")) {
                        return counter;
                    }

                    key = newKey;
                }
            }
        }

        int getCountWithSuffix(String key) {
            int counter = 0;
            while (true) {
                for (int action : this.sequence) {
                    counter += 1;
                    String newKey = this.map.get(key)[action];

                    if (newKey.endsWith("Z")) {
                        return counter;
                    }

                    key = newKey;
                }
            }
        }
    }
}
