package com.dmitrylovin.advent.days;

public class Day24 extends Day {
    public Day24() {
        super(24, 2, 47);
    }

    @Override
    public void calculate() {
        calculateWithBenchmark(1);
    }

    @Override
    protected long getResult(int part, String... inputData) {
        if (part == 0)
            return p1(inputData);

        double[][] shards = new double[3][];
        //double[] shard0 = {19,13,30,-2,1,-2};
        //double[] shard1 = {18,19,22,-1,-1,-2};
        //double[] shard2 = {12,31,28,-1,-2,-1};

        int index = 0;
        for (int i : new int[]{0, 1, 3}) {
            String line = inputData[i];
            String[] data = line.split(" *@ *");
            String[] pos = data[0].split(" *, *");
            String[] vel = data[1].split(" *, *");
            shards[index] = new double[]{
                    Double.parseDouble(pos[0]),
                    Double.parseDouble(pos[1]),
                    Double.parseDouble(pos[2]),
                    Double.parseDouble(vel[0]),
                    Double.parseDouble(vel[1]),
                    Double.parseDouble(vel[2])};
            index++;
        }

        System.out.println(parallel(shards[0], shards[1]));
        System.out.println(parallel(shards[1], shards[2]));
        System.out.println(parallel(shards[0], shards[2]));

        int i = 0;
        while (true) {
            i++;
            int min = (i - 1) * 100;
            int max = i * 100;

            for (int t0 = min; t0 < max; t0++) {
                for (int t1 = min; t1 < max; t1++) {
                    for (int t2 = min; t2 < max; t2++) {
                        double[] x = solveSystemOfEquations(shards[0][0], shards[1][0], shards[2][0], shards[0][3], shards[1][3], shards[2][3], t0, t1, t2);
                        double[] y = solveSystemOfEquations(shards[0][1], shards[1][1], shards[2][1], shards[0][4], shards[1][4], shards[2][4], t0, t1, t2);
                        double[] z = solveSystemOfEquations(shards[0][2], shards[1][2], shards[2][2], shards[0][5], shards[1][5], shards[2][5], t0, t1, t2);

                        if (x == null || y == null || z == null)
                            continue;

                        System.out.printf("[%.0f, %.0f, %.0f] [%.0f, %.0f, %.0f]%n", x[0], y[0], z[0], x[1], y[1], z[1]);
                        // Print the results

                        return (long) (x[0] + y[0] + z[0]);
                    }
                }
            }

            System.out.printf("[%d - %d]%n", min, max);
        }
    }

    public int p1(String... inputData) {
        long[][] input = new long[inputData.length][];
        long min = input.length == 5 ? 7 : 200000000000000L;
        long max = input.length == 5 ? 27 : 400000000000000L;
        for (int i = 0; i < inputData.length; i++) {
            String line = inputData[i];
            String[] data = line.split(" *@ *");
            String[] pos = data[0].split(" *, *");
            String[] vel = data[1].split(" *, *");
            input[i] = new long[]{
                    Long.parseLong(pos[0]),
                    Long.parseLong(pos[1]),
                    Long.parseLong(pos[2]),
                    Long.parseLong(vel[0]),
                    Long.parseLong(vel[1]),
                    Long.parseLong(vel[2])};
        }

        int result = 0;

        for (int i = 0; i < inputData.length - 1; i++) {
            long[] p1 = input[i];
            for (int j = i + 1; j < input.length; j++) {
                long[] p2 = input[j];
                //System.out.println(""+i+" - "+j + "["+p1[3]+", "+p1[4]+"] ["+p2[3]+", "+p2[3]+"]");
                double cx = p2[0] - p1[0];
                double cy = p2[1] - p1[1];
                double t = ((-cy * p2[3]) + (cx * p2[4])) / ((-p1[4] * p2[3]) + (p1[3] * p2[4]));
                if (t == Double.POSITIVE_INFINITY)
                    continue;
                double ix = t * p1[3] + p1[0];
                double iy = t * p1[4] + p1[1];
                if (ix < min || ix > max || iy < min || iy > max)
                    continue;

                if (((p1[3] < 0) && ix > p1[0]) || ((p1[3] > 0) && ix < p1[0]) ||
                        ((p2[3] < 0) && ix > p2[0]) || ((p2[3] > 0) && ix < p2[0]) ||
                        ((p1[4] < 0) && iy > p1[1]) || ((p1[4] > 0) && iy < p1[1]) ||
                        ((p2[4] < 0) && iy > p2[1]) || ((p2[4] > 0) && iy < p2[1]))
                    continue;

                result += 1;
                //System.out.printf("a:[%d,%d] b:[%d,%d] c:[%.0f,%.0f] t:%.3f %.3f - %.3f%n", p1[0], p1[1], p2[0], p2[1], cx, cy, t, ix, iy);
                //if (parallelXY(p1[3], p1[4], p2[3], p2[4])) {
                //    System.out.println("P");
                //    continue;
                //}
            }
        }
        return result;
    }

