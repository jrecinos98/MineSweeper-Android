package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.recinos.myminesweepergame.R;


/**
 * Created by recinos on 2/9/18.
 */

public class Cell extends View {
    private int value;
    private boolean isMine;
    private boolean isOpened;
    private boolean flagged;
    private int x;
    private int y;
    private int position;
    public static int cellsOpened=0;

    private static Integer[] thumbnails = {
            R.drawable.empty, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight, R.drawable.flagged, R.drawable.mine, R.drawable.block,
    };

    public Cell(Context context, int position, int x, int y ) {
        super(context);
        this.position=position;
        this.x=x;
        this.y=y;

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
    /**
     *
     * @param canvas
     */
    private void drawCell(Canvas canvas){
        Drawable drawable;
        //This is where long click glitch might be.
        if (this.isFlagged() && !this.isOpened()) {
            drawable = ContextCompat.getDrawable(getContext(), Cell.thumbnails[9]);
        }
        else if (this.isOpened()) {
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
    /**
     * Checks whether a cell should be searched by path finding algorithm.
     * @return
     */
    public boolean isMarked(){
        if(isFlagged() || isOpened()){
            return true;
        }
        return false;
    }
    //plan to remove
    public void setValue(int value) {
        isMine=false;
        isOpened=false;
        flagged=false;
        this.value = value;
    }
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * Marks a cell in the grid as opened and invalidates the view to call the overridden onDraw method to draw the correct image.
     */
    public void setOpened() {
        if (!this.isFlagged() && !this.isOpened()){
            this.isOpened=true;
            cellsOpened++;
            invalidate(); //invalidate view. After invalidation it gets redrawn (calls onDraw)
        }
    }
    public boolean isMine() {
        return isMine;
    }
    public void setMine(){
        this.isMine=true;
        value=10;
    }
    public boolean isFlagged() {
        return flagged;
    }
    public void setFlagged() {
        this.flagged = true;
        invalidate();
    }
    public void unFlag(){
        this.flagged=false;
        invalidate();
    }
    public void setXPos(int xCor){
        x=xCor;
    }
    public void setYPos(int yCor){
        y=yCor;
    }
    public int getYPos(){
        return y;
    }
    public int getXPos() {
        return x;
    }


}
