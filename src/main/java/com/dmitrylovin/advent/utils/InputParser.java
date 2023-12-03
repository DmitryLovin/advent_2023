package com.dmitrylovin.advent.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputParser {
    static final String INPUT_PATH = "input_data/";

    public static String[] parseInput(int day) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(String.format("%sday_%d", INPUT_PATH, day));
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        return reader.lines().toArray(String[]::new);
    }
}
