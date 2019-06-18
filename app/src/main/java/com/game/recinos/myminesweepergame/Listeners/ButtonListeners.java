package com.game.recinos.myminesweepergame.Listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.GameActivity;
import com.game.recinos.myminesweepergame.Grid.Grid;
import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.SettingsActivity;
import com.game.recinos.myminesweepergame.util.Util;

/**
 * A container class for various OnClickListener implementations
 */
public abstract class ButtonListeners {

    /**
     * Launches the game using the values from the size of the Grid and the difficulty.
     */
    public static class SliderCustomButton implements View.OnClickListener{
        Display display;
        GridView sizeGrid;
        Context mContext;
        //Only needed to dismiss the dialog
        AlertDialog myCustomDialog;
        Constants.DifficultyWrap wrapper;
        //Num of columns in demo grid
        double colInGrid=3.0;

        public SliderCustomButton(Context context,Display display, GridView sizeGrid, Constants.DifficultyWrap wrap, AlertDialog dialog){
            this.display=display;
            this.sizeGrid= sizeGrid;
            wrapper=wrap;
            mContext=context;
            myCustomDialog= dialog;
        }
        private int getColNum(double DisplayWidth, double requested){
            int tempCol= (int) (DisplayWidth/requested);
            //The number of pixels remaining smaller than a block.
            double leftOverSpace= DisplayWidth%requested;
            //If the leftOverSpace is greater than 1/3 the block then add an extra block
            if(leftOverSpace>requested/4){
                tempCol++;
            }
            return tempCol;
        }

        public int getMines(Constants.GAME_DIFFICULTY difficulty, int colAmount){
            int mines=0;
            switch(difficulty){
                case EASY:
                    mines=12;
                    break;
                case MEDIUM:
                    mines=30;
                    break;
                case HARD:
                    mines=50;
                    break;
            }
            int mineNum= (int) Math.sqrt(mines-(mines/2))*colAmount;
            if (difficulty==Constants.GAME_DIFFICULTY.HARD && colAmount<10){
                mineNum-=11;
            }
            if(colAmount<10){
                mineNum-=4;
            }
            return mineNum;

        }

        public Constants.GAME_DIFFICULTY generateCustomDiff(int toolbarHeight, int colAmount, int rowNum, int mineNum){
            Constants.GAME_DIFFICULTY difficulty;
            difficulty=Constants.GAME_DIFFICULTY.CUSTOM;
            difficulty.setToolBarHeight(toolbarHeight);
            difficulty.setEnumWidth(colAmount);
            difficulty.setEnumHeight(rowNum);
            difficulty.setMineNum(mineNum);
            return difficulty;
        }
        @Override
        public void onClick(View v){
            Constants.GAME_DIFFICULTY difficulty= wrapper.getDifficulty();
            Point size = new Point();
            display.getSize(size);
            int toolbarHeight= difficulty.getToolBarHeight();
            double DisplayWidth = size.x;
            double newHeight = size.y - toolbarHeight;

            //The requested width for each individual cell.
            double requestedSize= sizeGrid.getLayoutParams().width / colInGrid;
            //The possible amount of columns on screen
            int colAmount= getColNum(DisplayWidth, requestedSize);
            //After obtaining the number of columns get the size of the blocks
            double blockSize= DisplayWidth/colAmount;

            int rowNum= (int)((newHeight)/blockSize)+1;
            double leftOverHeight= (newHeight %blockSize);
            if(leftOverHeight<blockSize){
                toolbarHeight+=leftOverHeight;
                rowNum--;
            }
            int mineNum= getMines(difficulty, colAmount);
            //Generates Custom Difficulty
            difficulty= generateCustomDiff(toolbarHeight, colAmount, rowNum, mineNum);

            Intent toGame= new Intent(mContext,GameActivity.class);
            toGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            toGame.putExtra("GAME_DIFFICULTY", difficulty);

            //Pass height separately
            String t_height= Integer.toString(difficulty.getToolBarHeight());
            toGame.putExtra("TOOLBAR_HEIGHT", t_height);

            myCustomDialog.dismiss();
            mContext.startActivity(toGame);
        }
    }

