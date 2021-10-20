package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class MindfulnessSessionActivity extends AppCompatActivity {

    ImageView backgroundImage;
    ImageView backgroundDescription;
    ImageView load;
    View nullView;
    //Duraciones de cada actividad
    long[] durations = new long[]{30000, 30000, 30000, 30000, 5000};
    int imageNo = 1;
    Handler handlerChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfulness_session);
        handlerChange = new Handler();
        //Elementos importantes, background con las instrucciones de que hacer, load con la barra de el numero de actividad
        backgroundImage = findViewById(R.id.ActMindfulnessImage);
        backgroundDescription = findViewById(R.id.ActMindfulnessDescription);
        load = findViewById(R.id.loadBar);
        //Cambio de pantallas automatico con la duracion establecida en durations
        autoChange((long) durations[imageNo-1]);
    }

    @Override
    //Configuracion pantalla completa
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }

    //Salida de las actividades, eliminar el handler de cambio asincronico
    public void exit(View _v){ handlerChange.removeCallbacks(waitingInstance); startActivity(new Intent(this, MindfulnessActivity.class)); }

    //Cambiar entre siguiente y anterior actividad, si es atras, ejecuta changeActivity con un -1, adelante con +1
    public void changeButton(View v){ changeActivity("atras".equals(v.getContentDescription().toString()) ? -1 : 1); }

    //Declaracion del handlerChange asincronico para cambiar de actividades
    private void autoChange(long time){ handlerChange.postDelayed(waitingInstance, (long) time); }

    //cambiar de actividad
    private void changeActivity(int change){
        //Eliminar el cambio asincronico
        handlerChange.removeCallbacks(waitingInstance);
        //Revisar que no sea la ultima o primera actividad
        if(imageNo + change > 5){ exit(nullView); return; }
        if(imageNo + change < 1){ change = 0; }
        imageNo += change;
        //Activar nuevo autoChange con tiempo correspondiente
        autoChange((long) durations[imageNo-1]);
        //Cambiar el fondo de la pantalla
        backgroundImage.setImageResource(getResources().getIdentifier("mindfulness_img_act" + imageNo, "drawable", getPackageName()));
        backgroundDescription.setImageResource(getResources().getIdentifier("mindfulness_bottom_act" + imageNo, "drawable", getPackageName()));
        load.setImageResource(getResources().getIdentifier("actload" + imageNo, "drawable", getPackageName()));
    }

    //WaitingInstance, realiza un cambio de actividad en un tiempo especifico, necesita declararse para poder borrarse de ser necesario
    private final Runnable waitingInstance = new Runnable() {
        @Override
        public void run() { changeActivity(1); }
    };

}