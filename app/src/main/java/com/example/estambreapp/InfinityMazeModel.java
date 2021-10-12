package com.example.estambreapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class InfinityMazeModel {

    private int[] posRunner;
    private int[] posExitDoor;
    private int[] mazeSizes; // It is declared when getMazeMatrix is called
    private int[][] mazeMatrix;
    private int insertKeyIteration;
    private int actualIteration;
    private int numKeysRemaining;
    private int numKeysInserted;
    private HashSet<String> posKeys = new HashSet<>();

    // Setter for posRunner
    public void setPosRunner(int[] newPos){
        posRunner = newPos;
    }

    // Getter for posRunner
    public int[] getPosRunner(){
        return posRunner;
    }

    // Getter for posExitDoor
    public int[] getPosExitDoor(){
        return posExitDoor;
    }

    // Setter for numKeysRemaining
    public void setNumKeysRemaining(int newNumKeys){
        numKeysRemaining = newNumKeys;
    }

    // Getter for numKeysRemaining
    public int getNumKeysRemaining(){
        return numKeysRemaining;
    }

    // Getter for mazeSizes
    public int[] getMazeTableSize(){
        return mazeSizes;
    }

    // Getter for posKeys
    public HashSet<String> getPosKeys() {
        return posKeys;
    }

    // Delete given key
    public void deletePosKey(String key) {
        posKeys.remove(key);
    }

    // Setting the size of the mazeMatrix
    public void setMazeTableSize(){
        // The recursiveMazeGenerator only works correctly if
        // the num of rows and columns are odd numbers

        int[][] sizes = new int[][]{
                {7,5}, {7, 7}, {9, 7}, // Easy
                {9, 9}, {11, 9}, {11,11}, {13,11}, // Medium
                {15, 13}, {17, 13}, {17, 15}, {19, 15} // Difficult
        };

        mazeSizes = sizes[(int) (Math.random()*sizes.length)];
    }

    // Function that validates if the position received is valid
    public boolean validNewPos(Integer[] pos){
        return (0 <= pos[0] && pos[0] < mazeSizes[0]) &&
                (0 <= pos[1] && pos[1] < mazeSizes[1]) &&
                mazeMatrix[pos[0]][pos[1]] == 0;
    }

    // This function uses the recursive backtracking algorithm to generate the maze
    // and also is in charge of adding the keys on different parts of the maze
    public void recursiveMazeGenerator(int[] pos){
        // First, we evaluate if it is possible to add a key in that pos
        if(numKeysInserted < numKeysRemaining &&
                actualIteration >= insertKeyIteration &&
                mazeMatrix[pos[0]][pos[1]] != 3)
        {
            mazeMatrix[pos[0]][pos[1]] = 3; // Set that pos to a key
            posKeys.add(pos[0] + "-" + pos[1]); // Add the key to the set
            actualIteration = 1; // Restart the iteration counter
            numKeysInserted++; // Increase the number of keys inserted
        }
        else mazeMatrix[pos[0]][pos[1]] = 1; // We add an open path if we can't insert a key
        actualIteration++;

        // Creating the array of possibilities to move in each position
        ArrayList<Integer[]> movementPossibilities = new ArrayList<Integer[]>(){{
            add(new Integer[]{-1, 0}); // UP
            add(new Integer[]{1, 0}); // DOWN
            add(new Integer[]{0, 1}); // RIGHT
            add(new Integer[]{0, -1}); // Left
        }};
        // Shuffle the array to pick random movement possibilities
        Collections.shuffle(movementPossibilities);

        // Loops through every possibility
        while(movementPossibilities.size() > 0){
            // Popping the last element from the arraylist because deletes faster
            Integer[] tryingNewPossibility = movementPossibilities.remove(movementPossibilities.size()-1);
            // Calculate the newPosition in the matrix
            Integer[] newPosition = new Integer[]{
                    // Multiplied by 2 because it is trying to move two blocks
                    // This allows us to have walls in the final mazeMatrix
                    pos[0] + (tryingNewPossibility[0] * 2),
                    pos[1] + (tryingNewPossibility[1] * 2)
            };
            // We validate if the newPosition is reachable
            if(validNewPos(newPosition)){
                // Making the block between the pos (actualPos) and the newPosition to be a open path
                mazeMatrix[pos[0] + tryingNewPossibility[0]][pos[1] + tryingNewPossibility[1]] = 1;
                // Recursively go to the newPosition
                recursiveMazeGenerator(new int[]{newPosition[0], newPosition[1]});
            }
        }
    }

    // Function that determines the best position for the exit door
    // It chooses the top row if the runner is lower or bottom row if is higher
    public void addExitDoor() {
        posExitDoor = new int[]{posRunner[0] < mazeSizes[0]/2 ? mazeSizes[0]-1 : 0, (int)(Math.random()*mazeSizes[1]-1)};
        // With the while loop we just make sure that the position in which we want to
        // insert the door is reachable (doesn't have a 0 above or below)
        while(mazeMatrix[posRunner[0] < mazeSizes[0]/2 ? posExitDoor[0]-1 : posExitDoor[0]+1][posExitDoor[1]] == 0)
            posExitDoor[1] = (int)(Math.random()*mazeSizes[1]-1);
        mazeMatrix[posExitDoor[0]][posExitDoor[1]] = 4; // Insert the door
    }

    /** - getMazeMatrix explanation -
     * // Function that calls other functions to construct the final maze
     * // Modifies the mazeMatrix attribute and also returns it
     * Matrix values meaning:
     *  0: Wall
     *  1: Open path
     *  2: Runner
     *  3: Key
     *  4: Exit door
     * @return matrix with values
     */
    public int[][] getMazeMatrix(){
        setMazeTableSize(); // Setting the Size of the maze
        mazeMatrix = new int[mazeSizes[0]][mazeSizes[1]]; // Initializing the matrix with 0

        // The maze generator only works (correctly) if the position of the runner are odd coordinates
        int row = (int) (Math.random()*mazeSizes[0]);
        row = (row % 2 == 0 && row != 0) ? row-1 : row % 2 == 0 ? row+1 : row; // Making sure that is odd
        int column = (int) (Math.random()*mazeSizes[1]);
        column = (column % 2 == 0 && column != 0) ? column-1 : column % 2 == 0 ? column+1 : column; // Making sure that is odd

        posRunner = new int[]{row, column}; // Assigning the position of the runner

        numKeysRemaining = 4; // This is going to be determined by the difficulty

        // Determining the amount of iterations that have to happen in mazeGenerator in order to insert a key
        // (mazeSizes[0]*mazeSizes[1] / 4) <- this is the amount of recursive calls made to the maze generator
        insertKeyIteration = (mazeSizes[0]*mazeSizes[1] / 4) / numKeysRemaining;

        // Initialization of attributes
        actualIteration = 0;
        numKeysInserted = 0;

        recursiveMazeGenerator(posRunner); // Calling the function that generates the maze

        numKeysRemaining = numKeysInserted; // Update to the num of keys inserted

        mazeMatrix[posRunner[0]][posRunner[1]] = 2; // Inserting the runner position

        addExitDoor(); // Adding the exit door

        return mazeMatrix;
    }
}
