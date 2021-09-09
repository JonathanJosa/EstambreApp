package com.example.estambreapp;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ImpostorModel {

    public Map<String, Integer> positionsIndividualImages = new HashMap<>();

    public int[] getTableButtonsSize(){
        return new int[]{ 5, 4 }; // Hardcoded for experimentation purposes
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
        int rows = getTableButtonsSize()[0];
        int columns = getTableButtonsSize()[1];
        int[] images = getImagesSet();
        int numOfImagesNeeded = rows * columns;
        System.out.println("Number of images needed: " + numOfImagesNeeded);

        // First, determine the number of images that will appear individually (only once)
        int numOfIndividualImages = (int) (Math.random()*(int)(images.length * 0.40)+1); // 40% of images
        System.out.println("Number of individual images: " + numOfIndividualImages);

        // Then, select randomly the images that will appear only once
        SortedSet<Integer> posOfIndividualImages = new TreeSet<>(); // Doesn't allow repetitions
        for(int i = 0; i < numOfIndividualImages; i++)
            posOfIndividualImages.add((int) (Math.random()*images.length));

        // Then, assign to each image how many times it will appear in the matrix
        ArrayList<Pair<Integer, Integer>> timesImagesAppear = new ArrayList<>(); // First: image, Second: NumOfTimes
        int numOfAppearancesImage = (numOfImagesNeeded-posOfIndividualImages.size()) / (images.length-posOfIndividualImages.size());
        System.out.println("Number of appearances of each image: " + numOfAppearancesImage);
        int leftOver = numOfImagesNeeded-posOfIndividualImages.size() - (numOfAppearancesImage * (images.length-posOfIndividualImages.size()));
        System.out.println("Left over: " + leftOver);
        boolean assignedLeftOver = false;
        for(int i = 0; i < images.length; i++){
            if(!posOfIndividualImages.isEmpty() && posOfIndividualImages.first() == i){
                timesImagesAppear.add(new Pair<>(images[i], 1)); // Images that appear only once
                posOfIndividualImages.remove(posOfIndividualImages.first());
            }
            else if (!assignedLeftOver) {
                timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage+leftOver));
                assignedLeftOver = true;
            }
            else timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage));
        }
        System.out.println("Final array: " + timesImagesAppear);

        // Finally, generate the images matrix in which every position indicates de image that should go inside
        ArrayList<Integer> positionIsIndividual = getIndividualPositionInFinalArray(timesImagesAppear);
        int[][] matrixImages = new int[rows][columns];
        int randomImagePos;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                randomImagePos = (int) (Math.random()*timesImagesAppear.size());

                if(positionIsIndividual.get(randomImagePos) == 1){
                    positionsIndividualImages.put(i + "-" + j, 1);
                    positionIsIndividual.set(randomImagePos, 0);
                }

                matrixImages[i][j] = timesImagesAppear.get(randomImagePos).first; // Set value to image
                timesImagesAppear.set(randomImagePos, new Pair<>(matrixImages[i][j],
                        timesImagesAppear.get(randomImagePos).second-1)); // Decrease by 1 the number of times it appears
                if(timesImagesAppear.get(randomImagePos).second == 0) timesImagesAppear.remove(randomImagePos);
            }
        }

        System.out.println(positionsIndividualImages);
        return matrixImages;
    }

    // Method for knowing the positions in which the timesImagesAppear[i].second == 1
    private ArrayList<Integer> getIndividualPositionInFinalArray(ArrayList<Pair<Integer, Integer>> array){
        ArrayList<Integer> positions = new ArrayList<>();
        for (Pair<Integer, Integer> par: array) {
            if(par.second == 1) positions.add(1); // If is an individual image, add 1
            else positions.add(0);
        }
        return positions;
    }
}
