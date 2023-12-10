package com.dmitrylovin.advent.days;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Day10 extends Day {
    static final Map<Character, Point[]> DIR_MAP = new HashMap<>() {{
        put('|', new Point[]{new Point(0, 1), new Point(0, -1)});
        put('-', new Point[]{new Point(1, 0), new Point(-1, 0)});
        put('L', new Point[]{new Point(0, -1), new Point(1, 0)});
        put('J', new Point[]{new Point(-1, 0), new Point(0, -1)});
        put('7', new Point[]{new Point(-1, 0), new Point(0, 1)});
        put('F', new Point[]{new Point(1, 0), new Point(0, 1)});
        put('.', new Point[]{new Point(0, 0), new Point(0, 0)});
    }};

    public Day10() {
        super(10, 8, 10);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        Field field = new Field(inputData[0].length(), inputData.length);

        IntStream.range(0, inputData.length).boxed().parallel().forEach((y) -> {
            for (int x = 0; x < inputData[y].length(); x++) {
                field.addPipe(x, y, inputData[y].charAt(x));
            }
        });

        Set<Point> connected = new HashSet<>();
        List<Point> polyPoints = new ArrayList<>();
        connected.add(field.start);
        polyPoints.add(field.start);
        Pipe pipe = field.findConnectedToStart();
        connected.add(pipe.pos);
        polyPoints.add(pipe.pos);

        while (true) {
            boolean found = false;
            for (Point point : pipe.connected) {
                if (connected.contains(point)) {
                    continue;
                }
                found = true;
                connected.add(point);
                pipe = field.pipes[point.y][point.x];
                if (part == 1 && !(pipe.key == '|' || pipe.key == '-'))
                    polyPoints.add(point);
            }
            if (!found) break;
        }

        if (part == 0) {
            return connected.size() / 2;
        }

        BufferedImage image = new BufferedImage(
                field.pipes[0].length,
                field.pipes.length,
                BufferedImage.TYPE_INT_RGB
        );

        int[] xPoints = new int[polyPoints.size()];
        int[] yPoints = new int[xPoints.length];
        int nPoints = xPoints.length;

        int minX = field.pipes[0].length;
        int maxX = 0;

        int minY = field.pipes.length;
        int maxY = 0;

        for (int z = 0; z < nPoints; z++) {
            xPoints[z] = polyPoints.get(z).x;
            yPoints[z] = polyPoints.get(z).y;

            if (xPoints[z] < minX)
                minX = xPoints[z];

            if (xPoints[z] > maxX)
                maxX = xPoints[z];

            if (yPoints[z] < minY)
                minY = yPoints[z];

            if (yPoints[z] > maxY)
                maxY = yPoints[z];
        }

        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(xPoints, yPoints, nPoints);

        /*try {
            ImageIO.write(image,"PNG",new File(String.format("./out_%s.png", System.nanoTime())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        int finalMinX = minX;
        int finalMaxX = maxX;
        return IntStream.range(minY + 1, maxY)
                .parallel()
                .map(
                        (y) -> IntStream
                                .range(finalMinX + 1, finalMaxX)
                                .map((x) -> !connected.contains(new Point(x, y)) && image.getRGB(x, y) == -1 ? 1 : 0)
                                .sum()).sum();
    }

    static class Field {
        Point start;
        Pipe[][] pipes;

        Field(int width, int height) {
            this.pipes = new Pipe[height][width];
        }

        void addPipe(int x, int y, char key) {
            pipes[y][x] = new Pipe(x, y, key);
            if (key == 'S')
                start = new Point(x, y);
        }

        Pipe findConnectedToStart() {
            for (int x = start.x - 1; x <= start.x + 1; x++) {
                for (int y = start.y - 1; y <= start.y + 1; y++) {
                    if (x < 0 || y < 0)
                        continue;
                    if (x == start.x && y == start.y)
                        continue;
                    for (Point point : pipes[y][x].connected) {
                        if (point.equals(start))
                            return pipes[y][x];
                    }
                }
            }
            return null;
        }
    }

    static class Pipe {
        Point pos;
        Point[] connected = new Point[2];
        char key;

        Pipe(int x, int y, char dir) {
            key = dir;
            pos = new Point(x, y);
            Point[] dirs;
            if ((dirs = DIR_MAP.get(dir)) != null) {
                for (int i = 0; i < 2; i++) {
                    connected[i] = pos.add(dirs[i]);
                }
            }
        }
    }

    record Point(int x, int y) {
        @Override
        public String toString() {
            return String.format("(%d : %d)", x, y);
        }

        @Override
        public int hashCode() {
            return this.x * 1000 + y;
        }

        public boolean equals(Point point) {
            return x == point.x && y == point.y();
        }

        public Point add(Point point) {
            return new Point(x + point.x, y + point.y);
        }
    }
}
