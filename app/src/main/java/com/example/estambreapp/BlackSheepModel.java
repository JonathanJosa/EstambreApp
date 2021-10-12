package com.example.estambreapp;


import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.LayoutDirection;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;

public class BlackSheepModel {

    GamesModel gameProperties;
    Double difficulty = 50.0;
    Context context;
    Float dpi;
    Boolean counting = false;
    Boolean started = false;
    Integer whiteSheeps;
    Integer blackSheeps;
    Integer totalTime;
    Integer velocity;


    public BlackSheepModel(Context ctx){
        context = ctx;
        dpi = context.getResources().getDisplayMetrics().density;
        gameProperties = new GamesModel(ctx, "BlackSheep");
        difficulty = gameProperties.getDifficulty();
        System.out.println(difficulty);
        whiteSheeps = getWhiteSheeps();
        blackSheeps = getBlackSheeps();
        totalTime = getTotalTime();
        velocity = getVelocity();
    }

    public void resetGame(){
        difficulty = gameProperties.getDifficulty();
        System.out.println(difficulty);
        whiteSheeps = getWhiteSheeps();
        blackSheeps = getBlackSheeps();
        totalTime = getTotalTime();
        velocity = getVelocity();
    }

    public void playing(){
        if(counting) gameProperties.endGame();
        gameProperties.startTimeCount();
        counting = !counting;
    }

    private Integer getWhiteSheeps(){ return 2 + ((int) (difficulty/35)); }
    private Integer getBlackSheeps(){ return 1 + ((int) (difficulty/80)); }
    private Integer getTotalTime(){ return (int) (Math.random() * ((int) (difficulty/30) + 0.99) + 3); }
    private Integer getVelocity(){
        int val = 2500 - ((int) (Math.random() * ((int) (difficulty * 10))));
        return val < 0 ? 500 : 500 + val;
    }


    public RelativeLayout.LayoutParams randomLayouts(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (80f * dpi), (int) (80f * dpi));
        params.leftMargin = (int) ((Math.random() * 225.99) * dpi);
        params.topMargin = (int) ((Math.random() * 425.99) * dpi);
        return params;
    }

    public boolean sheepClicked(View v, String tp){
        if(started){
            v.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            if(tp.equals("sheepw")){
                Toast.makeText(context, "Oveja equivocada :(", Toast.LENGTH_SHORT).show();
                gameProperties.penalty(800);
            }else{
                Toast.makeText(context, "Muy bien!!", Toast.LENGTH_SHORT).show();
                blackSheeps -= 1;
                return blackSheeps == 0;
            }
        }else{
            Toast.makeText(context, "Mezclando", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void mixSheeps(View btn, Integer repeat){
        if(repeat == 0) return;
        else if(totalTime-1 > repeat && (Math.random() >= 0.75 || repeat == 1)) ((ImageButton) btn).setImageResource(R.drawable.blacksheep_sheepw);
        //btn.setBackgroundResource(R.drawable.blacksheep_sheepw);

        //btn.setBackground(R.drawable.blacksheep_sheepw);
        //btn.setImageResource(getResources().getIdentifier("blacksheep_"+btnType, "drawable", getPackageName()));

        //btn.setHasTransientState(true);
        //btn.setTransitionVisibility(View.GONE);
        //ViewGroup temp = (ViewGroup) btn.getParent();
        //temp.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        //(new AutoTransition()).addTarget(btn).setDuration(velocity);
        //btn.setLayoutParams(randomLayouts());
        //TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE,pozice_motyl.leftMargin, Animation.ABSOLUTE, next1, Animation.ABSOLUTE, pozice_motyl.topMargin, Animation.ABSOLUTE, next2);

        int newLeft = (int) (((Math.random() * 135.99)) * dpi);
        int newTop = (int) (((Math.random() * 335.99)) * dpi);

        int beforeLeft = btn.getLeft();
        int beforeTop = btn.getTop();

        btn.animate().translationX(newLeft - beforeLeft).setDuration(velocity);
        btn.animate().translationY(newTop - beforeTop).setDuration(velocity);

        (new Handler()).postDelayed(() -> mixSheeps(btn, repeat-1), velocity);

        //btn.animate().
        //btn.animate().withEndAction(() -> mixSheeps(btn, repeat-1));
    }

}
