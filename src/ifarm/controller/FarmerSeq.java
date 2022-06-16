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
    private Timer timer = new Timer();
    
    public void generateFarmersActivitiesSeq(int numOfFarmers, int[] numOfActivities, JSONArray userArr,JSONArray farmsArr,JSONArray plantsArr,JSONArray fertArr,JSONArray pestArr) {

        try {
            Random rand = new Random();
            Utility util = new Utility();
//            String users = util.readFile("farmer.txt");
//            JSONArray userArr = new JSONArray(users);
            JSONObject userObj = null;
            String userID = "";
            List<String> userFarm = new ArrayList<>();
            
            timer.setStartTime();
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
                    userFarm = util.stringToArray(userObj.getString("farms"));
                }

                // generate activities random number to access any random content from the array
                indexDb = generateActivitiesSeq(userID, userFarm, indexDb, numOfActivities[i],farmsArr, plantsArr, fertArr, pestArr);
                indexDb++;
            }
            timer.setEndTime();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

    }
    
    public int generateActivitiesSeq(String userID, List<String> userFarms, int index, int numOfActivities,JSONArray farmArr,JSONArray plantsArr,JSONArray fertArr,JSONArray pestArr ) throws FileNotFoundException, JSONException {
        Utility util = new Utility();
        Random rand = new Random();
        // get the farm belong to the farmer
        // get farms from farmer
//            String farms = util.readFile("farms.txt");
//            JSONArray farmArr = new JSONArray(farms);
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
                GenerateActivity1 randAct = new GenerateActivity1(userFarms, farmObj, plantsArr, fertArr, pestArr);
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
    
    public double getExecutionTime() {
        return timer.calcDuration();
    }
}
