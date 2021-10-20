package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class SimonSaysActivity extends AppCompatActivity {
    int rondas = 3;

    // Instance to game model
    SimonSaysModel model;
    Button[] btn; // declare a buttons array, for our game buttons
    MediaPlayer[] players; // declare a buttons array, for our media players

    // we instance our labels and buttons
    Button purpleBtn;
    Button blueBtn;
    Button yellowBtn;
    Button greenBtn;
    ImageButton startBtn;
    TextView scorelbl;
    float dpi;

    TextView instructions;

    KonfettiView konfettiView;

    // we create a instance to our MediaPlayers, one for each of our mp3 effect sounds.
    private MediaPlayer purplePlayer;
    private MediaPlayer bluePlayer;
    private MediaPlayer yellowPlayer;
    private MediaPlayer greenPlayer;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_says);

        model = new SimonSaysModel(this);

        //dpi = getResources().getDisplayMetrics().density;
        dpi = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics());

        // we assign the object to our view component
        purpleBtn = findViewById(R.id.purpleBtn);
        blueBtn = findViewById(R.id.blueBtn);
        yellowBtn = findViewById(R.id.yellowBtn);
        greenBtn = findViewById(R.id.greenBtn);
        startBtn = findViewById(R.id.startBtn);

        scorelbl = findViewById(R.id.scoreLbl);
        instructions = findViewById(R.id.instructionsText);

        //we assign our sound effects in our MediaPlayers
        purplePlayer = MediaPlayer.create(this, R.raw.red);
        bluePlayer = MediaPlayer.create(this, R.raw.blue);
        yellowPlayer = MediaPlayer.create(this, R.raw.yellow);
        greenPlayer = MediaPlayer.create(this, R.raw.green);

        // we assign our buttons to our array of buttons
        btn = new Button[]{ purpleBtn, blueBtn, yellowBtn, greenBtn };
        players = new MediaPlayer[]{purplePlayer, bluePlayer, yellowPlayer, greenPlayer};

        //set our game state label as empty.
        instructions.setText("Presiona el botón para comenzar a jugar ");

        konfettiView = findViewById(R.id.viewKonfetti2);

        //we desable our buttons before starting the game
        isEnabledBtn(false);
        initGame();
    }

    private void initGame(){
        scorelbl.setText("Nivel: " + model.getDifficulty());
        startBtn.setVisibility(View.VISIBLE);
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
        purplePlayer.release();
        bluePlayer.release();
        yellowPlayer.release();
        greenPlayer.release();
    }


    // function that returns a integer that stands for the clicked button, in a list that
    // goes from 0 - 3 , which represents our 4 buttons.
    private void sizeIncrease(int chn, Button button){
        button.setLayoutParams(new TableRow.LayoutParams((int) (150 * dpi), (int) ((100 + chn) * dpi)));
        (new Handler()).postDelayed(() -> {
            if(chn <= 50) sizeIncrease(chn+3, button);
        }, 1);
    }

    private void sizeReduce(int chn, Button button){
        button.setLayoutParams(new TableRow.LayoutParams((int) (150 * dpi), (int) ((100 + chn) * dpi)));
        (new Handler()).postDelayed(() -> {
            if(chn <= 0) sizeIncrease(chn+3, button);
            else sizeReduce(chn-3, button);
        }, 1);
    }

    // it also prepare and start the animation effect when a button is clicked.
    private int flashAndPlay(Button button){
        int selectedBtn = Integer.parseInt(button.getContentDescription().toString());
        (players[selectedBtn]).start();
        sizeReduce(50, button);
        return selectedBtn;
    }

    // we use this function to trigger the animation events and check the every clicked button
    // with the pattern that "simon" said.
    @SuppressLint("SetTextI18n")
    public void onTap(View v){

        // we send the button key (index) as a parameter to clicked function in our model.
        model.clicked(flashAndPlay((Button) v));
        // we assign temp as our result comparing the clicked button and the real pattern.
        int temp = model.checkedClick();
        // depending on the value of temp variable ...
        if(temp == 1){
            //... we increment score and we goes to next level
            instructions.setText("¡¡ Has completado correctamente la secuencia !!");
            isEnabledBtn(false); // we disable game buttons
            showKonfettiAnimation();
            (new Handler()).postDelayed(this::endGame, 2000);

        }else if(temp == -1){
            // ... we finish the game and restart the game.
            instructions.setText("¡Ups!, botón equivocado. \n Fin del Juego.");
            isEnabledBtn(false); // we disable game buttons
            (new Handler()).postDelayed(this::endGame, 500);
        }
    }

    // function that either, disable or enable game buttons
    @SuppressLint("SetTextI18n")
    public void isEnabledBtn(boolean option){
        purpleBtn.setEnabled(option);
        yellowBtn.setEnabled(option);
        blueBtn.setEnabled(option);
        greenBtn.setEnabled(option);
        if(option) instructions.setText("¿Cuál fue el patrón que Simon dijo?");
    }



    // function that start game or goes to next level
    @SuppressLint("SetTextI18n")
    public void startGame(View v){

        v.setVisibility(View.INVISIBLE);

        int n = 0;
        instructions.setText("Observa ciudadosamente la secuencia que simón dice...");

        // we call the function flashAndPlay to give to our user, the pattern that
        // should be replicated.
        for(int num: model.getRandomPattern()){
            (new Handler()).postDelayed(() -> {
                (btn[num]).getBackground().setAlpha(180);
                flashAndPlay(btn[num]);
            }, 1000L * n);
            n++;
            (new Handler()).postDelayed(() -> (btn[num]).getBackground().setAlpha(255), (1000L * n) - 200);
        }

        (new Handler()).postDelayed(() -> isEnabledBtn(true), 1000L * n);

    }

    // end game function, redirect to gameOptions Activity.
    private void endGame() { // Go to GameOptionsActivity
        if(rondas == 1){
            (new Handler()).postDelayed(() -> startActivity(new Intent(this, GameOptionsActivity.class).putExtra("game","SimonSays")), 1500);
        } else {
            rondas--;
            (new Handler()).postDelayed(this::initGame, 1500);
        }
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
