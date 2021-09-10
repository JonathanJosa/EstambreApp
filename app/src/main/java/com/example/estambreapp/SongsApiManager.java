package com.example.estambreapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SongsApiManager {

    int musicIndex = 0;
    String[] musicUrl = new String[]{

            "https://firebasestorage.googleapis.com/v0/b/estambreapp.appspot.com/o/1.mp3?alt=media&token=a0dfb33a-6add-4fae-8ed9-a203e3644b66",
            "https://firebasestorage.googleapis.com/v0/b/estambreapp.appspot.com/o/2.mp3?alt=media&token=ea3dc64f-ddeb-4606-8461-e9dc2312634f",
            "https://firebasestorage.googleapis.com/v0/b/estambreapp.appspot.com/o/3.mp3?alt=media&token=201d5b36-e1d9-4e20-8a1a-ea6c6da18275"
    };



    public void prepareMediaPlayer(MediaPlayer mediaPlayer, Context context) {
        try {

            mediaPlayer.setDataSource(musicUrl[ musicIndex ]); // URL of music file
            mediaPlayer.prepare();
        } catch (Exception exception) {
            Toast.makeText(context,exception.getMessage(), Toast.LENGTH_SHORT).show(); // mostramos mensaje pequeño de lo que salio mal

        }
    }

    public void nextSong( MediaPlayer mediaPlayer, Context context, ImageButton button) {

        musicIndex = calculateIndexNextSong( musicIndex++ );


        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            prepareMediaPlayer(mediaPlayer, context);
            mediaPlayer.start();
        } else {
            mediaPlayer.reset();
            prepareMediaPlayer(mediaPlayer, context);
            mediaPlayer.start();
            button.setImageResource(R.drawable.pause);
        }

    }

    public void prevSong(MediaPlayer mediaPlayer, Context context, ImageButton button){

        if(mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() <= 5000 ){

            musicIndex = calculateIndexPrevSong( musicIndex-- );
            mediaPlayer.stop();
            mediaPlayer.reset();
            prepareMediaPlayer( mediaPlayer, context );
            mediaPlayer.start();

        }
        else if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
        else {
            musicIndex = calculateIndexPrevSong( musicIndex-- );
            mediaPlayer.reset();
            prepareMediaPlayer( mediaPlayer, context );
            mediaPlayer.start();
            button.setImageResource(R.drawable.pause);
        }
    }


    public void pausePlay(MediaPlayer mediaPlayer, Context context, ImageButton button){

        if(mediaPlayer.isPlaying()){

            mediaPlayer.pause();
            button.setImageResource(R.drawable.play);

        } else {

            mediaPlayer.start();
            button.setImageResource(R.drawable.pause);

        }
    }

    public void killMediaPlayer( MediaPlayer mediaPlayer, ImageButton button ){

        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;

        if(mediaPlayer.isPlaying())
            button.setImageResource(R.drawable.pause);

    }


    public int calculateIndexNextSong( int index ) {

        if( index >= musicUrl.length -1 ){
            return 0;
        }else {
            return  index + 1;
        }

    }

    public int calculateIndexPrevSong( int index ){
        if( index <= 0){
            return musicUrl.length - 1;
        } else {
            return index - 1;
        }
    }


}
