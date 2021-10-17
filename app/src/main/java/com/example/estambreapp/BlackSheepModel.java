package com.example.estambreapp;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class BlackSheepModel {

    GamesModel gameProperties;
    Double difficulty;
    Context context;
    Float dpi;
    Boolean started = false;
    Integer whiteSheeps;
    Integer blackSheeps;
    Integer totalTime;
    Integer velocity;
    Float sheepSize;


    //Atributo context pasado desde activity
    public BlackSheepModel(Context ctx){
        context = ctx;
        //Declaracion de la densidad de pantalla
        dpi = context.getResources().getDisplayMetrics().density;
        //Modelo de juegos para la dificultad ajustada
        gameProperties = new GamesModel(ctx, "BlackSheep");
        //Establecer las variables usadas en el juego
        settingGame();
    }

    public void settingGame(){
        //Dificultad ajustada del ultimo juego
        difficulty = gameProperties.getDifficulty();
        System.out.println(difficulty);
        //Cantidad de ovejas que tendra el juego
        whiteSheeps = getWhiteSheeps();
        blackSheeps = getBlackSheeps();
        //Cantidad de giros que podran dar las ovejas y velocidad
        totalTime = getTotalTime();
        velocity = getVelocity();
        //Tamaño de la oveja
        sheepSize = sheepSize();
    }

    //Ajuste de la partida
    public void playing(){
        //La partida esta en progreso, inicia el conteo del juego
        if(started) gameProperties.startTimeCount();
        //La partida ha terminado, entonces el contador se termina y se hace el analisis de partida + guardado de dificultad
        gameProperties.endGame();
    }


    //Calculo de cantidad adecuada de ovejas, tiempo, repeticiones y tamaño en base a la dificultad
    private Integer getWhiteSheeps(){ return 2 + ((int) (difficulty/35)); }
    private Integer getBlackSheeps(){ return 1 + ((int) (difficulty/80)); }
    private Integer getTotalTime(){ return (int) (Math.random() * ((int) (difficulty/30) + 0.99) + 3); }
    private Integer getVelocity(){ return Math.max(500 , 500 + (2500 - ((int) (Math.random() * ((int) (difficulty * 10)))))); }
    private float sheepSize(){ return Math.max(140 - (whiteSheeps * 5), 50); }


    //Creacion de layouts random para ubicar aleatoriamente las ovejas
    public RelativeLayout.LayoutParams randomLayouts(){
        //Creacion de los layout de tamaño con sheepSize y la densidad de pantalla
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (sheepSize * dpi), (int) (sheepSize * dpi));
        //Establecemos margins para posiciones aleatorios
        params.leftMargin = (int) ((Math.random() * (325.99 - sheepSize - 5)) * dpi);
        params.topMargin = (int) ((Math.random() * (525.99 - sheepSize - 5)) * dpi);
        return params;
    }

    //Tarea cuando se clickea una oveja
    public boolean sheepClicked(View v, String tp){
        //Si el juego ha iniciado
        if(started){
            //Una vez se clickeo la ovje,a esta desaparece
            v.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            //Si la oveja es blanca
            if(tp.equals("sheepw")){
                //Penalty
                Toast.makeText(context, "Oveja equivocada :(", Toast.LENGTH_SHORT).show();
                gameProperties.penalty(1500);
            }else{
                //La oveja es negra, reducir conteo de ovejas negras y return true si ya clickeo todas las ovejas negras
                Toast.makeText(context, "Muy bien!!", Toast.LENGTH_SHORT).show();
                blackSheeps -= 1;
                return blackSheeps == 0;
            }
        }else{
            //El juego aun no inicia, continua mezclando
            Toast.makeText(context, "Mezclando", Toast.LENGTH_SHORT).show();
        }
        //El juego aun no termina, mezclando aun o ovejas negras restantes
        return false;
    }

    //Tareas asincronicas de traslacion del boton, al establecer el movimiento que tendra cada oveja, se establece una segunda tarea asincrona que hará el siguiente movimiento
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void mixSheeps(View btn, Integer repeat) {
        //Cantidad de repeticiones, si estas se terminan, entonces tambien la tarea asincrona.
        if (repeat == 0) return;
        //En caso contrario, se hace un random para cambiar el color de la oveja, negra -> blanco
        else if (totalTime - 1 > repeat && (Math.random() >= 0.75 || repeat == 1)) ((ImageButton) btn).setImageResource(R.drawable.blacksheep_sheepw);

        //Animaciones de los botones, traslacion de un punto random en el mapa - la posicion actual - 1/2 el tamaño de la oveja. Se añade un tiempo de traslacion (velocidad)
        btn.animate().translationX((int) ((((Math.random() * (335.99))) - (btn.getX() - sheepSize)) * 0.65)).setDuration(velocity);
        btn.animate().translationY((int) ((((Math.random() * (535.99))) - (btn.getY() - sheepSize)) * 0.45)).setDuration(velocity);

        //Tarea asincrona una vez terminada la ejecucion del movimiento, recuciendo las repeticiones en 1
        (new Handler()).postDelayed(() -> mixSheeps(btn, repeat - 1), velocity);
    }

}
