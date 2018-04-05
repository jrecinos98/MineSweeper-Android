package com.game.recinos.myminesweepergame.util;

import com.game.recinos.myminesweepergame.GameActivity;
import com.game.recinos.myminesweepergame.Views.Grid.Cell;
import com.game.recinos.myminesweepergame.Views.Grid.Grid;

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
                surroundingCells(grid, temp.getRow(), temp.getCol());
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
        int rowStart=row-1;
        int rowEnd=row+1;
        int colStart=column-1;
        int colEnd=column+1;
        //if the cell is on the left edge.
        if (rowStart <0){
            rowStart=row;
        }
        //if the cell is in the right edge.
        else if(rowEnd > grid.getGridWidth()-1){
            rowEnd=row;
        }
        //if the cell is in the top edge.
        if (colStart < 0){
            colStart=column;
        }
        //if the cell is in the bottom edge.
        else if(colEnd > grid.getGridHeight()-1){
            colEnd=column;
        }
        for(int k=rowStart; k<=rowEnd; k++){
            for(int n=colStart; n<=colEnd; n++){
                if(grid.getCell(k,n).isOpenable() && grid.getCell(k,n).getValue()==0) {
                    GameActivity.incrementCorrectMoves();
                    grid.getCell(k,n).setOpened();
                    stack.push(grid.getCell(k, n));
                }
                else if (grid.getCell(k,n).isOpenable()){
                    GameActivity.incrementCorrectMoves();
                    grid.getCell(k,n).setOpened();
                   // grid.addVisibleCell(grid.getCell(k,n));
                }
            }
        }

    }
    public static ArrayList<Integer> findNeighborPositions(int row, int column, Cell[][] grid){
        //If cell is not on edge then use passed values.
        int rowStart=row-1;
        int rowEnd=row+1;
        int colStart=column-1;
        int colEnd=column+1;
        ArrayList<Integer> neighborPositions = new ArrayList<>();
        //if the cell is on the left edge.
        if (rowStart <0){
            rowStart=row;
        }
        //if the cell is in the right edge.
        else if(rowEnd > grid.length-1){
            rowEnd=row;
        }
        //if the cell is in the top edge.
        if (colStart < 0){
            colStart=column;
        }
        //if the cell is in the bottom edge.
        else if(colEnd > grid[0].length-1){
            colEnd=column;
        }
        for(int k=rowStart; k<=rowEnd; k++) {
            for (int n = colStart; n <= colEnd; n++) {
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
    public static void mineFinder(Cell[][] grid, final int row, final int column){
        //if mine is not in a corner or edge the default values defined below will be used.
        //If cell is not on edge then use passed values.
        int rowStart=row-1;
        int rowEnd=row+1;
        int colStart=column-1;
        int colEnd=column+1;
        //if the cell is on the left edge.
        if (rowStart <0){
            rowStart=row;
        }
        //if the cell is in the right edge.
        else if(rowEnd > grid.length-1){
            rowEnd=row;
        }
        //if the cell is in the top edge.
        if (colStart < 0){
            colStart=column;
        }
        //if the cell is in the bottom edge.
        else if(colEnd > grid[0].length-1){
            colEnd=column;
        }
        for(int k=rowStart; k<=rowEnd; k++){
            for(int n=colStart; n<=colEnd; n++){
                if(!grid[k][n].isMine()){
                    grid[k][n].increment();
                }
            }
        }

    }
}