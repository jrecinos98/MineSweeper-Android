package com.example.recinos.myminesweepergame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.Views.Grid.Grid;


/**
 * Created by recinos on 2/11/18.
 */

public class MenuActivity extends AppCompatActivity {
    Constants.GAME_DIFFICULTY difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button myEasyButton = findViewById(R.id.myEasyButton);
        Button myMediumButton=  findViewById(R.id.myMediumButton);
        Button myHardButton= findViewById(R.id.myHardButton);
        Button myLoadButton= findViewById(R.id.myLoadButton);
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
}
