package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class BlackSheepActivity extends AppCompatActivity {

    GamesModel gameProperties; //Global declare, required for timeCount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_sheep);
        gameProperties = new GamesModel(this, "BlackSheep"); //Required, constructor of model
        //gameProperties.getDifficulty(); //Return double
        //gameProperties.startTimeCount();
        //gameProperties.endGame();
        //gameProperties.penalty(double);

        moveEndGame();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    private void moveEndGame(){ startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","BlackSheep")); }

}