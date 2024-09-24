package com.dmitrylovin.advent.days;

import java.util.HashMap;
import java.util.Map;

public class Day18 extends Day {
    Map<String, int[]> movements = new HashMap<>();

    public Day18() {
        super(18, 62, 952408144115L);
        movements.put("R", new int[]{1, 0});
        movements.put("D", new int[]{0, 1});
        movements.put("L", new int[]{-1, 0});
        movements.put("U", new int[]{0, -1});

        movements.put("0", new int[]{1, 0});
        movements.put("1", new int[]{0, 1});
        movements.put("2", new int[]{-1, 0});
        movements.put("3", new int[]{0, -1});
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(100);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        if (part == 0)
            return partOne(inputData);
        return partTwo(inputData);
    }

    long partTwo(String... inputData) {
        long x = 0;
        long y = 0;

        long p = 0;
        long xA = 0;
        long yA = 0;

        for (String line : inputData) {
            String prep = line.split("#")[1].replace(")", "");
            int[] dir = movements.get("" + prep.charAt(5));
            int multi = Integer.parseInt(prep.substring(0, 5), 16);
            p += multi;
            long nextX = x + dir[0] * multi;
            long nextY = y + dir[1] * multi;
            xA += x * nextY;
            yA += y * nextX;

            x = nextX;
            y = nextY;
        }

        return (xA - yA) / 2 + p/2 + 1;
    }

    long partOne(String... inputData) {
        long x = 0;
        long y = 0;

        long p = 0;
        long xA = 0;
        long yA = 0;

        for (String line : inputData) {
            String[] split = line.split(" ");
            int[] dir = movements.get(split[0]);
            int multi = Integer.parseInt(split[1]);
            p += multi;
            long nextX = x + dir[0] * multi;
            long nextY = y + dir[1] * multi;
            xA += x * nextY;
            yA += y * nextX;

            x = nextX;
            y = nextY;
        }

        return (xA - yA) / 2 + p/2 + 1;
    }
}
