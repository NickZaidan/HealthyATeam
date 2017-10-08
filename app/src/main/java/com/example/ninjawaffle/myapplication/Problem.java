package com.example.ninjawaffle.myapplication;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicholas Zaidan on 8/09/2017.
 */

//Refer to Person object class, same logic
public class Problem {
    String problem;
    String currentTime;
    String timeArrived;
    String id;
    String eta;


    //An empty constructor to make sure program doesnt crash when invalid inputs are stored
    public Problem() {

    }

    public Problem(String problem, String id, String eta) {
        this.problem = problem;
        this.id = id;
        this.currentTime = DateFormat.getDateTimeInstance().format(new Date());
        this.timeArrived = " ";
        this.eta = eta;

    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("problem", problem);
        result.put("currentTime", currentTime);
        result.put("timeArrived", timeArrived);
        result.put("personID", id);
        result.put("ETA", eta);
        return result;
    }
}