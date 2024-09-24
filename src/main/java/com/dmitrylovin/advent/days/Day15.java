package com.dmitrylovin.advent.days;

import java.util.*;

public class Day15 extends Day{
    public Day15() {
        super(15, 1320, 145);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(1);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        String[] data = inputData[0].split(",");
        if(part == 0)
            return partOne(data);
        return partTwo(data);
    }

    private int partTwo(String[] data){
        Map<Integer, LinkedHashMap<String, Integer>> hashMap = new HashMap<>();
        for(String seq: data){
            boolean add = seq.contains("=");
            String[] d = seq.split("[=-]");
            int key = getValue(d[0]);

            if(add){
                if(!hashMap.containsKey(key)) {
                    hashMap.put(key, new LinkedHashMap<>());
                }
                hashMap.get(key).put(d[0],Integer.parseInt(d[1]));
            }
            else{
                if(!hashMap.containsKey(key)) {
                    hashMap.put(key, new LinkedHashMap<>());
                }
                hashMap.get(key).remove(d[0]);
            }
        }
        //System.out.println(hashMap);
        int result = 0;
        for(Map.Entry<Integer, LinkedHashMap<String, Integer>> map: hashMap.entrySet()){
            int i = 1;
            for(Map.Entry<String, Integer> entry : map.getValue().entrySet()){
                result += (map.getKey() + 1) * i * entry.getValue();
                i++;
            }
        }
        return result;
    }

    private int partOne(String[] data){
        int result = Arrays.stream(data).mapToInt(this::getValue).sum();
        return result;
    }

    private int getValue(String data){
        char[] chars = data.toCharArray();
        int result = 0;
        for(char ch : chars){
            result += (int) ch;
            result *= 17;
            result %= 256;
        }
        return result;
    }

}
