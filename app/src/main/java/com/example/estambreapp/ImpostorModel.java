package com.example.estambreapp;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ImpostorModel {

    // Map that saves the position of the images that appear only once
    // The position is saved as an string with syntax: "'row'-'column'"
    // Example: The key "2-3", indicates that the position on row 2 and column 3 has an individual image
    public Map<String, Integer> positionsIndividualImages = new HashMap<>();

    public int numOfIndividualImages; // Indicates how many images appear only once

    public int numOfGamesPlayed = 0;

    public int[] getTableButtonsSize(){
        // En vista de que no es posible asignarle un padding a los lados de los botones,
        // hay que tener unos tamaños de la tabla definidos aquí los cuales hagan que las imágenes
        // no se vean distorcionadas y que dependan de la dificultad, parámetro que estaría bien que
        // recibiera esta función
        // Por ejemplo: si la dificultad es media, la función regresará siempre un [5, 4],
        // tamaño que debió ser probado y comprobado que se ve bien en cada imagen

        return new int[]{ 4, 3 }; // Hardcoded for experimentation purposes
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
        int[] images = getImagesSet();
        int numOfImagesNeeded = rows * columns;
        System.out.println("Number of images needed: " + numOfImagesNeeded);

        // First, determine the number of images that will appear individually (only once)
        numOfIndividualImages = (int) (Math.random()*(int)(images.length * 0.50)+1); // 50% of images
        System.out.println("Number of individual images: " + numOfIndividualImages);

        // Then, select randomly the images that will appear only once
        SortedSet<Integer> posOfIndividualImages = new TreeSet<>(); // Doesn't allow repetitions
        for(int i = 0; i < numOfIndividualImages; i++)
            posOfIndividualImages.add((int) (Math.random()*images.length));
        numOfIndividualImages = posOfIndividualImages.size(); // Reassign this variable to avoid errors

        // Then, assign to each image how many times it will appear in the matrix
        ArrayList<Pair<Integer, Integer>> timesImagesAppear = new ArrayList<>(); // First: image, Second: NumOfTimes
        int numOfAppearancesImage = (numOfImagesNeeded-numOfIndividualImages) / (images.length-numOfIndividualImages);
        System.out.println("Number of appearances of each image: " + numOfAppearancesImage);
        int leftOver = numOfImagesNeeded-numOfIndividualImages - (numOfAppearancesImage * (images.length-numOfIndividualImages));
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
        int[][] matrixImages = new int[rows][columns];
        ArrayList<Integer> positionIsIndividual = getIndividualPositionInFinalArray(timesImagesAppear);
        System.out.println("Individual images array: " + positionIsIndividual);
        int randomImagePos;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                randomImagePos = (int) (Math.random()*timesImagesAppear.size());
                if(positionIsIndividual.get(randomImagePos) == 1){ // Verify if the the image is individual
                    positionsIndividualImages.put(i + "-" + j, 1);
                    positionIsIndividual.set(randomImagePos, 0);
                }
                matrixImages[i][j] = timesImagesAppear.get(randomImagePos).first; // Set value to image
                timesImagesAppear.set(randomImagePos, new Pair<>(matrixImages[i][j],
                        timesImagesAppear.get(randomImagePos).second-1)); // Decrease by 1 the number of times it appears
                if(timesImagesAppear.get(randomImagePos).second == 0){
                    timesImagesAppear.remove(randomImagePos);
                    positionIsIndividual.remove(randomImagePos);
                }
            }
        }

        System.out.println(positionsIndividualImages);
        return matrixImages;
    }

    // Method for knowing the positions in which the timesImagesAppear[i].second == 1
    // Returns an array with 0's and 1's:
    //      1 indicates that in that position is an image that appears only once
    //      0 indicates that the image has more than 1 repetition
    private ArrayList<Integer> getIndividualPositionInFinalArray(ArrayList<Pair<Integer, Integer>> array){
        ArrayList<Integer> positions = new ArrayList<>();
        for (Pair<Integer, Integer> par: array)
            positions.add((par.second == 1 ? 1 : 0)); // If is an individual image, add 1
        return positions;
    }
}
