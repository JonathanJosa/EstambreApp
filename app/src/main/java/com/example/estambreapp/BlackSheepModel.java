package com.example.estambreapp;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.LayoutDirection;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BlackSheepModel {

    GamesModel gameProperties;
    Double difficulty = 50.0;
    Context context;
    Pair<Integer, Integer> sheeps;
    Float dpi;
    Boolean started = true;

    public BlackSheepModel(Context ctx){
        context = ctx;
        dpi = context.getResources().getDisplayMetrics().density;
        gameProperties = new GamesModel(ctx, "BlackSheep");
        difficulty = gameProperties.getDifficulty();
        //gameProperties.startTimeCount();
        sheeps = new Pair<Integer, Integer>(getWhiteSheeps(), getBlackSheeps());
    }

    private Integer getWhiteSheeps(){
        return 3;
    }

    private Integer getBlackSheeps(){
        return 1;
    }

    public LinearLayout.LayoutParams randomLayouts(){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (90f * dpi),(int) (90f * dpi), 300f);

        return lp;
    }

    public void sheepClicked(View v, String tp){
        if(started){
            v.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            if(tp.equals("sheepw")){
                //gameProperties.penalty(0.5);
                Toast.makeText(context, "Oveja equivocada :(", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Muy bien!!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Mezclando", Toast.LENGTH_SHORT).show();
        }
    }


}
