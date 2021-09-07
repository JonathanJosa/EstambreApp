package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ImpostorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostor);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}