package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class SimonSaysActivity extends AppCompatActivity {

    // Instance to game model

    SimonSaysModel model;
    Button[] btn; // declare a buttons array, for our game buttons

    // we instance our labels and buttons
    Button redBtn;
    Button blueBtn;
    Button yellowBtn;
    Button greenBtn;
    Button startBtn;
    TextView scorelbl;
    TextView gameState;
    TextView instructions;

    KonfettiView konfettiView;

    // we create a instance to our MediaPlayers, one for each of our mp3 effect sounds.
    private MediaPlayer redPlayer;
    private MediaPlayer bluePlayer;
    private MediaPlayer yellowPlayer;
    private MediaPlayer greenPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_says);

        model = new SimonSaysModel(this);

        // we assign the object to our view component
        redBtn = findViewById(R.id.redBtn);
        blueBtn = findViewById(R.id.blueBtn);
        yellowBtn = findViewById(R.id.yellowBtn);
        greenBtn = findViewById(R.id.greenBtn);
        startBtn = findViewById(R.id.startBtn);

        scorelbl = findViewById(R.id.scoreLbl);
        gameState = findViewById(R.id.statelbl);
        instructions = findViewById(R.id.instructionsText);

        //we assign our sound effects in our MediaPlayers

        redPlayer = MediaPlayer.create(this, R.raw.red);
        bluePlayer = MediaPlayer.create(this, R.raw.blue);
        yellowPlayer = MediaPlayer.create(this, R.raw.yellow);
        greenPlayer = MediaPlayer.create(this, R.raw.green);

        // we assign our buttons to our array of buttons
        btn = new Button[]{ redBtn, blueBtn, yellowBtn, greenBtn };

        //set our game state label as empty.
        gameState.setText("");
        instructions.setText("Presiona el botón 'INICIO' para comenzar a jugar ");

        konfettiView = findViewById(R.id.viewKonfetti2);

        //we desable our buttons before starting the game
        isEnabledBtn(false);

        scorelbl.setText("Nivel: " + model.getDifficulty());

    }


    @Override
    //Configuracion para pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }


    // we declare a onDestroy method to handle MediaPlayers
    @Override
    protected void onDestroy() {
        super.onDestroy();
        redPlayer.release();
        bluePlayer.release();
        yellowPlayer.release();
        greenPlayer.release();
    }


    // function that returns a integer that stands for the clicked button, in a list that
    // goes from 0 - 3 , which represents our 4 buttons.

    // it also prepare and start the animation effect when a button is clicked.
    private int flashAndPlay(int delay, Button button){

        int selectedBtn = 0;
        ObjectAnimator animator;
        final MediaPlayer player;

        if(button == redBtn){
            player = redPlayer;
            animator = ObjectAnimator.ofObject(redBtn,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    redBtn.getBackgroundTintList().getDefaultColor(),
                    0xFFFFFFFF);
            selectedBtn = 0;
        } else  if(button == blueBtn){
            player = bluePlayer;
            animator = ObjectAnimator.ofObject(blueBtn,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    blueBtn.getBackgroundTintList().getDefaultColor(),
                    0xFFFFFFFF);
            selectedBtn = 1;
        } else  if(button == yellowBtn){
            player = yellowPlayer;
            animator = ObjectAnimator.ofObject(yellowBtn,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    yellowBtn.getBackgroundTintList().getDefaultColor(),
                    0xFFFFFFFF);
            selectedBtn = 2;
        } else {
            player = greenPlayer;
            animator = ObjectAnimator.ofObject(greenBtn,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    greenBtn.getBackgroundTintList().getDefaultColor(),
                    0xFFFFFFFF);
            selectedBtn = 3;
        }

        animator.setDuration(400);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE); // turns button white and eventually, goes back to the original color.
        animator.setStartDelay(delay); // we use our delay that receives the function

        // when the animation starts, the player is going to start as well.
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                player.start();
            }
        });
        animator.start();
        return selectedBtn;
    }

    // we use this function to trigger the animation events and check the every clicked button
    // with the pattern that "simon" said.
    public void onTap(View v){

        Button tapped = (Button) v;

        // we send the button key (index) as a parameter to clicked function in our model.
        model.clicked(flashAndPlay(0, tapped));
        // we assign temp as our result comparing the clicked button and the real pattern.
        String temp = model.checkedCick();
        // depending on the value of temp variable ...
        if(temp.equals("You Win")){
            //... we increment score and we goes to next level
            instructions.setText("¡¡ Has completado correctamente la secuencia !!");
            isEnabledBtn(false); // we disable game buttons
            showKonfettiAnimation();
            endGame();

        }else if( temp.equals("Wrong pattern") ){
            // ... we finish the game and restart the game.
            instructions.setText("¡Ups!, botón equivocado. \n Fin del Juego.");
            gameState.setText("Fin del juego");
            isEnabledBtn(false); // we disable game buttons
            endGame();

        }

    }

    // function that either, disable or enable game buttons
    public void isEnabledBtn(boolean option){
        redBtn.setEnabled(option);
        yellowBtn.setEnabled(option);
        blueBtn.setEnabled(option);
        greenBtn.setEnabled(option);
    }



    // function that start game or goes to next level
    public void startGame(View v){
        int n = 0;

        scorelbl.setText("Nivel: " + model.getDifficulty());
        instructions.setText("Observa ciudadosamente la secuencia que simón dice...");

        // disable the game buttons
        startBtn.setEnabled(false);
        // change the text button, originally Start button
        startBtn.setText("INICIO");

        // we call the function flashAndPlay to give to our user, the pattern that
        // should be replicated.
        for(int num:model.getRandomPattern()){
            flashAndPlay(1000*n, btn[num]);
            n++;
        }

        isEnabledBtn(true);
        instructions.setText("¿Cuál fue el patrón que Simon dijo?");



    }

    // end game function, redirect to gameOptions Activity.
    private void endGame() { // Go to GameOptionsActivity
        (new Handler()).postDelayed(() -> startActivity(new Intent(this,
                GameOptionsActivity.class).putExtra("game","SimonSays")), 3000);
    }

    // Konffetti animation that appears when the player wins
    private void showKonfettiAnimation(){
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 2000L);
    }


    public void exit(View _v) { startActivity(new Intent(this, GameInstructionsActivity.class).putExtra("game", "SimonSays")); }

}
