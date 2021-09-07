package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Properties;

public class ovejaNegra extends AppCompatActivity {

    propertiesGames gameProperties = new propertiesGames("ovejaNegra");
    //Declarar metodo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oveja_negra);
        //gameProperties.getDifficulty(); obtener dificultad
        //gameProperties.startTimeCount(); Iniciar conteo de tiempo de reaccion
        //gameProperties.endGame(); Terminar conteo de tiempo/Juego terminado o ganado
        endGame();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    private void endGame(){
        //moreContent
        startActivity(new Intent(this, nextGame.class).putExtra("game","ovejaNegra"));
    }

}