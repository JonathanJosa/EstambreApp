package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class InfinityMazeActivity extends AppCompatActivity {

    TableLayout mazeTable;
    InfinityMazeModel infinityMazeModel;
    int[][] mazeMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_maze);

        infinityMazeModel = new InfinityMazeModel();

        mazeTable = findViewById(R.id.mazeTable);
        createMazeTable();
    }

    private void createMazeTable(){
        int[] mazeSizes = infinityMazeModel.getMazeTableSize(); // Obtaining the size of the maze
        int numRows = mazeSizes[0], numColumns = mazeSizes[1];
        mazeMatrix = infinityMazeModel.getMazeMatrix(); // Obtaining the matrix / maze

        // Obtaining the lengths of the sides of the table
        int[] lenSidesTable = {mazeTable.getLayoutParams().height, mazeTable.getLayoutParams().width};
        // Calculating the lengths that the buttons should have according to the amount of buttons needed
        int heightBtn = lenSidesTable[0]/numRows, widthBtn = lenSidesTable[1]/numColumns, lenSideBtn;
        boolean greaterHeight; // Boolean that indicates if the height is greater than the width
        // If the height is greater than the width, assign the len of the btn to be the width, and vice versa
        if(heightBtn > widthBtn){
            greaterHeight = true;
            lenSideBtn = widthBtn;
        } else {
            greaterHeight = false;
            lenSideBtn = heightBtn;
        }

        for (int i = 0; i < numRows; i++) {
            TableRow tableRow = new TableRow(this);
            // Setting the height of each row to be equal to the len of the buttons
            TableLayout.LayoutParams mazeTableLayoutParams = new TableLayout.LayoutParams(
                    lenSideBtn, lenSideBtn, 1.0f
            );

            if(i+1 == numRows && greaterHeight) // Inserting the margin in bottom if needed
                mazeTableLayoutParams.setMargins(0,0,0,lenSidesTable[0] - lenSideBtn*numRows);

            tableRow.setLayoutParams(mazeTableLayoutParams);
            mazeTable.addView(tableRow);

            for (int j = 0; j < numColumns; j++) {
                Button square = new Button(this);

                TableRow.LayoutParams mazeRowLayoutParams = new TableRow.LayoutParams(
                        lenSideBtn, lenSideBtn, 1.0f
                );

                if((j == 0 || j+1 == numColumns) && !greaterHeight)
                    mazeRowLayoutParams.setMargins(j == 0 ? (lenSidesTable[1] - lenSideBtn*numColumns)/2 : 0,
                            0,0, j != 0 ? (lenSidesTable[1] - lenSideBtn*numColumns)/2 : 0);

                square.setLayoutParams(mazeRowLayoutParams);

                if(mazeMatrix[i][j] == 0 || mazeMatrix[i][j] == 1) // If is a wall or an open path
                    square.setBackgroundColor(Color.parseColor(mazeMatrix[i][j] == 0 ? "#2F3136" : "#FFFFFF"));
                else // if is the runner, exit door or a key
                    square.setBackgroundResource(mazeMatrix[i][j] == 3 ? R.drawable.maze_key :
                            mazeMatrix[i][j] == 2 ? R.drawable.maze_runner : R.drawable.maze_door);

                tableRow.addView(square);
            }
        }

    }

    public void onArrowClicked(View v){ // Everytime an arrow is clicked, this function is called
        int viewID = v.getId();
        int[] actualPos = infinityMazeModel.getPosRunner();
        int[] mazeTableSizes = infinityMazeModel.getMazeTableSize();
        if(viewID == R.id.arrowUpBtn) { // Trying to move up
            if(actualPos[0] != 0 && mazeMatrix[actualPos[0]-1][actualPos[1]] != 1){
                // Setting the actual position to an empty path
                TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                // Setting the position above to the runner
                actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]-1);
                if(mazeMatrix[actualPos[0]-1][actualPos[1]] == 4 && infinityMazeModel.getNumKeysRemaining() == 0) { // Checking if the next position is the exit
                    /*
                    Estaría cool que solo se mostrara la puerta de salida cuando se hayan seleccionado
                    todas las llaves, para que así siga buscando y no intente salir antes.
                     */
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]-1, actualPos[1]});
                    Toast.makeText(this, "Felicidades, encontraste la salida", Toast.LENGTH_SHORT).show();
                } else{
                    if(mazeMatrix[actualPos[0]-1][actualPos[1]] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]-1][actualPos[1]] = 0; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]-1, actualPos[1]});
                }
            }
            else
                Toast.makeText(this, "No te puedes mover hacia arriba", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowDownBtn) {
            //move down
            if(actualPos[0]+1 != mazeTableSizes[0] && mazeMatrix[actualPos[0]+1][actualPos[1]] != 1){
                // Setting the actual position to an empty path
                TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                // Setting the position above to the runner
                actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]+1);
                if(mazeMatrix[actualPos[0]+1][actualPos[1]] == 4 && infinityMazeModel.getNumKeysRemaining() == 0) { // Checking if the next position is the exit
                    /*
                    Estaría cool que solo se mostrara la puerta de salida cuando se hayan seleccionado
                    todas las llaves, para que así siga buscando y no intente salir antes.
                     */
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]+1, actualPos[1]});
                    Toast.makeText(this, "Felicidades, encontraste la salida", Toast.LENGTH_SHORT).show();
                } else{
                    if(mazeMatrix[actualPos[0]+1][actualPos[1]] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]+1][actualPos[1]] = 0; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]+1, actualPos[1]});
                }
            }
            else
                Toast.makeText(this, "No te puedes mover hacia abajo", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowRightBtn) {
            // move right
            if(actualPos[1]+1 != mazeTableSizes[1] && mazeMatrix[actualPos[0]][actualPos[1]+1] != 1){
                // Setting the actual position to an empty path
                TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                // Setting the position above to the runner
                actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                if(mazeMatrix[actualPos[0]][actualPos[1]+1] == 4 && infinityMazeModel.getNumKeysRemaining() == 0) { // Checking if the next position is the exit
                    /*
                    Estaría cool que solo se mostrara la puerta de salida cuando se hayan seleccionado
                    todas las llaves, para que así siga buscando y no intente salir antes.
                     */
                    actualBtn.getChildAt(actualPos[1]+1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]+1});
                    Toast.makeText(this, "Felicidades, encontraste la salida", Toast.LENGTH_SHORT).show();
                } else{
                    if(mazeMatrix[actualPos[0]][actualPos[1]+1] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]][actualPos[1]+1] = 0; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]+1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]+1});
                }
            }
            else
                Toast.makeText(this, "No te puedes mover hacia la derecha", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowLeftBtn) {
            // move left
            if(actualPos[1] != 0 && mazeMatrix[actualPos[0]][actualPos[1]-1] != 1){
                // Setting the actual position to an empty path
                TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                // Setting the position above to the runner
                actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                if(mazeMatrix[actualPos[0]][actualPos[1]-1] == 4 && infinityMazeModel.getNumKeysRemaining() == 0) { // Checking if the next position is the exit
                    /*
                    Estaría cool que solo se mostrara la puerta de salida cuando se hayan seleccionado
                    todas las llaves, para que así siga buscando y no intente salir antes.
                     */
                    actualBtn.getChildAt(actualPos[1]-1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]-1});
                    Toast.makeText(this, "Felicidades, encontraste la salida", Toast.LENGTH_SHORT).show();
                } else{
                    if(mazeMatrix[actualPos[0]][actualPos[1]-1] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]][actualPos[1]-1] = 0; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]-1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]-1});
                }
            }
            else
                Toast.makeText(this, "No te puedes mover hacia la izquierda", Toast.LENGTH_SHORT).show();
        }
    }

    // If I try to go back to the game instructions, the app crashes
    public void exitGame(View _v){ startActivity(new Intent(this, GamesHomeActivity.class)); }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}