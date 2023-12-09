package com.dmitrylovin.advent.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class Day9 extends Day<ToIntFunction<String>> {
    public Day9() {
        super(9, 114, 2);
        formatters.add(this::partOne);
        formatters.add(this::partTwo);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String... input) {
        return Arrays.stream(input).parallel().mapToInt(formatters.get(part)).sum();
    }

    private int partOne(String input) {
        return new SensorData(Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()).prediction();
    }

    private int partTwo(String input) {
        return new SensorData(Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()).history();
    }

    static class SensorData {
        private final List<Integer> seq;
        private final int first;
        private final int last;

        private int[] state;

        SensorData(int[] state) {
            this.state = state;
            this.seq = new ArrayList<>();
            this.first = state[0];
            this.last = state[state.length - 1];
        }

        int prediction() {
            int result = 0;
            if (calculateNextState()) {
                for (int i = seq.size() - 1; i >= 0; i--) {
                    result += seq.get(i);
                }
            }
            return last + result;
        }

        int history() {
            int result = 0;
            if (calculatePrevState()) {
                for (int i = seq.size() - 1; i >= 0; i--) {
                    result = seq.get(i) - result;
                }
            }
            return first - result;
        }

        private int[] downState() {
            int[] result = new int[this.state.length - 1];
            for (int i = 0; i < result.length; i++) {
                result[i] = this.state[i + 1] - state[i];
            }

            return result;
        }

        private boolean calculateNextState() {
            int[] nextState = downState();
            boolean result = Arrays.stream(nextState).allMatch((i) -> i == 0);

            if (!result) {
                seq.add(nextState[nextState.length - 1]);
                this.state = nextState;
            }

            return result || calculateNextState();
        }

        private boolean calculatePrevState() {
            int[] nextState = downState();
            boolean result = Arrays.stream(nextState).allMatch((i) -> i == 0);

            if (!result) {
                seq.add(nextState[0]);
                this.state = nextState;
            }

            return result || calculatePrevState();
        }
    }
}
