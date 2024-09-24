package com.dmitrylovin.advent.days;

import java.util.*;

public class Day17 extends Day {

    public Day17() {
        super(17, 102, 94);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(1);
    }

    int height;
    int width;
    int last;
    Map<Integer, Integer> map;
    Map<Integer, Integer> history;
    int length;

    @Override
    protected long getResult(int part, String... inputData) {
        map = new HashMap<>();
        history = new HashMap<>();
        height = inputData.length;
        width = inputData[0].length();
        last = width * height - 1;
        length = Integer.MAX_VALUE;//0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map.put(y * width + x, inputData[y].charAt(x) - '0');
                //if (x == y || x + 1 == y) {
                //    length += inputData[y].charAt(x) - '0';
                //}
            }
        }

        history.put(0,0);

        //Set<Integer> indices = new HashSet<>();
        //indices.add(0);

        int res = Math.min(getState(0, 0, 1, 0, 0, 0), getState(0, 0, 0, 1, 0, 0));
        //getState(0, 0, 1, 0, 0, 0, Set.copyOf(indices));
        //getState(0, 0, 0, 1, 0, 0, Set.copyOf(indices));
        System.out.println(res);
        //System.out.println(history);
        return res;
    }

    private int getState(int x, int y, int dx, int dy, int steps, int res) {
        x += dx;
        y += dy;

        //System.out.printf("X: %d | Y: %d%n", x, y);

        if (x < 0 || x >= width || y < 0 || y >= height) {
            //System.out.println("OUT OF BOUNDS");
            return length;
        }

        int index = (y * width + x);
        res += map.get(index);

        if (index == last) {
            return res;
        }

        steps += 1;

        int i = (1+index) * 10000 + (2 + dy) * 1000 + (2+dx) * 100 + steps;

        if (history.getOrDefault(i, length) <= res) {
            //System.out.printf("WAS HERE: %d%n", res);
            return length;
        }

        history.put(i, res);

        //System.out.printf("%d,%d,%d%n",x,y,res);

        int rot = Math.min(
                getState(x, y, dx == 0 ? 1 : 0, dy == 0 ? 1 : 0, 0, res),
                getState(x, y, dx == 0 ? -1 : 0, dy == 0 ? -1 : 0, 0, res));

        if (steps != 3) {
            rot = Math.min(rot, getStateV2(x, y, dx, dy, steps, res));
        }

        length = Math.min(length, rot);

        //System.out.println(rot);

        return rot;
    }

    private int getStateV2(int x, int y, int dx, int dy, int steps, int res) {
        x += dx;
        y += dy;

        //System.out.printf("X: %d | Y: %d%n", x, y);

        if (x < 0 || x >= width || y < 0 || y >= height) {
            //System.out.println("OUT OF BOUNDS");
            return Integer.MAX_VALUE;
        }

        int index = (y * width + x);
        res += map.get(index);

        steps += 1;

        if (index == last) {
            return steps > 3 ? res : Integer.MAX_VALUE;
        }

        int i = index * 10000 + (2 + dy) * 1000 + (2+dx) * 100 + steps;

        if (history.getOrDefault(i, length) <= res) {
            //System.out.printf("WAS HERE: %d%n", res);
            return length;
        }

        history.put(i, res);

        //System.out.printf("%d,%d,%d%n",x,y,res);

        int rot = length;

        if (steps >= 4){
            rot = Math.min(
                    getStateV2(x, y, dx == 0 ? 1 : 0, dy == 0 ? 1 : 0, 0, res),
                    getStateV2(x, y, dx == 0 ? -1 : 0, dy == 0 ? -1 : 0, 0, res));
            if(steps != 10){
                rot = Math.min(rot, getStateV2(x, y, dx, dy, steps, res));
            }
        }else{
            rot = Math.min(rot, getStateV2(x, y, dx, dy, steps, res));
        }

        length = Math.min(length, rot);

        //System.out.println(rot);

        return rot;
    }
}
