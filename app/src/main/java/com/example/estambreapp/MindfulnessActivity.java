package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MindfulnessActivity extends AppCompatActivity {

    public MediaPlayer mediaPlayer;
    public SongsApiManager songsApiManager;
    public ImageButton playPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness);


        playPauseButton = findViewById(R.id.imageButton16);
        mediaPlayer = new MediaPlayer();
        songsApiManager = new SongsApiManager();
        Context context = getApplicationContext();

        songsApiManager.prepareMediaPlayer(mediaPlayer, context);
        mediaPlayer.start();

    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    public void moveHome(View _v){  songsApiManager.killMediaPlayer(mediaPlayer, playPauseButton); startActivity(new Intent(this, HomeActivity.class)); }
    public void moveActivities(View _v){  songsApiManager.killMediaPlayer(mediaPlayer, playPauseButton); startActivity(new Intent(this, MindfulnessSessionActivity.class)); }

    public void pressPlayBack(View v){ songsApiManager.prevSong(mediaPlayer, getApplicationContext(), playPauseButton); }
    public void pressPlayNext(View v){  songsApiManager.nextSong(mediaPlayer, getApplicationContext(), playPauseButton); }
    public void pressPausePlay(View v) {
        songsApiManager.pausePlay(mediaPlayer, getApplicationContext(), playPauseButton);
        v.setContentDescription("play".equals(v.getContentDescription().toString()) ? "pause" : "play");
    }

}