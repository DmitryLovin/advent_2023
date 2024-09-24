package com.dmitrylovin.advent.utils;

public class MathUtils {
    public static double[][] multiplyMatrices(double[][] a, double[][] b){
        double[][] r = new double[a.length][b[0].length];

        for(int i = 0;i<a.length;i++){
            for(int j =0;j<b[0].length;j++){
                double sum = 0;
                for(int k = 0; k < a[i].length;k++){
                    sum += a[i][k] * b[k][j];
                }
                r[i][j] = sum;
            }
        }

        return r;
    }

    public static double[][] multiplyMatrix(double[][] a, double b){
        double[][] r = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                r[i][j] = a[i][j] * b;
            }
        }
        return r;
    }
}
