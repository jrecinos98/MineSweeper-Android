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
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                grid[row][col] = new Cell(context,row,col,row+(col * width)); //position= x+(y * width);
            }
        }
        return grid;
    }
    public static void generateGrid(Cell[][] grid, int minenumber, int position) {
        int row = position%grid.length;
        int col= position/grid.length;
        Random r = new Random();
        ArrayList<Integer> neighbors=Finder.findNeighborPositions(row,col,grid);
        //First Place all the mines in the map.
        while (minenumber> 0) {
            int rowNum = r.nextInt(grid.length);
            int colNum = r.nextInt(grid[0].length);
            //10 is the mine
            if (!grid[rowNum][colNum].isMine() && !neighbors.contains(grid[rowNum][colNum].getPosition())) {
                grid[rowNum][colNum].makeMine();
                Finder.mineFinder(grid, rowNum, colNum);
                minenumber--;
            }
        }

    }
}
