package com.example.recinos.myminesweepergame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.recinos.myminesweepergame.Constants.Constants;


/**
 * Created by recinos on 2/11/18.
 */

public class MenuActivity extends AppCompatActivity {
    Constants.GameDifficulty difficulty= Constants.GameDifficulty.EASY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button playButton= (Button) findViewById(R.id.myPlayButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                toGame.putExtra("GameDifficulty", difficulty);
                startActivity(toGame);
                finish();
            }
        });


    }
}
