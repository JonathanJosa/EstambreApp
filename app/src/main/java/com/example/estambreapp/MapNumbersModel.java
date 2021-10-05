package com.example.estambreapp;

import android.app.appsearch.ReportSystemUsageRequest;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MapNumbersModel {

    private int[] numberList = {1,2,3,4,5,6,7,8,9,10}; // numbers to be selected by algorithm to be found

    public int[] numbers;

    private HashMap <String, Integer> toBeFound = new HashMap<String, Integer>();


    // we generate our matrix of buttons, it will depend on the level data, to set up it.
    public int[] getTableButtonsSize(){
        return new int[]{ 5, 4 }; // Hardcoded for experimentation purposes
    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar) {
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

    // we generate our numbers which the user is going to search.
    // receives as a parameter the quantity of numbers to generate.
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

    // function that shuffles a 2D matrix
    void shuffleMatrix(int[][] a) {
        Random random = new Random();

        for (int i = a.length - 1; i > 0; i--) {
            for (int j = a[i].length - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                int temp = a[i][j];
                a[i][j] = a[m][n];
                a[m][n] = temp;
            }
        }
    }


    public void fillHashMap(int[] arrayNumbersToSearch){
        for (int i = 0;i<arrayNumbersToSearch.length;i++){
            toBeFound.put(String.valueOf(arrayNumbersToSearch[i]), 1);
        }
        //System.out.println(Arrays.toString(arrayNumbersToSearch));
    }

    public int[] getNumbersToBeFound(){
        return numbers;
    }

    // function that returns a 2D matrix of integers that includes the number selected by the algorithm, and which will be searched by user.
    public int[][] getRandomNumbersMatrix() {

        int[] size = getTableButtonsSize(); // we get our table size
        int[][] matrix = new int[size[0]][size[1]]; // declare a matrix with size[1] and size[0] length.
        numbers =  generateNumbersToSearch(4); // we generate our random numbers to find.

        fillHashMap(numbers); // we fill our hashmap

        int count = 0; //variable to handle 'numbers' array numbers push into the matrix.

        //System.out.println(Arrays.toString(numbers));

        for( int i = 0; i < size[0] ; i++) {
            for(int j = 0; j < size[1] ; j++) {

                // we assure that the numbers which will be searched get pushed into the matrix first.
                if( count < numbers.length ){
                    matrix[i][j] = numbers[count];
                    count++;
                } else {
                    // and then, we fill it out with random numbers between 0 and 9
                    matrix[i][j] = (int) (Math.random() * 10); //generates between 0 - 9

                    if(toBeFound.containsKey(String.valueOf(matrix[i][j]))){
                        toBeFound.put( String.valueOf(matrix[i][j]), toBeFound.get(String.valueOf(matrix[i][j])) + 1 );
                    }
                }

            }
        }


        //System.out.println(Arrays.deepToString(matrix));

        shuffleMatrix(matrix); // finally, we shuffle the matrix

        //System.out.println(Arrays.deepToString(matrix));
        //System.out.println(toBeFound);

        return matrix;
    }



    public void checkButtonValue(  ){

    }




}
