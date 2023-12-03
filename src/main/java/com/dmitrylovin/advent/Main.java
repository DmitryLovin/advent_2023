package com.dmitrylovin.advent;

import com.dmitrylovin.advent.days.Day;
import com.dmitrylovin.advent.days.Day1;
import com.dmitrylovin.advent.days.Day2;
import com.dmitrylovin.advent.days.Day3;
import com.dmitrylovin.advent.utils.Benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Main {
    private static final Map<Integer, Supplier<Day>> DAYS;

    static {
        DAYS = new HashMap<>();

        DAYS.put(1, Day1::new);
        DAYS.put(2, Day2::new);
        DAYS.put(3, Day3::new);
    }

    private static final BufferedReader READER = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int day = pickValue("day");
        int part = pickValue("part");

        Benchmark.measureMillis(() ->
                System.out.println(
                        DAYS.get(day).get().calculate(part)
                ));
    }

    private static int pickValue(String type) throws IOException {
        System.out.printf("Pick a %s: %n", type);
        return Integer.parseInt(READER.readLine());
    }
}