package com.game.recinos.myminesweepergame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;


import com.game.recinos.myminesweepergame.Adapters.GridAdapters;
import com.game.recinos.myminesweepergame.Constants.Constants;
import com.game.recinos.myminesweepergame.Listeners.ButtonListeners;
import com.game.recinos.myminesweepergame.Listeners.SlidersListeners;


/**
 * Created by recinos on 2/11/18.
 */

public class MenuActivity extends AppCompatActivity {
    AlertDialog myCustomDialog;
    Button myLoadButton;
    Button myCustomButton;
    Button mySettingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.game.recinos.myminesweepergame.R.layout.activity_menu);
        initButtons();
        setUpListeners();
    }

    public void initButtons(){
        myLoadButton= findViewById(com.game.recinos.myminesweepergame.R.id.myLoadButton);
        myCustomButton=findViewById(com.game.recinos.myminesweepergame.R.id.myCustomButton);
        createSettingsButton();
    }
    public void createSettingsButton(){
        mySettingsButton= findViewById(com.game.recinos.myminesweepergame.R.id.mySettingsButton);
        mySettingsButton.setBackgroundResource(Constants.SETTING);
    }
    public void setUpListeners(){
        mySettingsButton.setOnClickListener(new ButtonListeners.SettingsListener(getApplicationContext()));
        myLoadButton.setOnClickListener(new ButtonListeners.LoadListener(getApplicationContext()));
        //Creates the Dialog box (Not visible initially)
        createSizeCustom();
        myCustomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myCustomDialog.show();

                //Not sure if to delete, ask for confirmation or simply let it be replaced if user starts new game
                //Util.deleteSave(getApplicationContext(), "GameSave.ser");
            }
        });
    }
    //Create the one that asks about columns and rows.
    public void createCustom(){
        android.app.AlertDialog.Builder wonBuilder= new android.app.AlertDialog.Builder(MenuActivity.this);
        final View wonView= getLayoutInflater().inflate(com.game.recinos.myminesweepergame.R.layout.dialog_custom,null);
        wonBuilder.setView(wonView);
        Button myGoButton= wonView.findViewById(com.game.recinos.myminesweepergame.R.id.customGo);
        myCustomDialog=wonBuilder.create();
        myCustomDialog.setCanceledOnTouchOutside(false);
        myGoButton.setOnClickListener(new ButtonListeners.CustomGoListener(getApplicationContext(),wonView,myCustomDialog));
    }

    //Creates the slider Dialog
    public void createSizeCustom(){
        android.app.AlertDialog.Builder sizeBuilder= new android.app.AlertDialog.Builder(MenuActivity.this);
        final View sizeView= getLayoutInflater().inflate(com.game.recinos.myminesweepergame.R.layout.dialog_size,null);
        sizeBuilder.setView(sizeView);
        final GridView sizeGrid= sizeView.findViewById(com.game.recinos.myminesweepergame.R.id.mySizeGrid);
        final SeekBar sliderBar= sizeView.findViewById(com.game.recinos.myminesweepergame.R.id.mySeekBar);
        final SeekBar mineBar= sizeView.findViewById(com.game.recinos.myminesweepergame.R.id.seekBar2);
        final Button myPlayButton= sizeView.findViewById(com.game.recinos.myminesweepergame.R.id.customStartButton);
        Constants.GAME_DIFFICULTY customDifficulty=Constants.GAME_DIFFICULTY.EASY;
        Constants.DifficultyWrap difficultyWrap= new Constants.DifficultyWrap(customDifficulty);
        myCustomDialog= sizeBuilder.create();
        myCustomDialog.setCanceledOnTouchOutside(false);
        myCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sizeGrid.setAdapter(new GridAdapters.SliderGrid(getApplicationContext()));
        sliderBar.setOnSeekBarChangeListener(new SlidersListeners.GridSeekBarListener(sizeGrid));
        myPlayButton.setOnClickListener(new ButtonListeners.SliderCustomButton(getApplicationContext(),getWindowManager().getDefaultDisplay(),sizeGrid,difficultyWrap,myCustomDialog));
        mineBar.setOnSeekBarChangeListener(new SlidersListeners.MineSliderListener(difficultyWrap));
    }


}
