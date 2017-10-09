package com.example.ninjawaffle.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nicholas Zaidan on 27/09/2017.
 */

public class InsertMedicareActivity extends AppCompatActivity {
    //Variable declaration

    EditText medicareNumber;
    EditText medicareNumberID;
    EditText medicareProblem;
    Spinner medicareETA;
    Button submitButton;
    DatabaseReference databaseMedicarePerson;
    DatabaseReference databaseMedicare;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_medicare);

        //Assigning buttons to variables
        medicareNumber = (EditText) findViewById(R.id.insertMedicareNumber);
        medicareNumberID = (EditText) findViewById(R.id.insertMedicareNumberID);
        medicareProblem = (EditText) findViewById(R.id.insertMedicareProblemText);
        medicareETA = (Spinner) findViewById(R.id.medicareETA);
        submitButton = (Button) findViewById(R.id.submitMedicare);
        databaseMedicarePerson = FirebaseDatabase.getInstance().getReference("Users");
        databaseMedicare = FirebaseDatabase.getInstance().getReference("Problems"); //This links the firebase database to the variable

        //Pressing the submit button entry
        submitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                submitEntry();
            }
        });
    }

    private void submitEntry(){
        //Getting text entries as a string
        String medicareNumber = this.medicareNumber.getText().toString().trim();
        String medicareID = this.medicareNumberID.getText().toString().trim();
        String medicareProblem = this.medicareProblem.getText().toString().trim();
        String medicareETA = this.medicareETA.getSelectedItem().toString();

        if(errorChecking(medicareNumber, medicareID, medicareProblem)==true){
            String id = medicareNumber + medicareID; //Combining the complete medicare amountOfEntries
            Problem problem = new Problem(medicareProblem, id, medicareETA); //Creating problem object
            Medicare medicare = new Medicare(id);
            String problemId = databaseMedicare.push().getKey(); //The unique problem id for database storage
            Map<String, Object> postValues = problem.toMap(); //Storing object in a map
            Map<String, Object> postValues2 = medicare.toMap();
            Map<String, Object> childUpdate = new HashMap<>(); //Person hashmap to be put in database
            Map<String, Object> childUpdate2 = new HashMap<>();
            childUpdate.put("/" + problemId + "/", postValues); //Directory of where to put problem in database
            childUpdate2.put("/" + "MedicareEntry" + "/" +  id, postValues2); //Directory of where to put problem in database
            databaseMedicare.updateChildren(childUpdate); //Updating
            databaseMedicarePerson.updateChildren(childUpdate2);
            Toast.makeText(this, "Problem Submitted", Toast.LENGTH_LONG).show(); //A toast is just like a display message. This will notify people that the push was successful
        }
    }

    private boolean errorChecking(String medicareNumber, String medicareID, String medicareProblem){

        //If any field is empty
        if(TextUtils.isEmpty(medicareNumber) || TextUtils.isEmpty(medicareID) || TextUtils.isEmpty(medicareProblem)){
            Toast.makeText(this, "Please enter in correct details", Toast.LENGTH_LONG).show();
            return false;
        }

        //If medicare amountOfEntries isn't 10 characters in length
        if(medicareNumber.length() != 10){
            Toast.makeText(this, "Insert a correct medicare amountOfEntries", Toast.LENGTH_LONG).show();
            return false;
        }

        //If medicare amountOfEntries isn't a amountOfEntries
        if(!medicareNumber.matches("[0-9]+")){
            Toast.makeText(this, "Insert a correct medicare amountOfEntries", Toast.LENGTH_LONG).show();
            return false;
        }

        //If medicare lastdigit isn't a amountOfEntries
        if(!medicareID.matches("[0-9]+")){
            Toast.makeText(this, "Insert a correct medicare final amountOfEntries", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}