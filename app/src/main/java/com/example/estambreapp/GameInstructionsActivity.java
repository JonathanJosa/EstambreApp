package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GameInstructionsActivity extends AppCompatActivity {

    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_instructions);

        //Obtener parametro del juego que se desean obtener instrucciones
        gameName = getIntent().getStringExtra("game");
        //Crear objeto temporal de la view de imagen y colocar la correspondiente al juego, logo e instrucciones
        ((ImageView) findViewById(R.id.backgroundImage)).setImageResource(getResources().getIdentifier("instructions_"+ gameName.toLowerCase(), "drawable", getPackageName()));
        //Establecer color de fondo desde el mismo color del juego
        ((View) this.getWindow().getDecorView()).setBackgroundColor(Color.parseColor((new GamesControllerModel()).getColorGame(gameName)));
    }

    @Override
    //Configuraciones de pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //Boton de iniciar juego ejecuta esta funcion, un intent a el activity de la variable gameName, una busqueda dentro del layout para mover a esa activity
    public void startGame(View _v) throws ClassNotFoundException { startActivity(new Intent(this, Class.forName("com.example.estambreapp." + gameName + "Activity"))); }
    //Regresar al home
    public void moveHomeGames(View _v){ startActivity(new Intent(this, GamesHomeActivity.class)); }
}