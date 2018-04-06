package com.game.recinos.myminesweepergame.util;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.Views.Grid.GridComponent;
import com.game.recinos.myminesweepergame.Views.Grid.GridComponent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by recinos on 2/5/18.
 */
public class Util {
    public static GridComponent[][] generateInitial(int width, int height){
        GridComponent[][] grid = new GridComponent[width][height];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                grid[row][col] = new GridComponent(row,col,row+(col * width)); //position= x+(y * width);
            }
        }
        return grid;
    }
    public static void generateGrid(GridComponent[][] grid, int minenumber, int position) {
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
    public static int convertDpToPixel(float dp){
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            float px = dp * (metrics.densityDpi / 160f);
            return Math.round(px);
        }
}
