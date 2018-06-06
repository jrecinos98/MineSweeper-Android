package com.game.recinos.myminesweepergame.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.CustomViews.Grid.Cell;
import com.game.recinos.myminesweepergame.Grid.GridComponent;

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
                convertView = new Cell(mContext, position, R.drawable.block);
                //convertView.setBackgroundResource(com.game.recinos.myminesweepergame.R.drawable.block);
            }
            return convertView;
        }
    }
    /**
     * GridAdapter used for the actual game grid.
     */
    public static class GameGridAdapter extends BaseAdapter{
        GridComponent[][] cells;
        Context mContext;
        //Cell[][] views;
        public GameGridAdapter(GridComponent[][] data, Context context) {
            cells=data;
            mContext=context;
            //views= new Cell[data.length][data[0].length];
        }
        public int getCount() {
            return cells.length * cells[0].length;
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            int row = position % cells.length;
            int col = position / cells.length;
            if (convertView == null) {
                int currentImage = cells[row][col].getCurrentImage();
                convertView = new Cell(mContext, position, currentImage);
                //views[row][col]= (Cell) convertView;
            }
            Cell temp = (Cell) convertView;
            if (temp.getCurrentImage() != cells[row][col].getCurrentImage()) {
                temp.setCurrentImage(cells[row][col].getCurrentImage());
            }
            //temp.invalidate();
            return convertView;
        }
        public void refresh() {
            notifyDataSetChanged();
        }
    }
}
