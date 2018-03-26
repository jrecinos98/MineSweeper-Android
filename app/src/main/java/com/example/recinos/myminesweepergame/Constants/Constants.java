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
        EASY(12,16,20),
        MEDIUM(15,20,40),
        HARD(20,28,80);
        private int width;
        private int height;
        private int mineNum;
        GameDifficulty(int w, int h, int m){
            this.width=w;
            this.height=h;
            this.mineNum=m;
        }
        public int getWidth(){return width;}
        public int getHeight(){return height;}
        public int getMineNum(){return mineNum;}
    }
}
