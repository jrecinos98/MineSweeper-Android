package com.example.recinos.myminesweepergame.Constants;

import com.example.recinos.myminesweepergame.R;

/**
 * Created by recinos on 3/23/18.
 */

public abstract class Constants {
    public enum GAME_STATE{
        PLAYING,
        LOST,
        WON
    }
    public enum GAME_DIFFICULTY{
        EASY(12,19,20,185),
        MEDIUM(15,24,40,160),
        HARD(20,32,105,160);
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
        public int getWidth(){return width;}
        public int getHeight(){return height;}
        public int getMineNum(){return mineNum;}
        public int getToolBarHeight(){return toolBarHeight;}
    }
    public static int SMILEY_NORMAL= R.drawable.normal;
    public static int SMILEY_RESET=R.drawable.reset;
    public static int SMILEY_SCARED=R.drawable.scared;
    public static int SMILEY_DEAD=R.drawable.dead;

    public static int TOOLBAR_FLAG= R.drawable.toolbar_flag;
    public static int TOOLBAR_QUESTION= R.drawable.toolbar_question;
    public static int TOOLBAR_MINE=R.drawable.toolbar_mine;
    public static int TOOLBAR_HINT=R.drawable.lightbulb;
    public static int SETTING= R.drawable.settings;

    private static Integer[] CLOCK_IMAGES={
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
}
