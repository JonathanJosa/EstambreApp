package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class InfinityMazeActivity extends AppCompatActivity {

    TableLayout mazeTable;
    InfinityMazeModel infinityMazeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_maze);

        infinityMazeModel = new InfinityMazeModel();

        mazeTable = findViewById(R.id.mazeTable);
        createMazeTable();
    }

    private void createMazeTable(){
        int[] mazeSizes = infinityMazeModel.getMazeTableSize();
        int numRows = mazeSizes[0], numColumns = mazeSizes[1];
        int[][] mazeMatrix = infinityMazeModel.getMazeMatrix();

        int[] lenSidesTable = {mazeTable.getLayoutParams().height, mazeTable.getLayoutParams().width};
        int heightBtn = lenSidesTable[0]/numRows, widthBtn = lenSidesTable[1]/numColumns, lenSideBtn;
        boolean greaterHeight;
        if(heightBtn > widthBtn){
            greaterHeight = true;
            lenSideBtn = widthBtn;
        } else {
            greaterHeight = false;
            lenSideBtn = heightBtn;
        }

        for (int i = 0; i < numRows; i++) {
            TableRow tableRow = new TableRow(this);
            TableLayout.LayoutParams mazeTableLayoutParams = new TableLayout.LayoutParams(
                    lenSideBtn, lenSideBtn, 1.0f
            );

            if(i+1 == numRows && greaterHeight)
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

                if(mazeMatrix[i][j] == 0 || mazeMatrix[i][j] == 1)
                    square.setBackgroundColor(Color.parseColor(mazeMatrix[i][j] == 0 ? "#2F3136" : "#FFFFFF"));
                else
                    square.setBackgroundResource(mazeMatrix[i][j] == 3 ? R.drawable.maze_key :
                            mazeMatrix[i][j] == 2 ? R.drawable.maze_runner : R.drawable.maze_door);

                tableRow.addView(square);
            }
        }

    }

    public void onArrowClicked(View v){ // Everytime an arrow is clicked, this function is called
        int viewID = v.getId();
        if(viewID == R.id.arrowUpBtn){
            // move up
            Toast.makeText(this, "Moving up", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowDownBtn) {
            //move down
            Toast.makeText(this, "Moving down", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowRightBtn) {
            // move right
            Toast.makeText(this, "Moving right", Toast.LENGTH_SHORT).show();
        } else if(viewID == R.id.arrowLeftBtn) {
            // move left
            Toast.makeText(this, "Moving left", Toast.LENGTH_SHORT).show();
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