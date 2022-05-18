package ifarm.controller;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Farmer implements Runnable {
ActivityLog log;
private int index;

    public Farmer(ActivityLog log){
        this.log = log;
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

            if (userArr != null) {
                userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
                userID = userObj.getString("id");
                //System.out.println(Thread.currentThread().getName() + " " + userObj.getString("name"));
            }

            // generate activities
            for (int i = 0; i < 1; i++) {
                int numOfActivities = rand.nextInt(10) + 1000;
                index = log.generateActivities(userID, index, numOfActivities, log);
                index++;
                //System.out.println(Thread.currentThread().getName() + " " + numOfActivities + " with index DB "+indexDb);
            }
             

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
        Logger.getLogger(Farmer.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }
    
}
