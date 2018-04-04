package com.example.recinos.myminesweepergame;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.Views.Grid.Cell;
import com.example.recinos.myminesweepergame.Views.Grid.Grid;
import com.example.recinos.myminesweepergame.util.Util;


/**
 * Created by recinos on 2/11/18.
 */

public class MenuActivity extends AppCompatActivity {
    Constants.GAME_DIFFICULTY difficulty;
    AlertDialog myCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button myEasyButton = findViewById(R.id.myEasyButton);
        Button myMediumButton=  findViewById(R.id.myMediumButton);
        Button myHardButton= findViewById(R.id.myHardButton);
        Button myLoadButton= findViewById(R.id.myLoadButton);
        Button myCustomButton=findViewById(R.id.myCustomButton);
        myEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                difficulty=Constants.GAME_DIFFICULTY.EASY;
                toGame.putExtra("GAME_DIFFICULTY", difficulty);
                startActivity(toGame);
            }
        });
        myMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                difficulty=Constants.GAME_DIFFICULTY.MEDIUM;
                toGame.putExtra("GAME_DIFFICULTY", difficulty);
                startActivity(toGame);
            }
        });
        myHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                difficulty=Constants.GAME_DIFFICULTY.HARD;
                toGame.putExtra("GAME_DIFFICULTY", difficulty);
                startActivity(toGame);
            }
        });
        myLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Grid.saveExist()){
                    Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                    difficulty=Constants.GAME_DIFFICULTY.LOAD;
                    toGame.putExtra("GAME_DIFFICULTY",difficulty);
                    startActivity(toGame);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Save File Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myCustomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //createCustom().show();
                createSizeCustom();
                myCustomDialog.show();
            }
        });

        createSettingsButton();

    }
    public void createSettingsButton(){
        Button myHintButton= findViewById(R.id.myHintButton);
        myHintButton.setBackgroundResource(Constants.SETTING);
        myHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent= new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
    public AlertDialog createCustom(){
        android.app.AlertDialog.Builder wonBuilder= new android.app.AlertDialog.Builder(MenuActivity.this);
        final View wonView= getLayoutInflater().inflate(R.layout.dialog_custom,null);
        wonBuilder.setView(wonView);
        Button myGoButton= wonView.findViewById(R.id.customGo);
        myCustomDialog=wonBuilder.create();
        myGoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText myColText= wonView.findViewById(R.id.myColText);
                EditText myRowText= wonView.findViewById(R.id.myRowText);
                EditText myMinetext= wonView.findViewById(R.id.myMineText);
                if(myRowText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter the number of rows", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(myColText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter the number of columns", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(myMinetext.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter the number of mines", Toast.LENGTH_SHORT).show();
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
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                toGame.putExtra("GAME_DIFFICULTY", difficulty);
                myCustomDialog.dismiss();
                startActivity(toGame);
            }
        });
        myCustomDialog.setCanceledOnTouchOutside(false);
        return myCustomDialog;

    }
    public void createSizeCustom(){
        android.app.AlertDialog.Builder sizeBuilder= new android.app.AlertDialog.Builder(MenuActivity.this);
        final View sizeView= getLayoutInflater().inflate(R.layout.dialog_size,null);
        sizeBuilder.setView(sizeView);
        final GridView sizeGrid= sizeView.findViewById(R.id.mySizeGrid);
        final SeekBar sliderBar= sizeView.findViewById(R.id.mySeekBar);
        final SeekBar mineBar= sizeView.findViewById(R.id.seekBar2);
        final Button myPlayButton= sizeView.findViewById(R.id.customStartButton);
        difficulty=Constants.GAME_DIFFICULTY.EASY;
        sizeGrid.setAdapter(new SizeAdapter());
        sliderBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress=50;
            int defaultProgress=50;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int sizeParameter=(seekBar.getMax()-progress)+defaultProgress;
                if(progress>lastProgress){
                    sizeGrid.getLayoutParams().width=Util.convertDpToPixel(sizeParameter);
                    sizeGrid.getLayoutParams().height=Util.convertDpToPixel(sizeParameter);
                    sizeGrid.requestLayout();
                }
                else if (progress<lastProgress){
                    sizeGrid.getLayoutParams().width= Util.convertDpToPixel(sizeParameter);
                    sizeGrid.getLayoutParams().height=Util.convertDpToPixel(sizeParameter);
                    sizeGrid.requestLayout();
                }
                lastProgress=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mineBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    difficulty=Constants.GAME_DIFFICULTY.EASY;
                }
                else if(progress==1){
                    difficulty=Constants.GAME_DIFFICULTY.MEDIUM;
                }
                else{
                    difficulty=Constants.GAME_DIFFICULTY.HARD;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        myPlayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Display display = getWindowManager().getDefaultDisplay();
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
                //If the leftOverSpace is smaller than half the block. Then let it be the same number
                if(leftOverSpace< desiredWidth/3){

                }
                else if(leftOverSpace>desiredWidth/3){
                    availableBlocks++;
                }
                desiredWidth= DisplayWidth/availableBlocks;
                int rowNum= (int)((DisplayHeight-difficulty.getToolBarHeight())/desiredWidth);
                //double leftOverHeight= ((DisplayHeight-difficulty.getToolBarHeight())%desiredWidth);
                int toolbarHeight= difficulty.getToolBarHeight();
                /*Log.d("LEFTOVER", leftOverHeight+"");
                Log.d("DESIRED", desiredWidth+"");
                if(leftOverHeight<desiredWidth/3){
                    toolbarHeight+=leftOverHeight;
                }
                else{
                    toolbarHeight+=leftOverHeight;
                }
                Log.d("TOOLBAR", toolbarHeight+"");
                Log.d("MINES", difficulty+"");*/
                int mineNum= (int)Math.sqrt(difficulty.getMineNum())*availableBlocks;
                difficulty=Constants.GAME_DIFFICULTY.CUSTOM;
                difficulty.setToolBarHeight(toolbarHeight);
                difficulty.setEnumWidth(availableBlocks);
                difficulty.setEnumHeight(rowNum);
                difficulty.setMineNum(mineNum);
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                toGame.putExtra("GAME_DIFFICULTY", difficulty);
                myCustomDialog.dismiss();
                startActivity(toGame);
            }
        });

        myCustomDialog= sizeBuilder.create();
        myCustomDialog.setCanceledOnTouchOutside(false);
    }
    public class SizeAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= new Cell(getApplicationContext(),0,0,position);
                convertView.setBackgroundResource(R.drawable.block);
            }
            return convertView;
        }
    }
}
