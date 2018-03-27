package com.example.recinos.myminesweepergame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.recinos.myminesweepergame.Constants.Constants;


/**
 * Created by recinos on 2/11/18.
 */

public class MenuActivity extends AppCompatActivity {
    Constants.GameDifficulty difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button myEasyButton = findViewById(R.id.myEasyButton);
        Button myMediumButton=  findViewById(R.id.myMediumButton);
        Button myHardButton= findViewById(R.id.myHardButton);
        myEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                difficulty=Constants.GameDifficulty.EASY;
                toGame.putExtra("GameDifficulty", difficulty);
                startActivity(toGame);
            }
        });
        myMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                difficulty=Constants.GameDifficulty.MEDIUM;
                toGame.putExtra("GameDifficulty", difficulty);
                startActivity(toGame);
            }
        });
        myHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                difficulty=Constants.GameDifficulty.HARD;
                toGame.putExtra("GameDifficulty", difficulty);
                startActivity(toGame);
            }
        });

    }
}
