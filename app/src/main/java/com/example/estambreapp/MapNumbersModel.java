package com.example.estambreapp;

import android.app.appsearch.ReportSystemUsageRequest;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MapNumbersModel {

    private int[] numberList = {1,2,3,4,5,6,7,8,9,10};


    // we generate our matrix of buttons, it will depend on the level data, to set up it.
    public int[] getTableButtonsSize(){
        return new int[]{ 5, 4 }; // Hardcoded for experimentation purposes
    }


    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    // we generate our numbers that our user going to look for.
    // receives as a parameter the quantity of numbers.
    public int[] generateNumbersToSearch( int numbersToPick ) {

        //we shuffle the original numberList
        shuffleArray(numberList);

        //create a new array of integers
        int[] chosenNumbers = new int[numbersToPick];

        // we traverse the number list and stopped when numbersToPick index.
        for ( int i = 0; i < numbersToPick ; i++ ){
            chosenNumbers[i] = numberList[i];
        }

        return chosenNumbers;

    }


    public int[][] getRandomNumbersMatrix() {

        int[] size = getTableButtonsSize(); // we get our table size
        int[][] matrix = new int[size[0]][size[1]]; // declare a matrix with size[1] and size[0] length.
        int[] numbers =  generateNumbersToSearch(4); // we generate our random numbers to find.
        int numbersSize = numbers.length;
        int count = 0;

        System.out.println(Arrays.toString(numbers));

        for( int i = 0; i < size[0] ; i++) {
            for(int j = 0; j < size[1] ; j++) {

                if( count < numbersSize ){
                    matrix[i][j] = numbers[count];
                    count++;
                } else {
                    matrix[i][j] = (int) (Math.random() * 10); //generates between 0 - 9
                }

            }
        }

        System.out.println(Arrays.deepToString(matrix));

        return matrix;
    }




}
