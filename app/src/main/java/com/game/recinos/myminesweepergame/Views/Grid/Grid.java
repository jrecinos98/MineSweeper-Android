package com.game.recinos.myminesweepergame.Views.Grid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.GameActivity;
import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.util.Util;
import com.game.recinos.myminesweepergame.util.Finder;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by recinos on 2/7/18.
 */


public class Grid implements Serializable, Parcelable{
    private GridComponent[][] gameGrid;
    private Constants.GAME_DIFFICULTY GAME_DIFFICULTY;
    private int flagNum;
    private transient Context gameContext;
    private transient MediaPlayer ring;
    @SuppressLint("ClickableViewAccessibility")
    public Grid(Constants.GAME_DIFFICULTY diff, Context context){
        GAME_DIFFICULTY=diff;
        flagNum=getMineNum();
        gameContext=context;
        gameGrid = Util.generateInitial(getGridWidth(),getGridHeight());
    }

    /**
     * gets a cell at the specified row and column
     * @param row row number
     * @param col column number
     * @return the cell at that position
     */
    public GridComponent getCell(int row, int col){
        return gameGrid[row][col];
    }

    /**
     * returns the cell at the given position
     * @param position the position in  the grid.
     * @return Reference to the cell at that position.
     */
    public GridComponent getCell(int position){
        int row= position%getNumColumns();
        int col= position/getNumColumns();
        return gameGrid[row][col];
    }
    public int getNumColumns(){return getGridWidth();}
    public int getGridWidth(){return GAME_DIFFICULTY.getWidth();}
    public int getGridHeight(){return GAME_DIFFICULTY.getHeight();}
    public int getMineNum(){return GAME_DIFFICULTY.getMineNum();}
    public GridComponent[][] getCells(){return gameGrid;}
    public int getFlagNum(){return flagNum;}
    public void incremementFlagNum(){flagNum++;}
    public void decrementFlagNum(){flagNum--;}

    /**
     * @return If the user clicked on the action button it returns true.
     */
    public boolean isActionButtonActive(){
        return !(GameActivity.getActionButtonTag()==Constants.TOOLBAR_MINE);
    }

    /**
     * It makes the given cell either a flag, a question mark, or normal.
     * @param temp
     */
    public void handleActionButton(GridComponent temp){
        GameActivity.clickVibrate(1);
        if(GameActivity.getActionButtonTag()== Constants.TOOLBAR_FLAG){
            if(temp.isFlagged()){
                //temp.unFlag();
                flagNum++;
                temp.setNormal();
            }
            else {
                if(temp.isOpenable()){
                    flagNum--;
                }
                temp.setFlagged();
            }
        }
        else if(GameActivity.getActionButtonTag()==Constants.TOOLBAR_QUESTION){
            if(temp.isQuestion()){
               // temp.unQuestion();
                flagNum++;
                temp.setNormal();
            }
            else {
                if(temp.isOpenable()){
                    flagNum--;
                }
                temp.setQuestion();
            }
        }
    }

    /**
     * Called when the clicked cell has a value > 0 and it should reveal a number.
     * @param temp the tapped cell
     */
    public void handleValueCell(GridComponent temp){
        GameActivity.incrementCorrectMoves();
        temp.setOpened();
        GameActivity.clickVibrate(5);


    }



    /**
     * Before the first click the grid contains no values yet. After the first click the grid is generated and the first clicked cell is opened.
     * @param position The position of the first clicked cell.
     */
    public void handleFirstClick(int position){
        if(GameActivity.getActionButtonTag()==Constants.TOOLBAR_MINE){
            Util.generateGrid(gameGrid,getMineNum(),position);
            GameActivity.GAME_STATE=Constants.GAME_STATE.PLAYING;
        }
    }

    /**
     * Used to find the cells that should open up if an empty cell is clicked.
     * @param position position of clicked empty cell.
     */
    public void handleCellEmpty(int position){
        GameActivity.clickVibrate(5);
        int row= position%getGridWidth();
        int col= position/getGridWidth();
        Finder.findEmpty(row,col,this);
        //gridAdapter.notifyDataSetChanged(); //Notifies Adapter that the cells changed. Has a bug on first cell.

    }
    /**
     * When a mine is clicked.
     * @param temp reference to the clicked cell containing the mine.
     */
    public void handleClickedMine(GridComponent temp){
        GameActivity.clickVibrate(600);
        temp.showAsClickedMine();
        gameOver();
    }
    /**
     * Used to reveal all the mines in the grid. Called when the game is over.
     */
    private void revealMines(){
        for(int row=0; row<getGridWidth(); row++){
            for (int col=0; col<getGridHeight(); col++){
                if(gameGrid[row][col].isFlagged() || gameGrid[row][col].isQuestion()){
                    gameGrid[row][col].setNormal();
                }
                if(gameGrid[row][col].isMine()){
                    if(!gameGrid[row][col].isHint() && !gameGrid[row][col].isClickedMine()) {
                        gameGrid[row][col].showAsMine();
                    }
                }
            }
        }
    }
    /**
     * Called when the player clicks on a mine.
     */
    private void gameOver(){
        GameActivity.resetCorrectMoves();
        GameActivity.GAME_STATE= Constants.GAME_STATE.LOST;
        revealMines();
    }
    /**
     * called when a user wins the game.
     */
    public void gameWon(){
        GameActivity.GAME_STATE= Constants.GAME_STATE.WON;
        GameActivity.showWonDialog();
    }

    public Constants.GAME_STATE getGAME_STATE(){return GameActivity.GAME_STATE;}
    public Constants.GAME_DIFFICULTY getDifficulty(){return GAME_DIFFICULTY;}



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Grid createFromParcel(Parcel in) {
            return Grid(in);
        }
        public Grid[] newArray(int size) {
            return new Grid[size];
        }
}