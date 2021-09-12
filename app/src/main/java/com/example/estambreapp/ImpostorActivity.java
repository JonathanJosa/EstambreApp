package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

        impostorModel = new ImpostorModel();
        konfettiView = findViewById(R.id.konfettiAnimation);

        indicationsTitle = findViewById(R.id.indicationsTxt);
        tableButtons = findViewById(R.id.impostorTableButtons);
        createTableButtons(); // Calling the function for the creation of the Button Matrix
    }

    private void createTableButtons() {
        indicationsTitle.setText("Encuentra las imágenes que NO se repiten");
        tableButtons.removeAllViews();

        int[] sizeTable = impostorModel.getTableButtonsSize();
        int numRows = sizeTable[0];
        int numColumns = sizeTable[1];
        int[][] matrixImages = impostorModel.getImagesMatrix();

        for(int row = 0; row < numRows; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            )); // Making the row expand and use the complete size of the horizontal space
            tableRow.setPadding(1,70,1,1);
            tableButtons.addView(tableRow); // Creating a new row in the table
            for(int column = 0; column < numColumns; column++){
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                )); // Making the column expand and use the complete size of the vertical space

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
                if(impostorModel.positionsIndividualImages.get(row+"-"+column) != null) {
                    messageButtonClicked(true);
                    button.setBackgroundResource(R.drawable.impostor_check);
                    if(impostorModel.positionsIndividualImages.get(row+"-"+column) == 1) {
                        impostorModel.numOfIndividualImages--; // Decrease number of individual images to find
                        impostorModel.positionsIndividualImages.put(row + "-" + column, 2);
                        if (impostorModel.numOfIndividualImages == 0)// If player selected all individual images
                            onSelectedAllCorrectImages();
                    }
                }
                else messageButtonClicked(false);
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
        showKonfettiAnimation();
        indicationsTitle.setText("¡Bien hecho!\nEncontraste todas las imágenes");
        Toast.makeText(this, "Excelente compañer@", Toast.LENGTH_SHORT).show();
        impostorModel.numOfGamesPlayed++;
        impostorModel.positionsIndividualImages = new HashMap<>(); // Restart the map
        if(impostorModel.numOfGamesPlayed == 3) endGame();
        else (new Handler()).postDelayed(() -> createTableButtons(), 3000); // Create the new table
    }

    private void endGame() { // Go to GameOptionsActivity
        (new Handler()).postDelayed(() -> startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","Impostor")), 3000);
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