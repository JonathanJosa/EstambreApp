package com.example.estambreapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BlackSheepActivity extends AppCompatActivity{

    BlackSheepModel sheepModel;
    RelativeLayout board;
    TextView instruccion;
    int rounds = 3;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_sheep);
        //Views utilizadas
        board = findViewById(R.id.LinearLayoutBoard);
        instruccion = findViewById(R.id.restante);
        //modelo
        sheepModel = new BlackSheepModel(this);
        createBoard();
    }


    @Override
    //Configuracion para pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //Next round cuando se hayan clickeado todas las ovejas negras
    private void nextRound(){
        //Cambio de instruccion
        instruccion.setText("Bien hecho!!!");
        //Finalizar juego y conteo para dificultad automatica
        sheepModel.started = false;
        sheepModel.playing();
        //Reduccion de un round, toal de 3
        rounds -= 1;
        Toast.makeText(this, "Ganaste Round!!", Toast.LENGTH_SHORT).show();
        //Eliminar los botones restantes del tablero
        board.removeAllViews();
        //Si ya ha terminado los rounds se cambia de pantalla con delay de 1000 y rompe la funcion nextRound
        if(rounds == 0){
            (new Handler()).postDelayed(() -> startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","BlackSheep")), 1000);
            return;
        }
        //Recreacion del tablero y reinicio de variables
        sheepModel.settingGame();
        (new Handler()).postDelayed(this::createBoard, 1000);
    }

    //Configuracion de los botones de ovejas
    private ImageButton setButtonsSettings(ImageButton btn, String btnType){
        //Descripcion del tipo de obveja
        btn.setContentDescription(btnType);
        //Establecer imagen(oveja blanca o negra)
        btn.setImageResource(getResources().getIdentifier("blacksheep_"+btnType, "drawable", getPackageName()));
        //Centrar
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //Sin tint de fondo
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
        //On click -> ejecutar sheepClicked del modelo, si retorna un true, cambio de ronda, false -> sigue jugando
        btn.setOnClickListener((View v) -> {if(sheepModel.sheepClicked(v, btnType)) nextRound();});
        //Layouts con margenes aleatorios y tamaños ajustado
        btn.setLayoutParams(sheepModel.randomLayouts());
        return btn;
    }

    //Creacion del tablero
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createBoard(){
        //TextView muestra mensaje de mezclando
        instruccion.setText("Mezclando...");
        //Ciclo de creacion en rango de numero de ovejas
        //ejecucion de funcion setButtonSettings con un boton recien creado y el tipo de oveja (blanco o negro)
        for(int i=0; i<sheepModel.whiteSheeps; i++){ board.addView(setButtonsSettings(new ImageButton(this), "sheepw")); }
        for(int i=0; i<sheepModel.blackSheeps; i++){ board.addView(setButtonsSettings(new ImageButton(this), "sheepb")); }
        //Una vez creadas los botones de obejas, se mandan al modelo para que decida las transiciones que tendran en X, Y
        for(View btn: board.getTouchables()){ sheepModel.mixSheeps(btn, sheepModel.totalTime); }
        //Ejecucion asincrona al terminar los movimientos de todos los botones
        (new Handler()).postDelayed(() -> {
            //Establecer inicio del juego
            sheepModel.started = true;
            //Iniciar conteo para dificultad automatica
            sheepModel.playing();
            //Cambiar instruccion
            instruccion.setText("Encuentra la oveja negra!!");
        }, (long) sheepModel.velocity * sheepModel.totalTime);
    }

}