package com.example.recinos.myminesweepergame;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
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

@SuppressWarnings("ClickableViewAccessibility")
public class GameActivity extends AppCompatActivity {
    public static Constants.GAME_DIFFICULTY difficulty;
    public static Constants.GAME_STATE GAME_STATE;
    private static AlertDialog wonDialog;
    private static Button mySmileyButton;
    public static Vibrator vibe;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        difficulty = (Constants.GAME_DIFFICULTY) getIntent().getSerializableExtra("GAME_DIFFICULTY");
        GAME_STATE= Constants.GAME_STATE.PLAYING;
        setContentView(R.layout.activity_main);
        setToolBarHeight(difficulty.getToolBarHeight());
        initToolBarButtons();
        vibe= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initWonDialog();

    }
    private void createSmiley(){
        mySmileyButton= findViewById(R.id.smileyButton);
        mySmileyButton.setBackgroundResource(Constants.SMILEY_NORMAL);
        mySmileyButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        mySmileyButton.setBackgroundResource(Constants.SMILEY_RESET);
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
    public static void updateSmileyButton(int id){
        mySmileyButton.setBackgroundResource(id);
    }
    private void createHintButton(){
        Button myHintButton= findViewById(R.id.myHintButton);
        myHintButton.setBackgroundResource(Constants.TOOLBAR_MINE);
        myHintButton.setOnClickListener(new View.OnClickListener() {
            private int currentImage=Constants.TOOLBAR_MINE;
            private Button hintButton= findViewById(R.id.myHintButton);
            @Override
            public void onClick(View v) {
                if (v == hintButton) {
                    if(currentImage== Constants.TOOLBAR_MINE){
                        currentImage= Constants.TOOLBAR_FLAG;
                        v.setBackgroundResource(currentImage);
                    }
                    else if (currentImage == Constants.TOOLBAR_FLAG) {
                        currentImage = Constants.TOOLBAR_QUESTION;
                        v.setBackgroundResource(currentImage);
                    }
                    else if(currentImage== Constants.TOOLBAR_QUESTION){
                        currentImage= Constants.TOOLBAR_HINT;
                        v.setBackgroundResource(currentImage);
                    }
                    else if(currentImage==Constants.TOOLBAR_HINT){
                        currentImage=Constants.TOOLBAR_MINE;
                        v.setBackgroundResource(currentImage);
                    }

                }
            }
        });
    }
    public void createSettingsButton(){
        Button mySettingsButton= findViewById(R.id.mySettingsButton);
        mySettingsButton.setBackgroundResource(Constants.SETTING);
        mySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public static void showWonDialog(){
        wonDialog.show();
    }
    public static void clickVibrate(int duration) {
        vibe.vibrate(duration);
    }
    public void initWonDialog(){
        //Use mview.findViewById rather than just findViewById.
        // Because if we dont specify mview then the widgets will be
        // searched for in activity_main.xml and they are not there.
        AlertDialog.Builder wonBuilder= new AlertDialog.Builder(GameActivity.this);
        final View wonView= getLayoutInflater().inflate(R.layout.dialog_custom_won,null);
        wonBuilder.setView(wonView);
        wonDialog = wonBuilder.create();
    }
    public void setToolBarHeight(int height){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myToolbar.getLayoutParams();
        layoutParams.height = height;
        myToolbar.setLayoutParams(layoutParams);
        myToolbar.requestLayout();
    }
    public void initToolBarButtons(){
        createSmiley();
        createHintButton();
        createSettingsButton();

    }

}