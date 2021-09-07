package com.example.estambreapp;

import java.util.Arrays;

public class gamesController {
    //La isla siniestra - Netflix
    //Si ven esto, son gays

    String[] gamesArray = new String[]{"ovejaNegra", "mapNumbers", "impostor", "mazeCoins"};
    String[] colorGame = new String[]{"F0D9FF", "3DB2FF", "CEE5D0", "2F3136"};

    public String[] getAllGames(){ return gamesArray; }

    public String getRandomGame(String lastGame){
        String randomGame = gamesArray[(int) Math.floor(Math.random() * gamesArray.length)];
        return randomGame.equals(lastGame) ? getRandomGame(lastGame) : randomGame;
    }

    public String getColorGame(String game){ return colorGame[(int) Arrays.asList(gamesArray).indexOf(game)]; }

}
