package com.game.recinos.myminesweepergame;
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
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.Listeners.ButtonListeners;
import com.game.recinos.myminesweepergame.Listeners.GridListeners;
import com.game.recinos.myminesweepergame.Views.Grid.Cell;
import com.game.recinos.myminesweepergame.Views.Grid.Grid;
import com.game.recinos.myminesweepergame.Views.Toolbar.MineCounter;
import com.game.recinos.myminesweepergame.Views.Toolbar.GameClock;
import com.game.recinos.myminesweepergame.Adapters.GridAdapters;



@SuppressWarnings("ClickableViewAccessibility")
public class GameActivity extends AppCompatActivity {
    public static Constants.GAME_DIFFICULTY difficulty;
    public volatile static Constants.GAME_STATE GAME_STATE;

    private static AlertDialog wonDialog;
    private AlertDialog warningDialog;


    public static Vibrator vibe;


    private Button mySmileyButton;
    public GameClock gameTimer;
    public MineCounter mineCounter;
    public static Handler timerHandler = new Handler();

    private Grid gameGrid;
    private GridAdapters.GameGridAdapter myAdapter;

    public static int correctMoves=0;
    public volatile static int time=0;
    public static int actionTag;
    public static int hintTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        difficulty = (Constants.GAME_DIFFICULTY) getIntent().getSerializableExtra("GAME_DIFFICULTY");
        GAME_STATE= Constants.GAME_STATE.NOT_STARTED;
        int ToolBarHeight= difficulty.getToolBarHeight();
        vibe= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setContentView(R.layout.activity_main);
        setToolBarHeight(ToolBarHeight);
        initToolBarButtons();
        setUpGrid();
        initWonDialog();
        initWarningDialog();
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
     * Inflates the gameGrid and adds the adapter and sets up the grid listener.
     */
    public void setUpGrid() {
        gameGrid= new Grid(difficulty,getApplicationContext());
        myAdapter= new GridAdapters.GameGridAdapter(gameGrid.getCells(),getApplicationContext());
        final GridView myMinesweeperGrid = findViewById(R.id.myMinesweeperGrid);
        myMinesweeperGrid.setNumColumns(difficulty.getWidth());
        myMinesweeperGrid.setAdapter(myAdapter);
        myMinesweeperGrid.setOnItemClickListener(new GridListeners.GridOnItemClickListener(gameGrid,mySmileyButton,mineCounter,gameTimer,myAdapter));
        myMinesweeperGrid.setOnItemLongClickListener(new GridListeners.GridOnItemLongClickListener(gameGrid,mineCounter,myAdapter));
        myMinesweeperGrid.setOnTouchListener(new GridListeners.GridOnTouchListener(gameGrid,getApplicationContext(),mySmileyButton, myAdapter,myMinesweeperGrid));
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
                        v.setBackgroundResource(Constants.SMILEY_RESET);
                        return false; // if you want to handle the touch event return true
                    case MotionEvent.ACTION_UP:
                        if(GAME_STATE==Constants.GAME_STATE.PLAYING){
                            warningDialog.show();}
                        else {
                            resetCorrectMoves();
                            recreate();
                        }
                }
                return false;}});
    }
    /**
     * updates the image on the smiley.
     * @param id The image to be changed to
     */
    public void updateSmileyButton(int id){
        mySmileyButton.setBackgroundResource(id);
    }
    /**
     * Initializes the action button
     */
    private void createActionButton(){
        Button myActionButton= findViewById(R.id.myActionButton);
        myActionButton.setBackgroundResource(Constants.TOOLBAR_MINE);
        myActionButton.setTag(Constants.TOOLBAR_MINE);
        GameActivity.actionTag=Constants.TOOLBAR_MINE;
        myActionButton.setOnClickListener(new ButtonListeners.ActionButtonListener());
    }
    /**
     * initializes the hint button.
     */
    public void createHintButton(){
        Button myHintButton= findViewById(R.id.myHintButton);
        myHintButton.setBackgroundResource(Constants.TOOLBAR_HINT);
        myHintButton.setTag(-1);
        GameActivity.hintTag=-1;
        myHintButton.setOnClickListener(new ButtonListeners.HintButtonListener());
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
        Button timer0= findViewById(R.id.timer0);
        timer0.setBackgroundResource(Constants.getClockImage(0));
        Button timer1= findViewById(R.id.timer1);
        timer1.setBackgroundResource(Constants.getClockImage(0));
        Button timer2= findViewById(R.id.timer2);
        timer2.setBackgroundResource(Constants.getClockImage(0));
        Button timer3 = findViewById(R.id.timer3);
        timer3.setBackgroundResource(Constants.getClockImage(0));
        gameTimer= new GameClock(timer0,timer1,timer2,timer3,timerHandler);
    }
    /**
     * Dialog box shown when the game is won
     */
    public static void showWonDialog(){
        wonDialog.show();
    }
    /**
     * makes device vibrate
     * @param duration duration of the vibration
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
        warningDialog.setCanceledOnTouchOutside(false);
        final Button yesButton= warningView.findViewById(R.id.myYesButton);
        yesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clickVibrate(1);
                GameActivity.resetCorrectMoves();
                warningDialog.dismiss();
                recreate();
            }
        });
        Button noButton= warningView.findViewById(R.id.myNoButton);
        noButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clickVibrate(1);
                warningDialog.dismiss();
                updateSmileyButton(Constants.SMILEY_NORMAL);
            }
        });
        yesButton.setOnTouchListener(new ButtonListeners.WarningButtonsListener(yesButton));
        noButton.setOnTouchListener(new ButtonListeners.WarningButtonsListener(noButton));
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
        return actionTag;
    }
    public static int getHintButtonTag(){
        return hintTag;
    }


}