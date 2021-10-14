package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MapNumbersActivity extends AppCompatActivity {

    MapNumbersModel mapNumbersModel;
    TextView instructions;
    LinearLayout instructionsLayout;
    TableLayout tableNumbers;
    TextView operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_numbers);


        mapNumbersModel = new MapNumbersModel();

        instructions = findViewById(R.id.mapNumbersInstructions);
        operation = findViewById(R.id.textViewOperation);
        instructionsLayout = findViewById(R.id.numbersToBefound);
        tableNumbers = findViewById(R.id.mapNumbersButtons);


        operation.setText("");

        createTableButtonsNumbers();

        //mapNumbersModel.generateOperationAndNumberToBeFound(5);



    }

    private void createTableButtonsNumbers() {


        tableNumbers.removeAllViews();


        int[] sizeTable = mapNumbersModel.getTableButtonsSize(); // we get the size of our numbers table
        //int[][] numMatrix = mapNumbersModel.getRandomNumbersMatrix(); // generates random matrix for our table
        int[][] numMatrix = mapNumbersModel.getRandomNumbersMatrixWithOperation();



        int numRows = sizeTable[0];
        int numColumns = sizeTable[1];

        createInstructions();
        /*
        for (int n = 0 ; n < numMatrix.length ; n++)
        {
            System.out.println(Arrays.toString(numMatrix[n]));
        }*/


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
                //button.setId(idCounter);

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

        instructionsLayout.removeAllViews(); // removemos vistas anteriores, para refrescar la view.

        int[] arraySenuelo = {0};

        TableRow tr = new TableRow(this);
        //int[] numbersToBeFound = mapNumbersModel.getNumbersToBeFound();
        int[] numbersToBeFound = arraySenuelo;

        int[] lenSizeInstructions = { instructionsLayout.getLayoutParams().height, instructionsLayout.getLayoutParams().width };

        // variable que vamos a usar para clacular el 20% del valor del ancho del contenedor
        int sizeButtonWhenLessThanFour = (int) (lenSizeInstructions[1] * .20);


        int sizeButtons = (numbersToBeFound.length < 4 ? sizeButtonWhenLessThanFour : (lenSizeInstructions[1]/numbersToBeFound.length) );


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

        tr.setGravity(Gravity.CENTER_HORIZONTAL);

        instructionsLayout.addView(tr);

       operation.setText("2 + 5 + 3 =");
       operation.setTextSize(30);
       operation.setTextColor(Color.parseColor("#000000"));


        for (int i = 0;i<numbersToBeFound.length;i++) {
            Button btn = new Button(this);

            TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                    sizeButtons,
                    sizeButtons
            );

            if( numbersToBeFound.length <4 ){
                tableRowLayoutParams.setMargins(0,0, 0,0);
            } else {
                tableRowLayoutParams.setMargins((i+1 == 1) ? 0: marginBetweenColumns/(numbersToBeFound.length),0, (i+1 != numbersToBeFound.length)? 0 : marginBetweenColumns/(numbersToBeFound.length),0);
            }


            btn.setLayoutParams(tableRowLayoutParams);

            btn.setBackgroundResource(R.drawable.mapnumbers_normalbtn);
            //btn.setText(String.valueOf(numbersToBeFound[i]));
            btn.setText("??");
            btn.setTextColor(Color.parseColor("#FFFFFF"));
            btn.setTextSize(25);

            tr.addView(btn);
        }

    }


    private View.OnClickListener getOnClick(final int idButton, Button selectedButton){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // aqui iniciamos el proceso de chequeo al momento de presionar un botón.
                mapNumbersModel.checkButtonValue( selectedButton, instructions );


                // comprobamos que hayamos completado los numeros a encontrar de la ronda.
                if(mapNumbersModel.getNextRound()){

                    //seteamos las variables importantes al estado original
                    mapNumbersModel.nextRound = false;
                    mapNumbersModel.toBeFound.clear(); // we clear the hashmap

                    // volvemos a generar otra matriz y otros numeros a buscar


                    mapNumbersModel.LevelNumber++;
                    mapNumbersModel.calculateLevelParams(mapNumbersModel.LevelNumber);


                    instructions.setText("¡ Excelente !, encontraste todos los números  ");
                    if( mapNumbersModel.LevelNumber > 4 ){
                        endGame();
                    }else {
                        (new Handler()).postDelayed( () -> createTableButtonsNumbers(), 3000 );
                    }


                }

                //Toast.makeText(getApplicationContext(), "Clicked" + idButton +", selected no. "+ selectedButton.getText(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void endGame() { // Go to GameOptionsActivity
        (new Handler()).postDelayed(() -> startActivity(new Intent(this,
                GameOptionsActivity.class).putExtra("game","MapNumbers")), 3000);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}