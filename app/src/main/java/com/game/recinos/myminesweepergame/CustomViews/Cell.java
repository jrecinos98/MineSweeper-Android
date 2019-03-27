package com.game.recinos.myminesweepergame.CustomViews.Grid;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.Serializable;


/**
 * Created by recinos on 2/9/18.
 */

public class Cell extends View implements Serializable {
    int position;
    private int currentImage;
    public Cell(Context context, int position, int image) {
        super(context);
        this.position=position;
        currentImage=image;

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
        drawable = ContextCompat.getDrawable(getContext(), currentImage);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }
    public void setCurrentImage(int image){
        currentImage=image;
    }
    public int getCurrentImage(){
        return currentImage;
    }


}
