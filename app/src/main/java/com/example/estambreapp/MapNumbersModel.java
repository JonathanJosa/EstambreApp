package com.example.estambreapp;

import android.app.appsearch.ReportSystemUsageRequest;
import android.content.Context;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Handler;

public class MapNumbersModel {

    GamesModel gameProperties;

    private int[] numberList = {1,2,3,4,5,6,7,8,9,10}; // numbers to be selected by algorithm to be found

    public int[] numbers; // array where we store our numbers to found

    public int LevelNumber = 1; // number of games played

    public HashMap <String, Integer> toBeFoundMap = new HashMap<String, Integer>(); // {5=2, 6=1, 9=3, 10=1}

    public boolean nextRound = false; // variable para controlar el el cambio de ronda

    public int numbersCompleted = 1; // contador que usamos para ver cuantos elementos del mapa hemos descubierto.

    int[] arrayNumberToFound = new int[1]; // array when we create number by mathematical operation result

    public String operationString = ""; // string where we store our operation string to show in our view.

    public MapNumbersModel(Context context) {
        gameProperties = new GamesModel(context, "MapNumbers");
    }

    public void startOrEndGame(boolean option){ // If true, start time count; otherwise, end time count
        if (option) gameProperties.startTimeCount();
        else gameProperties.endGame();
    }

    public double getDifficulty(){
        return gameProperties.getDifficulty();
    }


    public int[] getNumbersToBeFound(){
        return numbers;

    }
    public boolean getNextRound(){
        return nextRound;
    }


    // we generate our matrix of buttons, it will depend on the level data, to set up it.
    public int[] getTableButtonsSize(){

        double numDiffOfGame = gameProperties.getDifficulty();

        System.out.println("Difficulty: " + numDiffOfGame);
        // dependiendiendo de la dificultad ....
        return ( numDiffOfGame < 50 ) ? new int[]{ 3, 2 }:
                ( numDiffOfGame < 75 ) ? new int[]{ 4, 3 }:
                ( numDiffOfGame < 100) ? new int[]{ 4, 3 }:
                ( numDiffOfGame < 125) ? new int[]{6, 4}:
                ( numDiffOfGame < 150) ? new int[] { 6, 5 } : new int[]{ 7, 5 };

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
        double curentDifficulty = getDifficulty();

        numbers = ( curentDifficulty < 50 ) ? generateNumbersToSearch(1):
                  ( curentDifficulty < 75 ) ? generateNumbersToSearch(2):// we generate our random numbers to find.
                  ( curentDifficulty < 100 ) ? generateNumbersToSearch(3): generateNumbersToSearch(4);

        fillHashMap(numbers); // we fill our hashmap

        int count = 0; //variable to handle 'numbers' array numbers push into the matrix.

        for( int i = 0; i < size[0] ; i++) {
            for(int j = 0; j < size[1] ; j++) {

                // we assure that the numbers which will be searched get pushed into the matrix first.
                if( count < numbers.length ){
                    matrix[i][j] = numbers[count];
                    count++;
                } else {
                    // and then, we fill it out with random numbers between 0 and 9
                    matrix[i][j] = (int) (Math.random() * 10); //generates between 0 - 9

                    if(toBeFoundMap.containsKey(String.valueOf(matrix[i][j]))){
                        toBeFoundMap.put( String.valueOf(matrix[i][j]), toBeFoundMap.get(String.valueOf(matrix[i][j])) + 1 );
                    }
                }

            }
        }

        shuffleMatrix(matrix); // finally, we shuffle the matrix

        return matrix;
    }

    public void fillHashMap(int[] arrayNumbersToSearch){
        for (int i = 0;i<arrayNumbersToSearch.length;i++){
            toBeFoundMap.put(String.valueOf(arrayNumbersToSearch[i]), 1);
        }
    }


    public void checkNumbersToBeFound( TextView instructionsText ){
        numbersCompleted++;
        if(numbersCompleted > numbers.length){ // en caso de generar normales va el de numbers

            System.out.println("La ronda temina, pq encontraste todos los numeros :D");

            nextRound = true;
            numbersCompleted = 1; // inicializamos a 1 otra vez la cantidad de numeros encontrados.

        }
    }

