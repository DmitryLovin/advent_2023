package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.Function;

public class Day5 extends Day<Function<String[], Long>> {
    private final String[] mapKeys = {
            "seed-to-soil",
            "soil-to-fertilizer",
            "fertilizer-to-water",
            "water-to-light",
            "light-to-temperature",
            "temperature-to-humidity",
            "humidity-to-location"
    };

    public Day5() {
        super(5, 35, 46);
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
        String[] data = (String.join(".", input)).split("\\.\\.");
        List<Long> seeds = Arrays.stream(data[0].replace("seeds: ", "").split(" ")).collect(
                ArrayList::new,
                (l, v) -> l.add(Long.parseLong(v)),
                ArrayList::addAll
        );

        Map<String, List<long[]>> maps = new HashMap<>();
        fillMaps(maps, data);

        for (String key : mapKeys) {
            for (int i = 0; i < seeds.size(); i++) {
                long seed = seeds.get(i);
                for (long[] entry : maps.get(key)) {
                    if (seed >= entry[1] && seed < (entry[1] + entry[2])) {
                        seeds.set(i, seed - entry[1] + entry[0]);
                        break;
                    }
                }
            }
        }
        return seeds.stream().min(Long::compareTo).orElse(0L);
    }

    private long partTwo(String... input) {
        String[] data = (String.join(".", input)).split("\\.\\.");
        List<Long> seeds = Arrays.stream(data[0].replace("seeds: ", "").split(" ")).collect(
                ArrayList::new,
                (l, v) -> l.add(Long.parseLong(v)),
                ArrayList::addAll
        );

        Map<String, List<long[]>> maps = new HashMap<>();
        fillMaps(maps, data);

        for (String key : mapKeys) {
            for (int i = 0; i < seeds.size(); i += 2) {
                long seed = seeds.get(i);
                long length = seeds.get(i + 1);

                for (long[] entry : maps.get(key)) {
                    if (seed >= entry[1] && seed < (entry[1] + entry[2])) {
                        long new_start = (seed - entry[1]) + entry[0];
                        long max = (entry[0] + entry[2]) - 1;
                        if ((new_start + length - 1) > max) {
                            long new_length = (max - new_start) + 1;
                            long new_seed = (seed + new_length);
                            long new_seed_length = (length - new_length);
                            seeds.add(new_seed);
                            seeds.add(new_seed_length);
                            seeds.set(i + 1, new_length);
                        }
                        seeds.set(i, new_start);
                        break;
                    } else if ((seed + length - 1) >= entry[1] && (seed + length - 1) < (entry[1] + entry[2])) {
                        long new_start = entry[0];
                        long diff = entry[1] - seed;
                        long new_length = length - diff;

                        seeds.set(i, new_start);
                        seeds.set(i + 1, new_length);
                        seeds.add(seed);
                        seeds.add(diff);
                        break;
                    } else if ((entry[1] > seed && (entry[1] + entry[2]) < (seed + length))) {
                        long new_start = entry[0];
                        long new_length = entry[2];

                        long new_right = entry[1] + entry[2];
                        long new_left_length = entry[1] - seed;
                        long new_right_length = length - new_length - new_left_length;

                        seeds.set(i, new_start);
                        seeds.set(i + 1, new_length);

                        seeds.add(seed);
                        seeds.add(new_left_length);
                        seeds.add(new_right);
                        seeds.add(new_right_length);
                        break;
                    }
                }
            }
        }

        long min_result = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size(); i += 2) {
            min_result = Math.min(seeds.get(i), min_result);
        }

        return min_result;
    }

    private void fillMaps(Map<String, List<long[]>> maps, String[] data) {
        for (int i = 0; i < mapKeys.length; i++) {
            String key = mapKeys[i];
            List<long[]> mapEntryList = new ArrayList<>();
            maps.put(key, mapEntryList);

            for (String entry : data[i + 1].replace(String.format("%s map:.", key), "").split("\\.")) {
                long[] entryData = Arrays.stream(entry.split(" ")).mapToLong(Long::parseLong).toArray();

                mapEntryList.add(entryData);
            }
        }
    }
}
