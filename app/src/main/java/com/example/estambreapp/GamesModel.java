package com.example.estambreapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.Long;

public class GamesModel{

    private SharedPreferences preferencesUser;
    private String gameName;
    private Long time;
    private Double difficulty; //Promedio 100.0

    public GamesModel(Context context, String game){
        gameName = game;
        preferencesUser = context.getSharedPreferences(gameName, Context.MODE_PRIVATE);
        difficulty = Double.parseDouble(preferencesUser.getString("Difficulty", "50.0"));
    }

    private void saveLastGame(double df){
        preferencesUser.edit().putString("Difficulty", String.valueOf(df)).commit();
        difficulty = Double.parseDouble(preferencesUser.getString("Difficulty", "50.0"));
    }

    private double calculateAdjustDifficulty(double totalTime){
        return  ((((new GamesControllerModel()).getSituableTime(gameName) / totalTime) * (difficulty * 1.1)) + (difficulty * 9)) / 10;
    }

    public double getDifficulty(){
        return difficulty * 1.1;
    }

    public void startTimeCount(){
        time = (System.currentTimeMillis()) * -1;
    }

    public void penalty(double penaltyTime){
        time  += ((Double.valueOf(penaltyTime * 1000)).longValue());
    }

    public void endGame(){
        saveLastGame(
                calculateAdjustDifficulty(
                        (Long.valueOf(time + (System.currentTimeMillis()))).doubleValue() / 1000));
    }

}
