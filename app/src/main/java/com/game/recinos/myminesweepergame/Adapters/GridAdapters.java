package com.game.recinos.myminesweepergame.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.game.recinos.myminesweepergame.Views.Grid.Cell;

/**
 * Container class for various Grid Adapters
 */
public abstract class GridAdapters {
    /**
     * Defines the grid the user sees when adjusting the size of the cells.
     */
    public static class SliderGrid extends BaseAdapter {
        private Context mContext;
        public SliderGrid(Context context) {
            mContext=context;
        }
        @Override
        public int getCount() {
            return 9;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new Cell(mContext, 0, 0, position);
                convertView.setBackgroundResource(com.game.recinos.myminesweepergame.R.drawable.block);
            }
            return convertView;
        }
    }
    /**
     * GridAdapter used for the actual game grid.
     */
    public static class GameGridAdapter extends BaseAdapter{
        Cell [][] cells;
        public GameGridAdapter(Cell[][] mCells) { cells=mCells;}
        public int getCount() {
            return cells.length * cells[0].length;
        }
        public Object getItem(int position) {
            return getCell(position);
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCell(position);
        }
        public Cell getCell(int position){
            int row= position% cells.length;
            int col= position/ cells.length;
            return cells[row][col];
        }
    }
}
