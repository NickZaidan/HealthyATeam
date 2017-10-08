package com.example.ninjawaffle.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas Zaidan on 8/09/2017.
 */


public class InsertDetailsActivity extends AppCompatActivity {

    //XML Declaration variables

    EditText uniqueId;
    EditText firstNameText; //The field in which you enter patients name. Null at this stage.
    EditText secondNameText;
    EditText mobilePhoneNumber;
    EditText dobText;
    Spinner genderText;
    Spinner etaSpinner;
    EditText editProblem; //The field in which you enter the problem. Null at this stage
    Button buttonAdd; //The button that is pressed. Null at this stage
    Button returnButton;
    Button uploadPhoto;
    DatabaseReference databasePerson; //The reference to the firebase database. Null at this stage.
    DatabaseReference databaseProblem;
    StorageReference databaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asssigning variables

        databaseStorage = FirebaseStorage.getInstance().getReference();
        databasePerson = FirebaseDatabase.getInstance().getReference("Users"); //This links the firebase database to the variable
        databaseProblem = FirebaseDatabase.getInstance().getReference("Problems");
        uniqueId = (EditText) findViewById(R.id.insertPersonalId);
        firstNameText = (EditText) findViewById(R.id.editText); //Connecting the variable to the textbox
        secondNameText = (EditText) findViewById(R.id.insertLast);
        mobilePhoneNumber = (EditText) findViewById(R.id.insertPhoneNumber);
        dobText = (EditText) findViewById(R.id.insertDOB);
        genderText = (Spinner) findViewById(R.id.genderSpinner);
        etaSpinner = (Spinner) findViewById(R.id.etaSpinner);
        editProblem = (EditText) findViewById(R.id.editText2); //Connecting the variable to the textbox
        buttonAdd = (Button) findViewById(R.id.button2); //Connecting the variable to the submit button
        returnButton = (Button) findViewById(R.id.button3);
        uploadPhoto = (Button) findViewById(R.id.pictureUpload);




        //Button events

        //Return button on click event
        returnButton.setOnClickListener(new Button.OnClickListener(){
           @Override
            public void onClick(View v){
               Intent intent = new Intent(InsertDetailsActivity.this, Homepage.class);
               startActivity(intent); //Changes page
           }
        });

        //Submit on click event
        buttonAdd.setOnClickListener(new View.OnClickListener() { //Creates the event when you click the button
            @Override
            public void onClick(View v) {
                addPerson(); //When button is pressed. Run the addPerson method
            }
        });


        //Upload photo on click event
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }


    //Currently this function is commented out because firebase storage is no longer linked. TODO
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = databaseStorage.child("Photos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(InsertDetailsActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InsertDetailsActivity.this, "Photo was not uploaded", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    private void addPerson(){
        String firstName = this.firstNameText.getText().toString().trim(); //The string that stores the name. Grabs it from the textname box
        String secondName = this.secondNameText.getText().toString().trim(); //Last name as a string
        String phone = this.mobilePhoneNumber.getText().toString().trim(); //Mobile phone number as a string
        String dob = this.dobText.getText().toString().trim(); //Date of Birth as a string
        String gender = this.genderText.getSelectedItem().toString(); //Gender as a string
        String problem = this.editProblem.getText().toString().trim(); //The string that stores the problem. Grabs it for the problem box
        String tempId = this.uniqueId.getText().toString().trim(); //The unique id stored as a string
        String eta = this.etaSpinner.getSelectedItem().toString();

        if(errorChecking(firstName, secondName, phone, dob, problem, tempId)==true){ //Runs the error checking function
            String actualId = tempId + firstName + secondName;//Declaring unique id
            Person person = new Person(actualId, firstName,secondName,phone,dob,gender); //Creating the person object
            Problem problemObject = new Problem(problem, actualId, eta); //Creating the problem object
            String problemId = databasePerson.push().getKey(); //The unique problem id for database storage
            Map<String, Object> postValues = person.toMap(); //Storing object in a map
            Map<String, Object> postValues2 = problemObject.toMap(); //Storing problems in a map
            Map<String, Object> childUpdate = new HashMap<>(); //Person hashmap to be put in database
            Map<String,Object> childUpdate2 = new HashMap<>(); //Problem hashmap to put in database
            childUpdate.put("/" + "ManuallyEntered" + "/" + actualId, postValues); //Directory of where to put person in database
            childUpdate2.put("/" + problemId + "/", postValues2); //Directory of where to put problem in database
            databasePerson.updateChildren(childUpdate); //Updating
            databaseProblem.updateChildren(childUpdate2); //Updating
            Toast.makeText(this, "Person added", Toast.LENGTH_LONG).show(); //A toast is just like a display message. This will notify people that the push was successful

        } else{
            Toast.makeText(this, "Enter data into the fields", Toast.LENGTH_LONG).show(); //The error message
        }

    }

    private boolean errorChecking(String firstName, String lastName, String phone, String dob, String problem, String tempId ){

        //If fields are empty
        if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(problem) || TextUtils.isEmpty(tempId)){
            return false;
        }

        //If the key isn't 3 digits
        if(tempId.length()!=3){
            return false;
        }

        //If the key contains letters
        if(!tempId.matches("[0-9]+")){
            return false;
        }

        //If phone contains letters
        if(!phone.matches("[0-9]+")){
            return false;
        }

        //If first name contains numbers
        if(firstName.matches("[0-9]+")){
            return false;
        }

        //If last name contains numbers
        if(lastName.matches("[0-9]+")){
            return false;
        }


        return true;
    }
}
