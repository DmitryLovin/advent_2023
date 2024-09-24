package com.dmitrylovin.advent.days;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Day16 extends Day {
    private int width;
    private int height;
    Map<Integer, Character> map;
    Map<Character, Function<int[], int[]>> elementSuppliers = new HashMap<>();
    Map<Integer, Set<Integer>> light;

    public Day16() {
        super(16,46,51);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(1);

    }

    @Override
    protected long getResult(int part, String... inputData) {
        elementSuppliers.put('/', (dir) -> new int[]{-dir[1], -dir[0]});
        elementSuppliers.put('\\', (dir) -> new int[]{dir[1], dir[0]});
        elementSuppliers.put('-', (dir) -> new int[]{dir[0] + dir[1], 0});
        elementSuppliers.put('|', (dir) -> new int[]{0, dir[1] + dir[0]});
        width = inputData[0].length();
        height = inputData.length;

        map = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (inputData[y].charAt(x) != '.')
                    map.put(y * width + x, inputData[y].charAt(x));
            }
        }

        if(part == 0){
            return partOne(inputData);
        }

        int res = check(0,0,1,0);
        res = Math.max(res, check(0,0,0,1));

        res = Math.max(res, check(width - 1,0,0,1));
        res = Math.max(res, check(width - 1,0,-1,0));

        res = Math.max(res, check(width - 1,height - 1,-1,0));
        res = Math.max(res, check(width - 1,height - 1,0,-1));

        res = Math.max(res, check(0,height - 1,0,-1));
        res = Math.max(res, check(width - 1,height - 1,1,0));

        for(int x = 1; x < width - 1; x++){
            res = Math.max(res, check(x, 0, 0, 1));
            res = Math.max(res, check(x, height -1, 0, -1));
        }

        for(int y = 1; y < height - 1; y++){
            res = Math.max(res, check(0, y, 1, 0));
            res = Math.max(res, check(width - 1, y, -1, 0));
        }

        System.out.println(res);

        return res;
    }

    private int check(int x, int y, int dx, int dy){
        light = new HashMap<>();
        int index = y * width + x;
        int[] initDir = {dx,dy};

        Set<Integer> init = new HashSet<>();
        light.put(index, init);
        init.add(dy * 10 + dx);

        if(map.containsKey(index))
            initDir = elementSuppliers.get(map.get(index)).apply(initDir);

        getStep(x, y, initDir[0], initDir[1]);

        return light.size();
    }

    private int partOne(String... inputData){
        light = new HashMap<>();

        Set<Integer> init = new HashSet<>();
        init.add(1);
        light.put(0, init);

        int[] initDir = {1,0};
        if(map.containsKey(0))
            initDir = elementSuppliers.get(map.get(0)).apply(initDir);

        getStep(0, 0, initDir[0], initDir[1]);

        return light.size();
    }

    private boolean getStep(int x, int y, int dx, int dy) {
        x += dx;
        y += dy;

        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        int index = y * width + x;
        int dir = dy * 10 + dx;
        if(light.containsKey(index)){
            if(light.get(index).contains(dir)){
                return false;
            }

        }else{
            light.put(index, new HashSet<>());
        }

        light.get(index).add(dir);

        Character c = map.get(index);
        if(c == null){
            return getStep(x, y, dx, dy);
        }
        else{
            int[] dirs = elementSuppliers.get(c).apply(new int[]{dx, dy});
            //System.out.printf("%d-%d%n",dirs[0],dirs[1]);
            if(c == '/' || c == '\\'){
                return getStep(x, y, dirs[0], dirs[1]);
            }else{
                return getStep(x, y, dirs[0], dirs[1]) || getStep(x, y, -dirs[0], -dirs[1]);
            }
        }
    }
}
