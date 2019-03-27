package com.game.recinos.myminesweepergame.CustomViews.Toolbar;

import android.widget.Button;

import com.game.recinos.myminesweepergame.Constants.Constants;


/**
 * wrapper class for the mine counter.
 */
public class MineCounter {
        private Button mine0Button;
        private Button mine1Button;
        private Button mine2Button;
        public int mineNum;
        public MineCounter(int mineNum, Button mine0, Button mine1, Button mine2){
            this.mineNum=mineNum;
            //determines value at 100th place
            int hundred= mineNum/100;
            //determines value at 10th place
            int tenth=(mineNum-(hundred*100))/10;
            //determines value at 1st place
            int digit= (mineNum-(hundred*100)-(tenth*10));
            mine0Button=mine0;
            mine0Button.setBackgroundResource(Constants.getClockImage(hundred));

            mine1Button=mine1;
            mine1Button.setBackgroundResource(Constants.getClockImage(tenth));

            mine2Button=mine2;
            mine2Button.setBackgroundResource(Constants.getClockImage(digit));
        }
        public void setMineCounter(int newMineNum){
            mineNum=newMineNum;
            //determines value at 100th place
            int hundred= newMineNum/100;
            //determines value at 10th place
            int tenth=(newMineNum-(hundred*100))/10;
            //determines value at 1st place
            int digit= (newMineNum-(hundred*100)-(tenth*10));
            mine0Button.setBackgroundResource(Constants.getClockImage(hundred));

            mine1Button.setBackgroundResource(Constants.getClockImage(tenth));

            mine2Button.setBackgroundResource(Constants.getClockImage(digit));
        }

}
