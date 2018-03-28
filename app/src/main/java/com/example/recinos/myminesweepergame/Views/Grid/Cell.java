package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.example.recinos.myminesweepergame.GameActivity;
import com.example.recinos.myminesweepergame.R;


/**
 * Created by recinos on 2/9/18.
 */

public class Cell extends View {
    private int value;
    private boolean isMine;
    private boolean isOpened;
    private boolean flagged;
    private boolean clickedMine;
    private boolean animate;
    private boolean hintCell;
    private int x;
    private int y;
    private int currentImage;
    public static int cellsOpened=0;
    private static int FLAG= R.drawable.flagged;
    private static int QUESTION=R.drawable.question;
    private static int MINE= R.drawable.mine;
    private static int HINT= R.drawable.nonbomb;
    private static int NORMAL= R.drawable.block;
    private static int CLICKED= R.drawable.clicked_bomb;
    int position;

    private static Integer[] thumbnails = {
            R.drawable.empty, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight,
    };

    public Cell(Context context, int x, int y, int position) {
        super(context);
        this.x=x;
        this.y=y;
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
    /**
     *
     * @param canvas
     */
    private void drawCell(Canvas canvas){
        Drawable drawable;
        //This is where long click glitch might be.
        if(animate){
            drawable= ContextCompat.getDrawable(getContext(),Cell.thumbnails[0]);
        }
        else if(this.isMine() && this.isOpened()){
            if(this.isClickedMine()){
                drawable= ContextCompat.getDrawable(getContext(),CLICKED);
            }
            else
                drawable= ContextCompat.getDrawable(getContext(),MINE);
        }
       else if ((this.isFlagged() && !this.isOpened()) || hintCell) {
            //currentImage=FLAG;
            drawable = ContextCompat.getDrawable(getContext(), currentImage);
        }
        else if (this.isOpened()) {
            currentImage=Cell.thumbnails[this.getValue()];
            drawable = ContextCompat.getDrawable(getContext(), Cell.thumbnails[this.getValue()]);
        }
        else{
            currentImage=NORMAL;
            drawable= ContextCompat.getDrawable(getContext(),NORMAL);
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
        clickedMine=false;
        this.value = value;
    }
    public void increment() {
        if (!this.isMine()) {
            value++;
        }
    }
    public void decrement(){
        if(!this.isMine()){
            value--;
        }
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
    public void setAnimate(){
        animate=true;
    }
    public void setToNormal(){
        animate=false;
        currentImage=NORMAL;
        this.unFlag();
    }
    public boolean getAnimate(){return animate;}
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
        currentImage=FLAG;
        this.flagged = true;
        invalidate();
    }
    public void unFlag(){
        this.flagged=false;
        invalidate();
    }
    public void setAsClickedMine(){
        if(this.isMine()){
            clickedMine=true;
        }
    }
    public boolean isClickedMine(){
        return clickedMine;
    }
    public int getYPos(){
        return y;
    }
    public int getXPos() {
        return x;
    }
    public int getPosition(){return position;}
    public void setNextFlagImage(){
        if(currentImage==NORMAL){
            currentImage=FLAG;
            this.setFlagged();
        }
        else if(currentImage==FLAG){
            currentImage=QUESTION;
            invalidate();
        }
        else if(currentImage==QUESTION){
            currentImage=NORMAL;
            this.unFlag();
        }
    }
    public void setQuestion(){
        this.setFlagged();
        currentImage=QUESTION;
        invalidate();

    }
    public boolean isQuestion(){
        if(currentImage==QUESTION){
            return true;
        }
        return false;
    }
    public void setHintCell(){
        hintCell=true;
        isMine=false;
        isOpened=true;
        currentImage=HINT;
        invalidate();
    }


}
