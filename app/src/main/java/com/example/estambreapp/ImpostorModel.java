package com.example.estambreapp;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ImpostorModel {

    GamesModel gameProperties;

    // Map that saves the position of the images that appear only once
    // The position is saved as an string with syntax: "'row'-'column'"
    // Example: The key "2-3", indicates that the position on row 2 and column 3 has an individual image
    public Map<String, Integer> positionsIndividualImages = new HashMap<>();

    public int numOfIndividualImages; // Indicates how many images appear only once

    public int numOfGamesPlayed = 0;

    public ImpostorModel(Context context){
        gameProperties = new GamesModel(context, "Impostor");
    }

    public void startOrEndGame(boolean option){ // If true, start time count; otherwise, end time count
        if (option) gameProperties.startTimeCount();
        else gameProperties.endGame();
    }

    public void setPenalty(double penaltyTime){
        gameProperties.penalty(penaltyTime); // Sending penalty in seconds
    }

    public int[] getTableButtonsSize(){
        // Difficulty = 50.0 <- is the difficulty in which every game starts
        // 100.0 is the average medium difficulty for the game
        // Penalty(penaltyTime) <- when player makes a mistake, give it a penalty. If you send 1, it equals to 1 second

        double numDifficultyOfGame = gameProperties.getDifficulty();
        System.out.println("The difficulty for this game is: " + numDifficultyOfGame);
        return (numDifficultyOfGame < 50) ? new int[]{ 3, 2 } :
                (numDifficultyOfGame < 75) ? new int[]{ 4, 3 } :
                (numDifficultyOfGame < 110) ? new int[]{ 5, 4 } :
                (numDifficultyOfGame < 130) ? new int[]{ 6, 4 } :
                (numDifficultyOfGame < 160) ? new int[]{ 6, 5 } : new int[]{ 7, 5 };
    }


    private int[] getImagesSet() { // Hardcoded images for experimentation purposes
        // The idea here is to return a random set of images depending on the difficulty
        return (new int[]{
                R.drawable.impostor_plant1,
                R.drawable.impostor_plant2,
                R.drawable.impostor_plant3,
                R.drawable.impostor_plant4,
                R.drawable.impostor_plant5,
                R.drawable.impostor_plant6,
                R.drawable.impostor_plant7,
                R.drawable.impostor_plant8,
        });
    }

    public int[][] getImagesMatrix(){
        int[] sizeTable = getTableButtonsSize();
        int rows = sizeTable[0];
        int columns = sizeTable[1];
        int numOfImagesNeeded = rows * columns;
        int[] images = getImagesSet();
        // If the set of images is greater than the numOfImagesNeeded, delete the unnecessary elements
        if(images.length > numOfImagesNeeded){
            int[] copyImages = new int[numOfImagesNeeded];
            System.arraycopy(images,0, copyImages,0,numOfImagesNeeded); // Copying only the images needed
            images = copyImages;
        }
        System.out.println("Number of images needed: " + numOfImagesNeeded);

        // First, determine the number of images that will appear individually (only once)
        numOfIndividualImages = (int) (Math.random()*(int)(images.length * 0.50)+1); // 50% of images

        // Then, select randomly the images that will appear only once
        SortedSet<Integer> posOfIndividualImages = new TreeSet<>(); // Doesn't allow repetitions
        for(int i = 0; i < numOfIndividualImages; i++)
            posOfIndividualImages.add((int) (Math.random()*images.length));
        numOfIndividualImages = posOfIndividualImages.size(); // Reassign this variable to avoid errors
        System.out.println("Number of individual images: " + numOfIndividualImages);

        // Then, assign to each image how many times it will appear in the matrix
        ArrayList<Pair<Integer, Integer>> timesImagesAppear = new ArrayList<>(); // First: image, Second: NumOfTimes
        int numOfAppearancesImage = (numOfImagesNeeded-numOfIndividualImages) / (images.length-numOfIndividualImages);
        if(numOfAppearancesImage == 1) numOfAppearancesImage = 0; // Images cannot appear once cause they are not the individual ones
        System.out.println("Number of appearances of each image: " + numOfAppearancesImage);
        int leftOver = numOfImagesNeeded-numOfIndividualImages - (numOfAppearancesImage * (images.length-numOfIndividualImages));
        System.out.println("Left over: " + leftOver);

        // Did this just to divide the left over into two images (if possible)
        int subtractLeftOver = leftOver/2; // 2
        boolean isLeftOver = leftOver%2 != 0; // true

        // Then, this for cycle has to verify that the correct number of images are used
        for(int i = 0; i < images.length && numOfImagesNeeded > 0; i++){
            if(!posOfIndividualImages.isEmpty() && posOfIndividualImages.first() == i){
                timesImagesAppear.add(new Pair<>(images[i], 1)); // Images that appear only once
                posOfIndividualImages.remove(posOfIndividualImages.first());
                numOfImagesNeeded--;
            }
            else if (leftOver > 0) {
                if (leftOver == 3 || leftOver == 2) { // Hardcoded because it breaks my code
                    timesImagesAppear.add(new Pair<>(images[i], leftOver));
                    numOfImagesNeeded -= leftOver;
                    leftOver -= leftOver;
                } else {
                    if(isLeftOver){
                        timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage+subtractLeftOver+1));
                        leftOver -= (numOfAppearancesImage+subtractLeftOver+1);
                        numOfImagesNeeded -= (numOfAppearancesImage+subtractLeftOver+1);
                    }else{
                        timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage+subtractLeftOver));
                        leftOver -= (numOfAppearancesImage+subtractLeftOver);
                        numOfImagesNeeded -= (numOfAppearancesImage+subtractLeftOver);
                    }
                    isLeftOver = false;
                }
            }
            else if(numOfAppearancesImage > 0){
                timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage));
                numOfImagesNeeded -= numOfAppearancesImage;
            }
        }
        if(numOfImagesNeeded > 0)
            // if the amount of images used is not complete, assign the left over to the last non-individual image
            for(int i = timesImagesAppear.size()-1; i >= 0; i--)
                if(timesImagesAppear.get(i).second != 1){
                    timesImagesAppear.set(i, new Pair<>(timesImagesAppear.get(i).first,
                            timesImagesAppear.get(i).second + numOfImagesNeeded));
                    break;
                }
        System.out.println("Final array: " + timesImagesAppear);

        // --- CREATING THE FINAL MATRIX OF IMAGES --- //
        int[][] matrixImages = new int[rows][columns]; // Final matrix

        // Here, we determine the positions in which the individual images will appear
        positionsIndividualImages = generatePositionsIndividualImages(timesImagesAppear, rows, columns);
        int randomImagePos;
        // Then, start to insert the images in the matrix
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(positionsIndividualImages.containsKey(i+"-"+j)){ // Check if in that position should go an individual image
                    matrixImages[i][j] = positionsIndividualImages.get(i+"-"+j);
                } else { // Insert the random image
                    randomImagePos = generateRandomImagePos(timesImagesAppear); // Random position in array of pairs (avoiding positions of individual images)
                    matrixImages[i][j] = timesImagesAppear.get(randomImagePos).first;
                    if(timesImagesAppear.get(randomImagePos).second-1 == 1)
                        timesImagesAppear.set(randomImagePos, new Pair<>(matrixImages[i][j],
                                timesImagesAppear.get(randomImagePos).second-2)); // Decrease by 2 the number of times it appears (to avoid it turn into 1)
                    else if(timesImagesAppear.get(randomImagePos).second-1 <= 0)
                        timesImagesAppear.remove(randomImagePos); // Delete element
                    else
                        timesImagesAppear.set(randomImagePos, new Pair<>(matrixImages[i][j],
                                timesImagesAppear.get(randomImagePos).second-1)); // Decrease by 1 the number of times it appears
                }
            }
        }

        System.out.println("Positions of individual images: " + positionsIndividualImages);
        return matrixImages;
    }

    // Method for generating the positions in which the individual images will go
    private Map<String, Integer> generatePositionsIndividualImages(
            ArrayList<Pair<Integer, Integer>> timesImagesAppear, int rows, int columns) {
        Map<String, Integer> map = new HashMap<>();
        int posRow, posColumn;
        for(int i = 0; i < timesImagesAppear.size(); i++){
            if(timesImagesAppear.get(i).second == 1){
                posRow = (int) (Math.random()*rows);
                posColumn = (int) (Math.random()*columns);
                if(!map.containsKey(posRow + "-" + posColumn))
                    map.put(posRow + "-" + posColumn, timesImagesAppear.get(i).first);
                else i--;
            }
        }
        return map;
    }

    // Method for not generating a position which corresponds to an individual image
    private int generateRandomImagePos(ArrayList<Pair<Integer, Integer>> timesImagesAppear) {
        int random = (int) (Math.random()*timesImagesAppear.size());
        boolean hasOthers = false;
        for (Pair<Integer, Integer> par: timesImagesAppear) // Traverse the array to check if there are non-individual images
            if(par.second != 1){
                hasOthers = true;
                break;
            }
        if (!hasOthers) return 0; // The array has only individuals so return anything
        while(timesImagesAppear.get(random).second == 1)
            random = (int) (Math.random()*timesImagesAppear.size());
        return random;
    }

}
