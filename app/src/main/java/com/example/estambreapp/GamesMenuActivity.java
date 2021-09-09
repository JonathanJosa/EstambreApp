package com.example.estambreapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.style.LineHeightSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GamesMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_menu);
        createButtons((LinearLayout) findViewById(R.id.layoutMenu), getResources().getDisplayMetrics().density);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }


    public void moveMenuHome(View _v){ startActivity(new Intent(this, GamesHomeActivity.class)); }
    public void startGame(View v){ startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", v.getContentDescription().toString())); }


    private void createButtons(LinearLayout ll, float dpi){
        boolean dir = true;
        int margins = 0;
        for(String gameName:(new GamesControllerModel()).getAllGames()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (185f * dpi),(int) (190f * dpi));
            lp.setMarginStart(dir ? 0 : (int) ((350f - 185f) * dpi ));
            lp.setMargins(dir ? 0 : (int) ((350f - 185f) * dpi ), -margins, !dir ? 0 : (int) ((350f - 185f) * dpi ), 0);
            margins = (int) (70f * dpi);
            dir = !dir;
            ImageButton game = new ImageButton(this);
            game.setContentDescription(gameName);
            game.setImageResource(getResources().getIdentifier("juego_"+ gameName.toLowerCase(), "drawable", getPackageName()));
            game.setScaleType(ImageView.ScaleType.FIT_CENTER);
            game.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
            game.setOnClickListener((View v) -> startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", v.getContentDescription().toString())));
            game.setLayoutParams(lp);
            ll.addView(game);
        }
    }
}

