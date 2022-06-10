/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifarm.controller;

/**
 *
 * @author user
 */

import ifarm.data.Activity;
import ifarm.dataAccess.activityDA;
import ifarm.dbConnection;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FarmerSeq {
    
        public void generateFarmersActivitiesSeq(int numOfFarmers) {

        try {
            Random rand = new Random();
            Utility util = new Utility();
            String users = util.readFile("farmer.txt");
            JSONArray userArr = new JSONArray(users);
            List<String> userFarms = new ArrayList<>();
            JSONObject userObj = null;
            String userID = "";

            if (userArr.length() <= 0) {
                userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
                userID = userObj.getString("id");
            }

            // generate farmers
            int indexDb = 1;
            for (int i = 0; i < numOfFarmers; i++) {

               if (userArr.length() != 0) {
                    // choose random farmer
                    userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
                    // get farmer id
                    userID = userObj.getString("id");
                    // get farmer details
                    // userFarm = util.stringToArray(userObj.getString("farms"));
                    userFarms = util.stringToArray(userObj.getString("farms"));
                }
                
                // generate random number to access any random content from the array
                int numOfActivities = rand.nextInt(10) + 1000;
                indexDb = generateActivitiesSeq(userID, indexDb, numOfActivities, userFarms);
                indexDb++;
            }

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

    }
    
    public int generateActivitiesSeq(String userID, int index, int numOfActivities, List<String> userFarms) throws FileNotFoundException, JSONException {
        Utility util = new Utility();
        Random rand = new Random();
        // get the farm belong to the farmer
        // get farms from farmer
            String farms = util.readFile("farms.txt");
            JSONArray farmArr = new JSONArray(farms);
            JSONObject farmObj = null;
            String farmID = ""; 
            if (!userFarms.isEmpty()) {
                farmID = userFarms.get(rand.nextInt(userFarms.size()));
                // get the selected farm details
                farmObj = farmArr.getJSONObject(Integer.valueOf(farmID)-1);
            }  
        
        try {
            // generate activities
            for (int i = 0; i < numOfActivities; i++) {
                GenerateActivity randAct = new GenerateActivity(userFarms, farmObj);
                String date = randAct.getDate();
                String action = randAct.getAction();
                String type = randAct.getType();
                String quantity = randAct.getQuantity();
                String unit = randAct.getUnit();
                String field = randAct.getField();
                String row = randAct.getRow();

                Activity act = new Activity(String.valueOf(index), date, action, type, unit, quantity, field, row, farmID, userID);
                activityDA actDA = new dbConnection().getActivityDA();
                actDA.addActivities(act);

                util.writeLog("Success: " + date + " " + action + " " + type + " successfully inserted");
                System.out.println(index + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + field + " " + row + " " + farmID + " " + userID);

                // increment indexDb
                index++;
            }

        } catch (SQLException e) {
            util.writeLog("Fail: " + userID + ": The operation is failed executed");
            e.printStackTrace();
        }
        return index;
    }
    
}
