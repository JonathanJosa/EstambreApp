package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MazeCoinsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_coins);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}