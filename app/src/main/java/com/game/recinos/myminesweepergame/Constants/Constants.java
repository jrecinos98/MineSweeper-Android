package com.game.recinos.myminesweepergame.Constants;

import android.os.Parcel;
import android.os.Parcelable;

import com.game.recinos.myminesweepergame.R;

import java.io.Serializable;

/**
 * Created by recinos on 3/23/18.
 */

public abstract class Constants implements Serializable {
    public enum GAME_STATE{
        PLAYING,
        LOST,
        WON,
        NOT_STARTED,
        PAUSED,
        RELOADED
    }
    public enum GAME_DIFFICULTY{
        EASY(12,19,15,200),
        MEDIUM(15,24,45,200),
        HARD(20,32,110,200),
        LOAD,
        CUSTOM;

        private int width;
        private int height;
        private int mineNum;
        private int toolBarHeight;
        GAME_DIFFICULTY(int w, int h, int m, int t){
            this.width=w;
            this.height=h;
            this.mineNum=m;
            this.toolBarHeight=t;
        }
        GAME_DIFFICULTY(){}

        public int getWidth(){return width;}
        public int getHeight(){return height;}
        public int getMineNum(){return mineNum;}
        public int getToolBarHeight(){return toolBarHeight;}
        public void setEnumWidth(int w){width=w;}
        public void setEnumHeight(int h){height=h;}
        public void setMineNum(int m){mineNum=m;}
        public void setToolBarHeight(int t){toolBarHeight=t;}

    }
    public final static String SAVE_NAME= "GameSave.ser";

    public final static int SMILEY_NORMAL= R.drawable.normal;
    public final static int SMILEY_RESET=R.drawable.reset;
    public final static int SMILEY_SCARED=R.drawable.scared;
    public final static int SMILEY_DEAD=R.drawable.dead;
    public final static int SMILEY_WON=R.drawable.won;

    public final static int TOOLBAR_FLAG= R.drawable.toolbar_flag;
    public final static int TOOLBAR_QUESTION= R.drawable.toolbar_question;
    public final static int TOOLBAR_MINE=R.drawable.toolbar_mine;
    public final static int TOOLBAR_HINT=R.drawable.lightbulb;
    public final static int SETTING= R.drawable.settings;

    private final static Integer[] CLOCK_IMAGES={
            R.drawable.clock_zero,R.drawable.clock_one,R.drawable.clock_two, R.drawable.clock_three,
            R.drawable.clock_four, R.drawable.clock_five, R.drawable.clock_six, R.drawable.clock_seven,
            R.drawable.clock_eight, R.drawable.clock_nine,
    };
    public static int getClockImage(int value){
        if(value>=0 && value<10){
            return CLOCK_IMAGES[value];
        }
        else
            return R.drawable.clock_start;
    }
    //Builds a new Difficulty enum with the given arguments.
    public static Constants.GAME_DIFFICULTY generateCustomDiff(int toolbarHeight, int colAmount, int rowNum, int mineNum){
        Constants.GAME_DIFFICULTY difficulty;
        difficulty=Constants.GAME_DIFFICULTY.CUSTOM;
        difficulty.setToolBarHeight(toolbarHeight);
        difficulty.setEnumWidth(colAmount);
        difficulty.setEnumHeight(rowNum);
        difficulty.setMineNum(mineNum);
        return difficulty;
    }
    //Wrapper to difficulty enum. Used to modify the difficulty once created
    // Also a wrapper used when serializing and making sure the fields are saved and can be restored when restoring from file
    public static class DifficultyWrap implements Serializable{
        Constants.GAME_DIFFICULTY difficulty;
        private int width;
        private int height;
        private int mineNum;
        private int toolBarHeight;

        public DifficultyWrap(Constants.GAME_DIFFICULTY diff) {
            difficulty=diff;
            width=diff.getWidth();
            height=diff.getHeight();
            mineNum= diff.getMineNum();
            toolBarHeight= diff.getToolBarHeight();
        }
        public Constants.GAME_DIFFICULTY getDifficulty(){
            if (difficultyReset()){
                return restoreDifficulty();
            }
            return difficulty;
        }
        //Used when restoring from a file
        private Constants.GAME_DIFFICULTY restoreDifficulty(){
            difficulty= generateCustomDiff(toolBarHeight,width,height,mineNum);
            return difficulty;
        }
        //When serialized the fields in the enum are saved but the values themselves are not and equal 0
        private Boolean difficultyReset(){
            return (difficulty.getToolBarHeight() == 0 && difficulty.width==0 && difficulty.height==0 && difficulty.mineNum==0);
        }
        public void setDifficulty(Constants.GAME_DIFFICULTY diff){
            difficulty=diff;
            width=diff.getWidth();
            height=diff.getHeight();
            mineNum= diff.getMineNum();
            toolBarHeight= diff.getToolBarHeight();
        }
    }
}
