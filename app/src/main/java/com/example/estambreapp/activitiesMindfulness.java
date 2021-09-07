package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class activitiesMindfulness extends AppCompatActivity {

    ImageView background;
    View nullView;
    //Handler handler;
    int[] durations = new int[]{6000, 16000, 16000, 5000, 5000};
    int imageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_mindfulness);
        background = findViewById(R.id.backgroundView);
        autoChange((long) durations[imageNo-1]);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) (new fullScreenConfig()).hideSystemUI(getWindow().getDecorView());
    }

    public void exit(View _v){ startActivity(new Intent(this, mindfulnessHome.class)); }

    public void changeButton(View v){ changeActivity("atras".equals(v.getContentDescription().toString()) ? -1 : 1); }

    private void autoChange(long time){  }

    private void changeActivity(int change){
        if(imageNo + change > 5){ exit(nullView); }
        if(imageNo + change < 1){ return; }
        background.setImageResource(getResources().getIdentifier("act"+ (imageNo + change), "drawable", getPackageName()));
        imageNo += change;
        //autoChange((long) durations[imageNo-1]);
    }

}