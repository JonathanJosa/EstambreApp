package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class gameInstructions extends AppCompatActivity {

    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_instructions);

        gameName = getIntent().getStringExtra("game");
        ((ImageView) findViewById(R.id.backgroundImage)).setImageResource(getResources().getIdentifier("instructions_"+ gameName, "drawable", getPackageName()));

        ((View) this.getWindow().getDecorView()).setBackgroundColor( Color.parseColor((new gamesController()).getColorGame(gameName)) );

        ifNotDeclaredDifficult();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    private void ifNotDeclaredDifficult(){  }

    public void startGame(View _v) throws ClassNotFoundException { startActivity(new Intent(this, Class.forName("com.example.estambreapp." + gameName))); }

    public void moveHomeGames(View _v){ startActivity(new Intent(this, gamesHome.class)); }

}