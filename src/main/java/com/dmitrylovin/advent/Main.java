package com.dmitrylovin.advent;

import com.dmitrylovin.advent.days.*;
import com.dmitrylovin.advent.exceptions.NoImplementedDayException;
import com.dmitrylovin.advent.utils.MathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Main {
    private static final Map<Integer, Supplier<CalculableDay>> DAYS;

    static {
        DAYS = new HashMap<>();

        DAYS.put(1, Day1::new);
        DAYS.put(2, Day2::new);
        DAYS.put(3, Day3::new);
        DAYS.put(4, Day4::new);
        DAYS.put(5, Day5::new);
        DAYS.put(6, Day6::new);
        DAYS.put(7, Day7::new);
        DAYS.put(8, Day8::new);
        DAYS.put(9, Day9::new);
        DAYS.put(10, Day10::new);
        DAYS.put(12, Day12::new);
        DAYS.put(15, Day15::new);
        DAYS.put(16, Day16::new);
        DAYS.put(17, Day17::new);
        DAYS.put(18, Day18::new);
        DAYS.put(21, Day21::new);
        DAYS.put(23, Day23::new);
        DAYS.put(24, Day24::new);
    }

    private static final BufferedReader READER = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        test();

        int day = pickValue("day");

        if (!DAYS.containsKey(day))
            throw new NoImplementedDayException();

        DAYS.get(day).get().calculate();
    }

    private static int pickValue(String type) throws IOException {
        System.out.printf("Pick a %s: %n", type);
        return Integer.parseInt(READER.readLine());
    }

    private static void test(){
        double[][] a = new double[][]{
                {-3, 18, 0},
                {-2, 17, -6},
                {8, -14, 17}
                //{1,2,3},
                //{4,5,6}
        };

        double[][] b = new double[][]{
                {-7},
                {18},
                {-2}
                //{7,8},
                //{9,10},
                //{11,12}
        };

        for(double[] da : MathUtils.multiplyMatrices(MathUtils.multiplyMatrix(a, 1d / 17d), b)){
            for(double d : da){
                System.out.println(d);
            }
        }
    }
}