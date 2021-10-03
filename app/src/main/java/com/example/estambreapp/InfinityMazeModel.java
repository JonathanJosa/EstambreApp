package com.example.estambreapp;

public class InfinityMazeModel {

    public int[] getMazeTableSize(){
        return new int[]{7, 7};
    }

    public boolean[][] getMazeMatrix() {
        return new boolean[][] {
                {false, true, false, false, false, false},
                {false, true, false, true, true, true},
                {false, true, false, true, false, true},
                {false, true, false, true, false, true},
                {false, true, false, true, false, true},
                {false, true, true, true, false, true},
                {false, false, false, false, false, true}
        };
    }
}
