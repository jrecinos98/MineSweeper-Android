package com.game.recinos.myminesweepergame.CustomViews.Toolbar;

import android.os.Handler;
import android.widget.Button;

import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.GameActivity;

/**
 * Runnable class that instructs the handler how to display the time.
 */
public class GameClock implements Runnable{
    private int button3Time=0;
    private int button2Time=0;
    private int button1Time=0;
    private int button0Time=0;
    private Button timer0;
    private Button timer1;
    private Button timer2;
    private Button timer3;
    private Handler timerHandler;
   public GameClock(Button timer0, Button timer1, Button timer2, Button timer3, Handler timerHandler){
       this.timer0=timer0;
       this.timer1=timer1;
       this.timer2=timer2;
       this.timer3=timer3;
       this.timerHandler=timerHandler;
       if(GameActivity.time != 0 && GameActivity.time <3600){
           int minutes= GameActivity.time/60;
           int seconds= GameActivity.time-minutes;
           button0Time= minutes/10;
           button1Time= (minutes-button0Time*10);
           button2Time= (seconds/10);
           button3Time= (seconds-button2Time*10);
           timer3.setBackgroundResource(Constants.getClockImage(button3Time));
           timer2.setBackgroundResource(Constants.getClockImage(button2Time));
           timer1.setBackgroundResource(Constants.getClockImage(button1Time));
           timer0.setBackgroundResource(Constants.getClockImage(button0Time));
       }
   }
    @Override
    public void run() {
        if(GameActivity.GAME_STATE== Constants.GAME_STATE.PLAYING) {
            GameActivity.time++;
            button3Time++;
            if (button3Time >= 10) {
                button2Time++;
                button3Time = 0;
            }
            if (button2Time >= 6) {
                button2Time = 0;
                button1Time++;
            }
            if (button1Time >= 10) {
                button1Time = 0;
                button0Time++;
            }
            timer3.setBackgroundResource(Constants.getClockImage(button3Time));
            timer2.setBackgroundResource(Constants.getClockImage(button2Time));
            timer1.setBackgroundResource(Constants.getClockImage(button1Time));
            timer0.setBackgroundResource(Constants.getClockImage(button0Time));
            timerHandler.postDelayed(this, 1000);
        }
    }
}
