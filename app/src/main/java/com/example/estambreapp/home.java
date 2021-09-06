package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused)
            (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    public void moveMindfulness(View v){ startActivity(new Intent(this, mindfulnessHome.class)); }
    public void moveGames(View v){ startActivity(new Intent(this, gamesHome.class)); }

}