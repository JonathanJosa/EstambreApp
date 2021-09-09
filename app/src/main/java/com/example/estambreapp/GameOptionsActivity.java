package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class GameOptionsActivity extends AppCompatActivity {

    String nextGame;
    String lastGame;
    View nullView;
    Handler handlerChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
        handlerChange = new Handler();
        lastGame = (String) getIntent().getStringExtra("game");
        nextGame = (new GamesControllerModel()).getRandomGame(lastGame);
        setContextWindow(nextGame);
        handlerChange.postDelayed(waitingInstance, 5000);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    private void setContextWindow(String gameName){
        ((View) this.getWindow().getDecorView()).setBackgroundColor( Color.parseColor((new GamesControllerModel()).getColorGame(gameName)) );
        ((ImageView) findViewById(R.id.nextGameImage)).setImageResource(getResources().getIdentifier("load_" + gameName.toLowerCase(), "drawable", getPackageName()));
    }

    private void deleteWaitingInstance(){ handlerChange.removeCallbacks(waitingInstance); }

    public void exit(View _v){ deleteWaitingInstance(); startActivity(new Intent(this, GamesHomeActivity.class)); }
    public void replay(View _v) throws ClassNotFoundException { deleteWaitingInstance(); startActivity(new Intent(this, Class.forName("com.example.estambreapp." + lastGame + "Activity"))); }
    public void nextGame(View _v) throws ClassNotFoundException { deleteWaitingInstance(); startActivity(new Intent(this, Class.forName("com.example.estambreapp." + nextGame + "Activity"))); }

    private Runnable waitingInstance = new Runnable() {
        @Override
        public void run() {
            try {
                nextGame(nullView);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };
}