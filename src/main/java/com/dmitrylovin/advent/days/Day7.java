package com.dmitrylovin.advent.days;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 extends Day<Function<String, Day7.Hand>> {
    static Map<String, Integer> cardsMap = new HashMap<>();

    public Day7() {
        super(7, 6440, 5905);

        formatters.add(this::mapOne);
        formatters.add(this::mapTwo);

        cardsMap.put("A", 10);
        cardsMap.put("K", 11);
        cardsMap.put("Q", 12);
        cardsMap.put("J", 13);
        cardsMap.put("T", 14);
        cardsMap.put("9", 15);
        cardsMap.put("8", 16);
        cardsMap.put("7", 17);
        cardsMap.put("6", 18);
        cardsMap.put("5", 19);
        cardsMap.put("4", 20);
        cardsMap.put("3", 21);
        cardsMap.put("2", 22);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String[] input) {
        if (part == 1) {
            cardsMap.put("J", 23);
        }

        Hand[] hands = Arrays.stream(input).parallel()
                .map(formatters.get(part)).sorted().toArray(Hand[]::new);

        return IntStream.range(0, hands.length).boxed().mapToInt((i) -> hands[i].bid * (hands.length - i)).sum();
    }

    private Hand mapOne(String input) {
        String[] data = input.split(" ");

        return new Hand(data[0], -getPower(data[0]), Integer.parseInt(data[1]));
    }

    private Hand mapTwo(String input) {
        String[] data = input.split(" ");

        int lowestPower = 0;

        for (String card : cardsMap.keySet()) {
            int power = -getPower(data[0].replace("J", card));

            if (power < lowestPower) {
                lowestPower = power;
                if (power == -25)
                    break;
            }
        }

        return new Hand(data[0], lowestPower, Integer.parseInt(data[1]));
    }

    private int getPower(String hand) {
        return Arrays.stream(hand.split(""))
                .sorted()
                .collect(Collectors.groupingBy((x) -> x))
                .values()
                .stream()
                .mapToInt(List::size)
                .reduce(0, (x1, x2) -> Integer.sum(x1, x2 * x2));
    }

    static class Hand implements Comparable<Hand> {
        int power;
        int bid;
        int[] powers;

        Hand(String hand, int power, int bid) {
            this.power = power;
            this.bid = bid;
            this.powers = Arrays.stream(hand.split("")).mapToInt((s) -> cardsMap.get(s)).toArray();
        }

        @Override
        public int compareTo(Hand hand2) {
            int compare = Integer.compare(this.power, hand2.power);
            if (compare != 0) {
                return compare;
            }

            for (int i = 0; i < 5; i++) {
                compare = Integer.compare(powers[i], hand2.powers[i]);
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }
    }
}
