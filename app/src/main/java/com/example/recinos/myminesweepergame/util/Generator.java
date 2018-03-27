package com.example.recinos.myminesweepergame.util;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.example.recinos.myminesweepergame.R;
import com.example.recinos.myminesweepergame.Views.Grid.Cell;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by recinos on 2/5/18.
 */
public class Generator {
    public static Cell[][] generateInitial(Context context,int width,int height){
        Cell[][] grid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(context,x,y,x+(y * width)); //position= x+(y * width);
            }
        }
        return grid;
    }
    public static void generateGrid(Cell[][] grid, int minenumber, int position) {
        int x = position%grid.length;
        int y= position/grid.length;
        Random r = new Random();
        ArrayList<Integer> neighbors=Finder.findNeighborPositions(x,y,grid);
        //First Place all the mines in the map.
        while (minenumber> 0) {
            int xCor = r.nextInt(grid.length);
            int yCor = r.nextInt(grid[0].length);
            //10 is the mine
            if (!grid[xCor][yCor].isMine() && !neighbors.contains(grid[xCor][yCor].getPosition())) {
                grid[xCor][yCor].setMine();
                Finder.mineFinder(grid, xCor, yCor);
                minenumber--;
            }
        }

    }
}
