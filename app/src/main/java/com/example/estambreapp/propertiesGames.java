package com.example.estambreapp;

public class propertiesGames {

    String gameName;
    Double time = (double) 0.0;
    Double finalTime = (double) 0.0;
    Double difficulty = (double) 50.0;

    public propertiesGames(String game){
        gameName = game;

        //Create if not declared


        //if declared
        //difficulty = promoted(getAllAdjustDifficulty());
    }

    private void ifNotDeclared(){
        //Create String gameName on Preferences
        //Default 50.0
    }

    private double[] getAllAdjustDifficulties(){
        return new double[]{1.0};
    }

    private void saveLastGame(){
        //finalTime
        Double situableTime = (new gamesController()).getSituableTime(gameName);
        //difficulty * 1.1
        //calculateAdjustDifficulty()

        //Delete last item
        //Save on preferences

    }

    public double getDifficulty(){ return difficulty; }

    public void startTimeCount(){
        //time = actualTime();
    }

    public void endGame(){
        //finalTime += actualTime() - time;
        saveLastGame();
    }

    public void penalty(double penaltyTime){
        // finalTime += penaltyTime;
    }

}
