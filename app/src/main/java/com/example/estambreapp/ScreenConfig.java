package com.example.estambreapp;

import android.view.View;

public class ScreenConfig {

    //Constructor, ejecucion de unica funcion, posiblemente se agreguen mas, configuraciones de seguridad aplicadas a todas las activities
    public ScreenConfig(View decorWindow){
        hideSystemUI(decorWindow);
    }

    //Configuracion de pantalla completa, usando System_UI_Flag
    public void hideSystemUI(View decorWindow) {
        decorWindow.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }


}
