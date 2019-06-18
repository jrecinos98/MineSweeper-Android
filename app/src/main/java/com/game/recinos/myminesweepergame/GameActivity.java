package com.game.recinos.myminesweepergame;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.Listeners.ButtonListeners;
import com.game.recinos.myminesweepergame.Listeners.GridListeners;
import com.game.recinos.myminesweepergame.Grid.Grid;
import com.game.recinos.myminesweepergame.CustomViews.Toolbar.MineCounter;
import com.game.recinos.myminesweepergame.CustomViews.Toolbar.GameClock;
import com.game.recinos.myminesweepergame.Adapters.GridAdapters;
import com.game.recinos.myminesweepergame.util.Util;

import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


@SuppressWarnings("ClickableViewAccessibility")
public class GameActivity extends AppCompatActivity {
    public Constants.GAME_DIFFICULTY difficulty;
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


    public volatile static int time=0;
    public static int actionTag;
    public static int hintTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GAME_STATE= Constants.GAME_STATE.NOT_STARTED;
        int ToolBarHeight= Integer.parseInt(getIntent().getStringExtra("TOOLBAR_HEIGHT"));
        vibe= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setContentView(R.layout.activity_main);
        //Reason why toolbar is not increasing right
        setToolBarHeight(ToolBarHeight);
        initToolBarButtons();

        //Will occur when user was playing and went idle. Make sure the game wasn't over. If it did start a new one
        if(savedInstanceState != null && savedInstanceState.getSerializable("GameGrid") != null){
            GameActivity.GAME_STATE= Constants.GAME_STATE.RELOADED;
            GameActivity.time= savedInstanceState.getInt("Time");
            difficulty= (Constants.GAME_DIFFICULTY) savedInstanceState.getSerializable("Difficulty");
            //Difficulty must be defined before MineCounter created.
            createMineCounter();
            createTimer();
            reloadGame((Grid) savedInstanceState.getParcelable("GameGrid"));

            Toast.makeText(getApplicationContext(),"Game Has Been Reloaded", Toast.LENGTH_LONG).show();
        }
        else {
            Grid loaded= (Grid)getIntent().getExtras().getSerializable("GAME_GRID");

            //If grid is null then the game wasn't read from a file
            if(loaded == null){
                difficulty = (Constants.GAME_DIFFICULTY) getIntent().getSerializableExtra("GAME_DIFFICULTY");
                //If it is null it means that it didn't come from an intent but rather the game is reloaded from bundle.
                //Will only happen when user loses and leaves app. We want to reload a new game at that point with the same difficulty
                if (difficulty == null){
                    difficulty= (Constants.GAME_DIFFICULTY) savedInstanceState.getSerializable("Difficulty");
                    time=0;
                }
                createMineCounter();
                createTimer();
                setUpNewGrid();
            }
            else{
                //Here means that the game was loaded from a file so we reload everything
                Log.d("Reload", "Game has been reloaded");
                Bundle reloaded= getIntent().getExtras();
                GameActivity.GAME_STATE= Constants.GAME_STATE.RELOADED;
                GameActivity.time=  (Integer) reloaded.getInt("TIME");
                difficulty = (Constants.GAME_DIFFICULTY) getIntent().getSerializableExtra("GAME_DIFFICULTY");
                createMineCounter();
                createTimer();
                reloadGame(loaded);
            }

        }
        initWonDialog();
        initWarningDialog();

    }

    /**
     * When the application goes idle we save the grid to restore later.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(GAME_STATE != Constants.GAME_STATE.LOST){
            //User didnt lose so restore previous game
            Util.saveToBundle(outState, gameGrid, difficulty, time);
            super.onSaveInstanceState(outState);
        }
        else{
            //User lost. Reload grid with the same difficulty
            outState.putSerializable("Difficulty", difficulty);
        }
    }
    protected  void onPause(){
        super.onPause();
        if(GAME_STATE== Constants.GAME_STATE.PLAYING) {
            GAME_STATE = Constants.GAME_STATE.PAUSED;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(GAME_STATE==Constants.GAME_STATE.PAUSED) {
            GAME_STATE = Constants.GAME_STATE.RELOADED;
            //timerHandler.postDelayed(gameTimer,0);
        }

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //If game was lost don't save it
        if(GAME_STATE != Constants.GAME_STATE.LOST) {
            Bundle bundle= new Bundle();

            //Put all objects to be saved into an Object array
            Object items[]= new Object[2];
            items[0]= gameGrid;
            items[1]=time;

            Util.save(getApplicationContext(), Constants.SAVE_NAME, items);
            GameActivity.time = 0;
            Log.d("Save", String.valueOf(Util.saveExist(Constants.SAVE_NAME, fileList())));
        }
        else{
            //User lost so delete previous save file
            Util.deleteSave(getApplicationContext(),Constants.SAVE_NAME);
        }
        //resetGame();
    }
    /**
     * resets game
     */
    public void resetGame(){
        GAME_STATE= Constants.GAME_STATE.NOT_STARTED;
        GameActivity.time=0;
        initToolBarButtons();
        createMineCounter();
        createTimer();
        setUpNewGrid();
    }
    /**
     * Creates all the toolbar buttons.
     */
    public void initToolBarButtons(){
        createSmiley();
        createHintButton();
        createActionButton();

    }
    /**
     * Inflates the gameGrid and adds the adapter and sets up the grid listener.
     */
    public void setUpNewGrid() {
        gameGrid = new Grid(difficulty);
        setGridAdapter();
        setUpGridListeners();
    }

    /**
     * when an activity goes idle we restore the grid.
     * @param saved
     */
    public void reloadGame(Grid saved){
        gameGrid= saved;
        setGridAdapter();
        setUpGridListeners();
    }

    /**
     * Sets up the GridListeners for the grid.
     */
    private void setUpGridListeners(){
        final GridView myMinesweeperGrid = findViewById(R.id.myMinesweeperGrid);
        myMinesweeperGrid.setNumColumns(gameGrid.getDifficulty().getWidth());
        myMinesweeperGrid.setAdapter(myAdapter);
        myMinesweeperGrid.setOnItemClickListener(new GridListeners.GridOnItemClickListener(gameGrid,mySmileyButton,mineCounter,gameTimer,myAdapter));
        myMinesweeperGrid.setOnItemLongClickListener(new GridListeners.GridOnItemLongClickListener(gameGrid,mineCounter,myAdapter));
        myMinesweeperGrid.setOnTouchListener(new GridListeners.GridOnTouchListener(gameGrid,getApplicationContext(),mySmileyButton, myAdapter,myMinesweeperGrid));
    }

    /**
     * initializes GridAdapter.
     */
    private void setGridAdapter(){
        myAdapter= new GridAdapters.GameGridAdapter(gameGrid.getCells(),getApplicationContext());
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
                        else if (GAME_STATE !=Constants.GAME_STATE.NOT_STARTED) {
                            resetGame();
                        }
                        else{
                            v.setBackgroundResource(Constants.SMILEY_NORMAL);
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
        mineCounter= new MineCounter(difficulty.getMineNum(), mine0Button, mine1Button, mine2Button);
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
     * initializes the won dialog box
     */
    public void initWonDialog(){
        AlertDialog.Builder wonBuilder= new AlertDialog.Builder(GameActivity.this);
        final View wonView= getLayoutInflater().inflate(R.layout.dialog_won,null);
        wonBuilder.setView(wonView);
        wonDialog = wonBuilder.create();
    }

    /**
     * warns the user when they want to reset a started game.
     */
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
                warningDialog.dismiss();
                resetGame();
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