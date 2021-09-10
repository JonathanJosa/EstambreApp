package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MindfulnessActivity extends AppCompatActivity {

    public MediaPlayer mediaPlayer;
    SongsApiManager songsApiManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness);


        mediaPlayer = new MediaPlayer();

        Context context = getApplicationContext();

        songsApiManager.prepareMediaPlayer(mediaPlayer, context);
        mediaPlayer.start();



    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    public void moveHome(View _v){ startActivity(new Intent(this, HomeActivity.class)); }
    public void moveActivities(View _v){ startActivity(new Intent(this, MindfulnessSessionActivity.class)); }

    public void pressPlayBack(View _v){  }
    public void pressPlayNext(View _v){  }
    public void pressPausePlay(View v){
        v.setBackgroundResource(getResources().getIdentifier(v.getContentDescription().toString(), "drawable", getPackageName()));
        v.setContentDescription("play".equals(v.getContentDescription().toString()) ? "pause" : "play");
    }

}