package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GamesMenuActivity extends AppCompatActivity {

    float dpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_menu);
        //Creacion de los botones del menu, usando la view de tablero y la densidad como parametros
        dpi = getResources().getDisplayMetrics().density;
        createButtons((LinearLayout) findViewById(R.id.layoutMenu));
    }

    @Override
    //Configuracion pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //movimiento de pantallas
    public void moveMenuHome(View _v){ startActivity(new Intent(this, GamesHomeActivity.class)); }
    public void startGame(View v){ startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", v.getContentDescription().toString())); }

    private LinearLayout.LayoutParams setLayouts(int margins, boolean dir){
        //Layouts con tamaño fijo
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (185f * dpi),(int) (190f * dpi));
        lp.setMargins(dir ? 0 : (int) ((350f - 185f) * dpi ), -margins, !dir ? 0 : (int) ((350f - 185f) * dpi ), 0);
        return lp;
    }

    //Creacion y configuracion de botones
    private ImageButton setButtonConfig(ImageButton game, String gameName, LinearLayout.LayoutParams lp){
        //Parametros generales, descripcion, imagen, centrado, tinbrackground = transparent, layoutParams
        game.setContentDescription(gameName);
        game.setImageResource(getResources().getIdentifier("juego_"+ gameName.toLowerCase(), "drawable", getPackageName()));
        game.setScaleType(ImageView.ScaleType.FIT_CENTER);
        game.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
        //on click -> cambio de pantalla a las instrucciones del juego, parametro de "putExtra" es el nombre del juego
        game.setOnClickListener((View _v) -> startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", gameName)));
        game.setLayoutParams(lp);
        return game;
    }

    //Creacion de los botones de cada juego existente
    private void createButtons(LinearLayout ll){
        //Booleano de cambio de direccion en el boton, un boton izquierda e otro derecha
        boolean dir = true;
        int margins = 0;
        //Ciclo en array de nombre de juegos
        for(String gameName:(new GamesControllerModel()).getAllGames()) {
            //Margenes que depende del bool dir, para izquierda o derecha, top igual a -40% del tamaño * densidad, el primero debe ser 0
            //Se añade un boton al addView, tomado de la funcion de set buttonConfig, pasando el boton como parametro, gameName y layouts, que utilizan margen y direccion como boolean
            ll.addView(setButtonConfig(new ImageButton(this), gameName, setLayouts(margins, dir)));
            margins = (int) (70f * dpi);
            dir = !dir;
        }
    }
}

