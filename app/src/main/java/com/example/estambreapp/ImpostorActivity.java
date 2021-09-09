package com.example.estambreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class ImpostorActivity extends AppCompatActivity {

    TableLayout tableButtons;
    ImpostorModel impostorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostor);

        impostorModel = new ImpostorModel();

        tableButtons = findViewById(R.id.impostorTableButtons);
        createTableButtons(); // Calling the function for the creation of the Button Matrix
    }

    private void createTableButtons() {

        int numRows = impostorModel.getTableButtonsSize()[0];
        int numColumns = impostorModel.getTableButtonsSize()[1];

        for(int row = 0; row < numRows; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            )); // Making the row expand and use the complete size of the horizontal space
            tableButtons.addView(tableRow); // Creating a new row in the table
            for(int column = 0; column < numColumns; column++){
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                )); // Making the column expand and use the complete size of the vertical space

                button.setOnClickListener(clickButton(row, column));

                tableRow.addView(button);
            }
        }

    }

    private View.OnClickListener clickButton(int row, int column) {
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageButtonClicked(row, column);
            }
        });
    }

    private void messageButtonClicked(int posX, int posY) {
        // Function that shows the pop-up message (Java obligated me to create a separate function)
        Toast.makeText(this, "Button cliked in position: " + posX + "," + posY,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWindowFocusChanged(boolean focused){
        super.onWindowFocusChanged(focused);
        if(focused) new ScreenConfig(getWindow().getDecorView());
    }
}