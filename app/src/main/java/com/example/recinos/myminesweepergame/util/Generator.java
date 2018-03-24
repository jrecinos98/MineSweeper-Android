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
                grid[x][y] = new Cell(context, y * height + x,x,y);
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
        }/*
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y].invalidate();
            }
        }*/
        return grid;
    }
    /*
    public static int[][] generate(int minenumber, final int width, final int height){
        Random r = new Random();
        int[][] grid = new int[width][height];
        //initialize second layer.
        for (int x=0; x<width; x++){
            grid[x]=new int [height];
        }
        //First Place all the mines in the map.
        while (minenumber > 0){
            int x=r.nextInt(width);
            int y=r.nextInt(height);
            //10 is the mine
            if (grid[x][y] != 10){
                grid[x][y] =10;
                mineFinder(grid,x,y);
                minenumber--;
            }
        }
        /*
        for (int x=0; x<width;x++){
            for (int y=0; y<height; y++){
                Log.d("Create","position: ("+x+","+y+") "+grid[x][y] + "");
            }
        }
        return grid;
    }
    private static void mineFinder(int [][] mineArr, final int width, final int height){
        //if mine is not in a corner or edge the default values defined below will be used.
        int xStart=width-1;
        int xEnd=width+1;
        int yStart=height-1;
        int yEnd=height+1;
        //if the mine is on the left edge.
        if (xStart <0){
            xStart=width;
        }
        //if the mine is in the right edge.
        else if(xEnd > mineArr.length-1){
            xEnd=width;
        }
        //if the mine is in the top edge.
        if (yStart < 0){
            yStart=height;
        }
        //if the mine is in the bottom edge.
        else if(yEnd > mineArr[width].length-1){
            yEnd=height;
        }
        for(int k=xStart; k<=xEnd; k++){
            for(int n=yStart; n<=yEnd; n++){
                if(mineArr[k][n] != 10){
                    mineArr[k][n]+=1;
                }
            }
        }

    }
    public static boolean[][] initMarked(int width, int length){
        boolean [][] temp= new boolean[width][length];
        for(int x=0; x<width;x++){
            temp[x]= new boolean[length];
            for(int y=0; y<width;y++){
                temp[x][y]=false;
            }

        }
        return temp;
    }*/
}
