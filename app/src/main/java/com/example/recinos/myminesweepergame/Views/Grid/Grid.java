package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.GameEngine;
import com.example.recinos.myminesweepergame.GameActivity;
import com.example.recinos.myminesweepergame.util.Generator;
import com.example.recinos.myminesweepergame.util.PathFinder;

/**
 * Created by recinos on 2/7/18.
 */


public class Grid extends GridView{
    private int width;
    private int height;
    private Cell[][] gameGrid;
   // GridAdapter gridAdapter= new GridAdapter();
    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);

        width= GameActivity.difficulty.getWidth();
        height=GameActivity.difficulty.getHeight();
        this.setNumColumns(width);
        gameGrid = Generator.generateGrid(context,GameActivity.difficulty.getMineNum(),width,height);
        this.setAdapter(new GridAdapter());
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp= getCell(position);
                if(!temp.isOpened()){
                    if(temp.getValue()== 0 && !temp.isFlagged()){
                        handleCellEmpty(position);
                    }
                    else{
                        if (!temp.isFlagged()) {
                            handleCellOnTap(temp);
                        }
                    }
                }
            }
        });
        //Glitches on long click
        this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp= getCell(position);
                if (temp.isFlagged()) {
                    temp.unFlag();
                } else {
                    temp.setFlagged();
                }
                //gridAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    private void handleCellEmpty(int position){
        int x= position%width;
        int y= position/height;
        PathFinder.findEmpty(x,y,this);
        //gridAdapter.notifyDataSetChanged(); //Notifies Adapter that the cells changed. Has a bug on first cell.

    }
    private void handleCellOnTap(Cell cell){
        if (cell.isMine()){
            gameOver();
        }
        else
            cell.setOpened();
    }
    private void revealMines(){
        for(int x=0; x<width; x++){
            for (int y=0; y<height; y++){
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
       revealMines();
    }
    public Cell getCell(int x, int y){
        return gameGrid[x][y];
    }
    public Cell getCell(int position){
        int x= position%width;
        int y= position/height;
        return gameGrid[x][y];
    }
    public int getGridWidth(){return width;}
    public int getGridHeight(){return height;}

    private class GridAdapter extends BaseAdapter {
        public GridAdapter() { }
        public int getCount() {
            return width * height;
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            int x= position%width;
            int y= position/height;
            return gameGrid[x][y];
        }
    }

}