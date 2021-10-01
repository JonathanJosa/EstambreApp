package com.example.estambreapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SongsApiManager {

    int musicIndex = 0; // index of our sequence of music

    // create a int array, witch contains our songs' id
    Integer[] musicUri = { R.raw.bilateralharp, R.raw.bilateralstillness, R.raw.blessing, R.raw.movement, R.raw.springsunrise, R.raw.thedeep, R.raw.transie };


    // function that shuffles my music ids array, every time the mindfulness section is opened.
    public void shuffleMusicArray(){
        List<Integer> inList = Arrays.asList(musicUri);
        Collections.shuffle(inList);
        inList.toArray(musicUri);
    }


    // function to prepare our media player
    public void prepareMediaPlayer(MediaPlayer mediaPlayer, Context context) {

        AssetFileDescriptor afd = context.getResources().openRawResourceFd(musicUri[musicIndex]);// using a afd, we get the raw music file.
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength()); // URL of music file
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

        // we change the pause/play btn icon
        if(mediaPlayer.isPlaying()) {
            button.setImageResource(R.drawable.pause);
        }

        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }


    // function that handles index songs when require next song
    public int calculateIndexNextSong( int index ) {

        if( index >= musicUri.length -1 ){
            return 0;
        }else {
            return  index + 1;
        }

    }

    // function that handles index songs when require prev song
    public int calculateIndexPrevSong( int index ){
        if( index <= 0){
            return musicUri.length - 1;
        } else {
            return index - 1;
        }
    }


}
