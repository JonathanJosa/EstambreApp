package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MapNumbersActivity extends AppCompatActivity {

    MapNumbersModel mapNumbersModel;
    TextView instructions;
    LinearLayout instructionsLayout;
    TableLayout tableNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_numbers);


        mapNumbersModel = new MapNumbersModel();

        instructions = findViewById(R.id.mapNumbersInstructions);
        instructionsLayout = findViewById(R.id.numbersToBefound);
        tableNumbers = findViewById(R.id.mapNumbersButtons);


        createTableButtonsNumbers();
        createInstructions();



    }

    private void createTableButtonsNumbers() {

        int[] sizeTable = mapNumbersModel.getTableButtonsSize(); // we get the size of our numbers table
        int[][] numMatrix = mapNumbersModel.getRandomNumbersMatrix(); // generates random matrix for our table

        int numRows = sizeTable[0];
        int numColumns = sizeTable[1];

        int idCounter = 0;

        for(int row = 0; row < numRows; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            )); // Making the row expand and use the complete size of the horizontal space
             tableRow.setPadding(2,2,2,2);
            tableNumbers.addView(tableRow); // Creating a new row in the table
            for(int column = 0; column < numColumns; column++){
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                )); // Making the column expand and use the complete size of the vertical space

                button.setBackgroundResource(R.drawable.mapnumbers_normalbtn); // Assign image to button
                button.setId(idCounter);

                button.setText(String.valueOf(numMatrix[row][column]));
                button.setTextColor(Color.parseColor("#FFFFFF"));
                button.setOnClickListener(getOnClick(idCounter, button));
                button.setTextSize(25);

                //button.setOnClickListener(clickButton(row, column, button)); // Activates when button clicked

                tableRow.addView(button);
                idCounter++;
            }
        }


    }



    private void createInstructions(){

        int[] numbersToBeFound = mapNumbersModel.getNumbersToBeFound();

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f
        ));// Making the row expand and use the complete size of the horizontal space
        tr.setPadding(3,5,3,5);
        instructionsLayout.addView(tr);

        for (int i = 0;i<numbersToBeFound.length;i++) {
            Button btn = new Button(this);
            btn.setLayoutParams( new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f
            )); // Making the column expand and use the complete size of the vertical space
            btn.setBackgroundResource(R.drawable.mapnumbers_normalbtn);
            btn.setText(String.valueOf(numbersToBeFound[i]));
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setTextSize(25);

            tr.addView(btn);
        }

    }


    private View.OnClickListener getOnClick(final int i, Button selectedButton){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked" + i+", selected no. "+ selectedButton.getText(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}