package com.example.estambreapp;

import android.content.Context;

public class SimonSaysModel {


    GamesModel gameProperties;

    public int size;
    private int[] pattern;
    private int isCorrect = 0;
    private int currentPosition = 0;



    public SimonSaysModel(Context context) {
        gameProperties = new GamesModel(context, "SimonSays");
        gameProperties.setDifficulty("5");
        size = (int) gameProperties.getDifficulty();
    }

    // function that allow us create a random pattern, depending on buttons indexes
    // we make a random function that return only 0 - 3 values
    public int[] getRandomPattern(){
        pattern = new int[size];
        for(int i=0;i<size;i++){
            pattern[i] = (int)(Math.random()*3.99);
        }
        return pattern;
    }

    public int getDifficulty(){
        return (int) gameProperties.getDifficulty();
    }

    // we check if the clicked button is equal to real pattern
    public void clicked(int btn){
        if(pattern[currentPosition] == btn ){
            isCorrect = 1;
        }else{
            isCorrect = -1;
        }
    }


    // we return a string that relays on the result of clicked function
    public int checkedClick(){
        if(isCorrect == -1){
            gameProperties.saveMyDifficulty((double) Math.max(3, size - 1));
            return -1;
        }else if(size == currentPosition+1){
            gameProperties.saveMyDifficulty((double) size+1);
            return 1;
        }else{
            currentPosition++;
            return 0;
        }


    }
}
