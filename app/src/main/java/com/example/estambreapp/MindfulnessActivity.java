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
        songsApiManager = new SongsApiManager(); // instance to SongsApiManager
        Context context = getApplicationContext(); // we create a context send as a parameter in our model


        songsApiManager.shuffleMusicArray();
        songsApiManager.prepareMediaPlayer(mediaPlayer, context); // we prepare our media player
        mediaPlayer.start(); // we start MediaPlayer sound.



        // we use a listener with our MediaPlayer object, in order to switch the next song when last one finishes and I start fade in effect.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                songsApiManager.nextSong(mediaPlayer, getApplicationContext(), playPauseButton);
                songsApiManager.startFadeIn(mediaPlayer);
            }
        });

    }

    @Override
    //Configuracion de pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    public void moveHome(View _v){  songsApiManager.killMediaPlayer(mediaPlayer, playPauseButton); startActivity(new Intent(this, HomeActivity.class)); } // we kill the mediaPlayer and adjust the play/pause btn
    public void moveActivities(View _v){  songsApiManager.killMediaPlayer(mediaPlayer, playPauseButton); startActivity(new Intent(this, MindfulnessSessionActivity.class)); } // we kill the mediaPlayer and adjust the play/pause btn

    public void pressPlayBack(View v){ songsApiManager.prevSong(mediaPlayer, getApplicationContext(), playPauseButton); songsApiManager.startFadeIn(mediaPlayer);} // we call prevSong function and we initialize the fadein effect
    public void pressPlayNext(View v){ songsApiManager.handleNextSong(mediaPlayer, getApplicationContext(), playPauseButton);  } // we call fadeout to start effect into the mediaplayer
    public void pressPausePlay(View v) {
        songsApiManager.pausePlay(mediaPlayer, getApplicationContext(), playPauseButton); // we handle the play/pause button
        v.setContentDescription("play".equals(v.getContentDescription().toString()) ? "pause" : "play"); // change the button play/pause icon.
    }

}