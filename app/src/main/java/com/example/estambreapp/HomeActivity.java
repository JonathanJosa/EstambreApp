package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    //Configuracion pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //Cambios de activity
    public void moveMindfulness(View v){ startActivity(new Intent(this, MindfulnessActivity.class)); }
    public void moveGames(View v){ startActivity(new Intent(this, GamesHomeActivity.class)); }

}