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
        int numRows = mazeSizes[0];
        int numColumns = mazeSizes[1];
        int[][] mazeMatrix = infinityMazeModel.getMazeMatrix();

        int[] lenSidesTable = {mazeTable.getLayoutParams().height, mazeTable.getLayoutParams().width};
        int heightBtn = lenSidesTable[0]/numRows;
        int widthBtn = lenSidesTable[1]/numColumns;

        for (int i = 0; i < numRows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    widthBtn, heightBtn, 1.0f
            ));

            /*
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

             */
            mazeTable.addView(tableRow);
            for (int j = 0; j < numColumns; j++) {
                Button square = new Button(this);

                square.setLayoutParams(new TableRow.LayoutParams(
                        widthBtn, heightBtn,
                        /*
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                         */
                        1.0f
                ));

                if(mazeMatrix[i][j] == 0 || mazeMatrix[i][j] == 1)
                    //square.setBackgroundResource(mazeMatrix[i][j] == 0 ? R.drawable.impostor_check : R.drawable.impostor_wrong);
                    square.setBackgroundColor(Color.parseColor(mazeMatrix[i][j] == 0 ? "#2F3136" : "#FFFFFF"));
                else
                    //square.setBackgroundColor(Color.parseColor("#555555"));
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