    public void checkButtonValue(Button selectedValueButton, TextView instructionsText){

        String value = (String) selectedValueButton.getText(); // obtenemos el valor del botón


        if(toBeFoundMap.containsKey(value) && toBeFoundMap.get(value) > 0){

            toBeFoundMap.put( value, (toBeFoundMap.get(value) - 1) );
            selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_correctbtn);
            selectedValueButton.setEnabled(false);

            // si el valor de la llave del hashmap es cero, significa que ya no hay mas numeros a buscar

            if( toBeFoundMap.get(value) == 0 ){
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

        } else if( !toBeFoundMap.containsKey(value) ){

            selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_incorrectbtn);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_normalbtn);
                }
            }, 1500);

            gameProperties.penalty(0.5);
            System.out.println("El numero que elejiste no esta en la lista a encontrar");

        }

    }







    public List<Integer> getNumbersForOperations(int level ){

        List<Integer> numbers = new ArrayList<Integer>();
        int min = 1;
        int max = 15;

        if( level > 4  ){

            for( int i = 0; i < 3 ;i++ ){
                numbers.add((int) (Math.random()*(max-min+1)+min));
            }

        } else if( level <=4 ){

            for( int i = 0; i < 2 ;i++ ){
                numbers.add((int) (Math.random()*(max-min+1)+min));
            }
        }

        return numbers;
    }

    public List<String> getOperationSigns(int numberOfNumbers){
        List<String> signs = new ArrayList<String>();


        if( numberOfNumbers == 2 ){

            float signIndex = (float) Math.random();
            signs.add( ( signIndex > 0.5) ? "+" : "-" );

        }else if( numberOfNumbers > 2 ){
            for( int i = 0; i<2 ;i++ ){
                float signIndex = (float)Math.random();
                signs.add( ( signIndex > 0.5) ? "+" : "-" );
            }
        }

        return signs;
    }

    public int operationResult( List<Integer> listNumbers, List<String> listSigns, int indexNumbers, int indexSigns, int total ){

        if( indexNumbers  > listNumbers.size() - 1 ){
            return total;
        }else {
            if( listSigns.get(indexSigns) == "+" )
                    return  operationResult(listNumbers, listSigns, indexNumbers + 1, indexSigns + 1, total + listNumbers.get(indexNumbers));
            if( listSigns.get(indexSigns) == "-" )
                return operationResult(listNumbers, listSigns, indexNumbers + 1, indexSigns + 1, total - listNumbers.get(indexNumbers));

        }
        return total;
    }

    public String assembleOperation( List<Integer> listNumbers, List<String> listSigns, int indexNumbers, int indexSigns, String total ) {

        if( indexNumbers  > listNumbers.size() - 1 ){
            return total;
        }else {
            if( listSigns.get(indexSigns) == "+" )
                return  assembleOperation(listNumbers, listSigns, indexNumbers + 1, indexSigns + 1, total +" " +"+"+" " + String.valueOf(listNumbers.get(indexNumbers)));
            if( listSigns.get(indexSigns) == "-" )
                return assembleOperation(listNumbers, listSigns, indexNumbers + 1, indexSigns + 1, total +" " +"-"+" " + String.valueOf(listNumbers.get(indexNumbers)));
        }
        return total;
    }



    public  int generateOperationAndNumberToBeFound( int level ){

        // recibimos los numeros que vamos a calcular (dependiendo en el nivel)

        List<Integer> listOfNumbers = getNumbersForOperations(level);

        System.out.println( listOfNumbers );


        // a travez de una función, decidimos que operacion se va a realizar con los numeros
        List<String> signsForOperations =  getOperationSigns( listOfNumbers.size() );
        System.out.println( signsForOperations );

        // se realiza la operacion aquí, para concer el resultado
        int result = operationResult(listOfNumbers, signsForOperations, 1, 0, listOfNumbers.get(0));
        System.out.println("Resultado: " + result);

        // se envian o se muestran el texto en string de la operacion a ser resuelta por el usuario
        String operationStringAssembled = assembleOperation(listOfNumbers, signsForOperations, 1, 0, String.valueOf(listOfNumbers.get(0)));
        System.out.println("String de resultado: " + operationStringAssembled);
        operationString = operationStringAssembled;

        // eviamos el resultado a traves de una matriz (otra funcion )

        return result;

    }

    public int[][] getRandomNumbersMatrixWithOperation() {

        int[] size = getTableButtonsSize(); // we get our table size
        int[][] matrix = new int[size[0]][size[1]]; // declare a matrix with size[1] and size[0] length.


        int numberToFound =  ( getDifficulty() > 125 ) ? generateOperationAndNumberToBeFound(5):
                                        generateOperationAndNumberToBeFound(3); // we generate our random numbers to find.


        arrayNumberToFound[0] = numberToFound; //creamos el array, para poder mandar como parametro a fillHashMap

        fillHashMap(arrayNumberToFound); // we fill our hashmap

        int count = 0; //variable to handle 'numbers' array numbers push into the matrix.

        //calculamos el 20% por arriba y por abajo de nuestro nuemero a encontrar
        int min = numberToFound - 15;
        int max = numberToFound + 15;

        for( int i = 0; i < size[0] ; i++) {
            for(int j = 0; j < size[1] ; j++) {

                // we assure that the numbers which will be searched get pushed into the matrix first.
                if( count < arrayNumberToFound.length ){
                    matrix[i][j] = arrayNumberToFound[count];
                    count++;
                } else {
                    // and then, we fill it out with random numbers between 0 and 9
                    matrix[i][j] = (int)(Math.random() * ((max - min) + 1)) + min; //generates between 0 - 9

                    if(toBeFoundMap.containsKey(String.valueOf(matrix[i][j]))){
                        toBeFoundMap.put( String.valueOf(matrix[i][j]), toBeFoundMap.get(String.valueOf(matrix[i][j])) + 1 );
                    }
                }

            }
        }

        shuffleMatrix(matrix); // finally, we shuffle the matrix

        return matrix;
    }


}
