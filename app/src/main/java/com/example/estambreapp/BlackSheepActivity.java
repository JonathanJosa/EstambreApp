package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class BlackSheepActivity extends AppCompatActivity {

    BlackSheepModel sheepModel;
    LinearLayout board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_sheep);
        board = findViewById(R.id.LinearLayoutBoard);
        sheepModel  = new BlackSheepModel(this);
        createBoard();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    private void createBoard(){
        for(ImageButton button: sheepModel.getButtons()){
            board.addView(button);
        }
    }

    private void endGame(){
        startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","BlackSheep"));
    }

}