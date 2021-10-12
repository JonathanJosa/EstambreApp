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

public class InfinityMazeActivity extends AppCompatActivity {

    TableLayout mazeTable;
    InfinityMazeModel infinityMazeModel;
    TextView titleGame;
    int[][] mazeMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_maze);

        infinityMazeModel = new InfinityMazeModel();

        mazeTable = findViewById(R.id.mazeTable);
        titleGame = findViewById(R.id.titleTxt);

        createMazeTable();
    }

    private void createMazeTable(){
        mazeMatrix = infinityMazeModel.getMazeMatrix(); // Obtaining the matrix / maze
        int[] mazeSizes = infinityMazeModel.getMazeTableSize(); // Obtaining the size of the maze
        int numRows = mazeSizes[0], numColumns = mazeSizes[1];

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
                    square.setBackgroundColor(Color.parseColor(mazeMatrix[i][j] == 1 ? "#2F3136" : "#FFFFFF"));
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
            if(actualPos[0] != 0 && mazeMatrix[actualPos[0]-1][actualPos[1]] != 0){
                if(mazeMatrix[actualPos[0]-1][actualPos[1]] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze();
                    else Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]-1);
                    if(mazeMatrix[actualPos[0]-1][actualPos[1]] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]-1][actualPos[1]] = 1; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]-1, actualPos[1]});
                }
            }
            else Toast.makeText(this, "No te puedes mover hacia arriba", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowDownBtn) {
            //move down
            if(actualPos[0]+1 != mazeTableSizes[0] && mazeMatrix[actualPos[0]+1][actualPos[1]] != 0){
                if(mazeMatrix[actualPos[0]+1][actualPos[1]] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze(); // It's possible to get out of the maze
                    else Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]+1);
                    if(mazeMatrix[actualPos[0]+1][actualPos[1]] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]+1][actualPos[1]] = 1; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]+1, actualPos[1]});
                }
            }
            else Toast.makeText(this, "No te puedes mover hacia abajo", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowRightBtn) {
            // move right
            if(actualPos[1]+1 != mazeTableSizes[1] && mazeMatrix[actualPos[0]][actualPos[1]+1] != 0){
                if(mazeMatrix[actualPos[0]][actualPos[1]+1] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze();
                    else Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    if(mazeMatrix[actualPos[0]][actualPos[1]+1] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]][actualPos[1]+1] = 1; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]+1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]+1});
                }
            }
            else Toast.makeText(this, "No te puedes mover hacia la derecha", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowLeftBtn) {
            // move left
            if(actualPos[1] != 0 && mazeMatrix[actualPos[0]][actualPos[1]-1] != 0){
                if(mazeMatrix[actualPos[0]][actualPos[1]-1] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze();
                    else Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    if(mazeMatrix[actualPos[0]][actualPos[1]-1] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        mazeMatrix[actualPos[0]][actualPos[1]-1] = 1; // Deleting the key in the matrix
                    }
                    actualBtn.getChildAt(actualPos[1]-1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]-1});
                }
            }
            else Toast.makeText(this, "No te puedes mover hacia la izquierda", Toast.LENGTH_SHORT).show();
        }
    }

    // When the maze all the keys have been collected and the player found the exit door, this function is called
    public void completedMaze(){
        int[] posExitDoor = infinityMazeModel.getPosExitDoor(), posRunner = infinityMazeModel.getPosRunner();
        // Setting the previous runner pos to an empty path
        ((TableRow) mazeTable.getChildAt(posRunner[0])).getChildAt(posRunner[1]).setBackgroundColor(Color.parseColor("#2F3136"));
        // Setting the new pos, which was a door, to the runner
        ((TableRow) mazeTable.getChildAt(posExitDoor[0])).getChildAt(posExitDoor[1]).setBackgroundResource(R.drawable.maze_runner);
        infinityMazeModel.setPosRunner(posExitDoor);
        // Toast.makeText(this, "Felicidades, encontraste la salida", Toast.LENGTH_SHORT).show();
        titleGame.setText("Felicidades\nLograste salir de laberinto");

        // Exit game
        (new Handler()).postDelayed(() -> startActivity(new Intent(this,
                GameOptionsActivity.class).putExtra("game","InfinityMaze")), 2000);
    }

    // If I try to go back to the game instructions, the app crashes
    public void exitGame(View _v){ startActivity(new Intent(this, GamesHomeActivity.class)); }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}