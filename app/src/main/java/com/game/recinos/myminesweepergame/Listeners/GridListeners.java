package com.game.recinos.myminesweepergame.Listeners;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.GameActivity;
import com.game.recinos.myminesweepergame.Views.Grid.Cell;
import com.game.recinos.myminesweepergame.Views.Grid.Grid;
import com.game.recinos.myminesweepergame.Views.Toolbar.GameClock;
import com.game.recinos.myminesweepergame.Views.Toolbar.MineCounter;
import com.game.recinos.myminesweepergame.util.Finder;

import java.util.ArrayList;

public class GridListeners {
    /**
     * Handles the click on a cell in the grid and the animation, mine counter.
     */
    public static class GridOnItemClickListener implements GridView.OnItemClickListener{
        Grid gameGrid;
        Button smileyButton;
        MineCounter mineCounter;
        GameClock gameTimer;
        public GridOnItemClickListener(Grid myGrid, Button smiley, MineCounter mineCount, GameClock timer){
            gameGrid=myGrid;
            smileyButton=smiley;
            mineCounter=mineCount;
            gameTimer=timer;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cell temp= gameGrid.getCell(position);
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.NOT_STARTED){
                gameGrid.handleFirstClick(position);
                GameActivity.timerHandler.postDelayed(gameTimer,0);
            }
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING) {
                if (!temp.isOpened()) {
                    if(gameGrid.isActionButtonActive()){
                        gameGrid.handleActionButton(temp);
                    }
                    else if(GameActivity.getHintButtonTag()==1 && temp.isOpenable()){
                        if(temp.isMine()){
                            temp.showAsHintMine();
                        }
                        else{
                            if(temp.getValue() == 0 && temp.isOpenable()){
                                gameGrid.handleCellEmpty(position);
                            }
                            else {
                                gameGrid.handleValueCell(temp);
                            }
                        }
                    }
                    else if (temp.isMine() && temp.isOpenable()){
                        gameGrid.handleClickedMine(temp);
                        smileyButton.setBackgroundResource(Constants.SMILEY_DEAD);
                    }
                    else if (temp.getValue() == 0 && temp.isOpenable()) {
                        gameGrid.handleCellEmpty(position);
                    }
                    else if(temp.isOpenable()) {
                        gameGrid.handleValueCell(temp);
                    }
                }
                if(GameActivity.correctMoves == (gameGrid.getGridHeight()*gameGrid.getGridWidth())- gameGrid.getMineNum()){
                    gameGrid.gameWon();
                    smileyButton.setBackgroundResource(Constants.SMILEY_WON);
                }
            }
            if(gameGrid.getFlagNum() >=0)
                mineCounter.setMineCounter(gameGrid.getFlagNum());
        }
    }

    /**
     * Long clicking on a cell will flag it or make it into a question.
     */
    public static class GridOnItemLongClickListener implements GridView.OnItemLongClickListener{
        Grid gameGrid;
        MineCounter mineCounter;
        public GridOnItemLongClickListener(Grid grid, MineCounter mineCount){
            gameGrid=grid;
            mineCounter=mineCount;
        }
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Cell temp= gameGrid.getCell(position);
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING) {
                if (temp.isOpened()){
                    return false;//returning false ensures that there wont be vibration.
                }
                else {
                    if(temp.isOpenable()){
                        gameGrid.decrementFlagNum();
                    }
                    temp.setNextFlagImage();
                    if (!temp.isQuestion() && !temp.isFlagged())
                        gameGrid.incremementFlagNum();
                }
            }
            if (gameGrid.getFlagNum() >=0)
                mineCounter.setMineCounter(gameGrid.getFlagNum());
            return true;
        }
    }

    /**
     * First clicking on a cell animates the smiley. Scrolling and cell animation is handled here
     */
    @SuppressWarnings("ClickableViewAccessibility")
    public static class GridOnTouchListener implements GridView.OnTouchListener {
        Grid gameGrid;
        Button mySmiley;
        public GridOnTouchListener(Grid myGrid, Button smiley){
            gameGrid=myGrid;
            mySmiley=smiley;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            float currentXPosition = event.getX();
            float currentYPosition = event.getY();
            int position = gameGrid.pointToPosition((int) currentXPosition, (int) currentYPosition);
            if (position < 0) {
                position = 0;
            }
            Cell temp = gameGrid.getCell(position);
            ArrayList<Integer> neighbors = Finder.findNeighborPositions(temp.getRow(), temp.getCol(), gameGrid.getCells());
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                //return true;
            }
            handleAnimation(event, neighbors, temp);
            return false;
        }
        /**
         * Takes care of the animation events.
         *
         * @param event     The events.
         * @param neighbors The neighboring cells of the clicked cell (temp)
         * @param temp      the clicked cell.
         */
        public void handleAnimation(MotionEvent event, ArrayList<Integer> neighbors, Cell temp) {
            if (GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_UP) {
                //smileyButton.setBackgroundResource(Constants.SMILEY_NORMAL);
                mySmiley.setBackgroundResource(Constants.SMILEY_NORMAL);
                for (int neighborPos : neighbors) {
                    int row = neighborPos % gameGrid.getGridWidth();
                    int col = neighborPos / gameGrid.getGridWidth();
                    if (gameGrid.getCell(row,col).isAnimated()) {
                        gameGrid.getCell(row,col).undoAnimation();
                    }
                }
            } else if (GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_DOWN) {
                if (temp.isOpened() && temp.getValue() != 0) {
                    for (int neighborPos : neighbors) {
                        int row = neighborPos % gameGrid.getGridWidth();
                        int col = neighborPos / gameGrid.getGridWidth();
                        if (!gameGrid.getCell(row,col).isOpened() && !gameGrid.getCell(row,col).isAnimated()) {
                            gameGrid.getCell(row,col).animateCell();
                        }
                    }
                }
                if (!temp.isOpened() || (temp.isOpened() && temp.getValue() != 0)) {
                    //smileyButton.setBackgroundResource(Constants.SMILEY_SCARED);
                    mySmiley.setBackgroundResource(Constants.SMILEY_SCARED);
                }
            }
        }
    }
}
