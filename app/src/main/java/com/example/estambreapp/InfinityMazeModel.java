package com.example.estambreapp;

import java.util.HashMap;
import java.util.Map;

public class InfinityMazeModel {

    private int[] posRunner;
    private int[] posExitDoor;
    private int numKeysRemaining;

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

    public int[] getMazeTableSize(){
        return new int[]{7, 7};
    }

    /** - getMazeMatrix explanation -
     * Matrix values meaning:
     *  0: Open path
     *  1: Wall
     *  2: Runner
     *  3: Key
     *  4: Exit door
     * @return matrix with values
     */
    public int[][] getMazeMatrix() {
        posRunner = new int[]{0,1};
        posExitDoor = new int[]{6,5};
        numKeysRemaining = 3;
        return new int[][] {
                {1, 2, 1, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 3, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1},
                {1, 3, 1, 0, 1, 3, 1},
                {1, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 1, 1, 4, 1}
        };
    }
}
