package com.example.estambreapp;

import android.app.appsearch.ReportSystemUsageRequest;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Handler;

public class MapNumbersModel {

    private int[] numberList = {1,2,3,4,5,6,7,8,9,10}; // numbers to be selected by algorithm to be found

    public int[] numbers; // array where we store our numbers to found

    //----------------------------------------------------------------------------------------------
    // TEMPORAL
    public int LevelNumber = 1;

    private int rows = 3;
    private int cols = 2;
    private int lotNumbersToFound = 1;

    //----------------------------------------------------------------------------------------------

    public HashMap <String, Integer> toBeFound = new HashMap<String, Integer>(); // {5=2, 6=1, 9=3, 10=1}

    public boolean nextRound = false; // variable para controlar el el cambio de ronda

    public int numbersCompleted = 1; // contador que usamos para ver cuantos elementos del mapa hemos descubierto.


    public int[] getNumbersToBeFound(){
        return numbers;
    }
    public boolean getNextRound(){
        return nextRound;
    }

    //----------------------------------------------------------------------------------------------
    // TEMPORAL
    public void calculateLevelParams( int lvlNum ){
        switch (lvlNum){
            case 1:
                rows = 3; cols = 2;
                lotNumbersToFound = 1;
                break;
            case 2:
                rows = 4; cols = 3;
                lotNumbersToFound = 2;
                break;
            case 3:
                rows = 5; cols = 4;
                lotNumbersToFound = 3;
                break;
            case 4:
                rows = 5; cols = 4;
                lotNumbersToFound = 4;
            default:
                break;

        }


    }
    //----------------------------------------------------------------------------------------------



    // we generate our matrix of buttons, it will depend on the level data, to set up it.
    public int[] getTableButtonsSize(){
        return new int[]{ rows, cols }; // Hardcoded for experimentation purposes
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

    // function that shuffles a 2D matrix
    private void shuffleMatrix(int[][] a) {
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

    // function that returns a 2D matrix of integers that includes the number selected by the algorithm, and which will be searched by user.
    public int[][] getRandomNumbersMatrix() {

        int[] size = getTableButtonsSize(); // we get our table size
        int[][] matrix = new int[size[0]][size[1]]; // declare a matrix with size[1] and size[0] length.
        numbers =  generateNumbersToSearch(lotNumbersToFound); // we generate our random numbers to find.

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

        shuffleMatrix(matrix); // finally, we shuffle the matrix

        return matrix;
    }

    public void fillHashMap(int[] arrayNumbersToSearch){
        for (int i = 0;i<arrayNumbersToSearch.length;i++){
            toBeFound.put(String.valueOf(arrayNumbersToSearch[i]), 1);
        }
        //System.out.println(Arrays.toString(arrayNumbersToSearch));
    }


    public void checkNumbersToBeFound( TextView instructionsText ){
        numbersCompleted++;
        if(numbersCompleted > numbers.length){

            System.out.println("La ronda temina, pq encontraste todos los numeros :D");

            nextRound = true;
            numbersCompleted = 1; // inicializamos a 1 otra vez la cantidad de numeros encontrados.

        }
    }

    public void checkButtonValue(Button selectedValueButton, TextView instructionsText){

        String value = (String) selectedValueButton.getText(); // obtenemos el valor del botón


        if(toBeFound.containsKey(value) && toBeFound.get(value) > 0){

            toBeFound.put( value, (toBeFound.get(value) - 1) );
            selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_correctbtn);
            selectedValueButton.setEnabled(false);

            // si el valor de la llave del hashmap es cero, significa que ya no hay mas numeros a buscar

            if( toBeFound.get(value) == 0 ){
                System.out.println("no hay mas numeros de algo");
                instructionsText.setText(" ¡ Excelente buscador! , sigue buscando a los otros numeros \\ (•◡•) / ");

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        instructionsText.setText(" Encuentra estos numeros en la tabla de abajo");
                    }
                }, 3000);

                checkNumbersToBeFound( instructionsText ); // verificamos si aun hay mas numeros que buscar
            }

        } else if( !toBeFound.containsKey(value) ){

            selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_incorrectbtn);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_normalbtn);
                }
            }, 1500);

            System.out.println("El numero que elejiste no esta en la lista a encontrar");

        }

    }




    public  void generateOperationAndNumberToBeFound(  ){

    }


}
