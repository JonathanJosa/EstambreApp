package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        int[] lenSideTableNumbers = { tableNumbers.getLayoutParams().height, tableNumbers.getLayoutParams().width };

        int sizeSideBtn = Math.min( lenSideTableNumbers[0]/numRows, lenSideTableNumbers[1]/numColumns );

        int marginBetweenRows = (int) Math.max(lenSideTableNumbers[0] - sizeSideBtn*numRows, 0.12*lenSideTableNumbers[0]);
        int marginBetweenColumns = (int) Math.max(lenSideTableNumbers[1] - sizeSideBtn*numColumns, 0.12*lenSideTableNumbers[1]);

        sizeSideBtn = Math.min((lenSideTableNumbers[0]-marginBetweenRows)/numRows, (lenSideTableNumbers[1]-marginBetweenColumns)/numColumns);

        marginBetweenRows = lenSideTableNumbers[0] - sizeSideBtn*numRows;
        marginBetweenColumns = lenSideTableNumbers[1] - sizeSideBtn*numColumns;


        for(int row = 0; row < numRows; row++){
            TableRow tableRow = new TableRow(this);


            TableLayout.LayoutParams tableAllRowsLayoutParams = new TableLayout.LayoutParams(
                    lenSideTableNumbers[1],
                    sizeSideBtn
            );

            tableAllRowsLayoutParams.setMargins(0,marginBetweenRows/(numRows+1),0,(row+1 != numRows) ? 0 : marginBetweenRows/(numRows+1));
            tableRow.setLayoutParams(tableAllRowsLayoutParams);
            tableNumbers.addView(tableRow); // Creating a new row in the table


            for(int column = 0; column < numColumns; column++){

                Button button = new Button(this);

                TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                        sizeSideBtn,
                        sizeSideBtn
                );

                tableRowLayoutParams.setMargins(marginBetweenColumns/(numColumns+1), 0, (column+1 != numColumns) ? 0 : marginBetweenColumns/(numColumns+1),0);

                button.setLayoutParams(tableRowLayoutParams);

                button.setBackgroundResource(R.drawable.mapnumbers_normalbtn); // Assign image to button
                button.setId(idCounter);

                button.setText(String.valueOf(numMatrix[row][column]));
                button.setTextColor(Color.parseColor("#FFFFFF"));
                button.setTextSize(25);

                button.setOnClickListener(getOnClick(idCounter, button)); // Activates when button clicked

                tableRow.addView(button);
                idCounter++;
            }
        }


    }


    private void createInstructions(){
        TableRow tr = new TableRow(this);
        int[] numbersToBeFound = mapNumbersModel.getNumbersToBeFound();

        int[] lenSizeInstructions = { instructionsLayout.getLayoutParams().height, instructionsLayout.getLayoutParams().width };
        int sizeButtons =lenSizeInstructions[1]/numbersToBeFound.length;

        //int marginBetweenRows = (int) Math.max(lenSizeInstructions[0] - sizeButtons*1, 0.12*lenSizeInstructions[0]);
        int marginBetweenColumns = (int) Math.max(lenSizeInstructions[1] - sizeButtons*numbersToBeFound.length, 0.12*lenSizeInstructions[1]);

        sizeButtons = (lenSizeInstructions[1]-marginBetweenColumns)/numbersToBeFound.length;

        int marginBetweenRows = (int) (0.5 * (lenSizeInstructions[0] - sizeButtons));
        marginBetweenColumns = lenSizeInstructions[1] - sizeButtons*numbersToBeFound.length;



        TableRow.LayoutParams tableAllRowsLayoutParams = new TableRow.LayoutParams(
                lenSizeInstructions[1],
                lenSizeInstructions[0]
        );

        tableAllRowsLayoutParams.setMargins( 0,marginBetweenRows, 0,marginBetweenRows );

        tr.setLayoutParams(tableAllRowsLayoutParams);

        instructionsLayout.addView(tr);

        for (int i = 0;i<numbersToBeFound.length;i++) {
            Button btn = new Button(this);

            TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                    sizeButtons,
                    sizeButtons
            );

            tableRowLayoutParams.setMargins((i == 0) ? 0: marginBetweenColumns/(numbersToBeFound.length),0, (i+1 != numbersToBeFound.length)? 0 : marginBetweenColumns/(numbersToBeFound.length),0);

            btn.setLayoutParams(tableRowLayoutParams);

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