    /**
     * Signifies the game that the game should be loaded from save file.
     */
    public static class LoadListener implements View.OnClickListener{
        Context mContext;
        Constants.GAME_DIFFICULTY difficulty;
        public LoadListener(Context mcontext) {
            mContext=mcontext;
        }
        @Override
        public void onClick(View v) {
            if(Util.saveExist(Constants.SAVE_NAME, mContext.fileList())){
                Object items[]= Util.loadGame(mContext,Constants.SAVE_NAME);
                Grid loaded=  (Grid) items[0];
                difficulty=loaded.getDifficulty();
                Integer time= (Integer) items[1];

                Intent toGame= new Intent(mContext,GameActivity.class);
                toGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                toGame.putExtra("GAME_DIFFICULTY",difficulty);
                String t_height= Integer.toString(difficulty.getToolBarHeight());
                toGame.putExtra("TOOLBAR_HEIGHT", t_height);

                //Use a bundle to pass serialized objects to the next activity.
                Bundle bundle= new Bundle();
                bundle.putSerializable("GAME_GRID", loaded);
                bundle.putInt("TIME", time);
                toGame.putExtras(bundle);
                mContext.startActivity(toGame);
            }
            else{
                Toast.makeText(mContext, "No Save File Exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Launches the settings activity
     */
    public static class SettingsListener implements View.OnClickListener{
        Context mContext;
        public SettingsListener(Context mcontext) {
            mContext=mcontext;
        }
        @Override
        public void onClick(View v) {
            Intent settingsIntent= new Intent(mContext, SettingsActivity.class);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(settingsIntent);
        }
    }




    /**
     * Launches the game with specified difficulty
     */
    public static class DifficultyButtonListener implements  View.OnClickListener{
        Context mContext;
        Constants.GAME_DIFFICULTY difficulty;
        public DifficultyButtonListener(Context context, Constants.GAME_DIFFICULTY diff){
            mContext=context;
            difficulty=diff;
        }
        @Override
        public void onClick(View v) {
            Intent toGame= new Intent(mContext,GameActivity.class);
            toGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            toGame.putExtra("GAME_DIFFICULTY", difficulty);
            String t_height= Integer.toString(difficulty.getToolBarHeight());
            toGame.putExtra("TOOLBAR_HEIGHT", t_height);
            mContext.startActivity(toGame);
        }
    }
    /**
     * Button opens a dialog where the user can input the number of desired columns, rows and mines.
     */
    public static class CustomGoListener implements View.OnClickListener{
        Context mContext;
        Constants.GAME_DIFFICULTY difficulty;
        View wonView;
        AlertDialog myCustomDialog;
        public CustomGoListener(Context mcontext, View won, AlertDialog dialog) {
            mContext=mcontext;
            wonView=won;
            myCustomDialog=dialog;
        }
        @Override
        public void onClick(View v) {
            EditText myColText= wonView.findViewById(R.id.myColText);
            EditText myRowText= wonView.findViewById(R.id.myRowText);
            EditText myMinetext= wonView.findViewById(R.id.myMineText);
            if(myRowText.getText().toString().equals("")){
                Toast.makeText(mContext, "Please enter the number of rows", Toast.LENGTH_SHORT).show();
                return;
            }
            if(myColText.getText().toString().equals("")){
                Toast.makeText(mContext, "Please enter the number of columns", Toast.LENGTH_SHORT).show();
                return;
            }
            if(myMinetext.getText().toString().equals("")){
                Toast.makeText(mContext, "Please enter the number of mines", Toast.LENGTH_SHORT).show();
                return;
            }
            int rowNum=Integer.parseInt(myColText.getText().toString());
            int colNum=Integer.parseInt(myRowText.getText().toString());
            int mineNum= Integer.parseInt(myMinetext.getText().toString());
            if(colNum>20){
                colNum=20;
            }
            if(rowNum>100){
                rowNum=100;
            }
            if(mineNum > (colNum*rowNum-9)){
                mineNum=colNum*rowNum-9;
            }
            if(mineNum>colNum*rowNum){
                mineNum=999;
            }
            difficulty= Constants.GAME_DIFFICULTY.CUSTOM;
            difficulty.setEnumWidth(colNum);
            difficulty.setEnumHeight(rowNum);
            difficulty.setMineNum(mineNum);
            Intent toGame= new Intent(mContext,GameActivity.class);
            toGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            toGame.putExtra("GAME_DIFFICULTY", difficulty);
            myCustomDialog.dismiss();
            mContext.startActivity(toGame);
        }
    }

    /**
     * Clicking on the action listener changes the image and changes the tag in GameActivity.
     */
    public static class ActionButtonListener implements View.OnClickListener{
        private int currentImage=Constants.TOOLBAR_MINE;
        @Override
        public void onClick(View v) {
            if(GameActivity.GAME_STATE==Constants.GAME_STATE.PLAYING) {
                if (currentImage == Constants.TOOLBAR_MINE) {
                    currentImage = Constants.TOOLBAR_FLAG;
                } else if (currentImage == Constants.TOOLBAR_FLAG) {
                    currentImage = Constants.TOOLBAR_QUESTION;
                } else if (currentImage == Constants.TOOLBAR_QUESTION) {
                    currentImage = Constants.TOOLBAR_MINE;
                }
                v.setTag(currentImage);
                GameActivity.actionTag=currentImage;
                v.setBackgroundResource(currentImage);
            }
        }
    }

    /**
     * Clicking the button changes the tag in GameActivity.
     */
    public static class HintButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if((int)v.getTag()==-1){
                v.setTag(1);
            }
            else if((int)v.getTag()==1){
                v.setTag(-1);
            }
            GameActivity.hintTag=(int)v.getTag();
        }
    }

    public static class WarningButtonsListener implements  View.OnTouchListener{
        Button myButton;
        public WarningButtonsListener(Button button){
            myButton=button;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    myButton.setBackgroundResource(R.drawable.empty);
                    myButton.setTextColor(Color.WHITE);
                    return false;
                case MotionEvent.ACTION_UP:
                    myButton.setBackgroundResource(R.drawable.block);
                    myButton.setTextColor(Color.BLACK);
            }
            return false;
        }
    }

}
