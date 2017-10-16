package com.example.ninjawaffle.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ninjawaffle on 16/10/2017.
 */

public class OnSuccessActivity extends AppCompatActivity{
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_success);


        returnButton = (Button) findViewById(R.id.buttonReturnSuccess);


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnSuccessActivity.this, Homepage.class);
                startActivity(intent); //Changes page
            }
        });

    }
}
