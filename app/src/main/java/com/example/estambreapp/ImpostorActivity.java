package com.example.estambreapp;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ImpostorActivity extends AppCompatActivity {

    ImpostorModel impostorModel;
    TextView indicationsTitle;
    TableLayout tableButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostor);

        impostorModel = new ImpostorModel();

        indicationsTitle = findViewById(R.id.indicationsTxt);
        tableButtons = findViewById(R.id.impostorTableButtons);
        createTableButtons(); // Calling the function for the creation of the Button Matrix
    }

    private void createTableButtons() {
        indicationsTitle.setText("Encuentra las imágenes que NO se repiten");
        tableButtons.removeAllViews();
        int numRows = impostorModel.getTableButtonsSize()[0];
        int numColumns = impostorModel.getTableButtonsSize()[1];
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
        indicationsTitle.setText("¡Bien hecho!\nEncontraste todas las imágenes");
        Toast.makeText(this, "Excelente compañer@", Toast.LENGTH_SHORT).show();
        impostorModel.numOfGamesPlayed++;
        impostorModel.positionsIndividualImages = new HashMap<>(); // Restart the map
        if(impostorModel.numOfGamesPlayed == 3) endGame();
        else (new Handler()).postDelayed(() -> createTableButtons(), 3000); // Create the new table
    }

    private void endGame() { // Go to GameOptionsActivity
        (new Handler()).postDelayed(() -> startActivity(new Intent(this, GameOptionsActivity.class)), 3000);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}