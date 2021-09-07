package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class BlackSheepActivity extends AppCompatActivity {

    GamesModel gameProperties = new GamesModel("BlackSheep");
    //Declarar metodo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_sheep);
        //gameProperties.getDifficulty(); obtener dificultad
        //gameProperties.startTimeCount(); Iniciar conteo de tiempo de reaccion
        //gameProperties.endGame(); Terminar conteo de tiempo/Juego terminado o ganado
        //gameProperties.penalty(1.0); Penalty de puntaje
        endGame();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    private void endGame(){
        //moreContent
        startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","BlackSheep"));
    }

}