package com.game.recinos.myminesweepergame.Listeners;

import android.widget.GridView;
import android.widget.SeekBar;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.util.Util;

public class SlidersListeners {
    /**
     * Depending on how much and the direction the slider moves the size of the grid changes accordingly.
     */
    public static class GridSeekBarListener implements SeekBar.OnSeekBarChangeListener{
        int lastProgress=50;
        int defaultProgress=50;
        GridView sizeGrid;
        public GridSeekBarListener(GridView sGrid) {
            sizeGrid=sGrid;
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int sizeParameter=(seekBar.getMax()-progress)+defaultProgress;
            if(progress>lastProgress){
                sizeGrid.getLayoutParams().width= Util.convertDpToPixel(sizeParameter);
                sizeGrid.getLayoutParams().height=Util.convertDpToPixel(sizeParameter);
                sizeGrid.requestLayout();
            }
            else if (progress<lastProgress){
                sizeGrid.getLayoutParams().width= Util.convertDpToPixel(sizeParameter);
                sizeGrid.getLayoutParams().height=Util.convertDpToPixel(sizeParameter);
                sizeGrid.requestLayout();
            }
            lastProgress=progress;
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    /**
     * Depending on the progress of this slider the difficulty is chosen.
     */
    public static class MineSliderListener implements SeekBar.OnSeekBarChangeListener{
        Constants.DifficultyWrap wrapper;
        public MineSliderListener(Constants.DifficultyWrap wrap){
            wrapper=wrap;
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(progress==0){
                wrapper.setDifficulty(Constants.GAME_DIFFICULTY.EASY);
            } else if(progress==1){
                wrapper.setDifficulty(Constants.GAME_DIFFICULTY.MEDIUM);
            } else{
                wrapper.setDifficulty(Constants.GAME_DIFFICULTY.HARD);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
