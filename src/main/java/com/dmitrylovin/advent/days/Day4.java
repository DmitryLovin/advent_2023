package com.dmitrylovin.advent.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Day4 extends Day<String> {
    public Day4() {
        super(4);

        formatters.add(this::partOne);
        formatters.add(this::partTwo);

    }

    @Override
    public String calculate(int part) {
        //int sum = Arrays.stream(inputData).mapToInt(formatters.get(part)).sum();
        benchFirst();
        int sum = partTwo("");
        System.out.println();
        return String.format("Result: %d", sum);
    }

    private void benchFirst(){
        long[] result = new long[5000];
        int res = 0;
        for(int i=0; i<5000;i++){
            long time = System.nanoTime();
            res = Arrays.stream(inputData).mapToInt(this::partOne).sum();

            result[i] = System.nanoTime() - time;
        }
        System.out.println("p1. "+res);
        System.out.printf("5000 times in avg %.4f ms", Arrays.stream(result).sum() / 5000 / 1000000d);
        System.out.println();
    }

    private int partOne(String line) {
        String[] split = line.replaceFirst("Card (\\d+): +", "").split(" +\\| +");
        Set<String> winnings = Set.of(split[0].split(" +"));

        int count = (int) Arrays.stream(split[1].split(" +")).filter(winnings::contains).count();
        return count == 0 ? 0 : (int) Math.pow(2, count - 1);
    }

    private int partTwo(String f) {
        long result = 0;
        int res = 0;
        for (int z = 0; z < 5000; z++) {
            long time = System.nanoTime();
            Map<Integer, Integer> instances = new HashMap<>();
            for (int i = 0; i < inputData.length; i++) {
                String[] split = inputData[i].replaceFirst("Card (\\d+): +", "").split(" +\\| +");
                Set<String> winnings = Set.of(split[0].split(" +"));
                int c = instances.merge(i, 1, Integer::sum);

                int count = (int) Arrays.stream(split[1].split(" +")).filter(winnings::contains).count();
                for (int w = 1; w <= count; w++) {
                    instances.merge(i + w, c, Integer::sum);
                }
            }

            int r = 0;//res = instances.values().stream().mapToInt((s)->s).sum();
            for (int v : instances.values()) {
                r += v;
            }
            res = r;
            result += System.nanoTime() - time;
        }

        System.out.println("p2. "+res);
        System.out.printf("5000 times in avg %.4f ms", result / 5000 / 1000000d);
        return res;
    }
}
