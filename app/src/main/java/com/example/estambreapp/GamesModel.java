package com.example.estambreapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.Long;

public class GamesModel{

    private final SharedPreferences preferencesUser;
    private final String gameName;
    private Long time;
    private Double difficulty; //Promedio esperado de 100.0

    //Constructor de la clase
    public GamesModel(Context context, String game){
        //declaracion de variables
        gameName = game;
        //Conexion con preferences en gameName, posible aumento de parametros, ahora solo difficulty
        preferencesUser = context.getSharedPreferences(gameName, Context.MODE_PRIVATE);
        setDifficultyVar();
    }

    //If the game requires an specifies difficulty, this function work
    public void setDifficulty(String defaultValue){ difficulty = Double.parseDouble(preferencesUser.getString("Difficulty", defaultValue)); }

    //Guardar la dificultad que desees guardar
    public void saveMyDifficulty(double value){
        preferencesUser.edit().putString("Difficulty", String.valueOf(value)).apply();
        setDifficultyVar();
    }

    //Dificultad por defecto de 50.00 si es que no existe
    private void setDifficultyVar(){ difficulty = Double.parseDouble(preferencesUser.getString("Difficulty", "50.0")); }

    //Guardar la dificultad ajustada y reasignarla
    private void saveLastGame(double df){
        preferencesUser.edit().putString("Difficulty", String.valueOf(df)).apply();
        setDifficultyVar();
    }

    //Ajustar la dificultad al tiempo de juego del jugador, usando situable time y totalTime
    private double calculateAdjustDifficulty(double totalTime){ return ((((new GamesControllerModel()).getSituableTime(gameName) / totalTime) * (difficulty * 1.1)) + (difficulty * 9)) / 10; }

    //Obtener dificultad con 10% extra
    public double getDifficulty(){
        return difficulty * 1.15;
    }

    //medir tiempo, -tiempo actual + tiempo final
    public void startTimeCount(){
        time = (System.currentTimeMillis()) * -1;
    }

    //Penalty suma tiempo como reaccion tardia
    public void penalty(double penaltyTime){ time  += ((Double.valueOf(penaltyTime * 1000)).longValue()); }

    //Finalizar juego, guardando dificultad ajustada y tomando el timpo de reaccion como -tiempoInicial + tiempoFinal + Penalties
    public void endGame(){ saveLastGame(calculateAdjustDifficulty((Long.valueOf(time + (System.currentTimeMillis()))).doubleValue() / 1000)); }
}
