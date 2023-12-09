package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 extends Day<ToIntFunction<Day2.Game>> {
    private static final Pattern PREFIX = Pattern.compile("Game (\\d+): ");

    public Day2() {
        super(2, 8, 2286);
        formatters.add(this::partOne);
        formatters.add(this::partTwo);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(10000);
    }

    protected long getResult(int part, String... input) {
        return Arrays.stream(input).parallel().map(Game::build).mapToInt(formatters.get(part)).sum();
    }

    private int partOne(Game game) {
        return game.isPossible() ? game.index() : 0;
    }

    private int partTwo(Game game) {
        GameSet minPossible = new GameSet();
        Arrays.stream(game.sets).forEach((g) -> {
            minPossible.putRed(g.red);
            minPossible.putGreen(g.green);
            minPossible.putBlue(g.blue);
        });
        return minPossible.power();
    }

    record Game(int index, GameSet[] sets) {
        boolean isPossible() {
            return Arrays.stream(sets).allMatch(GameSet::isPossible);
        }

        static Game build(String input) {
            Matcher prefixMatcher = PREFIX.matcher(input);
            if (!prefixMatcher.find())
                return new Game(0, new GameSet[0]);
            int number = Integer.parseInt(prefixMatcher.group(1));
            input = prefixMatcher.replaceFirst("");

            GameSet[] sets = Arrays.stream(input.split("; ")).map(GameSet::build).toArray(GameSet[]::new);

            return new Game(number, sets);
        }
    }

    static class GameSet {
        int red;
        int green;
        int blue;

        GameSet() {
            this(0, 0, 0);
        }

        GameSet(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        void putRed(int red) {
            this.red = Math.max(this.red, red);
        }

        void putGreen(int green) {
            this.green = Math.max(this.green, green);
        }

        void putBlue(int blue) {
            this.blue = Math.max(this.blue, blue);
        }

        boolean isPossible() {
            return red < 13 && green < 14 && blue < 15;
        }

        int power() {
            return red * green * blue;
        }

        static GameSet build(String input) {
            Map<String, Integer> balls = new HashMap<>();

            Arrays.stream(input.split(", ")).forEach((s) -> {
                String[] hand = s.split(" ");
                int count = Integer.parseInt(hand[0]);
                balls.merge(hand[1], count, Integer::sum);
            });

            return new GameSet(
                    balls.getOrDefault("red", 0),
                    balls.getOrDefault("green", 0),
                    balls.getOrDefault("blue", 0));
        }
    }
}
