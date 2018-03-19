package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.recinos.myminesweepergame.GameEngine;
import com.example.recinos.myminesweepergame.R;


/**
 * Created by recinos on 2/9/18.
 */

public class Cell extends View {
    private int value;
    private boolean isBomb;
    private boolean isMarked;

    private boolean flagged;
    private int x;
    private int y;
    private int position;

    private static Integer[] thumbnails = {
            R.drawable.empty, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight, R.drawable.flagged, R.drawable.mine, R.drawable.block,
    };

    public Cell(Context context, int position ) {
        super(context);
        this.position=position;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,widthMeasureSpec);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawCell(canvas);
    }
    private void drawCell(Canvas canvas){
        Drawable drawable;
        //This is where long click glitch might be.
        if (this.isFlagged()&& !this.isClicked()) {
            drawable = ContextCompat.getDrawable(getContext(), Cell.thumbnails[9]);
        }
        else if (this.isClicked()) {
            drawable = ContextCompat.getDrawable(getContext(), Cell.thumbnails[this.getValue()]);
        }
        else{
            drawable= ContextCompat.getDrawable(getContext(),Cell.thumbnails[thumbnails.length-1]);
        }

        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }
    public int getValue() {
        return value;
    }
    //Only Called when the player taps a mine. This ensures that flagged cells are also revealed at the end of the game.
    public void isOver(){
        flagged=false;
        isMarked=true;
        invalidate();
    }
    public void setValue(int value) {
        isBomb=false;
        isMarked=false;
        flagged=false;
        /*if (this.isBomb()){
            isBomb=true;
        }*/
        this.value = value;
    }
    public boolean isClicked() {
        return isMarked;
    }
    public void setClicked() {
        this.isMarked=true;
        invalidate(); //invalidate view. After invalidation it gets redrawn (calls onDraw)
    }
    public void setPosition(int position){
        this.position=position;
        x=position % GameEngine.width;
        y=position/GameEngine.height;
    }
    public boolean isBomb() {
        return isBomb;
    }
    public void setBomb(){
        this.isBomb=true;
    }
    public boolean isFlagged() {
        return flagged;
    }
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    public int getYPos(){
        return y;
    }
    public int getXPos() {
        return x;
    }


}
