package com.example.recinos.myminesweepergame.Constants;

/**
 * Created by recinos on 3/23/18.
 */

public abstract class Constants {
    public enum GameState{
        PLAYING,
        LOST,
        WON
    }
    public enum GameDifficulty{
        EASY(12,18,20,300),
        MEDIUM(15,23,40,250),
        HARD(20,30,80,300);
        private int width;
        private int height;
        private int mineNum;
        private int toolBarHeight;
        GameDifficulty(int w, int h, int m, int t){
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
}
