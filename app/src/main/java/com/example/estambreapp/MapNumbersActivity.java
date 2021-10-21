package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.KonfettiView;


public class MapNumbersActivity extends AppCompatActivity {

    MapNumbersModel mapNumbersModel;
    TextView instructions;
    LinearLayout instructionsLayout;
    TableLayout tableNumbers;
    TextView operation;
    KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_numbers);

        // instance to our game model
        mapNumbersModel = new MapNumbersModel(this);

        instructions = findViewById(R.id.mapNumbersInstructions);
        operation = findViewById(R.id.textViewOperation);
        instructionsLayout = findViewById(R.id.numbersToBefound);
        tableNumbers = findViewById(R.id.mapNumbersButtons);
        konfettiView = findViewById(R.id.viewKonfetti);

        operation.setText("");// initialize the operation label text as empty
        createTableButtonsNumbers(); // we initialize the game by creating the table of numbers and generating the numbers to find.

    }


    // function that creates our matrix table filled with numbers that the user is going to find.
    private void createTableButtonsNumbers() {
        System.out.println("Difficulty: " + mapNumbersModel.getDifficulty());
        tableNumbers.removeAllViews(); // we clean the view to refresh elements.
        mapNumbersModel.startOrEndGame(true); // we start the timer to calculate difficulty.
        int[] sizeTable = mapNumbersModel.getTableButtonsSize(); // we get the size of our numbers table
        int[][] numMatrix; // we create our matrix variable as empty.

        //we get the current diff stored in our shared preferences and we create a conditional to determine what is going to create, based on the level.
        if( mapNumbersModel.getDifficulty() >= 100 ){
            numMatrix = mapNumbersModel.getRandomNumbersMatrixWithOperation(); // we create matrix starting from the result of a mathematical operation.
        } else {
            numMatrix = mapNumbersModel.getRandomNumbersMatrix(); // generates random matrix for our table starting from random numbers generated.
        }
        // we get our No. of rows and cols.
        int numRows = sizeTable[0]; int numColumns = sizeTable[1];

        createInstructions(); // generates our instructions set.

        // we get table sizes
        int[] lenSideTableNumbers = { tableNumbers.getLayoutParams().height, tableNumbers.getLayoutParams().width };
        // we calculate the size of each btn, without margin.
        int sizeSideBtn = Math.min( lenSideTableNumbers[0]/numRows, lenSideTableNumbers[1]/numColumns );
        // we calculate the total margin space horizontal and vertical.
        int marginBetweenRows = (int) Math.max(lenSideTableNumbers[0] - sizeSideBtn*numRows, 0.12*lenSideTableNumbers[0]);
        int marginBetweenColumns = (int) Math.max(lenSideTableNumbers[1] - sizeSideBtn*numColumns, 0.12*lenSideTableNumbers[1]);
        // now we calculate the real size of a btn, with consider the margin.
        sizeSideBtn = Math.min((lenSideTableNumbers[0]-marginBetweenRows)/numRows, (lenSideTableNumbers[1]-marginBetweenColumns)/numColumns);
        // we calculate the actual margin between each btn.
        marginBetweenRows = lenSideTableNumbers[0] - sizeSideBtn*numRows;
        marginBetweenColumns = lenSideTableNumbers[1] - sizeSideBtn*numColumns;

        for(int row = 0; row < numRows; row++){
            // creating programmatically, a table row with its layout params.
            TableRow tableRow = new TableRow(this);
            TableLayout.LayoutParams tableAllRowsLayoutParams = new TableLayout.LayoutParams(
                    lenSideTableNumbers[1],
                    sizeSideBtn
            );
            // we set the margins
            tableAllRowsLayoutParams.setMargins(0,marginBetweenRows/(numRows+1),0,(row+1 != numRows) ? 0 : marginBetweenRows/(numRows+1));
            tableRow.setLayoutParams(tableAllRowsLayoutParams);
            tableNumbers.addView(tableRow); // Creating a new row in the table


            for(int column = 0; column < numColumns; column++){
                // creating programmatically, a button and add the layout params.
                Button button = new Button(this);
                TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                        sizeSideBtn,
                        sizeSideBtn
                );
                // set the margin for each btn.
                tableRowLayoutParams.setMargins(marginBetweenColumns/(numColumns+1), 0, (column+1 != numColumns) ? 0 : marginBetweenColumns/(numColumns+1),0);
                button.setLayoutParams(tableRowLayoutParams);
                button.setBackgroundResource(R.drawable.mapnumbers_normalbtn); // Assign background image normal.
                //we set a text to our btn, corresponding to each number in our generated matrix.
                button.setText(String.valueOf(numMatrix[row][column]));
                button.setTextColor(Color.parseColor("#FFFFFF")); // set text color white
                button.setTextSize(25); // size text
                button.setOnClickListener(getOnClick(button)); // Activates when button clicked
                tableRow.addView(button);
            }
        }


    }

    // function that creates our instruction set, based on the level of the game.
    @SuppressLint("SetTextI18n")
    private void createInstructions(){
        instructionsLayout.removeAllViews(); //remove previous views to refresh our view.
        // creating programmatically, a table row with its layout params.
        TableRow tr = new TableRow(this);
        int[] numbersToBeFound; // declaring empty array, to store our numbers to find ( in case we generate random numbers)
        //we get the current diff stored in our shared preferences and we create a conditional to determine what is going to create, based on the level.
        if( mapNumbersModel.getDifficulty() >= 100 ){
            int[] specialArray = {0}; // creating a special array when we generate only one number ( result of mathematical operation)
            numbersToBeFound = specialArray; // assigning to our main array, the special array
            mapNumbersModel.numbers = specialArray; // we assign numbers variable the fake/special array.
            instructions.setText("Resuelve la operación y encuentra el resultado en la tabla");
        }else {
            numbersToBeFound = mapNumbersModel.getNumbersToBeFound(); // assigning the array with numbers generated.
        }
        // we get table sizes
        int[] lenSizeInstructions = { instructionsLayout.getLayoutParams().height, instructionsLayout.getLayoutParams().width };
        int sizeButtonWhenLessThanFour = (int) (lenSizeInstructions[1] * .20); // we use this variable to calculate 20% respect the total width size of the table.
        // we calculate the size, and establish a conditional, when we get less than 4 numbers in our numbersToBeFound array.
        int sizeButtons = (numbersToBeFound.length < 4 ? sizeButtonWhenLessThanFour : (lenSizeInstructions[1]/numbersToBeFound.length) );
        // we calculate the total margin space, horizontally.
        int marginBetweenColumns = (int) Math.max(lenSizeInstructions[1] - sizeButtons*numbersToBeFound.length, 0.12*lenSizeInstructions[1]);
        // we calculate the size of each btn, without margin.
        sizeButtons = (lenSizeInstructions[1]-marginBetweenColumns)/numbersToBeFound.length;
        // we calculate the actual margin between each btn.
        int marginBetweenRows = (int) (0.5 * (lenSizeInstructions[0] - sizeButtons));
        // we calculate the real space between each btn.
        marginBetweenColumns = lenSizeInstructions[1] - sizeButtons*numbersToBeFound.length;
        // we assign layout params to our table row.
        TableRow.LayoutParams tableAllRowsLayoutParams = new TableRow.LayoutParams(
                lenSizeInstructions[1],
                lenSizeInstructions[0]
        );
        // set margins
        tableAllRowsLayoutParams.setMargins( 0,marginBetweenRows, 0,marginBetweenRows );
        tr.setLayoutParams(tableAllRowsLayoutParams);
        tr.setGravity(Gravity.CENTER_HORIZONTAL); // center the content in our instructions container.
        instructionsLayout.addView(tr);
        // we set a conditional when the diff is greater or equal to 100
        if( mapNumbersModel.getDifficulty() >= 100 ){
            // we set the mathematical operation text.
            operation.setText(mapNumbersModel.operationString + " =");
            operation.setTextSize(30);
            operation.setTextColor(Color.parseColor("#000000"));
        } else {
            operation.setText(""); // otherwise, we clear the textView
        }

        for (int i = 0;i<numbersToBeFound.length;i++) {
            // creating each button for the instruction set.
            Button btn = new Button(this);
            // assigning layout params and borders
            TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                    sizeButtons,
                    sizeButtons
            );
            // if we've got less than 4 elements in our array with numbers to find ....
            if( numbersToBeFound.length == 1 ){
                tableRowLayoutParams.setMargins(0,0,0,0);
            } else if( numbersToBeFound.length == 2 || numbersToBeFound.length == 3 ){
                tableRowLayoutParams.setMargins(( i != 0) ? 25 : 0,0,( i != numbersToBeFound.length-1) ? 0: 25,0);
            }
            else {
                tableRowLayoutParams.setMargins((i+1 == 1) ? 0: marginBetweenColumns/(numbersToBeFound.length),0, (i+1 != numbersToBeFound.length)? 0 : marginBetweenColumns/(numbersToBeFound.length),0);
            }
            btn.setLayoutParams(tableRowLayoutParams);
            btn.setBackgroundResource(R.drawable.mapnumbers_normalbtn); // set image background in normal img.
            // establish conditional, when the diff level is greater or equal than 100...
            if( mapNumbersModel.getDifficulty() >= 100 ){
                btn.setText("??"); // shows interrogative.
            }else {
                btn.setText(String.valueOf(numbersToBeFound[i])); // otherwise, we set the matrix numbers.
            }
            btn.setTextColor(Color.parseColor("#FFFFFF")); // white color
            btn.setTextSize(25);
            tr.addView(btn);
        }

    }

    // function with click listener for each button we've generated above.
    @SuppressLint("SetTextI18n")
    private View.OnClickListener getOnClick(Button selectedButton){
        return view -> {

            // starting the checking process onClick.
            mapNumbersModel.checkButtonValue( selectedButton, instructions );

            // We assure that user have founded all numbers in the table.
            if(mapNumbersModel.getNextRound()){
                //reset vital variables.
                mapNumbersModel.nextRound = false;
                mapNumbersModel.toBeFoundMap.clear(); // we clear the hashmap

                // we right away, generates another table and instructions set
                mapNumbersModel.LevelNumber++;// increment the "games played" variable.
                showKonfettiAnimation();
                instructions.setText("¡ Excelente !, encontraste todos los números  "); // change text when user already found all numbers.
                mapNumbersModel.startOrEndGame(false); // Stopping timer count
                if( mapNumbersModel.LevelNumber > 4 ){ // if user just played 4 games, we finish the game.
                    endGame();
                }else {
                    (new Handler()).postDelayed(this::createTableButtonsNumbers, 3000 ); // otherwise, we continue the game.
                }
            }
        };
    }

    // end game function, redirect to gameOptions Activity.
    private void endGame() { // Go to GameOptionsActivity
        (new Handler()).postDelayed(() -> startActivity(new Intent(this,
                GameOptionsActivity.class).putExtra("game","MapNumbers")), 3000);
    }


    // Konffetti animation that appears when the player wins
    private void showKonfettiAnimation(){
        konfettiView.build()
                .addColors(Color.WHITE, Color.GREEN, Color.rgb(255, 237, 218))
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


    public void exit(View _v) { startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", "MapNumbers")); }
}