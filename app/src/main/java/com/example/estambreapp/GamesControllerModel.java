package com.example.estambreapp;

import java.util.Arrays;

public class GamesControllerModel {

    //Areglos de nombres de juegos, colores y tiempos
    //Los tiempos son de reaccion entre el inicio de poder interactuar con el juego, hasta el final del juego (victoria)
    //Se puede añadir un nuevo juego colocando aqui solo sus parametros y nombre, crea la clase como "Nombre" + "Activity"
    //Tambien se deben crear sus imagenes correspondientes -> "instructions_nombre", "juego_nombre", "load_nombre"
    //El menu mostrará automaticamente el acceso a este nuevo juego, igual que GamesOptions y GameInstructions
    String[] gamesArray = new String[]{"BlackSheep", "MapNumbers", "Impostor", "InfinityMaze"};
    String[] colorGame = new String[]{"#F0D9FF", "#3DB2FF", "#CEE5D0", "#2F3136"};
    Double[] situableTime = new Double[]{3.0, 10.0, 10.0, 80.0}; //seconds

    //Regresar nombres de juegos
    public String[] getAllGames(){ return gamesArray; }

    //Regresar un juego random, solo si este no es el ultimo juego (parametro)
    public String getRandomGame(String lastGame){
        //Selecion aleatoria
        String randomGame = gamesArray[(int) Math.floor(Math.random() * gamesArray.length)];
        //Son el mismo juego -> Recursivamente se vuelve a ejecutar || no lo son -> regresa valor (condicion final)
        return randomGame.equals(lastGame) ? getRandomGame(lastGame) : randomGame;
    }

    //Obtener color y tiempo del juego correspondiente, obetniendo index del nombre y accediendo con eso al parametro solicitado
    public String getColorGame(String game){ return colorGame[(int) Arrays.asList(gamesArray).indexOf(game)]; }
    public Double getSituableTime(String game){ return situableTime[(int) Arrays.asList(gamesArray).indexOf(game)]; }

}