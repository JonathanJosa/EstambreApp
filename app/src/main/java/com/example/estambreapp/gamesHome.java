package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class gamesHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_home);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused)
            (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    public void moveHome(View _v){ startActivity(new Intent(this, home.class)); }
    public void moveMenuGames(View _v){ startActivity(new Intent(this, menuGames.class)); }

    public void startGames(View _v) throws ClassNotFoundException {
        startActivity(new Intent(this, Class.forName("com.example.estambreapp." + ((String) (new gamesController()).getRandomGame(" ")))));
    }
}