package com.example.recinos.myminesweepergame;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button myResetButton = (Button) findViewById(R.id.myResetButton);
        final Button myMainMenuButton= (Button) findViewById(R.id.myMainMenuButton);

        //Use mview.findViewById rather than just findViewById.
        // Because if we dont specify mview then the widgets will be
        // searched for in activity_main.xml and they are not there.
        AlertDialog.Builder mBuilder= new AlertDialog.Builder(MainActivity.this);
        final View mView= getLayoutInflater().inflate(R.layout.dialog_custom,null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        myResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recreate(); This method restarts an Activity.
                dialog.show();
                Button noButton= (Button)mView.findViewById(R.id.myNoButton);
                Button yesButton= (Button) mView.findViewById(R.id.myYesButton);
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameEngine.getInstance().resetGame();
                        dialog.dismiss();

                    }
                });


            }
        });
        myMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent mIntent= new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(mIntent);
            }

        });


    }

}