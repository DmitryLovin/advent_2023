package com.dmitrylovin.advent.days;

import com.dmitrylovin.advent.utils.InputParser;

public class DayWithInput implements Day {
    protected String[] inputData;

    public DayWithInput(int day) {
        this.inputData = InputParser.parseInput(day);
    }

    @Override
    public String calculate(int part) {
        return null;
    }
}
