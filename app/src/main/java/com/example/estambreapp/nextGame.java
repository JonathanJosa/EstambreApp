package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class nextGame extends AppCompatActivity {

    String nextGame;
    String lastGame;
    Intent changeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_game);
        lastGame = (String) getIntent().getStringExtra("game");
        nextGame = (new gamesController()).getRandomGame(lastGame);
        setContextWindow(nextGame);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    private void setContextWindow(String gameName){
        ((View) this.getWindow().getDecorView()).setBackgroundColor( Color.parseColor((new gamesController()).getColorGame(gameName)) );
        ((ImageView) findViewById(R.id.nextGameImage)).setImageResource(getResources().getIdentifier("load_" + gameName.toLowerCase(), "drawable", getPackageName()));
    }

    public void exit(View _v){ startActivity(new Intent(this, gamesHome.class)); }
    public void replay(View _v) throws ClassNotFoundException { startActivity(new Intent(this, Class.forName("com.example.estambreapp." + lastGame))); }
    public void nextGame(View _v) throws ClassNotFoundException { startActivity(new Intent(this, Class.forName("com.example.estambreapp." + nextGame))); }

    private Runnable waitingInstance = new Runnable() {
        @Override
        public void run() {
            startActivity(changeScreen);
        }
    };
}