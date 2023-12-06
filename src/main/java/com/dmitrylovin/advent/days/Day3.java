package com.dmitrylovin.advent.days;

import java.util.*;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 extends Day<Day3.Element> {
    private static final Pattern PATTERN = Pattern.compile("(\\d+)|(\\*)|(&)|(\\$)|(-)|(\\+)|(%)|(/)|(#)|(=)|(@)");
    Field field = new Field();

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
    public void calculate() {
        for (int i = 0; i < 2; i++) {
            int sum = suppliers.get(i).get().stream().mapToInt(formatters.get(i)).sum();

            System.out.println(String.format("Result: %d", sum));
        }
    }

    private void prepareField() {
        for (int i = 0; i < inputData.length; i++) {
            Matcher matcher = PATTERN.matcher(inputData[i]);

            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    field.putNumber(matcher.start(), i, Integer.parseInt(matcher.group()));
                } else if (matcher.group(2) != null) {
                    field.putGear(matcher.start(), i);
                    field.putSymbol(matcher.start(), i);
                } else {
                    field.putSymbol(matcher.start(), i);
                }
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
