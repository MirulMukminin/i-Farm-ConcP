package ifarm.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Farmer implements Runnable{
LogAccess farmerLog;
private int numOfActivities;
HashMap<String, String> map = new HashMap<String, String>();

    public Farmer(LogAccess farmerLog, int numOfActivities) {
        this.farmerLog = farmerLog;
        this.numOfActivities = numOfActivities;
    }
    
    @Override
    public void run() {
    try {
        Random rand = new Random();
        Utility util = new Utility();
        String users = util.readFile("farmer.txt");
        JSONArray userArr = new JSONArray(users);
        JSONObject userObj = null;
        String userID = "";
        List<String> userFarm = new ArrayList<>();

        //get farmer id and farm object
        if (userArr.length() != 0) {
            userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
            userID = userObj.getString("id");
            userFarm = util.stringToArray(userObj.getString("farms"));
        }    
        
        int indexLog = 1;
        for(int i = 0; i<numOfActivities; i++){
            farmerLog = new LogAccess(userID, userFarm, indexLog, map);
            farmerLog.generateRandActivities();  
            indexLog++;
        }
        
        
    } catch (JSONException | FileNotFoundException ex) {
        Logger.getLogger(Farmer.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        
    }
    
}
