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

public class Farmer1 implements Callable<String>{
ActivityLog log;
private int index;
private int numofThread;
private String userID;
private String farmID;
private int numOfActivities;


    public Farmer1(int numOfActivities, ActivityLog log){
        this.log = log;
        this.numOfActivities = numOfActivities;
    }

    public String getUserID() {
        return userID;
    }

    public int getNumOfAct() {
        return numOfActivities;
    }

  
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setNumOfAct(int numOfAct) {
        this.numOfActivities = numOfAct;
    }

    public String getFarmID() {
        return farmID;
    }

    public void setFarmID(String farmID) {
        this.farmID = farmID;
    }

    

    @Override
    public String call() throws Exception{
        Thread.sleep(5);
        try {
            Random rand = new Random();
            Utility util = new Utility();
            String users = util.readFile("farmer.txt");
            JSONArray userArr = new JSONArray(users);
            JSONObject userObj = null;
            String farms = util.readFile("farms.txt");
            JSONArray farmArr = new JSONArray(farms);
            JSONObject farmObj = null;
            List<String> userFarms = new ArrayList<>();


            if (userArr.length() != 0 && userID==null) {
            // choose random farmer
            userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
            // get farmer id
            userID = userObj.getString("id");
            // get farmer details
            userFarms = util.stringToArray(userObj.getString("farms"));
            farmID = userFarms.get(rand.nextInt(userFarms.size()));
            // get the selected farm details
            farmObj = farmArr.getJSONObject(Integer.valueOf(farmID)-1);
            }
            
            if(!farmID.isEmpty())
                farmObj = farmArr.getJSONObject(Integer.valueOf(farmID)-1);
                    
       
            // generate activities
            for (int i = 0; i < numOfActivities; i++) {
                if(!Thread.currentThread().interrupted()){
                    index = log.generateActivities1(userID, index, farmID,farmObj);
                    index++;
                }
            }
             

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
        Logger.getLogger(Farmer.class.getName()).log(Level.SEVERE, null, ex);
    }
       return Thread.currentThread().getName() + " Done!"; 
    }

    
    
}
