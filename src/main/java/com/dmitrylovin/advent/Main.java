package com.dmitrylovin.advent;

import com.dmitrylovin.advent.days.*;
import com.dmitrylovin.advent.exceptions.NoImplementedDayException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
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
    }

    private static final BufferedReader READER = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int day = pickValue("day");

        if (!DAYS.containsKey(day))
            throw new NoImplementedDayException();

        DAYS.get(day).get().calculate();
    }

    private static int pickValue(String type) throws IOException {
        System.out.printf("Pick a %s: %n", type);
        return Integer.parseInt(READER.readLine());
    }
}