package com.dmitrylovin.advent.days;

import com.dmitrylovin.advent.utils.Benchmark;
import com.dmitrylovin.advent.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Day<T> implements CalculableDay {
    protected String[] inputData;
    private final String[][] testInputData = new String[2][];

    protected final List<T> formatters = new ArrayList<>();

    private final long[] testResults = new long[2];

    public Day(int day) {
        this(day, 0, 0);
    }

    public Day(int day, long testResultOne, long testResultTwo) {
        this.inputData = FileUtils.parseInput(day);
        this.testInputData[0] = FileUtils.parseTestInput(day);
        if (this.testInputData[0].length == 0) {
            this.testInputData[0] = FileUtils.parseTestInput(day, 1);
        }
        this.testInputData[1] = FileUtils.parseTestInput(day, 2);
        if (this.testInputData[1].length == 0) {
            this.testInputData[1] = this.testInputData[0];
        }

        this.testResults[0] = testResultOne;
        this.testResults[1] = testResultTwo;
    }

    @Override
    public void calculate() {

    }

    protected void calculateWithBenchmark(int times) {
        for (int i = 0; i < 2; i++) {
            if (!testCalculation(i)) {
                System.out.printf("Test #%d has been failed!%n", i + 1);
                continue;
            }

            //getResult(i, inputData);

            int finalI = i;

            long result = Benchmark.measure(() -> (getResult(finalI, inputData)), times);
            System.out.printf("p%d. %d%n", i + 1, result);
        }
    }

    private boolean testCalculation(int part) {
        long result = getResult(part, testInputData[part]);
        return test(result, part);
    }

    private boolean test(long result, int part) {
        return result == this.testResults[part];
    }

    protected long getResult(int part, String... inputData) {
        return 0;
    }
}
