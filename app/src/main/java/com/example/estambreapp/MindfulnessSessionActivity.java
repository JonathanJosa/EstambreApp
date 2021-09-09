package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class MindfulnessSessionActivity extends AppCompatActivity {

    ImageView background;
    View nullView;
    long[] durations = new long[]{1000, 2000, 3000, 4000, 5000};
    int imageNo = 1;
    Handler handlerChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness_session);
        handlerChange = new Handler();
        background = findViewById(R.id.backgroundView);
        autoChange((long) durations[imageNo-1]);
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    public void exit(View _v){ handlerChange.removeCallbacks(waitingInstance); startActivity(new Intent(this, MindfulnessActivity.class)); }

    public void changeButton(View v){ changeActivity("atras".equals(v.getContentDescription().toString()) ? -1 : 1); }

    private void autoChange(long time){ handlerChange.postDelayed(waitingInstance, (long) time); }

    private void changeActivity(int change){
        handlerChange.removeCallbacks(waitingInstance);
        if(imageNo + change > 5){ exit(nullView); return; }
        if(imageNo + change < 1){ change = 0; }
        imageNo += change;
        autoChange((long) durations[imageNo-1]);
        background.setImageResource(getResources().getIdentifier("act"+ (imageNo), "drawable", getPackageName()));
    }

    Runnable waitingInstance = new Runnable() {
        @Override
        public void run() { changeActivity(1); }
    };

}