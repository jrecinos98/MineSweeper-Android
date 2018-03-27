package com.example.recinos.myminesweepergame.util;

import com.example.recinos.myminesweepergame.Views.Grid.Cell;
import com.example.recinos.myminesweepergame.Views.Grid.Grid;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by recinos on 2/7/18.
 *
 */

public abstract class Finder {
    private static Stack<Cell> stack;
    //Assumes that the tapped button had value of 0.
    public static void findEmpty(int row, int col, Grid grid){
        stackPush(grid.getCell(row,col));
        Cell temp;
        while(!stack.empty()) {
            temp=stack.pop();
            //temp.open();
            //grid.addVisibleCell(temp);
            if (temp.getValue() == 0) {
                surroundingCells(grid, temp.getXPos(), temp.getYPos());
            }
        }
    }
    /**
     * Helper function to find the surrounding Cells
     * @param grid The game grid.
     * @param row  row it is found in.
     * @param column column it is found in.
     */
    private static void surroundingCells(Grid grid, final int row, final int column){
        //If cell is not on edge then use passed values.
        int xStart=row-1;
        int xEnd=row+1;
        int yStart=column-1;
        int yEnd=column+1;
        //if the cell is on the left edge.
        if (xStart <0){
            xStart=row;
        }
        //if the cell is in the right edge.
        else if(xEnd > grid.getGridWidth()-1){
            xEnd=row;
        }
        //if the cell is in the top edge.
        if (yStart < 0){
            yStart=column;
        }
        //if the cell is in the bottom edge.
        else if(yEnd > grid.getGridHeight()-1){
            yEnd=column;
        }
        for(int k=xStart; k<=xEnd; k++){
            for(int n=yStart; n<=yEnd; n++){
                if(!grid.getCell(k,n).isMarked() && grid.getCell(k,n).getValue()==0) {
                    grid.getCell(k,n).setOpened();
                    stack.push(grid.getCell(k, n));
                }
                else if (!grid.getCell(k,n).isMarked()){
                    grid.getCell(k,n).setOpened();
                   // grid.addVisibleCell(grid.getCell(k,n));
                }
            }
        }

    }
    public static ArrayList<Integer> findNeighborPositions(int row, int column, Cell[][] grid){
        //If cell is not on edge then use passed values.
        int xStart=row-1;
        int xEnd=row+1;
        int yStart=column-1;
        int yEnd=column+1;
        ArrayList<Integer> neighborPositions = new ArrayList<>();
        //if the cell is on the left edge.
        if (xStart <0){
            xStart=row;
        }
        //if the cell is in the right edge.
        else if(xEnd > grid.length-1){
            xEnd=row;
        }
        //if the cell is in the top edge.
        if (yStart < 0){
            yStart=column;
        }
        //if the cell is in the bottom edge.
        else if(yEnd > grid[0].length-1){
            yEnd=column;
        }
        for(int k=xStart; k<=xEnd; k++) {
            for (int n = yStart; n <= yEnd; n++) {
                neighborPositions.add(k+(n * grid.length));
            }
        }
        return neighborPositions;

    }

    /**
     * Helper to push a GridComponent into the Stack
     * @param cell
     */
    private static void stackPush(Cell cell){
        if(stack == null) {
            stack = new Stack<>();
        }
        stack.push(cell);
    }
    public static void mineFinder(Cell[][] grid, final int width, final int height){
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
        else if(xEnd > grid.length-1){
            xEnd=width;
        }
        //if the mine is in the top edge.
        if (yStart < 0){
            yStart=height;
        }
        //if the mine is in the bottom edge.
        else if(yEnd > grid[width].length-1){
            yEnd=height;
        }
        for(int k=xStart; k<=xEnd; k++){
            for(int n=yStart; n<=yEnd; n++){
                if(!grid[k][n].isMine()){
                    grid[k][n].increment();
                }
            }
        }

    }
}