package com.example.estambreapp;

public class SimonSaysModel {

    private int score = 0;
    private int size = 5;
    private int[] pattern;
    private int isCorrect = 0;
    private int curentPosition = 0;


    // function that allow us create a random pattern, depending on buttons indexes
    // we make a random function that return only 0 - 3 values
    public int[] getRandomPattern(){
        pattern = new int[size];
        for(int i=0;i<size;i++){
            pattern[i] = (int)(Math.random()*3.99);
        }
        return pattern;
    }

    // we check if the clicked button is equal to real pattern
    public void clicked(int btn){

        if( pattern[curentPosition] == btn ){
            isCorrect = 1;
        }else{
            isCorrect = -1;
        }
    }


    // we return a string that relays on the result of clicked function
    public String endGame(){
        if(isCorrect == -1){
            curentPosition = 0;
            size = 1;
            score = 0;
            return "Wrong pattern";

        }else if(size == curentPosition+1){
            size++;
            score++;
            curentPosition = 0;
            pattern = null;
            return "You Win";

        }else {
            curentPosition++;
            return "Ok";

        }


    }

    // getter for score value.
    public String getScore(){
        return "Score: " + String.valueOf(score);
    }



}
