package com.example.estambreapp;

public class InfinityMazeModel {

    public int[] getMazeTableSize(){
        return new int[]{7, 7};
    }

    /*
    public boolean[][] getMazeMatrix() {
        return new boolean[][] {
                {false, true, false, false, false, false, false},
                {false, true, false, true, true, true, false},
                {false, true, false, true, false, true, false},
                {false, true, false, true, false, true, false},
                {false, true, false, true, false, true, false},
                {false, true, true, true, false, true, false},
                {false, false, false, false, false, true, false}
        };
    }
     */

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
