package com.dmitrylovin.advent.days;

import java.util.*;

public class Day12 extends Day {
    public Day12() {
        super(12, 21, 525152);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(100);
    }

    @Override
    protected long getResult(final int part, String... inputData) {
        final int times = Math.max(1, part * 5);

        return Arrays.stream(inputData).map((line) -> {
            String[] parts = line.split(" ");
            int[] numbers = Arrays.stream(String.join(",", Collections.nCopies(times, parts[1])).split(","))
                    .mapToInt(Integer::parseInt).toArray();

            String springs = String.join("?", Collections.nCopies(times, parts[0])) + ".";

            return new ConditionRecord(springs.toCharArray(), numbers);
        }).parallel().mapToLong(ConditionRecord::calculate).sum();
    }

    static class ConditionRecord {
        protected final char[] springs;
        protected final int[] groups;
        protected Map<Integer, Long> states;

        public ConditionRecord(char[] springs, int[] groups) {
            this.springs = springs;
            this.groups = groups;
            this.states = new HashMap<>();
        }

        public long calculate() {
            return new State(this, 0, 0, 0).variants();
        }

        static class State {
            private final ConditionRecord record;
            private final int springIndex;
            private final int groupIndex;
            private final int size;
            private final int key;

            protected State(ConditionRecord record, int springIndex, int groupIndex, int size) {
                this.record = record;
                this.springIndex = springIndex;
                this.groupIndex = groupIndex;
                this.size = size;
                this.key = size + (groupIndex * 100) + (springIndex * 10000);
            }

            protected long variants() {
                if (record.states.containsKey(key)) {
                    return record.states.get(key);
                }

                long result = springIndex == record.springs.length ? last() : current();

                record.states.put(key, result);
                return result;
            }

            private long last() {
                return size == 0 && groupIndex == record.groups.length ? 1 : 0;
            }

            private long current() {
                return switch (record.springs[springIndex]) {
                    case '.' -> operational();
                    case '#' -> damaged();
                    case '?' -> unidentified();
                    default -> throw new RuntimeException("Unknown spring state");
                };
            }

            private long operational() {
                if (size == 0) return new State(record, springIndex + 1, groupIndex, 0).variants();

                if (record.groups[groupIndex] != size) return 0;

                return new State(record, springIndex + 1, groupIndex + 1, 0).variants();
            }

            private long damaged() {
                if (groupIndex == record.groups.length || size >= record.groups[groupIndex]) {
                    return 0;
                }

                return new State(record, springIndex + 1, groupIndex, size + 1).variants();
            }

            private long unidentified() {
                return operational() + damaged();
            }
        }
    }
}
