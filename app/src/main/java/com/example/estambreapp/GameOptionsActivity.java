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
        //Creacion del handler que cambiará en 5 segs la pantalla
        handlerChange = new Handler();
        //Juego del que se llega y siguiente juego aleatorio evitando repetir.
        lastGame = (String) getIntent().getStringExtra("game");
        nextGame = (new GamesControllerModel()).getRandomGame(lastGame);
        //Establecer configuracion de pantalla
        setContextWindow(nextGame);
        //Asincrono, si no elige en 5 seg, se hace el cambio automatico de pantalla, deleteWaitingInstance cancela el cambio automatico
        handlerChange.postDelayed(waitingInstance, 5000);
    }

    @Override
    //Configuracion de pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //Establecer color de fondo, forzando color definido para ln44
    private void setContextWindow(String gameName){
        String color = (new GamesControllerModel()).getColorGame(gameName);
        color = "#2F3136".equals(color) ? "#A0CED9" : color;
        ((View) this.getWindow().getDecorView()).setBackgroundColor( Color.parseColor(color));
        //Se coloca imagen del recurso (logo del siguiente juego), utiliza un objeto temporal
        ((ImageView) findViewById(R.id.nextGameImage)).setImageResource(getResources().getIdentifier("load_" + gameName.toLowerCase(), "drawable", getPackageName()));
    }

    //Usando el handler declarado y el waitingInstance, se elimina la tarea asincrona
    private void deleteWaitingInstance(){ handlerChange.removeCallbacks(waitingInstance); }

    //Cambio de pantalla, funciones ejecutadas por botones, eliminan la tarea asincrona existente y realizan el cambio de activity solicitado
    public void exit(View _v){ deleteWaitingInstance(); startActivity(new Intent(this, GamesHomeActivity.class)); }
    //Utilizan el nombre del juego para buscar la clase y realizar el cambio de pantalla
    public void replay(View _v) throws ClassNotFoundException { deleteWaitingInstance(); startActivity(new Intent(this, Class.forName("com.example.estambreapp." + lastGame + "Activity"))); }
    public void nextGame(View _v) throws ClassNotFoundException { deleteWaitingInstance(); startActivity((new Intent(this, GameInstructionsActivity.class)).putExtra("game", nextGame)); }

    //Ejecucion asincrono que de no ser detenida, hace el cambio de juego
    private final Runnable waitingInstance = new Runnable() {
        @Override
        public void run() {
            //Catch por si no es valido hacer el cambio a esa pantalla
            try {
                //nullView debido a que son funciones de botones, envian como parametro su view, nullView es una view sin valores declarados
                nextGame(nullView);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

}