package com.example.recinos.myminesweepergame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recinos.myminesweepergame.Constants.Constants;
import com.example.recinos.myminesweepergame.Views.Grid.Grid;


/**
 * Created by recinos on 2/11/18.
 */

public class MenuActivity extends AppCompatActivity {
    Constants.GAME_DIFFICULTY difficulty;
    AlertDialog myCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button myEasyButton = findViewById(R.id.myEasyButton);
        Button myMediumButton=  findViewById(R.id.myMediumButton);
        Button myHardButton= findViewById(R.id.myHardButton);
        Button myLoadButton= findViewById(R.id.myLoadButton);
        Button myCustomButton=findViewById(R.id.myCustomButton);
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
        myCustomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createCustom().show();
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
    public AlertDialog createCustom(){
        android.app.AlertDialog.Builder wonBuilder= new android.app.AlertDialog.Builder(MenuActivity.this);
        final View wonView= getLayoutInflater().inflate(R.layout.dialog_custom,null);
        wonBuilder.setView(wonView);
        Button myGoButton= wonView.findViewById(R.id.customGo);
        myCustomDialog=wonBuilder.create();
        myGoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText myColText= wonView.findViewById(R.id.myColText);
                EditText myRowText= wonView.findViewById(R.id.myRowText);
                EditText myMinetext= wonView.findViewById(R.id.myMineText);
                if(myRowText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter the Number Of Rows", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(myColText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter the Number Of Columns", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(myMinetext.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter the Number Of Mines", Toast.LENGTH_SHORT).show();
                    return;
                }
                int rowNum=Integer.parseInt(myColText.getText().toString());
                int colNum=Integer.parseInt(myRowText.getText().toString());
                int mineNum= Integer.parseInt(myMinetext.getText().toString());
                if(colNum>25){
                    colNum=25;
                }

                difficulty= Constants.GAME_DIFFICULTY.CUSTOM;
                difficulty.setEnumWidth(colNum);
                difficulty.setEnumHeight(rowNum);
                difficulty.setMineNum(mineNum);
                Intent toGame= new Intent(getApplicationContext(),GameActivity.class);
                toGame.putExtra("GAME_DIFFICULTY", difficulty);
                myCustomDialog.dismiss();
                startActivity(toGame);
            }
        });

        return myCustomDialog;

    }
}
