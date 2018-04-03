package com.example.recinos.myminesweepergame;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.Views.Toolbar.MineCounter;
import com.example.recinos.myminesweepergame.Views.Toolbar.GameClock;


@SuppressWarnings("ClickableViewAccessibility")
public class GameActivity extends AppCompatActivity {
    public static Constants.GAME_DIFFICULTY difficulty;
    public volatile static Constants.GAME_STATE GAME_STATE;

    private static AlertDialog wonDialog;
    private AlertDialog warningDialog;

    private static Button mySmileyButton;
    private static  Button myActionButton;
    private static Button myHintButton;
    private Button timer0;
    private Button timer1;
    private Button timer2;
    private Button timer3;



    public static Vibrator vibe;

    public static GameClock gameTimer;
    public static MineCounter mineCounter;
    public static Handler timerHandler = new Handler();
    public static int correctMoves=0;
    public volatile static int time=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        difficulty = (Constants.GAME_DIFFICULTY) getIntent().getSerializableExtra("GAME_DIFFICULTY");
        GAME_STATE= Constants.GAME_STATE.NOT_STARTED;
        int ToolBarHeight= difficulty.getToolBarHeight();
        vibe= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setContentView(R.layout.activity_main);
        initToolBarButtons();
        gameTimer=new GameClock(timer0,timer1,timer2,timer3,timerHandler);
        initWonDialog();
        initWarningDialog();
        setToolBarHeight(ToolBarHeight);
    }
    protected  void onPause(){
        super.onPause();
        wonDialog.dismiss();


    }
    public static void incrementCorrectMoves(){
        correctMoves++;
    }
    public static void resetCorrectMoves(){
        correctMoves=0;
    }

    /**
     *intializes the smiley icon
     */
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
                        if(GAME_STATE==Constants.GAME_STATE.PLAYING){
                            warningDialog.show();
                        }
                        else {
                            resetCorrectMoves();
                            recreate();
                        }
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    /**
     * updates the image on the smiley.
     * @param id The image to be changed to
     */
    public static void updateSmileyButton(int id){
        mySmileyButton.setBackgroundResource(id);
    }

    /**
     * Initializes the action button
     */
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
    /**
     * initializes the hint button.
     */
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
                else if((int)v.getTag()==1){
                    v.setTag(-1);
                }
            }
        });
    }
    /**
     * Initializes the wrapper class for the counter.
     */
    public void createMineCounter(){
        Button mine0Button= findViewById(R.id.mines0);
        Button mine1Button=findViewById(R.id.mines1);
        Button mine2Button= findViewById(R.id.mine2);
        mineCounter= new MineCounter(GameActivity.difficulty.getMineNum(), mine0Button, mine1Button, mine2Button);
    }
    /**
     * initializes the Timer in the toolbar
     */
    public void createTimer(){
        timer0= findViewById(R.id.timer0);
        timer0.setBackgroundResource(Constants.getClockImage(0));
        timer1= findViewById(R.id.timer1);
        timer1.setBackgroundResource(Constants.getClockImage(0));
        timer2= findViewById(R.id.timer2);
        timer2.setBackgroundResource(Constants.getClockImage(0));
        timer3 = findViewById(R.id.timer3);
        timer3.setBackgroundResource(Constants.getClockImage(0));
    }

    /**
     * Creates all the toolbar buttons.
     */
    public void initToolBarButtons(){
        createSmiley();
        createHintButton();
        createActionButton();
        createMineCounter();
        createTimer();

    }

    /**
     * Dialog box shown when the game is won
     */
    public static void showWonDialog(){
        wonDialog.show();
    }

    /**
     * makes device vibrate
     * @param duration
     */
    public static void clickVibrate(int duration) {
        vibe.vibrate(duration);
    }

    /**
     * initializes the wn dialog box
     */
    public void initWonDialog(){

        AlertDialog.Builder wonBuilder= new AlertDialog.Builder(GameActivity.this);
        final View wonView= getLayoutInflater().inflate(R.layout.dialog_won,null);
        wonBuilder.setView(wonView);
        wonDialog = wonBuilder.create();
    }
    public void initWarningDialog(){
        //Use mview.findViewById rather than just findViewById.
        // Because if we dont specify mview then the widgets will be
        // searched for in activity_main.xml and they are not there.
        AlertDialog.Builder warningBuilder= new AlertDialog.Builder(GameActivity.this);
        final View warningView= getLayoutInflater().inflate(R.layout.dialog_warning,null);
        warningBuilder.setView(warningView);
        warningDialog= warningBuilder.create();
        Button yesButton= warningView.findViewById(R.id.myYesButton);
        yesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GameActivity.resetCorrectMoves();
                warningDialog.dismiss();
                recreate();
            }
        });
        Button noButton= warningView.findViewById(R.id.myNoButton);
        noButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                warningDialog.dismiss();
                updateSmileyButton(Constants.SMILEY_NORMAL);
            }
        });
    }

    /**
     * programmatically adjusts the toolbar size
     * @param height the Tool bar height
     */
    public void setToolBarHeight(int height){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myToolbar.getLayoutParams();
        layoutParams.height = height;
        myToolbar.setLayoutParams(layoutParams);
        myToolbar.requestLayout();
    }

    public static int getActionButtonTag(){
        return (int)myActionButton.getTag();
    }
    public static int getHintButtonTag(){
        return (int) myHintButton.getTag();
    }


}