package com.dmitrylovin.advent;

import com.dmitrylovin.advent.days.Day;
import com.dmitrylovin.advent.days.Day1;
import com.dmitrylovin.advent.exceptions.NoDaysSpecifiedException;
import com.dmitrylovin.advent.exceptions.NoPartsSpecifiedException;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<Integer, Day> DAYS;

    static {
        DAYS = new HashMap<>();

        DAYS.put(1, new Day1());
    }

    public static void main(String[] args) {
        if(args.length == 0)
            throw new NoDaysSpecifiedException();

        if(args.length == 1)
            throw new NoPartsSpecifiedException();

        int key = Integer.parseInt(args[0]);
        int part = Integer.parseInt(args[1]);

        System.out.println(DAYS.get(key).calculate(part));
    }
}