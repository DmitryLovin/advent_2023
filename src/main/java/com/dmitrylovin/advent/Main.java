package com.dmitrylovin.advent;

import com.dmitrylovin.advent.days.Day;
import com.dmitrylovin.advent.days.Day1;
import com.dmitrylovin.advent.days.Day2;
import com.dmitrylovin.advent.days.Day3;
import com.dmitrylovin.advent.exceptions.NoDaysSpecifiedException;
import com.dmitrylovin.advent.exceptions.NoPartsSpecifiedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<Integer, Day> DAYS;

    static {
        DAYS = new HashMap<>();

        DAYS.put(1, new Day1());
        DAYS.put(2, new Day2());
        DAYS.put(3, new Day3());
    }

    private static final BufferedReader READER = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int day = pickDay();
        int part = pickPart();

        System.out.println(DAYS.get(day).calculate(part));
    }

    private static int pickDay() throws IOException {
        System.out.println("Pick a day: ");
        return Integer.parseInt(READER.readLine());
    }

    private static int pickPart() throws IOException {
        System.out.println("Pick a part: ");
        return Integer.parseInt(READER.readLine());
    }
}