package com.example.recinos.myminesweepergame.util;
import android.content.Context;

import com.example.recinos.myminesweepergame.Views.Grid.Cell;

import java.util.Random;

/**
 * Created by recinos on 2/5/18.
 */
public class Generator {
    public static Cell[][] generateGrid(Context context, int minenumber, final int width, final int height) {
        Random r = new Random();
        Cell[][] grid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(context, x+(y * width),x,y);
            }
        }
        //First Place all the mines in the map.
        while (minenumber> 0) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            //10 is the mine
            if (!grid[x][y].isMine()) {
                grid[x][y].setMine();
                PathFinder.mineFinder(grid, x, y);
                minenumber--;
            }
        }
        return grid;
    }
}
