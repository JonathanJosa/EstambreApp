package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class mindfulnessHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness_home);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused)
            (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    public void moveHome(View _v){ startActivity(new Intent(this, home.class)); }
    public void moveActivities(View _v){ startActivity(new Intent(this, activitiesMindfulness.class)); }

    public void pressPlayBack(View _v){  }
    public void pressPlayNext(View _v){  }
    public void pressPausePlay(View v){
        v.setBackgroundResource(getResources().getIdentifier(v.getContentDescription().toString(), "drawable", getPackageName()));
        v.setContentDescription("play".equals(v.getContentDescription().toString()) ? "pause" : "play");
    }

}