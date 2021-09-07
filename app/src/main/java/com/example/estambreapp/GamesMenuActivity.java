package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GamesMenuActivity extends AppCompatActivity {

    LinearLayout ll;
    LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_menu);
        ll = (LinearLayout) findViewById(R.id.layoutMenu);
        //lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp = new LinearLayout.LayoutParams(440, 500);
        createButtons((new GamesControllerModel()).getAllGames());
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }


    public void moveMenuHome(View _v){ startActivity(new Intent(this, GamesHomeActivity.class)); }
    public void startGame(View v){ startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", v.getContentDescription().toString())); }

    private void createButtons(String[] games){
        for(String gameName:games){
            System.out.println((String) gameName);
            ImageButton game = new ImageButton(this);
            game.setContentDescription(gameName);
            game.setImageResource(getResources().getIdentifier("juego_"+ gameName.toLowerCase(), "drawable", getPackageName()));
            game.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //Background
            game.setOnClickListener((View v) -> startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", v.getContentDescription().toString())));
            ll.addView(game, lp);
        }
    }
}
