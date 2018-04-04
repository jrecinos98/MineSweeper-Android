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
import com.example.recinos.myminesweepergame.util.Util;
import com.example.recinos.myminesweepergame.util.Finder;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by recinos on 2/7/18.
 */


public class Grid extends GridView implements Serializable{
    private static Grid instance;
    private Cell[][] gameGrid;


    private Constants.GAME_DIFFICULTY GAME_DIFFICULTY;
    private transient Context gameContext;

    private int mineNum;
    private int flagNum;
    private transient GridAdapter myAdapter;
    @SuppressLint("ClickableViewAccessibility")
    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        GAME_DIFFICULTY=GameActivity.difficulty;
        mineNum=getMineNum();
        flagNum=getMineNum();
        gameContext=context;
        setNumColumns(getGridWidth());
        gameGrid = Util.generateInitial(context,getGridWidth(),getGridHeight());
        myAdapter= new GridAdapter();
        setAdapter(myAdapter);
        instance=this;
        instance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Button hintButton=findViewById(R.id.myActionButton);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp= getCell(position);
                if(GameActivity.GAME_STATE == Constants.GAME_STATE.NOT_STARTED){
                    handleFirstClick(position);
                }
                if(GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING) {
                    if (!temp.isOpened()) {
                        if(isActionButtonActive()){
                            handleActionButton(temp);
                        }
                        else if(GameActivity.getHintButtonTag()==1 && temp.isOpenable()){
                            if(temp.isMine()){
                                temp.showAsHintMine();
                            }
                            else{
                                if(temp.getValue() == 0 && temp.isOpenable()){
                                    handleCellEmpty(position);
                                }
                                else {
                                    handleValueCell(temp);
                                }
                            }
                        }
                        else if (temp.isMine() && temp.isOpenable()){
                            handleClickedMine(temp);
                        }
                        else if (temp.getValue() == 0 && temp.isOpenable()) {
                            handleCellEmpty(position);
                        }
                        else if(temp.isOpenable()) {
                           handleValueCell(temp);
                        }
                    }
                    if(GameActivity.correctMoves == (getGridHeight()*getGridWidth())- mineNum){
                        gameWon();
                    }
                }
                if(flagNum >=0)
                    GameActivity.mineCounter.setMineCounter(flagNum);
            }
        });
        //Glitches on long click
        instance.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cell temp= getCell(position);
                if(GameActivity.GAME_STATE == Constants.GAME_STATE.PLAYING) {
                    if (temp.isOpened()){
                        return false;//returning false ensures that there wont be vibration.
                    }
                    else {
                        if(temp.isOpenable()){
                            flagNum--;
                        }
                        temp.setNextFlagImage();
                        if (!temp.isQuestion() && !temp.isFlagged())
                            flagNum++;
                    }
                }
                if (flagNum >=0)
                    GameActivity.mineCounter.setMineCounter(flagNum);
                return true;
            }
        });
        instance.setOnTouchListener(new OnTouchListener(){
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
                ArrayList<Integer> neighbors= Finder.findNeighborPositions(temp.getRow(),temp.getCol(),gameGrid);
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //return true;
                }
                handleAnimation(event,neighbors,temp);
                return false; }
        });
    }

    /**
     * gets a cell at the specified row and column
     * @param row row number
     * @param col column number
     * @return the cell at that position
     */
    public Cell getCell(int row, int col){
        return gameGrid[row][col];
    }

    /**
     * returns the cell at the given position
     * @param position the position in  the grid.
     * @return Reference to the cell at that position.
     */
    public Cell getCell(int position){
        int row= position%getNumColumns();
        int col= position/getNumColumns();
        return gameGrid[row][col];
    }

    /**
     * @return The GridAdapter
     */
    public GridAdapter getAdapter(){return myAdapter;}

    public int getGridWidth(){return GAME_DIFFICULTY.getWidth();}
    public int getGridHeight(){return GAME_DIFFICULTY.getHeight();}
    public int getMineNum(){return GAME_DIFFICULTY.getMineNum();}


    /**
     * @return If the user clicked on the action button it returns true.
     */
    public boolean isActionButtonActive(){
        return !(GameActivity.getActionButtonTag()==Constants.TOOLBAR_MINE);
    }

    /**
     * It makes the given cell either a flag, a question mark, or normal.
     * @param temp
     */
    public void handleActionButton(Cell temp){
        GameActivity.clickVibrate(1);
        if(GameActivity.getActionButtonTag()== Constants.TOOLBAR_FLAG){
            if(temp.isFlagged()){
                //temp.unFlag();
                flagNum++;
                temp.setNormal();
            }
            else {
                if(temp.isOpenable()){
                    flagNum--;
                }
                temp.setFlagged();
            }
        }
        else if(GameActivity.getActionButtonTag()==Constants.TOOLBAR_QUESTION){
            if(temp.isQuestion()){
               // temp.unQuestion();
                flagNum++;
                temp.setNormal();
            }
            else {
                if(temp.isOpenable()){
                    flagNum--;
                }
                temp.setQuestion();
            }
        }
    }

    public void handleValueCell(Cell temp){
        GameActivity.incrementCorrectMoves();
        temp.setOpened();
        GameActivity.clickVibrate(5);
    }



    /**
     * Before the first click the grid contains no values yet. After the first click the grid is generated and the first clicked cell is opened.
     * @param position The position of the first clicked cell.
     */
    private void handleFirstClick(int position){
        if(GameActivity.getActionButtonTag()==Constants.TOOLBAR_MINE){
            Util.generateGrid(gameGrid,getMineNum(),position);
            GameActivity.GAME_STATE=Constants.GAME_STATE.PLAYING;
            GameActivity.timerHandler.postDelayed(GameActivity.gameTimer,0);
        }
    }

    /**
     * Used to find the cells that should open up if an empty cell is clicked.
     * @param position position of clicked empty cell.
     */
    private void handleCellEmpty(int position){
        GameActivity.clickVibrate(5);
        int row= position%getGridWidth();
        int col= position/getGridWidth();
        Finder.findEmpty(row,col,this);
        //gridAdapter.notifyDataSetChanged(); //Notifies Adapter that the cells changed. Has a bug on first cell.

    }
    /**
     * When a mine is clicked.
     * @param temp reference to the clicked cell containing the mine.
     */
    private void handleClickedMine(Cell temp){
        GameActivity.clickVibrate(600);
        temp.showAsClickedMine();
        gameOver();
    }
    /**
     * Takes care of the animation events.
     * @param event The events.
     * @param neighbors The neighboring cells of the clicked cell (temp)
     * @param temp the clicked cell.
     */
    private void handleAnimation(MotionEvent event, ArrayList<Integer> neighbors, Cell temp){
        if (GameActivity.GAME_STATE==Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_UP) {
            //smileyButton.setBackgroundResource(Constants.SMILEY_NORMAL);
            GameActivity.updateSmileyButton(Constants.SMILEY_NORMAL);
            for (int neighborPos : neighbors) {
                int row = neighborPos % getGridWidth();
                int col = neighborPos / getGridWidth();
                if (gameGrid[row][col].isAnimated()) {
                    gameGrid[row][col].undoAnimation();
                }
            }
        }
        else if(GameActivity.GAME_STATE==Constants.GAME_STATE.PLAYING && event.getAction() == MotionEvent.ACTION_DOWN) {
            if(temp.isOpened() && temp.getValue()!=0){
                for(int neighborPos: neighbors){
                    int row = neighborPos%getGridWidth();
                    int col= neighborPos/getGridWidth();
                    if(!gameGrid[row][col].isOpened() && !gameGrid[row][col].isAnimated()){
                        gameGrid[row][col].animateCell();
                    }
                }
            }
            if(!temp.isOpened() || (temp.isOpened()&&  temp.getValue()!=0)) {
                //smileyButton.setBackgroundResource(Constants.SMILEY_SCARED);
                GameActivity.updateSmileyButton(Constants.SMILEY_SCARED);
            }
        }
    }
    /**
     * Used to reveal all the mines in the grid. Called when the game is over.
     */
    private void revealMines(){
        for(int row=0; row<getGridWidth(); row++){
            for (int col=0; col<getGridHeight(); col++){
                if(gameGrid[row][col].isFlagged() || gameGrid[row][col].isQuestion()){
                    gameGrid[row][col].setNormal();
                }
                if(gameGrid[row][col].isMine()){
                    if(!gameGrid[row][col].isHint() && !gameGrid[row][col].isClickedMine()) {
                        gameGrid[row][col].showAsMine();
                    }
                }
            }
        }
    }
    /**
     * Called when the player clicks on a mine.
     */
    private void gameOver(){
        GameActivity.resetCorrectMoves();
        GameActivity.GAME_STATE= Constants.GAME_STATE.LOST;
        GameActivity.updateSmileyButton(Constants.SMILEY_DEAD);
        revealMines();
        save();
    }
    /**
     * called when a user wins the game.
     */
    private void gameWon(){
        GameActivity.GAME_STATE= Constants.GAME_STATE.WON;
        GameActivity.updateSmileyButton(Constants.SMILEY_WON);
        GameActivity.showWonDialog();
    }

    public Constants.GAME_STATE getGAME_STATE(){return GameActivity.GAME_STATE;}
    public Constants.GAME_DIFFICULTY getDifficulty(){return GAME_DIFFICULTY;}
    /**
     * Saves the current game (this) into a serialized file
     */
    public void save() {
        //For Internal Storage.
        File path = gameContext.getFilesDir();
        //For External Storage SD.
        File pathExt= gameContext.getExternalFilesDir(null);
        File file = new File(path, "GameSave.ser");
        try {
            FileOutputStream fileOutputStream = gameContext.openFileOutput("GameSave.ser", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a serializable object.
     *
     * @param context The application context.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    public static<T extends Serializable> T loadPreviousGame(Context context) {
        T save = null;
        try {
            FileInputStream fileInputStream = context.openFileInput("GameSave.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            save = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return save;
    }
    /**
     * Removes a specified file.
     *
     * @param context The application context.
     */
    public static void deleteSave(Context context) {
        context.deleteFile("GameSave.ser");
    }
    /**
     * Checks whether a save file exists.
     * @return true if save file exists and false otherwise
     */
    public static boolean saveExist(){
        if (new File("MyGame.ser").exists()){
            return true;
        }
        return false;

    }




    /**
     * GridAdapter used for the gridView.
     */
    private class GridAdapter extends BaseAdapter{
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
            return getCell(position);
        }
    }
}