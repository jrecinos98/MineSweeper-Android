package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.example.recinos.myminesweepergame.GameEngine;
import com.example.recinos.myminesweepergame.util.PathFinder;

/**
 * Created by recinos on 2/7/18.
 */


public class Grid extends GridView{
    private int width=GameEngine.width;
    private int height=GameEngine.height;
    GridAdapter gridAdapter= new GridAdapter();
    public Grid(Context context,AttributeSet attrs) {
        super(context, attrs);
        this.setNumColumns(width);
        GameEngine.getInstance().createGrid(context);
        this.setAdapter(gridAdapter);
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp = (Cell) GameEngine.getInstance().getCell(position);
                if (!GameEngine.getInstance().getMarked(position)) {
                    if (temp.getValue() == 0) {
                        handleCellEmpty(position);
                    } else {
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
                Cell temp = (Cell) GameEngine.getInstance().getCell(position);
                if (temp.isFlagged()) {
                    temp.setFlagged(false);
                } else {
                    temp.setFlagged(true);
                }
                gridAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    private void handleCellEmpty(int position){
        int x= position%width;
        int y= position/height;
        PathFinder.findEmpty(x,y,GameEngine.getInstance().gameGrid, GameEngine.getInstance().marked);
        GameEngine.getInstance().updateCells(false);
        //gridAdapter.notifyDataSetChanged(); //Notifies Adapter that the cells changed. Has a bug on first cell.

    }
    private void handleCellOnTap(Cell cell){
        if (cell.isBomb()){
            GameEngine.getInstance().gameOver();
        }
        else
            cell.setClicked();
    }
    private class GridAdapter extends BaseAdapter {

        public GridAdapter() { }

        public int getCount() {
            return GameEngine.width * GameEngine.height;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return GameEngine.getInstance().getCell(position);
        }

    }

}