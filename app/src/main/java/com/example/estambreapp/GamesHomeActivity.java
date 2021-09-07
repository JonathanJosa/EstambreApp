package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GamesHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_home);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    public void moveHome(View _v){ startActivity(new Intent(this, HomeActivity.class)); }
    public void moveMenuGames(View _v){ startActivity(new Intent(this, GamesMenuActivity.class)); }

    public void startGames(View _v) throws ClassNotFoundException {
        startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", (String) (new GamesControllerModel()).getRandomGame(" ")));
    }
}