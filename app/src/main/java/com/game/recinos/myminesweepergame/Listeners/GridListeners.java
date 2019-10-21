package com.game.recinos.myminesweepergame.Listeners;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.game.recinos.myminesweepergame.Adapters.GridAdapters;
import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.GameActivity;
import com.game.recinos.myminesweepergame.Grid.Grid;
import com.game.recinos.myminesweepergame.Grid.GridComponent;
import com.game.recinos.myminesweepergame.CustomViews.Toolbar.GameClock;
import com.game.recinos.myminesweepergame.CustomViews.Toolbar.MineCounter;
import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.util.Finder;
import com.game.recinos.myminesweepergame.util.Util;

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
        Context mcontext;
        GridAdapters.GameGridAdapter myAdapter;

        public GridOnItemClickListener(Grid myGrid,  Button smiley, MineCounter mineCount, GameClock timer, GridAdapters.GameGridAdapter adapter, Context context){
            gameGrid=myGrid;
            smileyButton=smiley;
            mineCounter=mineCount;
            gameTimer=timer;
            myAdapter=adapter;
            mcontext=context;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GridComponent temp= gameGrid.getCell(position);
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.RELOADED){
                if(GameActivity.time==0){
                    GameActivity.playSound("empty");
                    gameGrid.handleFirstClick(position);
                }
                GameActivity.timerHandler.postDelayed(gameTimer,0);
                GameActivity.GAME_STATE= Constants.GAME_STATE.PLAYING;
            }
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.NOT_STARTED){
                GameActivity.playSound("empty");
                gameGrid.handleFirstClick(position);
                //myAdapter.refresh();
                GameActivity.timerHandler.postDelayed(gameTimer,0);
            }
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING) {
                if (!temp.isOpened()) {
                    if(gameGrid.isActionButtonActive()){
                        GameActivity.playSound("action_button");
                        gameGrid.handleActionButton(temp);
                    }
                    else if(GameActivity.getHintButtonTag()==1 && temp.isOpenable()){
                        if(temp.isMine()){
                            temp.showAsHintMine();
                        }
                        else{

                            if(temp.getValue() == 0 && temp.isOpenable()){
                                GameActivity.playSound("empty");
                                gameGrid.handleCellEmpty(position);
                            }
                            else {
                                GameActivity.playSound("value");
                                gameGrid.handleValueCell(temp);
                            }
                        }
                    }
                    else if (temp.isMine() && temp.isOpenable()){
                        GameActivity.playSound("defeat");
                        gameGrid.handleClickedMine(temp);
                        smileyButton.setBackgroundResource(Constants.SMILEY_DEAD);
                        Util.deleteSave(mcontext, Constants.SAVE_NAME);
                    }
                    else if (temp.getValue() == 0 && temp.isOpenable()) {
                        GameActivity.playSound("empty");
                        gameGrid.handleCellEmpty(position);
                    }
                    else if(temp.isOpenable()) {
                        GameActivity.playSound("value");
                        gameGrid.handleValueCell(temp);
                    }
                }
                if(gameGrid.getCorrectMoves() == (gameGrid.getGridHeight()*gameGrid.getGridWidth())- gameGrid.getMineNum()){
                    GameActivity.playSound("victory");
                    gameGrid.gameWon();
                    smileyButton.setBackgroundResource(Constants.SMILEY_WON);
                    Util.deleteSave(mcontext, Constants.SAVE_NAME);
                }

            }
            if(gameGrid.getFlagNum() >=0) {
                mineCounter.setMineCounter(gameGrid.getFlagNum());
            }
            myAdapter.refresh();
        }

    }

    /**
     * Long clicking on a cell will flag it or make it into a question.
     */
    public static class GridOnItemLongClickListener implements GridView.OnItemLongClickListener{
        Grid gameGrid;
        MineCounter mineCounter;
        GridAdapters.GameGridAdapter myAdapter;
        public GridOnItemLongClickListener(Grid grid,MineCounter mineCount,GridAdapters.GameGridAdapter adapter){
            gameGrid=grid;
            mineCounter=mineCount;
            myAdapter=adapter;

        }
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            GridComponent temp= gameGrid.getCell(position);
            if(GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING) {
                if (temp.isOpened()){
                    return false;//returning false ensures that there wont be vibration.
                }
                else {
                    GameActivity.playSound("action_button");
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
            myAdapter.refresh();
            return true;
        }
    }

    /**
     * First clicking on a cell animates the smiley. Scrolling and cell animation is handled here
     */
    @SuppressWarnings("ClickableViewAccessibility")
    public static class GridOnTouchListener implements GridView.OnTouchListener {
        private final Context mContext;
        Grid gameGrid;
        Button mySmiley;
        GridAdapters.GameGridAdapter myAdapter;
        GridView gridView;
        int current;
        ArrayList<Integer> neighbors;
        boolean animated;
        public GridOnTouchListener(Grid myGrid, Context context, Button smiley,GridAdapters.GameGridAdapter adapter, GridView instance ){
            gameGrid=myGrid;
            mySmiley=smiley;
            myAdapter=adapter;
            gridView=instance;
            mContext=context;
            current=-1;
            animated=false;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float currentXPosition = event.getX();
            float currentYPosition = event.getY();
            int position = gridView.pointToPosition((int) currentXPosition, (int) currentYPosition);
            if (position < 0) {
                position = 0;
            }
            //If the current touch is in a different cell or not one of the neighbors then fix
            if(position != current && current!=-1 && animated){

                GameActivity.playSound("smiley_unclick");
                restore();
                neighbors=null;
                animated=false;
            }
            current=position;
            GridComponent temp = gameGrid.getCell(position);
            neighbors = Finder.findNeighborPositions(temp.getRow(), temp.getCol(), gameGrid.getCells());
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                //return true;
            }
           // GameActivity.playSound("disappear");
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
        private void handleAnimation(MotionEvent event, ArrayList<Integer> neighbors, GridComponent temp) {
            if (GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_UP) {
                //smileyButton.setBackgroundResource(Constants.SMILEY_NORMAL);
                if (animated){
                    GameActivity.playSound("smiley_unclick");
                }
                animated=false;
                restore();
            } else if (GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_DOWN) {
                 if (temp.isOpened() && temp.getValue() != 0) {
                    animated=true;
                    GameActivity.playSound("smiley_click");
                    for (int neighborPos : neighbors) {
                        int row = neighborPos % gameGrid.getGridWidth();
                        int col = neighborPos / gameGrid.getGridWidth();
                        if (!gameGrid.getCell(row,col).isOpened() && !gameGrid.getCell(row,col).isAnimated()) {
                            gameGrid.getCell(row,col).animateCell();
                            myAdapter.refresh();
                        }
                    }
                }
                if (!temp.isOpened() || (temp.isOpened() && temp.getValue() != 0)) {
                    //smileyButton.setBackgroundResource(Constants.SMILEY_SCARED);
                    mySmiley.setBackgroundResource(Constants.SMILEY_SCARED);
                }
            }

        }

        private void restore(){
            mySmiley.setBackgroundResource(Constants.SMILEY_NORMAL);
            for (int neighborPos : neighbors) {
                int row = neighborPos % gameGrid.getGridWidth();
                int col = neighborPos / gameGrid.getGridWidth();
                if (gameGrid.getCell(row,col).isAnimated()) {
                    gameGrid.getCell(row,col).undoAnimation();
                    myAdapter.refresh();
                }
            }
        }

    }
}
