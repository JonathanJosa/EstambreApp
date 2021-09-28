package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ImpostorActivity extends AppCompatActivity {

    ImpostorModel impostorModel;
    TextView indicationsTitle;
    TableLayout tableButtons;
    KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostor);

        impostorModel = new ImpostorModel(this);
        konfettiView = findViewById(R.id.konfettiAnimation);

        indicationsTitle = findViewById(R.id.indicationsTxt);
        tableButtons = findViewById(R.id.impostorTableButtons);
        createTableButtons(); // Calling the function for the creation of the Button Matrix
        impostorModel.startOrEndGame(true);
    }

    private void createTableButtons() {
        indicationsTitle.setText("Encuentra las imágenes que NO se repiten");
        tableButtons.removeAllViews();

        int[] sizeTable = impostorModel.getTableButtonsSize();
        int numRows = sizeTable[0];
        int numColumns = sizeTable[1];
        int[][] matrixImages = impostorModel.getImagesMatrix();

        // First, we obtain the size of the table (i think is in pixels)
        int[] lenSidesTable = {tableButtons.getLayoutParams().height, tableButtons.getLayoutParams().width};
        // Then, we calculate the length that each side of the images should have (without including the margin)
        int sizeSideOfButton = Math.min(lenSidesTable[0]/numRows, lenSidesTable[1]/numColumns);
        // Then, we calculate the margin that each row and column should have according to the size of the side of the images
        // I used 20% of the size of the table if the margin left is too small
        int marginBetweenRows = (int) Math.max(lenSidesTable[0] - sizeSideOfButton*numRows, 0.2*lenSidesTable[0]);
        int marginBetweenColumns = (int) Math.max(lenSidesTable[1] - sizeSideOfButton*numColumns , 0.2*lenSidesTable[1]);
        // Then, we calculate the REAL length that each side of the images should have (including the margin)
        sizeSideOfButton = Math.min((lenSidesTable[0]-marginBetweenRows)/numRows, (lenSidesTable[1]-marginBetweenColumns)/numColumns);
        // Finally, we re-calculate the margin that each row and column should have according to the size of the side of the images
        marginBetweenRows = lenSidesTable[0] - sizeSideOfButton*numRows;
        marginBetweenColumns = lenSidesTable[1] - sizeSideOfButton*numColumns;

        for(int row = 0; row < numRows; row++){
            TableRow tableRow = new TableRow(this);

            // Setting the length of height and width of the row
            TableLayout.LayoutParams tableAllRowsLayoutParams = new TableLayout.LayoutParams(
                    lenSidesTable[1],
                    sizeSideOfButton
            );

            // Setting the margins between each row
            // In the case we are inserting the last row, we insert margin top and bottom
            tableAllRowsLayoutParams.setMargins(0,marginBetweenRows/(numRows+1),0,
                    (row+1 != numRows) ? 0 : marginBetweenRows/(numRows+1));

            tableRow.setLayoutParams(tableAllRowsLayoutParams);
            tableButtons.addView(tableRow);

            // Creating a new row in the table
            for(int column = 0; column < numColumns; column++){
                Button button = new Button(this);

                // Setting the length of height and width of each element/image in row
                TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                        sizeSideOfButton,
                        sizeSideOfButton
                );

                // Setting the margins between each column
                // In the case we are inserting the last column, we insert margin left and right
                tableRowLayoutParams.setMargins(marginBetweenColumns/(numColumns+1), 0,
                        (column+1 != numColumns) ? 0 : marginBetweenColumns/(numColumns+1), 0);

                button.setLayoutParams(tableRowLayoutParams);

                button.setBackgroundResource(matrixImages[row][column]); // Assign image to button

                button.setOnClickListener(clickButton(row, column, button)); // Activates when button clicked

                tableRow.addView(button);
            }

        }

    }

    private View.OnClickListener clickButton(int row, int column, Button button) {
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(impostorModel.positionsIndividualImages.containsKey(row+"-"+column)) {
                    messageButtonClicked(true);
                    button.setBackgroundResource(R.drawable.impostor_check);
                    if(impostorModel.positionsIndividualImages.get(row+"-"+column) != 1) { // != 1
                        impostorModel.numOfIndividualImages--; // Decrease number of individual images to find
                        impostorModel.positionsIndividualImages.put(row + "-" + column, 1); // insert 1
                        if (impostorModel.numOfIndividualImages == 0)// If player selected all individual images
                            onSelectedAllCorrectImages();
                    }
                }
                else {
                    messageButtonClicked(false);
                    impostorModel.setPenalty(0.5); // Setting a penalty of 0.5 seconds
                }

                System.out.println("Images left: " + impostorModel.numOfIndividualImages);

            }
        });
    }

    private void messageButtonClicked(boolean correctButton) {
        // Function that shows the pop-up message (Java obligated me to create a separate function)
        Toast.makeText(this, (correctButton ? "Bien hecho :)" : "Esa está repetida :("),
                Toast.LENGTH_SHORT).show();
    }

    private void onSelectedAllCorrectImages(){
        impostorModel.startOrEndGame(false); // Stopping timer count
        showKonfettiAnimation();
        indicationsTitle.setText("¡Bien hecho!\nEncontraste todas las imágenes");
        Toast.makeText(this, "Excelente compañer@", Toast.LENGTH_SHORT).show();
        impostorModel.numOfGamesPlayed++;
        impostorModel.positionsIndividualImages = new HashMap<>(); // Restart the map
        if(impostorModel.numOfGamesPlayed == 3) endGame();
        else (new Handler()).postDelayed(() -> createTableButtons(), 3000); // Create the new table
    }

    private void endGame() { // Go to GameOptionsActivity
        (new Handler()).postDelayed(() -> startActivity(new Intent(this,
                GameOptionsActivity.class).putExtra("game","Impostor")), 3000);
    }

    private void showKonfettiAnimation(){
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 2000L);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}