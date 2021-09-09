package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ImpostorActivity extends AppCompatActivity {

    TableLayout tableButtons;
    ImpostorModel impostorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostor);

        tableButtons = (TableLayout) findViewById(R.id.impostorTableButtons);
        createTableButtons();
    }

    private void createTableButtons() {
        // --- Error, no accede a la clase ---
        int numRows = 2; // (int) impostorModel.getTableButtonsSize()[0];
        int numColumns = 3; // (int) impostorModel.getTableButtonsSize()[1];

        for(int i = 0; i < numRows; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            )); // Making the row expand and use the complete size of the horizontal space
            tableButtons.addView(tableRow); // Creating a new row in the table
            for(int j = 0; j < numColumns; j++){
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                )); // Making the column expand and use the complete size of the vertical space
                tableRow.addView(button);
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}