package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 extends Day<ToLongFunction<Day8.Calculator>> {
    private final static Pattern SEQUENCE_PATTERN = Pattern.compile("[LR]");
    private final static Map<String, Integer> SEQUENCE_MAP = new HashMap<>() {{
        put("L", 0);
        put("R", 1);
    }};

    private final static String[] finishKeys = {"ZZZ", "Z"};

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
        return formatters.get(part).applyAsLong(Calculator.build(finishKeys[part], inputData));
    }

    private int partOne(Calculator calculator) {
        return calculator.getCount("AAA");
    }

    private long partTwo(Calculator calculator) {
        int[] counters = Arrays.stream(calculator.startKeys())
                .parallel()
                .mapToInt(calculator::getCount)
                .toArray();

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

    record Calculator(String finishKey, int[] sequence, Map<String, String[]> map) {
        static Calculator build(String finishKey, String... input) {
            return new Calculator(
                    finishKey,
                    SEQUENCE_PATTERN.matcher(input[0])
                            .results()
                            .mapToInt((r) -> SEQUENCE_MAP.get(r.group()))
                            .toArray(),
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

        int getCount(String key) {
            int counter = 0;

            for (int action : this.sequence) {
                counter += 1;
                String newKey = this.map.get(key)[action];

                if (newKey.endsWith(finishKey)) {
                    return counter;
                }

                key = newKey;
            }

            return counter + getCount(key);
        }
    }
}
