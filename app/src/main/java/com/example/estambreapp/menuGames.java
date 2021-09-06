package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;

public class menuGames extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_games);
        createButtons((new gamesController()).getAllGames());
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused)
            (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    public void moveMenuHome(View _v){ startActivity(new Intent(this, gamesHome.class)); }
    public void startGame(View v){ }

    private void createButtons(String[] games){
        for(String gameName:games){
            System.out.println((String) gameName);
            //Crear botones
        }
    }
}