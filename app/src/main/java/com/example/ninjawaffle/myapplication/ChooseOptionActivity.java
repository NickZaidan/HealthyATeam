package com.example.ninjawaffle.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ninjawaffle on 27/09/2017.
 */

public class ChooseOptionActivity extends AppCompatActivity {

    //Variable declaration
    Button medicareButton;
    Button manualButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_opton);

        //Assigning variables the button values
        medicareButton = (Button) findViewById(R.id.medicareButton);
        manualButton = (Button) findViewById(R.id.manualButton);


        //Button events

        //Option for medicare
        medicareButton.setOnClickListener(new View.OnClickListener() { //Creates the event when you click the button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseOptionActivity.this, InsertMedicareActivity.class);
                startActivity(intent);
            }
        });

        //Option to enter details manually
        manualButton.setOnClickListener(new View.OnClickListener() { //Creates the event when you click the button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseOptionActivity.this, InsertDetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}