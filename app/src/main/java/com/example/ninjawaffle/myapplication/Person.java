package com.example.ninjawaffle.myapplication;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas Zaidan on 4/09/2017.
 */


public class Person {
    //The values stored in Person
    String personId;
    String firstName;
    String secondName;
    String dob;
    String currentTime;
    String timeArrived;
    String phoneNumber;
    String gender;


    //An empty constructor to make sure program doesnt crash when invalid inputs are stored
    public Person(){

    }
    //The correct constructor, setting values to the variables
    public Person(String personId, String firstName, String secondName, String phone, String dob, String gender) {
        this.personId = personId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.dob = dob;
        this.phoneNumber = phone;
        this.gender = gender;

    }

    //Map creation
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("personId", personId);
        result.put("firstName", firstName);
        result.put("secondName", secondName);
        result.put("dob", dob);
        result.put("phoneNumber", phoneNumber);
        result.put("gender", gender);
        result.put("currentTime", currentTime);
        result.put("timeArrived", timeArrived);
        return result;
    }

    //A bunch of getters for the variables
    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

}
