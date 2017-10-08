package com.example.ninjawaffle.myapplication;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas Zaidan on 4/09/2017.
 */


public class Medicare {
    //The values stored in Person
    String personId;;
    String dob;
    String gender;


    //An empty constructor to make sure program doesnt crash when invalid inputs are stored
    public Medicare(){

    }
    //The correct constructor, setting values to the variables
    public Medicare(String personId) {
        this.personId = personId;
        this.dob = "";
        this.gender = "";

    }

    //Map creation
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("personId", personId);
        result.put("dob", dob);
        result.put("gender", gender);
        return result;
    }

    //A bunch of getters for the variables
    public String getPersonId() {
        return personId;
    }


}
