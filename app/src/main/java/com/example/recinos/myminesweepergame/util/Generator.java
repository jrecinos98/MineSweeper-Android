package com.example.recinos.myminesweepergame.util;
import java.util.Random;

/**
 * Created by recinos on 2/5/18.
 */
public class Generator {
    public static int[][] generate(int bombnumber, final int width, final int height){
        Random r = new Random();
        int[][] grid = new int[width][height];
        //initialize second layer.
        for (int x=0; x<width; x++){
            grid[x]=new int [height];
        }
        //First Place all the bombs in the map.
        while (bombnumber > 0){
            int x=r.nextInt(width);
            int y=r.nextInt(height);
            //10 is the bomb
            if (grid[x][y] != 10){
                grid[x][y] =10;
                bombFinder(grid,x,y);
                bombnumber--;
            }
        }
        /*
        for (int x=0; x<width;x++){
            for (int y=0; y<height; y++){
                Log.d("Create","position: ("+x+","+y+") "+grid[x][y] + "");
            }
        }*/
        return grid;
    }
    private static void bombFinder(int [][] bombArr, final int width, final int height){
        //if bomb is not in a corner or edge the default values defined below will be used.
        int xStart=width-1;
        int xEnd=width+1;
        int yStart=height-1;
        int yEnd=height+1;
        //if the bomb is on the left edge.
        if (xStart <0){
            xStart=width;
        }
        //if the bomb is in the right edge.
        else if(xEnd > bombArr.length-1){
            xEnd=width;
        }
        //if the bomb is in the top edge.
        if (yStart < 0){
            yStart=height;
        }
        //if the bomb is in the bottom edge.
        else if(yEnd > bombArr[width].length-1){
            yEnd=height;
        }
        for(int k=xStart; k<=xEnd; k++){
            for(int n=yStart; n<=yEnd; n++){
                if(bombArr[k][n] != 10){
                    bombArr[k][n]+=1;
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
    }
}
