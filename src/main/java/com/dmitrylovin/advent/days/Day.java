package com.dmitrylovin.advent.days;

import com.dmitrylovin.advent.utils.InputParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class Day<T> implements CalculableDay {
    protected String[] inputData;
    protected String plainInput;
    List<ToIntFunction<T>> formatters = new ArrayList<>();

    public Day(int day) {
        this.inputData = InputParser.parseInput(day);
        this.plainInput = String.join(".", inputData);
    }

    @Override
    public String calculate(int part) {
        return null;
    }
}
