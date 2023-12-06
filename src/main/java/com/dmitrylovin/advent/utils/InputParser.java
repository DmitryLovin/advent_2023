package com.dmitrylovin.advent.utils;

import com.dmitrylovin.advent.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputParser {
    static final String INPUT_PATH = "input_data/";
    static final ClassLoader loader = Main.class.getClassLoader();

    public static String[] parseInput(int day) {
        return parseInput(String.valueOf(day));
    }

    public static String[] parseTestInput(int day) {
        return parseInput(String.format("%d_test", day));
    }

    public static String[] parseTestInput(int day, int part) {
        return parseInput(String.format("%d_test_%d", day, part));
    }

    public static String[] parseInput(String suffix) {
        InputStream inputStream = loader.getResourceAsStream(String.format("%sday_%s", INPUT_PATH, suffix));

        if (inputStream == null) {
            return new String[0];
        }

        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        return reader.lines().toArray(String[]::new);
    }
}
