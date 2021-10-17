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

    // instance to GameModel ( difficulty calculator)
    GamesModel gameProperties;

    private int[] numberList = {1,2,3,4,5,6,7,8,9,10}; // numbers to be selected by algorithm to be found
    public int[] numbers; // array where we store our numbers to find
    public int LevelNumber = 1; // number of games played
    // we store in this HasMap the number generated to find, and the number of times it appear in our matrix.
    public HashMap <String, Integer> toBeFoundMap = new HashMap<String, Integer>(); // {5=2, 6=1, 9=3, 10=1} --> (Example)
    public boolean nextRound = false; // variable to handle round change.
    public int numbersCompleted = 1; // counter to handle how many numbers user has founded.
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

    // getter to numbers to find array
    public int[] getNumbersToBeFound(){
        return numbers;
    }

    public boolean getNextRound(){
        return nextRound;
    }

    // we generate our matrix of buttons, it will depend on the level data, to set up it.
    public int[] getTableButtonsSize(){

        double numDiffOfGame = gameProperties.getDifficulty();

        // depending on difficulty ...
        return ( numDiffOfGame < 50 ) ? new int[]{ 3, 2 }:
                ( numDiffOfGame < 75 ) ? new int[]{ 4, 3 }:
                ( numDiffOfGame < 100) ? new int[]{ 4, 3 }:
                ( numDiffOfGame < 125) ? new int[]{5, 4}:
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

    // we generate our numbers,to find.
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
        double curentDifficulty = getDifficulty(); // we get difficulty of game.

        // assign to numbers variable the result of calling generateNumbersToSearch, which return an array.
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

    // function that initialize our hashmap
    public void fillHashMap(int[] arrayNumbersToSearch){
        for (int i = 0;i<arrayNumbersToSearch.length;i++){
            toBeFoundMap.put(String.valueOf(arrayNumbersToSearch[i]), 1);
        }
    }

    // function that tell us if user already has found all numbers in a round.
    public void checkNumbersToFindYet( TextView instructionsText ){
        numbersCompleted++;
        if(numbersCompleted > numbers.length){ // if the value of the variable overpass the length of the array of numbers to find...
            nextRound = true; // we pass to other round.
            numbersCompleted = 1; // reset the total of numbers already found by user.
        }
    }

    // functions that handle, whether the button is correct or incorrect
    public void checkButtonValue(Button selectedValueButton, TextView instructionsText){

        String value = (String) selectedValueButton.getText(); // we get the text value of the selected button.

        // we check if the value is on the hashmap and if the number of appearances is greater than zero.
        if(toBeFoundMap.containsKey(value) && toBeFoundMap.get(value) > 0){
            toBeFoundMap.put( value, (toBeFoundMap.get(value) - 1) ); // we subtract 1 from the number of appearances.
            selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_correctbtn); // we change the image background as correct.
            selectedValueButton.setEnabled(false); // we disable the button.

            // if key value is zero, that means that there is no other number to find.
            if( toBeFoundMap.get(value) == 0 ){
                instructionsText.setText(" ¡Numero encontrado! , sigue buscando el resto  (•◡•)");
                // after 3 seconds, we set again, the original text.
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if( getDifficulty() < 100 )
                            instructionsText.setText(" Encuentra los siguientes números en la tabla");
                        else
                            instructionsText.setText("Resuelve la operación y encuentra el resultado en la tabla");
                    }
                }, 3000);

                checkNumbersToFindYet( instructionsText ); // we check if there's still numbers to find
            }

        } else if( !toBeFoundMap.containsKey(value) ){ // if the key is not on the hashmap, that means that the user pressed wrong button.

            selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_incorrectbtn); // set image background as incorrect
            selectedValueButton.setEnabled(false); // we disable the button.
            // ... for 1.5 seconds
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    selectedValueButton.setBackgroundResource(R.drawable.mapnumbers_normalbtn); // change as normal btn
                }
            }, 1500);

            selectedValueButton.setEnabled(true); // we enable the button.
            gameProperties.penalty(0.5); // we apply the penalty for pressed incorrect button.
        }

    }






    // function that generates random numbers to create an operation.
    public List<Integer> getNumbersForOperations(int level ){

        List<Integer> numbers = new ArrayList<Integer>();

        // establish the min and max range.
        int min = 1;
        int max = 15;

        // if the level is greater than 4, we generate 3 numbers
        if( level > 4  ){
            for( int i = 0; i < 3 ;i++ ){
                numbers.add((int) (Math.random()*(max-min+1)+min));
            }

        } else if( level <=4 ){ // if level is less or equal than 4, we generate 2 numbers.
            for( int i = 0; i < 2 ;i++ ){
                numbers.add((int) (Math.random()*(max-min+1)+min));
            }
        }

        return numbers;
    }

    // function that generates random signs for our basic operation.
    public List<String> getOperationSigns(int numberOfNumbers){

        List<String> signs = new ArrayList<String>();
        // if we have to operate with 2 numbers , we only generate 1 sign.
        if( numberOfNumbers == 2 ){
            float signIndex = (float) Math.random();
            signs.add( ( signIndex > 0.5) ? "+" : "-" ); // if signIndex > 0.5 we add plus sign , if signIndex < 0.5 we add a minus sign.

        }else if( numberOfNumbers > 2 ){ // if we have to operate with 3 numbers , we generate 2 signs.
            for( int i = 0; i<2 ;i++ ){
                float signIndex = (float)Math.random();
                signs.add( ( signIndex > 0.5) ? "+" : "-" );
            }
        }
        return signs;
    }

    // recursive function to calculate the result of the generated operation.
    public int operationResult( List<Integer> listNumbers, List<String> listSigns, int indexNumbers, int indexSigns, int total ){

        // our base case.
        if( indexNumbers  > listNumbers.size() - 1 ){
            return total;
        }else {
            // we add or abstract, the number secuence, depending on the sign.
            // for each call, we add 1 to our indexes, so that, we can traverse all List we recived as paramenters.
            if( listSigns.get(indexSigns) == "+" )
                    return  operationResult(listNumbers, listSigns, indexNumbers + 1, indexSigns + 1, total + listNumbers.get(indexNumbers));
            if( listSigns.get(indexSigns) == "-" )
                return operationResult(listNumbers, listSigns, indexNumbers + 1, indexSigns + 1, total - listNumbers.get(indexNumbers));

        }
        return total;
    }

    // recursive function to get the full string operation.
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

    // function that generates the random operation and returns the result of it.
    public  int generateOperationAndNumberToBeFound( int level ){

        List<Integer> listOfNumbers = getNumbersForOperations(level);

        // we get the signs of operation
        List<String> signsForOperations =  getOperationSigns( listOfNumbers.size() );

        // we calculate the result of the operation.
        int result = operationResult(listOfNumbers, signsForOperations, 1, 0, listOfNumbers.get(0));
        System.out.println("Resultado: " + result);

        // we concatenate all elements, to get a string with the operation
        String operationStringAssembled = assembleOperation(listOfNumbers, signsForOperations, 1, 0, String.valueOf(listOfNumbers.get(0)));
        System.out.println("String de resultado: " + operationStringAssembled);

        operationString = operationStringAssembled; // we assign the value to a global variable, so that, we can show the text in our view.

        return result;

    }

    // function that generates a random matrix based on a mathematical operation and its result.
    public int[][] getRandomNumbersMatrixWithOperation() {

        int[] size = getTableButtonsSize(); // we get our table size
        int[][] matrix = new int[size[0]][size[1]]; // declare a matrix with size[1] and size[0] length.
        int numberToFound =  ( getDifficulty() > 125 ) ? generateOperationAndNumberToBeFound(5):
                                        generateOperationAndNumberToBeFound(3); // we generate our random numbers to find.

        arrayNumberToFound[0] = numberToFound; //we create an array to fill our hashmap

        fillHashMap(arrayNumberToFound); // we fill our hashmap

        int count = 0; //variable to handle 'numbers' array numbers push into the matrix.

        //we establish the min and max range that the matrix is going to contain, based on the number to find (operation result)
        // (15 numbers up and down)
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