    private static double findT(
            double x, double y, double vx, double vy,
            double x0, double x1, double x2,
            double y0, double y1, double y2,
            double vx0, double vx1, double vx2,
            double vy0, double vy1, double vy2) {

        double A = x0 - x1 + vx1 - vx0;
        //double B = -vx1 +
        // A=x0−x1+Vx1−Vx0
        //B=−Vx1+VxB=−Vx1+Vx
        //C=x2−x1+Vx2−Vx1C=x2−x1+Vx2−Vx1
        //D=−Vx1+VxD=−Vx1+Vx
        //E=x2−x1+Vx2−Vx1E=x2−x1+Vx2−Vx1
        //F=Vx2−VxF=Vx2−Vx
        //G=y0−y1+Vy1−Vy0G=y0−y1+Vy1−Vy0
        //H=−Vy1+VyH=−Vy1+Vy
        //I=y2−y1+Vy2−Vy1I=y2−y1+Vy2−Vy1
        return 0;
    }

    private static double[] solveSystemOfEquations(double x0, double x1, double x2, double vx0, double vx1, double vx2, double t0, double t1, double t2) {
        double[] result = new double[2];

        double rvx0 = (x1 - x0 + (vx1 * t1) - (vx0 * t0)) / (t1 - t0);
        double rvx1 = (x2 - x0 + (vx2 * t2) - (vx0 * t0)) / (t2 - t0);
        double rvx2 = (x2 - x1 + (vx2 * t2) - (vx1 * t1)) / (t2 - t1);

        if (rvx0 != rvx1 || rvx1 != rvx2 || rvx0 != rvx2)
            return null;

        if (rvx0 == Double.POSITIVE_INFINITY || rvx0 == Double.NEGATIVE_INFINITY)
            return null;
        if (rvx1 == Double.POSITIVE_INFINITY || rvx1 == Double.NEGATIVE_INFINITY)
            return null;
        if (rvx2 == Double.POSITIVE_INFINITY || rvx2 == Double.NEGATIVE_INFINITY)
            return null;

        double rx0 = x0 + (vx0 * t0) - (rvx0 * t0);
        double rx1 = x1 + (vx1 * t1) - (rvx1 * t1);
        double rx2 = x2 + (vx2 * t2) - (rvx2 * t2);

        if (rx0 != rx1 || rx1 != rx2 || rx0 != rx2)
            return null;

        if (rx0 == Double.POSITIVE_INFINITY || rx0 == Double.NEGATIVE_INFINITY)
            return null;
        if (rx1 == Double.POSITIVE_INFINITY || rx1 == Double.NEGATIVE_INFINITY)
            return null;
        if (rx2 == Double.POSITIVE_INFINITY || rx2 == Double.NEGATIVE_INFINITY)
            return null;

        result[0] = rx0;
        result[1] = rvx0;

        return result;
    }

    private boolean parallel(double[] shard1, double[] shard2){
        return shard1[3] * shard2[4] == shard1[4] * shard2[3] &&
                shard1[3] * shard2[5] == shard1[5] * shard2[3] &&
                shard1[4] * shard2[5] == shard1[5] * shard2[4];
    }


    //128770086119375
    //218159652637145
    //441968877324087
    //220418718595203
    //131
    //-259
    //102
}
