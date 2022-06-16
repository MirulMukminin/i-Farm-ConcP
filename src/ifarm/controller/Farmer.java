package ifarm.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Farmer implements Runnable {
ActivityLog log;
private int index;
private int numOfActivities;

    public Farmer(int numOfActivities, ActivityLog log){
        this.log = log;
        this.numOfActivities = numOfActivities;
    }
    

    public void run() {
        
            try {
            Random rand = new Random();
            Utility util = new Utility();
            String users = util.readFile("farmer.txt");
            JSONArray userArr = new JSONArray(users);
            List<String> userFarms = new ArrayList<>();
            JSONObject userObj = null;
            String userID = "";

            if (userArr.length() != 0) {
            // choose random farmer
            userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
            // get farmer id
            userID = userObj.getString("id");
            // get farmer details
            userFarms = util.stringToArray(userObj.getString("farms"));
            }
            

            // generate activities
            for (int i = 0; i < numOfActivities; i++) {
                index = log.generateActivities(userID, index, userFarms);
                index++;
            }
             

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
        Logger.getLogger(Farmer.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
}