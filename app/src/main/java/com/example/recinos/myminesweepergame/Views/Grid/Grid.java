package com.example.recinos.myminesweepergame.Views.Grid;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.GameActivity;
import com.example.recinos.myminesweepergame.R;
import com.example.recinos.myminesweepergame.util.Generator;
import com.example.recinos.myminesweepergame.util.Finder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by recinos on 2/7/18.
 */


public class Grid extends GridView implements Serializable{
    private Grid instance;
    private Context gameContext;
    private Cell[][] gameGrid;
    private Constants.GAME_STATE GAME_STATE;
    private Constants.GAME_DIFFICULTY GAME_DIFFICULTY;
    private int mineNum;
    private boolean firstClick=false;
    private GridAdapter myAdapter;
    @SuppressLint("ClickableViewAccessibility")
    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameContext=context;
        GAME_STATE=GameActivity.GAME_STATE;
        GAME_DIFFICULTY=GameActivity.difficulty;
        mineNum=getMineNum();
        this.setNumColumns(getGridWidth());
        gameGrid = Generator.generateInitial(gameContext,getGridWidth(),getGridHeight());
        myAdapter= new GridAdapter();
        this.setAdapter(myAdapter);
        instance=this;
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Button hintButton=findViewById(R.id.myActionButton);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!firstClick && GameActivity.getActionButtonTag()==Constants.TOOLBAR_MINE){
                    Generator.generateGrid(gameGrid,getMineNum(),position);
                    firstClick=true;
                }
                Cell temp= getCell(position);
                if(GAME_STATE == Constants.GAME_STATE.PLAYING) {
                    if (!temp.isOpened()) {
                        if(GameActivity.getActionButtonTag()== Constants.TOOLBAR_FLAG){
                            if(temp.isFlagged() && !temp.isQuestion()){
                                temp.setToNormal();
                            }
                            else
                                temp.setFlagged();
                        }
                        else if(GameActivity.getActionButtonTag()==Constants.TOOLBAR_QUESTION){
                            if(temp.isQuestion()){
                                temp.unFlagClick();
                                temp.setToNormal();
                            }
                            else {
                                temp.setQuestion();
                            }
                        }
                        else if (temp.isMine() && !temp.isFlagged()){
                            temp.setAsClickedMine();
                            gameOver();
                        }
                        else if (temp.getValue() == 0 && !temp.isFlagged() && !temp.isOpened()) {
                            handleCellEmpty(position);
                        }
                        else if(!temp.isFlagged() && !temp.isMine()) {
                            temp.setOpened();
                        }
                        GameActivity.clickVibrate(5);
                    }
                    if(Cell.cellsOpened  == (getGridHeight()*getGridWidth())- mineNum){
                        GAME_STATE= Constants.GAME_STATE.WON;
                        gameWon();
                    }
                }
            }
        });
        //Glitches on long click
        this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp= getCell(position);
                if(GAME_STATE == Constants.GAME_STATE.PLAYING) {
                    if (temp.isOpened()){
                        return false;
                    }
                    else
                        temp.setNextFlagImage();
                }
                if(Cell.cellsOpened== (getGridHeight()*getGridWidth())-mineNum){
                    GAME_STATE= Constants.GAME_STATE.WON;
                    gameWon();
                }
                return true;
            }
        });
        this.setVerticalScrollBarEnabled(false);
        this.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button smileyButton= findViewById(R.id.smileyButton);
                float currentXPosition = event.getX();
                float currentYPosition = event.getY();
                int position = instance.pointToPosition((int) currentXPosition, (int) currentYPosition);
                if(position<0){
                    position=0;
                }
                Cell temp= getCell(position);
                ArrayList<Integer> neighbors= Finder.findNeighborPositions(temp.getXPos(),temp.getYPos(),gameGrid);
                //Log.d("Event",event.toString());
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                if (GAME_STATE==Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_UP) {
                    //smileyButton.setBackgroundResource(Constants.SMILEY_NORMAL);
                    GameActivity.updateSmileyButton(Constants.SMILEY_NORMAL);
                    for (int neighborPos : neighbors) {
                        int x = neighborPos % getGridWidth();
                        int y = neighborPos / getGridWidth();
                        if (gameGrid[x][y].getAnimate()) {
                            //screws with mineCounter
                            gameGrid[x][y].setToNormal();
                        }
                    }
                }
                else if(GAME_STATE==Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(temp.isOpened() && temp.getValue()!=0){
                            for(int neighborPos: neighbors){
                                int x = neighborPos%getGridWidth();
                                int y= neighborPos/getGridWidth();
                                if(!gameGrid[x][y].isOpened() && !gameGrid[x][y].getAnimate()){
                                    gameGrid[x][y].setAnimate();
                                }
                            }
                        }
                        if(!temp.isOpened() || (temp.isOpened()&&  temp.getValue()!=0)) {
                            //smileyButton.setBackgroundResource(Constants.SMILEY_SCARED);
                            GameActivity.updateSmileyButton(Constants.SMILEY_SCARED);
                        }
                    }
                return false; }
        });
    }
    private void handleCellEmpty(int position){
        int x= position%getGridWidth();
        int y= position/getGridWidth();
        Finder.findEmpty(x,y,this);
        //gridAdapter.notifyDataSetChanged(); //Notifies Adapter that the cells changed. Has a bug on first cell.

    }
    /*
    public void shuffleMine(int x, int y, Cell[][] grid){
        // This decrements the surrounding cells.
        grid.setSymbol(x,y,'0');
        cellSymbolUpdate(x,y);
        //update the current cell.
        grid.setSymbol(x,y,'.');
        cellSymbolUpdate(x,y);
        Random random = new Random();
        int row=x;
        int col=y;
        int bomb=1;
        //find an empty spot that is not
        while(bomb != 0){
            row = random.nextInt(grid.getSize());
            col = random.nextInt(grid.getSize());
            if((row!=x && col !=y) && !grid.isMine(row,col)) {
                grid.makeMine(row,col);
                //Would only be used for testing.
                //newMineNeighborsBeforeShuffle=getSurrounding(row,col,this);
                cellSymbolUpdate(row, col);
                bomb--;
            }
        }
    }
    private void cellSymbolUpdate(final int row, final int column){
        //If bomb is not on edge then use passed values.
        int xStart=row-1;
        int xEnd=row+1;
        int yStart=column-1;
        int yEnd=column+1;
        //if the bomb is on the left edge.
        if (xStart <0){
            xStart=row;
        }
        //if the bomb is in the right edge.
        else if(xEnd > gameGrid.length-1){
            xEnd=row;
        }
        //if the bomb is in the top edge.
        if (yStart < 0){
            yStart=column;
        }
        //if the bomb is in the bottom edge.
        else if(yEnd > gameGrid[row].length-1){
            yEnd=column;
        }
        char counter='0';
        for(int k=xStart; k<=xEnd; k++){
            for(int n=yStart; n<=yEnd; n++){
                //Got Lazy. Best approach would be to extract the counting into another function.
                if(gameGrid[k][n].isMine() && gameGrid[row][column].getSymbol() == '.'){
                    if (gameGrid[k][n].isMine()){
                        counter++;
                    }
                }
                if(!gameGrid[k][n].isMine()) {
                    if(gameGrid[row][column].isMine()){
                        gameGrid[k][n].iterate();
                    }
                    else if (gameGrid[row][column].getSymbol() == '0'){
                        gameGrid[k][n].decrement();
                    }
                }
            }
        }
        if(gameGrid[row][column].getSymbol() == '.'){
            gameGrid[row][column].setSymbol(counter);
        }

    }*/

    private void revealMines(){
        for(int x=0; x<getGridWidth(); x++){
            for (int y=0; y<getGridHeight(); y++){
                if(gameGrid[x][y].isFlagged()){
                    gameGrid[x][y].unFlag();
                }
                if(gameGrid[x][y].isMine()){
                    gameGrid[x][y].setOpened();
                }
            }
        }
    }
    private void gameOver(){
        GAME_STATE= Constants.GAME_STATE.LOST;
        GameActivity.updateSmileyButton(Constants.SMILEY_DEAD);
        revealMines();
        //gameOverPrompt();
    }
   /* private void gameOverPrompt(){
        GameActivity.showLostDialog();
    }*/
    private void gameWon(){
        GameActivity.showWonDialog();
    }
    public Constants.GAME_STATE getGAME_STATE(){return GAME_STATE;}
    public Constants.GAME_DIFFICULTY getGAME_DIFFICULTY(){return GAME_DIFFICULTY;}
    public Cell getCell(int x, int y){
        return gameGrid[x][y];
    }
    public GridAdapter getAdapter(){return myAdapter;}
    public Cell getCell(int position){
        int x= position%getNumColumns();
        int y= position/getNumColumns();
        return gameGrid[x][y];
    }
    public int getGridWidth(){return GAME_DIFFICULTY.getWidth();}
    public int getGridHeight(){return GAME_DIFFICULTY.getHeight();}
    public int getMineNum(){return GAME_DIFFICULTY.getMineNum();}


    private class GridAdapter extends BaseAdapter {
        public GridAdapter() { }
        public int getCount() {
            return getGridWidth() * getGridHeight();
        }
        public Object getItem(int position) {
            return getCell(position);
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getCell(position);
            }
            return convertView;
        }
    }

}