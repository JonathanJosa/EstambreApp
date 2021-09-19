package com.example.estambreapp                                                                                                                         ;

import android.content.Context                                                                                                                          ;
import android.content.SharedPreferences                                                                                                                ;

public class GamesModel                                                                                                                                 {

    SharedPreferences preferencesUser                                                                                                                   ;
    String gameName                                                                                                                                     ;
    Double time                                                                                                                                         ;
    Double difficulty                                                                                                                                   ; //Promedio 100.0

    public GamesModel(Context context, String game)                                                                                                     {
        gameName = game                                                                                                                                 ;
        preferencesUser = context.getSharedPreferences(gameName, Context.MODE_PRIVATE)                                                                  ;
        difficulty = Double.parseDouble(preferencesUser.getString("Difficulty", "50.0"))                                                          ;
                                                                                                                                                        }

    private void saveLastGame(double df)                                                                                                                {
        preferencesUser.edit().putString("Difficulty", String.valueOf(df)).commit()                                                                  ;
                                                                                                                                                        }

    private double calculateAdjustDifficulty(double totalTime)                                                                                          {
        return (((totalTime / (new GamesControllerModel()).getSituableTime(gameName)) * (difficulty * 1.1)) + (difficulty * 4)) / 5                     ;
                                                                                                                                                        }

    public double getDifficulty                                                                                                                         (){
        return difficulty * 1.1                                                                                                                         ;
                                                                                                                                                        }

    public void startTimeCount                                                                                                                          (){
        time = ((Long) (System.currentTimeMillis() / 1000)).doubleValue() * -1                                                                          ;
                                                                                                                                                        }

    public void penalty(double penaltyTime)                                                                                                             {
        time += penaltyTime                                                                                                                             ;
                                                                                                                                                        }

    public void endGame                                                                                                                                 (){
        saveLastGame                                                                                                                                    (
                calculateAdjustDifficulty                                                                                                               (
                        time + ((Long) (System.currentTimeMillis() / 1000)).doubleValue()
                                                                                                                                                        )
                                                                                                                                                        );
        time = 0.0                                                                                                                                      ;
                                                                                                                                                        }

                                                                                                                                                        }
