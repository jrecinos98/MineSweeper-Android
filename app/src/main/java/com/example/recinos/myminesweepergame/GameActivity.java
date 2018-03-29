package com.example.recinos.myminesweepergame;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    private static  Button myActionButton;
    private static Button myHintButton;
    public static Vibrator vibe;

    private static Clock gameTimer;
    public static MineCounter mineCounter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        difficulty = (Constants.GAME_DIFFICULTY) getIntent().getSerializableExtra("GAME_DIFFICULTY");
        GAME_STATE= Constants.GAME_STATE.PLAYING;
        gameTimer=new Clock();
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
    private void createActionButton(){
        myActionButton= findViewById(R.id.myActionButton);
        myActionButton.setBackgroundResource(Constants.TOOLBAR_MINE);
        myActionButton.setTag(Constants.TOOLBAR_MINE);
        myActionButton.setOnClickListener(new View.OnClickListener() {
            private int currentImage=Constants.TOOLBAR_MINE;
            private Button hintButton= findViewById(R.id.myActionButton);
            @Override
            public void onClick(View v) {
                if (v == hintButton) {
                    if(currentImage== Constants.TOOLBAR_MINE) {
                        currentImage = Constants.TOOLBAR_FLAG;
                    }
                    else if (currentImage == Constants.TOOLBAR_FLAG) {
                        currentImage = Constants.TOOLBAR_QUESTION;
                    }
                    else if(currentImage== Constants.TOOLBAR_QUESTION){
                        currentImage= Constants.TOOLBAR_MINE;
                    }
                    v.setTag(currentImage);
                    v.setBackgroundResource(currentImage);

                }
            }
        });
    }
    public void createHintButton(){
        myHintButton= findViewById(R.id.myHintButton);
        myHintButton.setBackgroundResource(Constants.TOOLBAR_HINT);
        myHintButton.setTag(-1);
        myHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)v.getTag()==-1){
                    v.setTag(1);
                }
            }
        });
    }
    public void createMineCounter(){
        mineCounter= new MineCounter(GameActivity.difficulty.getMineNum());
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
        createActionButton();
        createMineCounter();

    }
    public static int getActionButtonTag(){
        return (int)myActionButton.getTag();
    }
    public static int getHintButtonTag(){
        return (int) myHintButton.getTag();
    }
    public static void setHintButtonTag(){
        myHintButton.setTag(0);
    }
    public class Clock implements Runnable{
        volatile boolean playing;
        Button timer0= findViewById(R.id.timer0);
        Button timer1= findViewById(R.id.timer1);
        @Override
        public void run() {

        }
    }
    public class MineCounter{
        private Button mine0Button;
        private Button mine1Button;
        private Button mine2Button;
        int startMineNum;
        int newMineNum;
        MineCounter(int mineNum){
            this.startMineNum=mineNum;
            this.newMineNum=mineNum;
            //determines value at 100th place
            int hundred= mineNum/100;
            //determines value at 10th place
            int tenth=(mineNum-(hundred*100))/10;
            //determines value at 1st place
            int digit= (mineNum-(hundred*100)-(tenth*10));
            mine0Button=findViewById(R.id.mines0);
            mine0Button.setBackgroundResource(Constants.getClockImage(hundred));

            mine1Button=findViewById(R.id.mines1);
            mine1Button.setBackgroundResource(Constants.getClockImage(tenth));

            mine2Button=findViewById(R.id.mine2);
            mine2Button.setBackgroundResource(Constants.getClockImage(digit));
        }
        public void decreaseMineNum(){
            if(newMineNum>0) {
                newMineNum--;
                //determines value at 100th place
                int hundred= newMineNum/100;
                //determines value at 10th place
                int tenth=(newMineNum-(hundred*100))/10;
                //determines value at 1st place
                int digit= (newMineNum-(hundred*100)-(tenth*10));
                mine0Button=findViewById(R.id.mines0);
                mine0Button.setBackgroundResource(Constants.getClockImage(hundred));

                mine1Button=findViewById(R.id.mines1);
                mine1Button.setBackgroundResource(Constants.getClockImage(tenth));

                mine2Button=findViewById(R.id.mine2);
                mine2Button.setBackgroundResource(Constants.getClockImage(digit));
            }
        }
        public void increaseMineNum() {
            if (newMineNum < startMineNum) {
                newMineNum++;
                //determines value at 100th place
                int hundred = newMineNum / 100;
                //determines value at 10th place
                int tenth = (newMineNum - (hundred * 100)) / 10;
                //determines value at 1st place
                int digit = (newMineNum - (hundred * 100) - (tenth * 10));
                mine0Button = findViewById(R.id.mines0);
                mine0Button.setBackgroundResource(Constants.getClockImage(hundred));

                mine1Button = findViewById(R.id.mines1);
                mine1Button.setBackgroundResource(Constants.getClockImage(tenth));

                mine2Button = findViewById(R.id.mine2);
                mine2Button.setBackgroundResource(Constants.getClockImage(digit));
            }
        }
    }

}