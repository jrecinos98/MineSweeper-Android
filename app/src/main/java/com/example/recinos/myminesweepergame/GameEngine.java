package com.example.recinos.myminesweepergame;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;

import com.example.recinos.myminesweepergame.Views.Grid.Cell;
import com.example.recinos.myminesweepergame.util.Generator;

/**
 * Created by recinos on 2/5/18.
 */

public class GameEngine {
    /*
    protected static GameEngine instance;
    private Context mContext;
    public int [][] gameGrid;
    //Don't realy need a marked array.
    public boolean[][] marked;
    private Cell[][] MineSweeperGrid= new Cell[width][height];
    //only able to render a square grid correctly
    final public static int width = 10;
    final public static int height = 10;
    final public static int mineNum = 10;


    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }
    private GameEngine(){

    }
    public void gameOver(){
        GameEngine.getInstance().updateCells(true);
    }
    public void resetGame() {
        createGrid(mContext);
    }

    public void createGrid(Context context)
    {
        this.mContext=context;
        gameGrid= Generator.generate(mineNum,width,height);
        marked= Generator.initMarked(width,height);
        setGrid(mContext);
    }
    private void setGrid(final Context context){
        for (int x=0; x<width;x++){
            for(int y=0; y<height; y++){
                if(MineSweeperGrid[x][y]==null){
                    MineSweeperGrid[x][y]= new Cell(context,y*height+x);
                }
                MineSweeperGrid[x][y].setValue(gameGrid[x][y]);
                if (gameGrid[x][y]==10){
                    MineSweeperGrid[x][y].setMine();
                }
                MineSweeperGrid[x][y].invalidate();
            }
        }
    }

    public void updateCells(boolean game_over){
        for(int x=0; x<width; x++){
            for (int y=0; y<height; y++){
                if (game_over){
                    MineSweeperGrid[x][y].gameOver();
                }
                else if (marked[x][y]){
                    MineSweeperGrid[x][y].setOpened();
                }
                //setClicked calls invalidate() which redraws Cell.
            }
        }
    }

    public View getCell(int position) {
        int x= position%width;
        int y= position/height;
        return MineSweeperGrid[x][y];
    }

    public boolean getMarked(int position){
        int x= position%width;
        int y= position/height;
        return marked[x][y];
    }
*/
}
