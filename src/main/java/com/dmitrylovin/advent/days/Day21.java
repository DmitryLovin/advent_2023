package com.dmitrylovin.advent.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Day21 extends Day{
    Map<Integer,Integer> amount = new HashMap<>();

    public Day21(){
        super(21);
        amount.put(11,5000);
        amount.put(131,26501365);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(1);
    }

    @Override
    protected long getResult(int part, String... inputData) {

        //return super.getResult(part, inputData);
        part2(inputData, amount.get(inputData.length));
        return 0;
    }

    private static void part2(String[] rows, int targetSteps) {
        Xy[] Dir = new Xy[]{new Xy(-1,0),new Xy(1,0),new Xy(0,-1),new Xy(0,1)};
        int ymax = rows.length;
        int xmax = rows[0].length();
        var start = new Xy(ymax / 2,xmax / 2);//findStart(rows);

        var visited = new HashSet<Xy>();
        var frontier = new HashSet<Xy>();
        frontier.add(start);

        long[] counts = new long[3];

        int[] frontiers = new int[xmax];
        int[] d1 = new int[xmax];
        int[] d2 = new int[xmax];

        int step = 0;
        while (true) {

            var newFrontier = new HashSet<Xy>();
            for (var p : frontier) {
                for (var d : Dir) {
                    int x = p.x + d.x;
                    int y = p.y + d.y;
                    int x1 = (x % xmax);
                    int y1 = (y % ymax);
                    x1 = x1 >= 0 ? x1 : x1 + xmax;
                    y1 = y1 >= 0 ? y1 : y1 + ymax;

                    if (rows[y1].charAt(x1) != '#') {
                        var q = new Xy(x, y);
                        if (visited.add(q)) {
                            newFrontier.add(q);
                        }
                    }
                }
            }

            int fsize = newFrontier.size();
            counts[2] = fsize + counts[0];
            counts[0] = counts[1];
            counts[1] = counts[2];

            int ix = step % xmax;
            if (step >= xmax) {
                int dx = fsize - frontiers[ix];
                d2[ix] = dx - d1[ix];
                d1[ix] = dx;
            }
            frontiers[ix] = fsize;

            frontier = newFrontier;
            step++;

            if (step >= 2*xmax) {
                if (Arrays.stream(d2).allMatch(i -> i == 0)) {
                    break;
                }
            }
        }

        System.out.println("step: " + step);
        System.out.println(counts[2]);
        System.out.println(Arrays.toString(frontiers));
        System.out.println(Arrays.toString(d1));
        System.out.println(Arrays.toString(d2));

        // extrapolate
        for (int i = step; i < targetSteps; i++) {
            int ix = i % xmax;
            d1[ix] += d2[ix];
            frontiers[ix] += d1[ix];

            counts[2] = counts[0] + frontiers[ix];
            counts[0] = counts[1];
            counts[1] = counts[2];
        }

        System.out.println(counts[2]);
    }

    record Xy(int x, int y){}
}
