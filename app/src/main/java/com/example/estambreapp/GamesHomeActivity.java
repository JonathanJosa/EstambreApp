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
    //configuracion de pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //Botones de cambio de pantalla
    public void moveHome(View _v){ startActivity(new Intent(this, HomeActivity.class)); }
    public void moveMenuGames(View _v){ startActivity(new Intent(this, GamesMenuActivity.class)); }

    //Iniciar un juego random, se mueve a las instrucciones del juego, pasando como "putExtra" el nombre del juego deseado, obtenido por un random del controlador del los juegos
    public void startGames(View _v) { startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", (String) (new GamesControllerModel()).getRandomGame(" "))); }
}