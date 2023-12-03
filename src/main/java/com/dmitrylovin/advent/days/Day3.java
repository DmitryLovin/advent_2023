package com.dmitrylovin.advent.days;

import java.util.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 extends DayWithInput {
    private static final Pattern NUMBERS = Pattern.compile("(\\d+)");
    private static final Pattern SYMBOLS = Pattern.compile("(\\*)|(&)|(\\$)|(-)|(\\+)|(%)|(/)|(#)|(=)|(@)");
    private static final Pattern GEAR = Pattern.compile("(\\*)");
    Field field = new Field();

    List<ToIntFunction<Element>> formatters = new ArrayList<>();
    List<Supplier<Set<? extends Element>>> suppliers = new ArrayList<>();

    public Day3() {
        super(3);

        prepareField();

        formatters.add(this::partOne);
        formatters.add(this::partTwo);

        suppliers.add(() -> field.numbers);
        suppliers.add(() -> field.gears);
    }

    @Override
    public String calculate(int part) {
        int sum = suppliers.get(part - 1).get().stream().mapToInt(formatters.get(part - 1)).sum();

        return String.format("Result: %d", sum);
    }

    private void prepareField() {
        for (int i = 0; i < inputData.length; i++) {

            Matcher numbers = NUMBERS.matcher(inputData[i]);
            Matcher symbols = SYMBOLS.matcher(inputData[i]);
            Matcher gears = GEAR.matcher(inputData[i]);

            while (numbers.find()) {
                field.putNumber(numbers.start(), i, Integer.parseInt(numbers.group()));
            }

            while (symbols.find()) {
                field.putSymbol(symbols.start(), i);
            }

            while (gears.find()) {
                field.putGear(gears.start(), i);
            }
        }
    }

    private int partOne(Element element) {

        boolean valid = field.symbols.stream().anyMatch((symbol) -> ((Number) element).area.in(symbol.pos));
        return valid ? ((Number) element).value : 0;
    }

    private int partTwo(Element gear) {
        Set<Number> withGear = field.numbers.stream().filter((n) ->
                n.area.in(gear.pos)
        ).collect(Collectors.toSet());

        if (withGear.size() != 2)
            return 0;

        int result = 1;
        for (Number num : withGear) {
            result *= num.value;
        }

        return result;
    }

    static class Field {
        Set<Element> symbols = new HashSet<>();
        Set<Element> gears = new HashSet<>();
        Set<Number> numbers = new HashSet<>();

        void putSymbol(int x, int y) {
            symbols.add(new Element(x, y));
        }

        void putGear(int x, int y) {
            gears.add(new Element(x, y));
        }

        void putNumber(int x, int y, int value) {
            numbers.add(new Number(x, y, value));
        }
    }

    static class Element {
        Point pos;

        Element(int x, int y) {
            pos = new Point(x, y);
        }
    }

    static class Number extends Element {
        Area area;
        int value;

        Number(int x, int y, int value) {
            super(x, y);
            this.value = value;

            int length = Integer.toString(value).length();
            this.area = new Area(new Point(x - 1, y - 1), new Point(x + length, y + 1));
        }
    }

    record Area(Point from, Point to) {
        boolean in(Point point) {
            return point.x() >= from.x()
                    && point.x() <= to.x()
                    && point.y() >= from.y()
                    && point.y() <= to.y();
        }

    }

    record Point(int x, int y) {
    }
}
