package com.example.ninjawaffle.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicholas Zaidan on 8/09/2017.
 */


public class InsertDetailsActivity extends AppCompatActivity {
    long amountOfEntries;
    ArrayAdapter<String> spinnerAdapter;
    Uri uri = null;
    String actualId;
    String problemId;
    String dobClassVariable;

    //XML Declaration variables
    EditText uniqueId;
    EditText firstNameText; //The field in which you enter patients name. Null at this stage.
    EditText secondNameText;
    EditText mobilePhoneNumber;
    EditText dobText;
    EditText extraComments;
    Spinner genderText;
    Spinner etaSpinner;
    Spinner hospitalSpinner;
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
        firstNameText = (EditText) findViewById(R.id.editText); //Connecting the variable to the textbox
        secondNameText = (EditText) findViewById(R.id.insertLast);
        mobilePhoneNumber = (EditText) findViewById(R.id.insertPhoneNumber);
        dobText = (EditText) findViewById(R.id.insertDOB);
        genderText = (Spinner) findViewById(R.id.genderSpinner);
        etaSpinner = (Spinner) findViewById(R.id.etaSpinner);
        hospitalSpinner = (Spinner) findViewById(R.id.hospitalSpinnerXML);
        editProblem = (EditText) findViewById(R.id.editText2); //Connecting the variable to the textbox
        extraComments = (EditText) findViewById(R.id.insertAdditional);
        buttonAdd = (Button) findViewById(R.id.button2); //Connecting the variable to the submit button
        returnButton = (Button) findViewById(R.id.button3);
        uploadPhoto = (Button) findViewById(R.id.pictureUpload);



        //Assigning amount of entries to global variable of amountOfEntries
        databaseProblem.child("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amountOfEntries = dataSnapshot.getChildrenCount();
                updateSpinner(); //Updating Spinner option
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        spinnerAdapter = new ArrayAdapter<>(InsertDetailsActivity.this, android.R.layout.simple_spinner_item,test);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void addPerson(){
        String firstName = this.firstNameText.getText().toString().trim(); //The string that stores the name. Grabs it from the textname box
        String secondName = this.secondNameText.getText().toString().trim(); //Last name as a string
        String phone = this.mobilePhoneNumber.getText().toString().trim(); //Mobile phone amountOfEntries as a string
        String dob = this.dobText.getText().toString().trim(); //Date of Birth as a string
        String gender = this.genderText.getSelectedItem().toString(); //Gender as a string
        String problem = this.editProblem.getText().toString().trim(); //The string that stores the problem. Grabs it for the problem box
        String eta = this.etaSpinner.getSelectedItem().toString();
        String additional = this.extraComments.getText().toString().trim();

        if(errorChecking(firstName, secondName, phone, dob, problem)==true){ //Runs the error checking function
            addPersonFunction(firstName, secondName, phone, dob, gender, problem, eta, additional); //Put messing code function at the bottom
            uploadingPhoto();
        }

    }

    private void uploadingPhoto(){
        if (uri != null) {
            StorageReference filePath = databaseStorage.child(actualId).child(problemId);
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    return;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InsertDetailsActivity.this, "Photo was not uploaded", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean errorChecking(String firstName, String lastName, String phone, String dob, String problem){

        //If fields are empty
        if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(problem)){
            Toast.makeText(this, "Enter data into the fields", Toast.LENGTH_LONG).show(); //The error message
            return false;
        }

        //If phone contains letters
        if(!phone.matches("[0-9]+")){
            Toast.makeText(this, "Phone number can only contain numbers", Toast.LENGTH_LONG).show(); //The error message
            return false;
        }

        //If first name contains numbers
        if(firstName.matches("[0-9]+")){
            Toast.makeText(this, "First name cannot contain numbers", Toast.LENGTH_LONG).show(); //The error message
            return false;
        }

        //If last name contains numbers
        if(lastName.matches("[0-9]+")){
            Toast.makeText(this, "Second name cannot contain numbers", Toast.LENGTH_LONG).show(); //The error message
            return false;
        }

        String[] splitString = dob.split("/");
        int[] splitInt = new int[splitString.length];
        for(int i = 0; i < splitString.length; i++){
            String number = splitString[i];
            splitInt[i] = Integer.parseInt(number);
        }
        if(splitInt.length < 3){
            Toast.makeText(this, "Invalid length for Date of Birth", Toast.LENGTH_LONG).show();
            return false;
        }
        if(splitInt.length > 3){
            Toast.makeText(this, "Invalid length for Date of Birth", Toast.LENGTH_LONG).show();
            return false;
        }
        char slash;

        //If day isn't two digits
        slash = dob.charAt(1);
        if(slash == '/'){
            Toast.makeText(this, "Please set day as two digits", Toast.LENGTH_LONG).show();
            return false;
        }


        //If month isn't two digits
        slash = dob.charAt(4);
        if(slash == '/'){
            Toast.makeText(this, "Please set month as two digits", Toast.LENGTH_LONG).show();
            return false;
        }

        //If third character isn't a '/'
        slash = dob.charAt(2);
        if(slash !='/'){
            Toast.makeText(this, "Use '/' To separate dates (Days).", Toast.LENGTH_LONG).show();
            return false;
        }


        slash = dob.charAt(5);

        //If fifth character isn't a '/'
        if(slash !='/'){
            Toast.makeText(this, "Use '/' To separate dates (Months).", Toast.LENGTH_LONG).show();
            return false;
        }

        if(splitInt[0] > 31 ){
            Toast.makeText(this, "Incorrect day", Toast.LENGTH_LONG).show();
            return false;
        }
        if(splitInt[0] <= 0){
            Toast.makeText(this, "Incorrect day", Toast.LENGTH_LONG).show();
            return false;
        }

        if(splitInt[1] > 12){
            Toast.makeText(this, "Incorrect month", Toast.LENGTH_LONG).show();
            return false;
        }
        if(splitInt[1] <= 0){
            Toast.makeText(this, "Incorrect month", Toast.LENGTH_LONG).show();
            return false;
        }
        dobClassVariable = Integer.toString(splitInt[0]) + Integer.toString(splitInt[1]) + Integer.toString(splitInt[2]);

        //If phone number isn't valid
        if(phone.length()!=10 && phone.length()!=8){
            Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void addPersonFunction(String firstName, String secondName, String phone, String dob, String gender, String problem, String eta, String additional){
        actualId = dobClassVariable + firstName + " " + secondName;
        Person person = new Person(actualId, firstName,secondName,phone,dob,gender); //Creating the person object
        Problem problemObject = new Problem(problem, actualId, eta, additional); //Creating the problem object
        problemId = databasePerson.push().getKey(); //The unique problem id for database storage
        Map<String, Object> postValues = person.toMap(); //Storing object in a map
        Map<String, Object> postValues2 = problemObject.toMap(); //Storing problems in a map
        Map<String, Object> childUpdate = new HashMap<>(); //Person hashmap to be put in database
        Map<String,Object> childUpdate2 = new HashMap<>(); //Problem hashmap to put in database
        childUpdate.put("/" + "ManuallyEntered" + "/" + actualId, postValues); //Directory of where to put person in database
        childUpdate2.put("/" + problemId + "/", postValues2); //Directory of where to put problem in database
        databasePerson.updateChildren(childUpdate); //Updating
        databaseProblem.updateChildren(childUpdate2); //Updating
        Toast.makeText(this, "Problem added", Toast.LENGTH_LONG).show(); //A toast is just like a display message. This will notify people that the push was successful

    }
}
