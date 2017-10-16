package com.example.ninjawaffle.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.net.Uri;

/**
 * Created by Nicholas Zaidan on 8/09/2017.
 */


public class Homepage extends AppCompatActivity {

    //Variable declaration
    Button topLeft;
    Button bottomLeft;
    Button topRight;
    Button bottomRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //Assiging variables to button
        topLeft = (Button) findViewById(R.id.firstTile);
        bottomLeft = (Button) findViewById(R.id.secondTile);

        //Button events

        //Top left button, runs Choosing Option Activity
        topLeft.setOnClickListener(new View.OnClickListener() { //Creates the event when you click the button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, ChooseOptionActivity.class);
                startActivity(intent);
            }
        });

        //bottom left button, dials 000
        bottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "000";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

    }
}
