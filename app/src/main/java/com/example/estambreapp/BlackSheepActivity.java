package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BlackSheepActivity extends AppCompatActivity{

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

    private ImageButton setButtonsSettings(ImageButton btn, String btnType){
        btn.setContentDescription(btnType);
        btn.setImageResource(getResources().getIdentifier("blacksheep_"+btnType, "drawable", getPackageName()));
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btn.setLayoutParams(sheepModel.randomLayouts());
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
        btn.setOnClickListener((View v) -> sheepModel.sheepClicked(v, btnType));
        return btn;
    }

    private void createBoard(){
        for(int i=0; i<sheepModel.sheeps.first; i++){
            board.addView(setButtonsSettings(new ImageButton(this), "sheepw"));
        }
        for(int i=0; i<sheepModel.sheeps.second; i++){
            board.addView(setButtonsSettings(new ImageButton(this), "sheepb"));
        }
    }

    private void endGame(){
        startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","BlackSheep"));
    }

}