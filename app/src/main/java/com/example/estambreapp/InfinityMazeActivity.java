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

import java.util.HashSet;

public class InfinityMazeActivity extends AppCompatActivity {

    TableLayout mazeTable;
    InfinityMazeModel infinityMazeModel;
    TextView titleGame;
    int[][] mazeMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_maze);

        infinityMazeModel = new InfinityMazeModel(this);

        mazeTable = findViewById(R.id.mazeTable);
        titleGame = findViewById(R.id.titleTxt);

        infinityMazeModel.startGame(); // Starting time counter
        infinityMazeModel.setGameDifficulty(); // Setting the difficulty

        createMazeTable(); // Automatically create and show the maze
    }

    private void createMazeTable(){
        mazeMatrix = infinityMazeModel.getMazeMatrix(); // Obtaining the matrix / maze
        int[] mazeSizes = infinityMazeModel.getMazeTableSize(); // Obtaining the size of the maze
        int numRows = mazeSizes[0], numColumns = mazeSizes[1];

        // Obtaining the lengths of the sides of the table
        int[] lenSidesTable = {mazeTable.getLayoutParams().height, mazeTable.getLayoutParams().width};
        System.out.println("Length table: " + lenSidesTable[0] + ", " + lenSidesTable[1]);
        // Calculating the lengths that the buttons should have according to the amount of buttons needed
        int heightBtn = lenSidesTable[0]/numRows, widthBtn = lenSidesTable[1]/numColumns, lenSideBtn;
        boolean greaterHeight; // Boolean that indicates if the height is greater than the width
        // If the height is greater than the width, assign the len of the btn to be the width, and vice versa
        System.out.println("Height btn: " + heightBtn + ", Width btn: " + widthBtn);
        if(heightBtn > widthBtn){
            greaterHeight = true;
            lenSideBtn = widthBtn;
        } else {
            greaterHeight = false;
            lenSideBtn = heightBtn;
        }
        System.out.println("Final len side of btn: " + lenSideBtn + ", Is height greater? " + greaterHeight);
        for (int i = 0; i < numRows; i++) {
            TableRow tableRow = new TableRow(this);
            // Setting the height of each row to be equal to the len of the buttons
            TableLayout.LayoutParams mazeTableLayoutParams = new TableLayout.LayoutParams(
                    lenSideBtn, lenSideBtn, 1.0f
            );

            if((i+1 == numRows || i == 0) && greaterHeight) // Inserting the margin in top and bottom if needed
                mazeTableLayoutParams.setMargins(0, i == 0 ? (lenSidesTable[0] - lenSideBtn*numRows)/2 : 0,
                        0, i != 0 ? (lenSidesTable[0] - lenSideBtn*numRows)/2 : 0);

            tableRow.setLayoutParams(mazeTableLayoutParams);
            mazeTable.addView(tableRow);

            for (int j = 0; j < numColumns; j++) {
                Button square = new Button(this);

                TableRow.LayoutParams mazeRowLayoutParams = new TableRow.LayoutParams(
                        lenSideBtn, lenSideBtn, 1.0f
                );

                if((j == 0 || j+1 == numColumns) && !greaterHeight){
                    System.out.println("Margin to insert: " + ((lenSidesTable[1] - lenSideBtn*numColumns)/2));
                    mazeRowLayoutParams.setMargins(j == 0 ? (lenSidesTable[1] - lenSideBtn*numColumns)/2 : 0,
                            0, j != 0 ? (lenSidesTable[1] - lenSideBtn*numColumns)/2 : 0, 0);
                }

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
                    else { // If the player has not found all the keys
                        infinityMazeModel.setPenalty(1); // Add a penalty of 1 sec to the difficulty
                        Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]-1);
                    if(mazeMatrix[actualPos[0]-1][actualPos[1]] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        infinityMazeModel.deletePosKey((actualPos[0]-1) + "-" + actualPos[1]); // Removing key from set
                        mazeMatrix[actualPos[0]-1][actualPos[1]] = 1; // Deleting the key in the matrix
                    }
                    // Updating the runner position
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]-1, actualPos[1]});
                }
            }
        } else if(viewID == R.id.arrowDownBtn) {
            //move down
            if(actualPos[0]+1 != mazeTableSizes[0] && mazeMatrix[actualPos[0]+1][actualPos[1]] != 0){
                if(mazeMatrix[actualPos[0]+1][actualPos[1]] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze(); // It's possible to get out of the maze
                    else { // If the player has not found all the keys
                        infinityMazeModel.setPenalty(1); // Add a penalty of 1 sec to the difficulty
                        Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]+1);
                    if(mazeMatrix[actualPos[0]+1][actualPos[1]] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        infinityMazeModel.deletePosKey((actualPos[0]+1) + "-" + actualPos[1]); // Removing key from set
                        mazeMatrix[actualPos[0]+1][actualPos[1]] = 1; // Deleting the key in the matrix
                    }
                    // Updating the runner position
                    actualBtn.getChildAt(actualPos[1]).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0]+1, actualPos[1]});
                }
            }
        } else if(viewID == R.id.arrowRightBtn) {
            // move right
            if(actualPos[1]+1 != mazeTableSizes[1] && mazeMatrix[actualPos[0]][actualPos[1]+1] != 0){
                if(mazeMatrix[actualPos[0]][actualPos[1]+1] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze();
                    else { // If the player has not found all the keys
                        infinityMazeModel.setPenalty(1); // Add a penalty of 1 sec to the difficulty
                        Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    if(mazeMatrix[actualPos[0]][actualPos[1]+1] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        infinityMazeModel.deletePosKey(actualPos[0] + "-" + (actualPos[1]+1)); // Removing key from set
                        mazeMatrix[actualPos[0]][actualPos[1]+1] = 1; // Deleting the key in the matrix
                    }
                    // Updating the runner position
                    actualBtn.getChildAt(actualPos[1]+1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]+1});
                }
            }
        } else if(viewID == R.id.arrowLeftBtn) {
            // move left
            if(actualPos[1] != 0 && mazeMatrix[actualPos[0]][actualPos[1]-1] != 0){
                if(mazeMatrix[actualPos[0]][actualPos[1]-1] == 4) { // Checking if the next position is the exit
                    if(infinityMazeModel.getNumKeysRemaining() == 0) completedMaze();
                    else { // If the player has not found all the keys
                        infinityMazeModel.setPenalty(1); // Add a penalty of 1 sec to the difficulty
                        Toast.makeText(this, "No has encontrado todas las llaves", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Setting the actual position to an empty path
                    TableRow actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    actualBtn.getChildAt(actualPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
                    // Setting the position above to the runner
                    actualBtn = (TableRow) mazeTable.getChildAt(actualPos[0]);
                    if(mazeMatrix[actualPos[0]][actualPos[1]-1] == 3){ // Checking if the next position is a key
                        infinityMazeModel.setNumKeysRemaining(infinityMazeModel.getNumKeysRemaining()-1); // Decreasing the num of keys remaining
                        infinityMazeModel.deletePosKey(actualPos[0] + "-" + (actualPos[1]-1)); // Removing key from set
                        mazeMatrix[actualPos[0]][actualPos[1]-1] = 1; // Deleting the key in the matrix
                    }
                    // Updating the runner position
                    actualBtn.getChildAt(actualPos[1]-1).setBackgroundResource(R.drawable.maze_runner);
                    infinityMazeModel.setPosRunner(new int[]{actualPos[0], actualPos[1]-1});
                }
            }
        }

        // Finally, check if the new position has close keys
        searchCloseKeys(infinityMazeModel.getPosRunner());

    }

    // Function that receives a position, and shows the keys that are close to that position
    private void searchCloseKeys(int[] actualPos){
        HashSet<String> posKeys = infinityMazeModel.getPosKeys(); // Get the keys positions set
        for(String key: posKeys){
            String[] keyPosStr = key.split("-"); // Split the keys
            int[] keyPos = new int[]{Integer.parseInt(keyPosStr[0]), Integer.parseInt(keyPosStr[1])};

            // If the actual position of the runner is also 2 or less blocks close to the key, show the key
            if(Math.abs(keyPos[0]-actualPos[0]) < 3 && Math.abs(keyPos[1]-actualPos[1]) < 3) {
                TableRow actualBtn = (TableRow) mazeTable.getChildAt(keyPos[0]);
                actualBtn.getChildAt(keyPos[1]).setBackgroundResource(R.drawable.maze_key);
            } else { // If not, draw the position of the key to open path
                TableRow actualBtn = (TableRow) mazeTable.getChildAt(keyPos[0]);
                actualBtn.getChildAt(keyPos[1]).setBackgroundColor(Color.parseColor("#2F3136"));
            }
        }
    }

    // When the maze all the keys have been collected and the player found the exit door, this function is called
    public void completedMaze(){
        infinityMazeModel.endGame(); // Stopping time counter

        int[] posExitDoor = infinityMazeModel.getPosExitDoor(), posRunner = infinityMazeModel.getPosRunner();

        // Setting the previous runner pos to an empty path
        ((TableRow) mazeTable.getChildAt(posRunner[0])).getChildAt(posRunner[1]).setBackgroundColor(Color.parseColor("#2F3136"));
        // Setting the new pos, which was a door, to the runner
        ((TableRow) mazeTable.getChildAt(posExitDoor[0])).getChildAt(posExitDoor[1]).setBackgroundResource(R.drawable.maze_runner);
        infinityMazeModel.setPosRunner(posExitDoor);

        titleGame.setText("¡Lograste salir del laberinto!");

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