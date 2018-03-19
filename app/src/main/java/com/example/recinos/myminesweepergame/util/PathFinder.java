package com.example.recinos.myminesweepergame.util;

/**
 * Created by recinos on 2/7/18.
 *
 */

public class PathFinder {
    //Assumes that the tapped button had value of 0.
    public static void findEmpty(int k, int n, int[][] bombs, boolean[][] marked) {
        marked[k][n] = true;
        if (k - 1 >= 0) {
            if (marked[k - 1][n] == false) {
                if (bombs[k - 1][n] == 0)
                    findEmpty(k - 1, n, bombs, marked);
                else
                    marked[k - 1][n] = true;
            }
        }
        if (n - 1 >= 0) {
            if (marked[k][n - 1] == false) {
                if (bombs[k][n - 1] == 0)
                    findEmpty(k, n - 1, bombs, marked);
                else
                    marked[k][n - 1] = true;

            }
        }
        if (n + 1 <= bombs[k].length - 1) {
            if (marked[k][n + 1] == false) {
                if (bombs[k][n + 1] == 0)
                    findEmpty(k, n + 1, bombs, marked);
                else
                    marked[k][n + 1] = true;
            }
        }
        if (k + 1 <= bombs.length - 1) {
            if (marked[k + 1][n] == false) {
                if (bombs[k + 1][n] == 0) {
                    findEmpty(k + 1, n, bombs, marked);
                } else
                    marked[k + 1][n] = true;
            }
        }
    }
}