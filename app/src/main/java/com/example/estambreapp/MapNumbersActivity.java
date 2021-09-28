package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MapNumbersActivity extends AppCompatActivity {

    MapNumbersModel mapNumbersModel;
    TextView instructions;
    Button buttonInstructions;
    TableLayout tableNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_numbers);


        mapNumbersModel = new MapNumbersModel();

        instructions = findViewById(R.id.mapNumbersInstructions);
        buttonInstructions = findViewById(R.id.mapNumbersButton);
        tableNumbers = findViewById(R.id.mapNumbersButtons);

        createTableButtonsNumbers();



    }

    private void createTableButtonsNumbers() {

        int[] sizeTable = mapNumbersModel.getTableButtonsSize(); // we get the size of our numbers table
        int[][] numMatrix = mapNumbersModel.getRandomNumbersMatrix(); // generates random matrix for our table

        int numRows = sizeTable[0];
        int numColumns = sizeTable[1];

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
                button.setText("1");

                //button.setOnClickListener(clickButton(row, column, button)); // Activates when button clicked

                tableRow.addView(button);
            }
        }


    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}