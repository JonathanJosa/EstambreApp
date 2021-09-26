package com.example.estambreapp;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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

    //gameProperties.getDifficulty(); // obtener dificultad
    //gameProperties.startTimeCount(); // Iniciar conteo de tiempo de reaccion
    //gameProperties.endGame(); // Terminar conteo de tiempo/Juego terminado o ganado
    //gameProperties.penalty(1.0); // Penalty de puntaje

    // Difficulty = 50.0 <- is the difficulty in which every game starts
    // 100.0 is the average medium difficulty for the game
    // Penalty(penaltyTime) <- when player makes a mistake,

    public int[] getTableButtonsSize(){
        // En vista de que no es posible asignarle un padding a los lados de los botones,
        // hay que tener unos tamaños de la tabla definidos aquí los cuales hagan que las imágenes
        // no se vean distorcionadas y que dependan de la dificultad, parámetro que estaría bien que
        // recibiera esta función
        // Por ejemplo: si la dificultad es media, la función regresará siempre un [5, 4],
        // tamaño que debió ser probado y comprobado que se ve bien en cada imagen
        // Tamaños adecuados a usar: 3-2, 4-3, 5-4, 6-4, 6-5, 7-5
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
        int[] sizeTable = getTableButtonsSize();
        int rows = sizeTable[0];
        int columns = sizeTable[1];
        int numOfImagesNeeded = rows * columns;
        int[] images = getImagesSet();
        // If the set of images is greater than the numOfImagesNeeded, delete the unnecessary elements
        if(images.length > numOfImagesNeeded){
            int[] copyImages = new int[numOfImagesNeeded];
            System.arraycopy(images,0, copyImages,0,numOfImagesNeeded);
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
        System.out.println("Positions of individual images: " + posOfIndividualImages);

        // Then, assign to each image how many times it will appear in the matrix
        ArrayList<Pair<Integer, Integer>> timesImagesAppear = new ArrayList<>(); // First: image, Second: NumOfTimes
        int numOfAppearancesImage = (numOfImagesNeeded-numOfIndividualImages) / (images.length-numOfIndividualImages);
        if(numOfAppearancesImage == 1) numOfAppearancesImage = 0; // Images cannot appear once cause they are not the individual ones
        System.out.println("Number of appearances of each image: " + numOfAppearancesImage);
        int leftOver = numOfImagesNeeded-numOfIndividualImages - (numOfAppearancesImage * (images.length-numOfIndividualImages));
        System.out.println("Left over: " + leftOver);

        // This for cycle is complex because if has to verify that the correct number of images are used
        int subtractLeftOver = leftOver/2;
        boolean isLeftOver = leftOver%2 != 0; // true
        for(int i = 0; i < images.length && numOfImagesNeeded > 0; i++){
            if(!posOfIndividualImages.isEmpty() && posOfIndividualImages.first() == i){
                timesImagesAppear.add(new Pair<>(images[i], 1)); // Images that appear only once
                posOfIndividualImages.remove(posOfIndividualImages.first());
                numOfImagesNeeded--;
            }
            else if (leftOver > 0){
                if(leftOver == 3) { // Hardcoded because it breaks my code
                    timesImagesAppear.add(new Pair<>(images[i], 3));
                    numOfImagesNeeded -= 3;
                    leftOver -= 3;
                } else {
                    if(isLeftOver){
                        timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage+subtractLeftOver+1));
                        leftOver -= subtractLeftOver+1; // 1
                        numOfImagesNeeded -= subtractLeftOver+1;
                    }else{
                        timesImagesAppear.add(new Pair<>(images[i], numOfAppearancesImage+subtractLeftOver));
                        leftOver -= subtractLeftOver; // 1
                        numOfImagesNeeded -= subtractLeftOver;
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
            for(int i = timesImagesAppear.size()-1; i >= 0; i--)
                if(timesImagesAppear.get(i).second != 1)
                    timesImagesAppear.set(i, new Pair<>(timesImagesAppear.get(i).first,
                            timesImagesAppear.get(i).second + numOfImagesNeeded));
        System.out.println("Final array: " + timesImagesAppear);

        // Finally, generate the images matrix in which every position indicates de image that should go inside
        int[][] matrixImages = new int[rows][columns];
        ArrayList<Integer> positionIsIndividual = getIndividualPositionInFinalArray(timesImagesAppear);
        System.out.println("Individual images array: " + positionIsIndividual);
        int randomImagePos;

        // Una manera de hacer que las imágenes individuales se coloquen aleatoriamente es primero asignándoles
        // posiciones a ellas, es decir, random de su row y column, y que mientras recorre la matriz, las
        // ponga cuando sea necesario. Las demás igual las pone en un random fijándose que no haya patrones
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                randomImagePos = (int) (Math.random()*timesImagesAppear.size()); // Random position in array of pairs
                if(positionIsIndividual.get(randomImagePos) == 1){ // Verify if the image is individual
                    positionsIndividualImages.put(i + "-" + j, 1); // Insert individual image in map
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

        System.out.println("Positions of individual images: " + positionsIndividualImages);
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
