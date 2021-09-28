package com.example.estambreapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SongsApiManager {

    int musicIndex = 0; // index of our sequence of music

    // create a String array, witch contains our song links
    String[] musicUrl = new String[]{

            "https://firebasestorage.googleapis.com/v0/b/estambreapp.appspot.com/o/1.mp3?alt=media&token=a0dfb33a-6add-4fae-8ed9-a203e3644b66",
            "https://firebasestorage.googleapis.com/v0/b/estambreapp.appspot.com/o/2.mp3?alt=media&token=ea3dc64f-ddeb-4606-8461-e9dc2312634f",
            "https://firebasestorage.googleapis.com/v0/b/estambreapp.appspot.com/o/3.mp3?alt=media&token=201d5b36-e1d9-4e20-8a1a-ea6c6da18275"
    };


    // function to prepare our media player
    public void prepareMediaPlayer(MediaPlayer mediaPlayer, Context context) {
        try {
            mediaPlayer.setDataSource(musicUrl[ musicIndex ]); // URL of music file
            mediaPlayer.prepare();
        } catch (Exception exception) {
            Toast.makeText(context,exception.getMessage(), Toast.LENGTH_SHORT).show(); // Show a message to know what went wrong

        }
    }

    // function to next song
    // we receive MediaPlayer from the view, the app context, and the Play/Pause btn
    public void nextSong( MediaPlayer mediaPlayer, Context context, ImageButton button) {

        musicIndex = calculateIndexNextSong( musicIndex++ ); // calculate the current index of the song

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

    //function to previous song
    // we receive MediaPlayer from the view, the app context, and the Play/Pause btn
    public void prevSong(MediaPlayer mediaPlayer, Context context, ImageButton button){

        // if our mediaplayer is playing and the current time of the song is less or equal than 5 ms.
        if(mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() <= 5000 ){

            musicIndex = calculateIndexPrevSong( musicIndex-- ); // we calculate and refresh the current index song.
            mediaPlayer.stop();
            mediaPlayer.reset();
            prepareMediaPlayer( mediaPlayer, context );
            mediaPlayer.start();

        }
        else if (mediaPlayer.isPlaying()){ // if media player is only playing a tack.
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
        else { // otherwise, when mediaPlayer is paused
            musicIndex = calculateIndexPrevSong( musicIndex-- );
            mediaPlayer.reset();
            prepareMediaPlayer( mediaPlayer, context );
            mediaPlayer.start();
            button.setImageResource(R.drawable.pause);
        }
    }

    // function to handle play/pause button
    public void pausePlay(MediaPlayer mediaPlayer, Context context, ImageButton button){

        if(mediaPlayer.isPlaying()){

            mediaPlayer.pause();
            button.setImageResource(R.drawable.play);

        } else {

            mediaPlayer.start();
            button.setImageResource(R.drawable.pause);

        }
    }


    // function to kill MediaPlayer
    public void killMediaPlayer( MediaPlayer mediaPlayer, ImageButton button ){

        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;


        // we change the pause/play btn icon
        if(mediaPlayer.isPlaying())
            button.setImageResource(R.drawable.pause);

    }


    // function that handles index songs when require next song
    public int calculateIndexNextSong( int index ) {

        if( index >= musicUrl.length -1 ){
            return 0;
        }else {
            return  index + 1;
        }

    }

    // function that handles index songs when require prev song
    public int calculateIndexPrevSong( int index ){
        if( index <= 0){
            return musicUrl.length - 1;
        } else {
            return index - 1;
        }
    }


}
