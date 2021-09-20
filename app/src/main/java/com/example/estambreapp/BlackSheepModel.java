package com.example.estambreapp;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Pair;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BlackSheepModel {

    GamesModel gameProperties;
    Double difficulty = 50.0;
    Context context;
    Pair<Integer, Integer> sheeps;
    Float dpi;

    public BlackSheepModel(Context ctx){
        context = ctx;
        dpi = context.getResources().getDisplayMetrics().density;
        //gameProperties = new GamesModel(ctx, "BlackSheep");
        //difficulty = gamesProperties.getDifficulty();
        sheeps = new Pair<Integer, Integer>(getWhiteSheeps(), getBlackSheeps());
    }

    private Integer getWhiteSheeps(){
        return 3;
    }

    private Integer getBlackSheeps(){
        return 1;
    }

    private LinearLayout.LayoutParams randomLayouts(){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (90f * dpi),(int) (90f * dpi));
        //lp.setMarginStart(0);
        return lp;
    }

    public ImageButton[] getButtons(){
        ImageButton[] buttons = new ImageButton[sheeps.first + sheeps.second];

        for(int i=0; i<sheeps.first; i++){
            ImageButton btn = new ImageButton(context);
            btn.setContentDescription("whiteSheep");
            btn.setImageResource(context.getResources().getIdentifier("blacksheep_sheepw", "drawable", context.getPackageName()));
            btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
            //btn.setOnClickListener();
            btn.setLayoutParams(randomLayouts());
            buttons[i] = btn;
        }

        for(int i=sheeps.first; i<sheeps.second; i++){
            ImageButton btn = new ImageButton(context);
            btn.setContentDescription("blackSheep");
            btn.setImageResource(context.getResources().getIdentifier("blacksheep_sheepb", "drawable", context.getPackageName()));
            btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FFFFFF")));
            //btn.setOnClickListener();
            btn.setLayoutParams(randomLayouts());
            buttons[i] = btn;
        }

        return buttons;
    }


}
