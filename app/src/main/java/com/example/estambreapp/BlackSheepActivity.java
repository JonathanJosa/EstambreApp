package com.example.estambreapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BlackSheepActivity extends AppCompatActivity{

    BlackSheepModel sheepModel;
    RelativeLayout board;
    TextView instruccion;
    int rounds = 3;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_sheep);
        board = findViewById(R.id.LinearLayoutBoard);
        instruccion = findViewById(R.id.restante);
        sheepModel  = new BlackSheepModel(this);
        createBoard();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    private void nextRound(){
        instruccion.setText("Bien hecho!!!");
        sheepModel.playing();
        sheepModel.started = false;
        rounds -= 1;
        Toast.makeText(this, "Ganaste Round!!", Toast.LENGTH_SHORT).show();
        board.removeAllViews();
        if(rounds == 0){
            (new Handler()).postDelayed(this::endGame, 1000);
            return;
        }
        sheepModel.resetGame();
        (new Handler()).postDelayed(this::createBoard, 1000);
    }

    private ImageButton setButtonsSettings(ImageButton btn, String btnType){
        btn.setContentDescription(btnType);
        btn.setImageResource(getResources().getIdentifier("blacksheep_"+btnType, "drawable", getPackageName()));
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
        btn.setOnClickListener((View v) -> {if(sheepModel.sheepClicked(v, btnType)) nextRound();});
        btn.setLayoutParams(sheepModel.randomLayouts());
        return btn;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createBoard(){
        instruccion.setText("Mezclando...");
        for(int i=0; i<sheepModel.whiteSheeps; i++){ board.addView(setButtonsSettings(new ImageButton(this), "sheepw")); }
        for(int i=0; i<sheepModel.blackSheeps; i++){ board.addView(setButtonsSettings(new ImageButton(this), "sheepb")); }
        for(View btn: board.getTouchables()){ sheepModel.mixSheeps(btn, sheepModel.totalTime); }
        (new Handler()).postDelayed(() -> {
            sheepModel.started = true;
            sheepModel.playing();
            instruccion.setText("Encuentra la oveja negra!!");
        }, (long) sheepModel.velocity * sheepModel.totalTime);
    }

    private void endGame(){
        startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","BlackSheep"));
    }

}