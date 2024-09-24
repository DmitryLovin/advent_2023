package com.dmitrylovin.advent.days;

import java.util.*;

public class Day23 extends Day {
    int width = 0;
    int lastIndex;
    int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    char[][] chars;
    int part;
    int topMax;
    int iterations;
    Map<Integer, Integer> map;

    public Day23() {
        super(23, 94, 154);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(2);
    }

    Map<Integer, Map<Integer, Integer>> points;

    @Override
    protected long getResult(int part, String... inputData) {
        this.part = part;
        map = new HashMap<>();
        chars = new char[inputData.length][];
        for (int i = 0; i < inputData.length; i++) {
            chars[i] = inputData[i].toCharArray();
        }
        this.width = chars[0].length;
        this.lastIndex = chars.length * width - 2;
        topMax = 0;
        iterations = 0;

        if (part == 0) {
            int m = step(1, 0, new HashSet<>(), 12);
            return m;
        } else {
            points = new HashMap<>();
            points.put(1, new HashMap<>());
            for (int y = 0; y < width; y++) {
                for (int x = 0; x < width; x++) {
                    if (cross(x, y))
                        points.put(y * width + x, new HashMap<>());
                }
            }
            points.put(lastIndex, new HashMap<>());
            for (Map.Entry<Integer, Map<Integer, Integer>> entry : points.entrySet()) {
                Integer p = entry.getKey();
                int x = p % width;
                int y = p / width;
                for (int[] dir : dirs) {
                    Object[] nextPoint = findNext(x, y, dir);
                    if (nextPoint != null)
                        entry.getValue().put((int) nextPoint[0], (int) nextPoint[1]);
                }
            }
            int res = step(1, 0, new HashSet<>(1));
            System.out.println(iterations);
            return res;
        }
        //System.out.println(m);
        //return super.getResult(part, inputData);
    }

    private int step(int index, int length, Set<Integer> indices) {
        if (index == lastIndex) {
            return length;
        }

        indices.add(index);

        if (points.get(index).keySet().contains(lastIndex)) {
            return length + points.get(index).get(lastIndex);
        }

        iterations += 1;

        int max = 0;
        for (Map.Entry<Integer, Integer> entry : points.get(index).entrySet()) {
            int nIdx = entry.getKey();

            if (indices.contains(nIdx) || (indices.containsAll(points.get(nIdx).keySet()))) {
                //System.out.println(indices);
                //System.out.println(nIdx);
                continue;
            }

            int l = entry.getValue();
            max = Math.max(max, step(nIdx, length + l, new HashSet<>(indices)));
        }
        
        return max;
    }

    private Object[] findNext(int xI, int yI, int[] iDir) {
        int l = 1;
        //System.out.println("xi:"+xI+" yi:"+yI);
        int x = xI + iDir[0];
        int y = yI + iDir[1];
        if (x < 0 || x >= width || y < 0 || y >= width || chars[y][x] == '#')
            return null;
        //System.out.println("x:"+x+" y:"+y);
        Set<Integer> indices = new HashSet<>();
        indices.add(y * width + x);

        while (true) {
            l += 1;
            List<int[]> available = new ArrayList<>();

            for (int[] dir : dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx == xI && ny == yI)
                    continue;

                int index = ny * width + nx;

                //System.out.printf("[%d,%d] s: %d%n", nx ,ny, indices.size());
                if (points.containsKey(index))
                    return new Object[]{index, l};

                if (nx < 0 || nx >= width || ny < 0 || ny >= width)
                    continue;

                //System.out.println("in border");
                if (indices.contains(index))
                    continue;

                if (chars[ny][nx] == '#')
                    continue;

                x = nx;
                y = ny;
                //System.out.println("x:"+x+" y:"+y);
                indices.add(index);
                available.add(dir);
                break;
            }
        }
    }

    private boolean cross(int x, int y) {
        if (x == 0 || y == 0 || x == width - 1 || y == width - 1 || chars[y][x] == '#')
            return false;

        int ways = 0;

        if (chars[y][x - 1] != '#')
            ways += 1;
        if (chars[y][x + 1] != '#')
            ways += 1;
        if (chars[y + 1][x] != '#')
            ways += 1;
        if (chars[y - 1][x] != '#')
            ways += 1;

        return ways > 2;
    }

    int step(int x, int y, Set<Integer> indices, int tdir) {
        indices.add(y * width + x);
        if (y * width + x == lastIndex) {
            int res = indices.size() - 1;
            //topMax = Math.max(res, topMax);
            //System.out.println(topMax);
            return res;
        }
        /*int index = (y * width + x) * 100 + tdir;
        Integer pr = map.get(index);
        if(pr!=null && pr > (indices.size() - 1)){
            //System.out.println("x:"+x+" y:"+y + " s:"+indices.size() + " pr:" +pr + " dir:"+tdir);
            return 0;
        }
        //if(indices.size() == 154)
        //System.out.println("x");
        map.put(index, indices.size() - 1);*/

        int max = 0;

        while (true) {
            List<int[]> available = new ArrayList<>();
            if (x == 127 && y == 133) {
                available.add(dirs[2]);
            } else {
                for (int[] dir : dirs) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    //System.out.printf("[%d,%d] s: %d%n", nx ,ny, indices.size());

                    if (nx < 0 || nx >= width || ny < 0 || ny >= width)
                        continue;

                    //System.out.println("in border");
                    if (indices.contains(ny * width + nx))
                        continue;

                    if (chars[ny][nx] == '#')
                        continue;

                    if (part == 0) {
                        if (chars[ny][nx] == 'v' && dir == dirs[3])
                            continue;
                        if (chars[ny][nx] == '>' && dir == dirs[1])
                            continue;
                    }
                    available.add(dir);
                }
            }
            if (available.isEmpty()) {
                for (int index : indices) {
                    map.remove(index * 100 + 8);
                    map.remove(index * 100 + 9);
                    map.remove(index * 100 + 11);
                    map.remove(index * 100 + 12);
                }
                return max;
            } else if (available.size() == 1) {
                int[] d = available.get(0);
                x += d[0];
                y += d[1];
                //System.out.println("x:"+x+" y:"+y);
                tdir = 10 + 2 * d[1] + d[0];

                indices.add(y * width + x);
                if (y * width + x == lastIndex) {
                    //System.out.println("FINISH");
                    //System.out.println(indices);
                    int res = indices.size() - 1;
                    //topMax = Math.max(res, topMax);
                    //System.out.println(topMax);
                    return res;
                }
            } else {
                /*int index = (y * width + x) * 100 + tdir;
                Integer pr = map.get(index);

                if (pr != null && pr > indices.size() - 1) {
                    //System.out.println("x:"+x+" y:"+y+" pr:"+pr+" i:"+index);
                    return max;
                } else {
                    map.put(index, indices.size() - 1);
                }*/
                for (int[] dir : available) {

                    int tmp = step(x + dir[0], y + dir[1], new HashSet<>(indices), 10 + 2 * dir[1] + dir[0]);
                    max = Math.max(max, tmp);
                }
                return max;
            }
        }
    }
}
