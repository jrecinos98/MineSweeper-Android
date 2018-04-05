package com.game.recinos.myminesweepergame.Listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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
import com.game.recinos.myminesweepergame.R;
import com.game.recinos.myminesweepergame.SettingsActivity;
import com.game.recinos.myminesweepergame.Views.Grid.Grid;

/**
 * A container class for various OnClickListener implementations
 */
public abstract class ButtonListeners {
    /**
     * Launches the game with easy difficulty
     */
    public static class EasyDifficultyListener implements View.OnClickListener{
        Context mContext;
        Constants.GAME_DIFFICULTY difficulty;
        public EasyDifficultyListener(Context mcontext) {
            mContext=mcontext;
        }
        @Override
        public void onClick(View v) {
            Intent toGame= new Intent(mContext,GameActivity.class);
            difficulty=Constants.GAME_DIFFICULTY.EASY;
            toGame.putExtra("GAME_DIFFICULTY", difficulty);
            mContext.startActivity(toGame);
        }
    }

    /**
     * Launches the game with Medium difficulty
     */
    public static class MediumDifficultyListener implements View.OnClickListener{
        Context mContext;
        Constants.GAME_DIFFICULTY difficulty;
        public MediumDifficultyListener(Context mcontext) {
            mContext=mcontext;
        }
        @Override
        public void onClick(View v) {
            Intent toGame= new Intent(mContext,GameActivity.class);
            difficulty=Constants.GAME_DIFFICULTY.MEDIUM;
            toGame.putExtra("GAME_DIFFICULTY", difficulty);
            mContext.startActivity(toGame);
        }
    }

    /**
     * Launches the game in Hard difficulty
     */
    public static class HardDifficultyListener implements View.OnClickListener{
        Context mContext;
        Constants.GAME_DIFFICULTY difficulty;
        public HardDifficultyListener(Context mcontext) {
            mContext=mcontext;
        }
        @Override
        public void onClick(View v) {
            Intent toGame= new Intent(mContext,GameActivity.class);
            difficulty=Constants.GAME_DIFFICULTY.HARD;
            toGame.putExtra("GAME_DIFFICULTY", difficulty);
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
            if(Grid.saveExist()){
                Intent toGame= new Intent(mContext,GameActivity.class);
                difficulty=Constants.GAME_DIFFICULTY.LOAD;
                toGame.putExtra("GAME_DIFFICULTY",difficulty);
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
            mContext.startActivity(settingsIntent);
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
            toGame.putExtra("GAME_DIFFICULTY", difficulty);
            myCustomDialog.dismiss();
            mContext.startActivity(toGame);
        }
    }

    /**
     * Launches the game using the values from the size of the Grid and the difficulty.
     */
    public static class SliderCustomButton implements View.OnClickListener{
        Display display;
        GridView sizeGrid;
        Context mContext;
        AlertDialog myCustomDialog;
        Constants.DifficultyWrap wrapper;
        public SliderCustomButton(Context context,Display display, GridView sizeGrid, Constants.DifficultyWrap wrap, AlertDialog dialog){
            this.display=display;
            this.sizeGrid= sizeGrid;
            wrapper=wrap;
            mContext=context;
            myCustomDialog= dialog;
        }
        @Override
        public void onClick(View v){
            Constants.GAME_DIFFICULTY difficulty= wrapper.getDifficulty();
            Point size = new Point();
            display.getSize(size);
            int DisplayWidth = size.x;
            int DisplayHeight = size.y;
            //The desired width for each individual cell.
            double desiredWidth= sizeGrid.getLayoutParams().width/3;
            //The possible amount of blocks on screen
            int availableBlocks= (int)(DisplayWidth/desiredWidth);
            //The number of pixels remaining smaller than a block.
            double leftOverSpace= DisplayWidth%desiredWidth;
            //If the leftOverSpace is greater than 1/3 the block then add an extra block
            if(leftOverSpace>desiredWidth/3){
                availableBlocks++;
            }
            int newWidth= DisplayWidth/availableBlocks;
            int toolbarHeight= difficulty.getToolBarHeight();
            int rowNum= (int)((DisplayHeight-toolbarHeight)/newWidth);
            double leftOverHeight= ((DisplayHeight-toolbarHeight)%newWidth);
            Log.d("LEFTOVER", leftOverHeight+"");
            Log.d("DESIRED", newWidth+"");
            /*if(leftOverHeight<newWidth){
                toolbarHeight+=leftOverHeight;
            }
            /*else{
                toolbarHeight+=leftOverHeight;
            }*/
            Log.d("TOOLBAR", toolbarHeight+"");
            Log.d("MINES", difficulty+"");
            switch(difficulty){
                case EASY:
                    difficulty.setMineNum(12);
                    break;
                case MEDIUM:
                    difficulty.setMineNum(29);
                    break;
                case HARD:
                    difficulty.setMineNum(50);
                    break;
            }
            int mineNum= (int)Math.sqrt(difficulty.getMineNum()-(difficulty.getMineNum()/2))*availableBlocks;
            if (difficulty==Constants.GAME_DIFFICULTY.HARD && availableBlocks<10){
                mineNum-=11;
            }
            if(availableBlocks<10){
                mineNum-=4;
            }
            difficulty=Constants.GAME_DIFFICULTY.CUSTOM;
            difficulty.setToolBarHeight(toolbarHeight);
            difficulty.setEnumWidth(availableBlocks);
            difficulty.setEnumHeight(rowNum);
            difficulty.setMineNum(mineNum);
            Intent toGame= new Intent(mContext,GameActivity.class);
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