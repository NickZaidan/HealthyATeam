package com.example.ninjawaffle.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nicholas Zaidan on 27/09/2017.
 */

public class InsertMedicareActivity extends AppCompatActivity {
    //Variable declaration
    long amountOfEntries = 0;
    DatabaseReference databaseMedicarePerson;
    DatabaseReference databaseMedicare;
    ArrayAdapter<String> spinnerAdapter;
    String id;
    String problemId;
    Uri uri = null;
    StorageReference databaseStorage;

    //XML declaration
    EditText medicareNumber;
    EditText medicareNumberID;
    EditText medicareProblem;
    EditText extraComments;
    Spinner medicareETA;
    Spinner hospitalSpinner;
    Button submitButton;
    Button submitPhoto;
    Button returnButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_medicare);

        //Assigning buttons to variables
        medicareNumber = (EditText) findViewById(R.id.insertMedicareNumber);
        medicareNumberID = (EditText) findViewById(R.id.insertMedicareNumberID);
        medicareProblem = (EditText) findViewById(R.id.insertMedicareProblemText);
        extraComments = (EditText) findViewById(R.id.insertAdditionalMedicare);
        medicareETA = (Spinner) findViewById(R.id.medicareETA);
        hospitalSpinner = (Spinner) findViewById(R.id.hospitalSpinnerXMLMedicare);
        submitButton = (Button) findViewById(R.id.submitMedicare);
        submitPhoto = (Button) findViewById(R.id.submitPhotoMedicare);
        returnButton = (Button) findViewById(R.id.button3return);
        databaseMedicarePerson = FirebaseDatabase.getInstance().getReference("Users");
        databaseMedicare = FirebaseDatabase.getInstance().getReference("Problems"); //This links the firebase database to the variable
        databaseStorage = FirebaseStorage.getInstance().getReference();


        //Assigning amount of entries to global variable of amountOfEntries
        databaseMedicare.child("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amountOfEntries = dataSnapshot.getChildrenCount();
                updateSpinner(); //Updating Spinner option
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertMedicareActivity.this, Homepage.class);
                startActivity(intent); //
            }
        });
        //Pressing the submit button entry
        submitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                submitEntry();

            }
        });

        submitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }

    //Uploading photos function
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            uri = data.getData();
        }

    }



    private void updateSpinner(){
        String input = "Westmead Hospital - " + amountOfEntries + " patients";
        ArrayList<String> test = new ArrayList<>();
        test.add("Auburn - 3 patients");
        test.add("Blacktown - 8 patients");
        test.add("Mount Druitt - 15 patients");
        test.add(input);
        spinnerAdapter = new ArrayAdapter<>(InsertMedicareActivity.this, android.R.layout.simple_spinner_item,test);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void submitEntry(){
        //Getting text entries as a string
        String medicareNumber = this.medicareNumber.getText().toString().trim();
        String medicareID = this.medicareNumberID.getText().toString().trim();
        String medicareProblem = this.medicareProblem.getText().toString().trim();
        String medicareETA = this.medicareETA.getSelectedItem().toString();
        String additional = this.extraComments.getText().toString().trim();
        if(errorChecking(medicareNumber, medicareID, medicareProblem)==true){
            addPersonFunction(medicareNumber, medicareID, medicareProblem, medicareETA, additional);
            uploadingPhoto();
            Intent intent = new Intent(InsertMedicareActivity.this, OnSuccessActivity.class);
            startActivity(intent); //Changes page
        }
    }


    public boolean errorChecking(String medicareNumber, String medicareID, String medicareProblem){

        //If any field is empty
        if(TextUtils.isEmpty(medicareNumber) || TextUtils.isEmpty(medicareID) || TextUtils.isEmpty(medicareProblem)){
            Toast.makeText(this, "Please enter in correct details", Toast.LENGTH_LONG).show();
            return false;
        }

        //If medicare amountOfEntries isn't 10 characters in length
        if(medicareNumber.length() != 10){
            Toast.makeText(this, "Insert a correct medicare number", Toast.LENGTH_LONG).show();
            return false;
        }

        //If medicare amountOfEntries isn't a amountOfEntries
        if(!medicareNumber.matches("[0-9]+")){
            Toast.makeText(this, "Insert a correct medicare number", Toast.LENGTH_LONG).show();
            return false;
        }

        //If medicare last digit isn't a amountOfEntries
        if(!medicareID.matches("[0-9]+")){
            Toast.makeText(this, "Insert a correct final medicare number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void uploadingPhoto(){
        if (uri != null) {
            StorageReference filePath = databaseStorage.child(id).child(problemId);
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InsertMedicareActivity.this, "Photo was not uploaded", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void addPersonFunction(String medicareNumber, String medicareID, String problemDescription, String medicareETA, String additional){
        id = medicareNumber + medicareID; //Combining the complete medicare amountOfEntries
        Problem problem = new Problem(problemDescription, id, medicareETA, additional); //Creating problem object
        Medicare medicare = new Medicare(id);
        problemId = databaseMedicare.push().getKey(); //The unique problem id for database storage
        Map<String, Object> postValues = problem.toMap(); //Storing object in a map
        Map<String, Object> postValues2 = medicare.toMap();
        Map<String, Object> childUpdate = new HashMap<>(); //Person hashmap to be put in database
        Map<String, Object> childUpdate2 = new HashMap<>();
        childUpdate.put("/" + problemId + "/", postValues); //Directory of where to put problem in database
        childUpdate2.put("/" + "MedicareEntry" + "/" +  id, postValues2); //Directory of where to put problem in database
        databaseMedicare.updateChildren(childUpdate); //Updating
        databaseMedicarePerson.updateChildren(childUpdate2);
    }
}