package com.example.recinos.myminesweepergame;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.Views.Grid.Grid;


public class GameActivity extends AppCompatActivity {
    public static Constants.GameDifficulty difficulty;
    public static Constants.GameState gameState;
    public static AlertDialog lostDialog;
    private static AlertDialog wonDialog;
    private int SMILEYNORMAL= R.drawable.normal;
    private int SMILEYRESET=R.drawable.reset;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        difficulty = (Constants.GameDifficulty) getIntent().getSerializableExtra("GameDifficulty");
        gameState= Constants.GameState.PLAYING;
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myToolbar.getLayoutParams();
        layoutParams.height = difficulty.getToolBarHeight();
        myToolbar.setLayoutParams(layoutParams);
        myToolbar.requestLayout();
       //final Button myResetButton = (Button) findViewById(R.id.myResetButton);
        //final Button myMainMenuButton= (Button) findViewById(R.id.myMainMenuButton);

        //Use mview.findViewById rather than just findViewById.
        // Because if we dont specify mview then the widgets will be
        // searched for in activity_main.xml and they are not there.

        AlertDialog.Builder mBuilder= new AlertDialog.Builder(GameActivity.this);
        final View mView= getLayoutInflater().inflate(R.layout.dialog_custom,null);
        mBuilder.setView(mView);
        final AlertDialog resetDialog = mBuilder.create();

        AlertDialog.Builder myBuilder= new AlertDialog.Builder(GameActivity.this);
        final View lostView= getLayoutInflater().inflate(R.layout.dialog_custom_lost,null);
        myBuilder.setView(lostView);
        lostDialog = myBuilder.create();

        AlertDialog.Builder wonBuilder= new AlertDialog.Builder(GameActivity.this);
        final View wonView= getLayoutInflater().inflate(R.layout.dialog_custom_won,null);
        wonBuilder.setView(wonView);
        wonDialog = wonBuilder.create();
        final Button mySmileyButton= findViewById(R.id.smileyButton);
        mySmileyButton.setBackgroundResource(SMILEYNORMAL);
        mySmileyButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        mySmileyButton.setBackgroundResource(R.drawable.reset);
                        return false; // if you want to handle the touch event return true
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        recreate();
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });


    }
    public static void showLostDialog(){
       lostDialog.show();
    }
    public static void showWonDialog(){
        wonDialog.show();
    }

}