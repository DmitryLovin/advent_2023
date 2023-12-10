package com.dmitrylovin.advent.days;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.IntStream;

public class Day10 extends Day{
    static final Map<String, Point[]> DIR_MAP = new HashMap<>(){{
        put("|", new Point[]{new Point(0,1),new Point(0,-1)});
        put("-", new Point[]{new Point(1,0),new Point(-1,0)});
        put("L", new Point[]{new Point(0,-1),new Point(1,0)});
        put("J", new Point[]{new Point(-1,0),new Point(0,-1)});
        put("7", new Point[]{new Point(-1,0),new Point(0,1)});
        put("F", new Point[]{new Point(1,0),new Point(0,1)});
        put(".", new Point[]{new Point(0,0),new Point(0,0)});
    }};

    public Day10() {
        super(10, 8, 10);
    }

    @Override
    public void calculate(){
        calculateWithBenchmark(5000);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        Field field = new Field(inputData[0].length(), inputData.length);

        IntStream.range(0, inputData.length).boxed().parallel().forEach((y)->{
            String[] pipes = inputData[y].split("");
            for(int x = 0; x < pipes.length; x++){
                field.addPipe(x, y, pipes[x]);
            }
        });

        Map<Point, Integer> connected = new HashMap<>();
        connected.put(field.start, 0);

        Pipe pipe = field.findConnectedToStart();
        connected.put(pipe.pos, 1);

        int i = 2;
        while(true){
            boolean found = false;
            for(Point point: pipe.connected){
                if(connected.containsKey(point)){
                    continue;}
                found = true;
                connected.put(point, i++);
                pipe = field.pipes[point.y][point.x];
            }
            if(!found) break;
        }
        if(part == 0){
            return connected.size() / 2;
        }

        BufferedImage image = new BufferedImage(
                field.pipes[0].length,
                field.pipes.length,
                BufferedImage.TYPE_INT_RGB
        );

        int[] xPoints = new int[connected.size()];
        int[] yPoints = new int[connected.size()];
        int nPoints = connected.size();

        for(Map.Entry<Point, Integer> entry : connected.entrySet()){
            xPoints[entry.getValue()] = entry.getKey().x;
            yPoints[entry.getValue()] = entry.getKey().y;
        }


        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.fillPolygon(xPoints, yPoints, nPoints);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, nPoints);

        int count = 0;

        for(int y = 0; y<field.pipes.length; y++){
            for(int x = 0; x < field.pipes[y].length; x++){
                int rgb = image.getRGB(x,y);
                if(rgb==Color.GREEN.getRGB())
                    count++;
            }
        }
        return count;
    }

    static class Field {
        Point start;
        Pipe[][] pipes;

        Field(int width, int height){
            this.pipes = new Pipe[height][width];
        }

        void addPipe(int x, int y, String key){
            pipes[y][x] = new Pipe(x, y, key);
            if(key.equals("S"))
                start = new Point(x, y);
        }

        Pipe findConnectedToStart(){
            for(int x = start.x - 1; x <= start.x + 1; x++){
                for(int y = start.y - 1; y <= start.y + 1; y++){
                    if(x < 0 || y < 0)
                        continue;
                    if(x == start.x && y == start.y)
                        continue;
                    for(Point point : pipes[y][x].connected){
                        if(point.equals(start))
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
        String key;

        Pipe(int x, int y, String dir){
            key = dir;
            pos = new Point(x, y);
            Point[] dirs;
            if((dirs = DIR_MAP.get(dir)) != null){
                for(int i=0;i<2;i++){
                    connected[i] = pos.add(dirs[i]);
                }
            }
        }
    }

    record Point(int x, int y){
        @Override
        public String toString(){
            return String.format("(%d : %d)", x, y);
        }

        @Override
        public int hashCode(){
            return this.x * 1000 + y;
        }

        public boolean equals(Point point){
            return x == point.x && y == point.y();
        }

        public Point add(Point point){
            return new Point(x + point.x, y + point.y);
        }
    }
}
