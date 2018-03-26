package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.GameActivity;
import com.example.recinos.myminesweepergame.util.Generator;
import com.example.recinos.myminesweepergame.util.PathFinder;

/**
 * Created by recinos on 2/7/18.
 */


public class Grid extends GridView{
    private Context gameContext;
    private Cell[][] gameGrid;
    private Constants.GameState gameState;
    private Constants.GameDifficulty gameDifficulty;
    private int mineNum=0;
    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameContext=context;
        gameState=GameActivity.gameState;
        gameDifficulty=GameActivity.difficulty;
        mineNum=getMineNum();
        this.setNumColumns(getGridWidth());
        gameGrid = Generator.generateGrid(context,getMineNum(), getGridWidth(), getGridHeight());
        this.setAdapter(new GridAdapter());
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp= getCell(position);
                if(gameState == Constants.GameState.PLAYING) {
                    if (!temp.isOpened()) {
                        if (temp.isMine() && !temp.isFlagged()){
                            gameOver();
                        }
                        else if (temp.getValue() == 0 && !temp.isFlagged()) {
                            handleCellEmpty(position);
                        }
                        else if(!temp.isFlagged() && !temp.isMine()) {
                            temp.setOpened();
                        }
                    }
                    if(Cell.cellsOpened  == (getGridHeight()*getGridWidth())- mineNum){
                        gameState= Constants.GameState.WON;
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
                if(gameState == Constants.GameState.PLAYING) {
                    if (temp.isFlagged()) {
                        temp.unFlag();
                    } else {
                        temp.setFlagged();
                    }
                }
                if(Cell.cellsOpened== (getGridHeight()*getGridWidth())-mineNum){
                    gameState= Constants.GameState.WON;
                    gameWon();
                }
                return true;
            }
        });
    }
    private void handleCellEmpty(int position){
        int x= position%getGridWidth();
        int y= position/getGridWidth();
        PathFinder.findEmpty(x,y,this);
        //gridAdapter.notifyDataSetChanged(); //Notifies Adapter that the cells changed. Has a bug on first cell.

    }
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
        gameState= Constants.GameState.LOST;
        revealMines();
        gameOverPrompt();
    }
    private void gameOverPrompt(){
        GameActivity.showLostDialog();
    }
    private void gameWon(){
        GameActivity.showWonDialog();
    }
    public Constants.GameState getGameState(){return gameState;}
    public Constants.GameDifficulty getGameDifficulty(){return gameDifficulty;}
    public Cell getCell(int x, int y){
        return gameGrid[x][y];
    }
    public Cell getCell(int position){
        int x= position%getNumColumns();
        int y= position/getNumColumns();
        return gameGrid[x][y];
    }
    public int getGridWidth(){return gameDifficulty.getWidth();}
    public int getGridHeight(){return gameDifficulty.getHeight();}
    public int getMineNum(){return gameDifficulty.getMineNum();}


    private class GridAdapter extends BaseAdapter {
        public GridAdapter() { }
        public int getCount() {
            return getGridWidth() * getGridHeight();
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCell(position);
        }
    }

}