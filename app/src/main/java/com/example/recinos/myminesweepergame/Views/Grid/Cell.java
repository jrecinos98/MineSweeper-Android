package com.example.recinos.myminesweepergame.Views.Grid;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.example.recinos.myminesweepergame.R;

import java.io.Serializable;


/**
 * Created by recinos on 2/9/18.
 */

public class Cell extends View implements Serializable {
    private int value;
    private boolean isMine;
    private boolean isOpened;
    private boolean animated;

    private int row;
    private int col;
    private int position;


    private int currentImage;
    private int pastImage;


    private static int FLAG= R.drawable.flagged;
    private static int QUESTION=R.drawable.question;
    private static int MINE= R.drawable.mine;
    private static int HINT= R.drawable.nonbomb;
    private static int NORMAL= R.drawable.block;
    private static int CLICKED= R.drawable.clicked_bomb;
    private static Integer[] thumbnails = {
            R.drawable.empty, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight,
    };

    public Cell(Context context, int row, int col, int position) {
        super(context);
        this.row=row;
        this.col=col;
        this.position=position;
        currentImage=NORMAL;

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
    private void drawCell(Canvas canvas) {
        Drawable drawable;
        if (revealValue()) {
            currentImage = Cell.thumbnails[getValue()];
        }
        drawable = ContextCompat.getDrawable(getContext(), currentImage);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }
    /*
    //plan to remove
    public void setValue(int value) {
        isMine=false;
        isOpened=false;
        flagged=false;
        clickedMine=false;
        this.value = value;
    }*/
    /**
     *
     * @return Number reflecting the number of neighboring mines
     */
    public int getValue() {
        return value;
    }
    /**
     * Increments the current value of the cell.
     */
    public void increment() {
        if (!this.isMine()) {
            value++;
        }
    }
    /**
     * Decrements the value of the cell.
     */
    public void decrement(){
        if(!this.isMine()){
            value--;
        }
    }
    /**
     *
     * @return
     */
    public int getCol(){
        return col;
    }
    public int getRow() {
        return row;
    }
    public int getPosition(){return position;}
    /**
     *
     * @return Whether this cell has been opened.
     */
    public boolean isOpened() {
        return isOpened;
    }
    /**
     * Marks a cell in the grid as opened and invalidates the view to call the overridden onDraw method to draw the correct image.
     */
    public void setOpened() {
        if (!this.isFlagged() && !this.isOpened()){
            this.isOpened=true;
            invalidate(); //invalidate view. After invalidation it gets redrawn (calls onDraw)
        }
    }
    /**
     * @return True if the cell is a mine. False otherwise.
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Marked as a mine. The image is still normal as the mine is unrevealed.
     */
    public void makeMine(){
        this.isMine=true;
    }
    /**
     * changes the current image back to the stock normal image and it is able to be clicked.
     */
    public void setNormal(){
        currentImage=NORMAL;
        //has to be unflagged to get rid of a flag or question when their action button is active. See Grid onClickListener()
        // this.flagged=false;
        invalidate();
    }
    /**
     * @return True if the current image is a flag.
     */
    public boolean isFlagged() {
        return (currentImage==FLAG);
    }
    /**
     * Changes the current image to a flag and redraws the cell.
     */
    public void setFlagged() {
        currentImage=FLAG;
        invalidate();
       /* if(!isQuestion() && !isFlagged())
            GameActivity.mineCounter.decreaseMineNum();*/
    }
    /**
     * Changes the current image to a question mark and redraws the cell.
     */
    public void setQuestion(){
        currentImage=QUESTION;
        invalidate();

    }
    /**
     * @return True if the current image is question mark. Else return false.
     */
    public boolean isQuestion(){
        return (currentImage==QUESTION);
    }

    /**
     * Sets cell image to empty image temporarily.
     */
    public void animateCell(){
        pastImage=currentImage;
        currentImage=Cell.thumbnails[0];
        animated=true;
        invalidate();
    }

    /**
     * Sets cell image back to the image it had before being animated.
     */
    public void undoAnimation(){
        currentImage=pastImage;
        animated=false;
        invalidate();
    }
    /**
     *
     */
    public boolean isAnimated(){
        return animated;
    }



    /**
     * if this was the mine that was clicked when losing the game it has a different image.
     */
    public void showAsClickedMine(){
        if(this.isMine()){
            currentImage= CLICKED;
            setOpened();
        }
    }
    /**
     * Returns true if the cell contains the mine that was clicked.
     */
    public boolean isClickedMine(){
        return(currentImage==CLICKED);
    }
    /**
     * used to change the cell image to a mine image.
     */
    public void showAsMine(){
        if(isMine()) {
            currentImage = MINE;
            setOpened();
        }
    }
    /**
     * Changes the current image to hint image. Only changes to it if the cell is a mine. otherwise it is opened as normal.
     */
    public void showAsHintMine(){
        if(isMine()){
            currentImage=HINT;
            setOpened();
        }
    }
    /**
     *
     * @return true if the cell was marked as a hint cell
     */
    public boolean isHint(){
        return (currentImage==HINT);
    }
    /**
     * If the cell should be revealed as a number then it returns true.
     * @return True if the cell is not flagged, is not a question and is not a mine.
     */
    public boolean revealValue(){
        if(isOpened() && !isFlagged() && !isQuestion() && !isMine()){
            return true;
        }
        return false;
    }
    /**
     * Checks whether a cell should be searched by path finding algorithm and whether a click on the cell should be considered.
     * @return whether a cell can be clicked
     */
    public boolean isOpenable(){
        if(isFlagged() || isOpened() || isQuestion() || isHint()){
            return false;
        }
        return true;
    }
    /**
     * When a user long presses an unopened cell the sequence follows: NORMAL->FLAG->QUESTION->NORMAL
     */
    public void setNextFlagImage(){
        if(currentImage==NORMAL){
            //currentImage=FLAG;
            setFlagged();
        }
        else if(isFlagged()){
            setQuestion();
            /*currentImage=QUESTION;
            invalidate();*/
        }
        else if(isQuestion()){
            setNormal();
           // this.unFlag();
        }
    }




    //Used in Grid.onClickListener() method . FIX LATER!!
    /*
    public void unFlagClick(){
        flagged=false;
    }*/


}
