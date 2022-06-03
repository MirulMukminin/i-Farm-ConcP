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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Scanner;

public class FarmerSeq {
    
        private HashMap<String, String> farmsMapping = new HashMap<>();
        private HashMap<String, String> waitingAct = new HashMap<>();
        private List<Activity> waitingList = new ArrayList<>();
        private int waitingCounter = 0;
    
        public void generateFarmersActivitiesSeq(int numOfFarmers, int[] numOfActivities) {

        try {
            Random rand = new Random();
            Utility util = new Utility();
            String users = util.readFile("farmer.txt");
            JSONArray userArr = new JSONArray(users);
            JSONObject userObj = null;
            String userID = "";
            List<String> userFarm = new ArrayList<>();
            List<String> userList = new ArrayList<>();
            
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
                indexDb = generateActivitiesSeq(userID, userFarm, indexDb, numOfActivities[i]);
                indexDb++;
            }
            
            System.out.println("Farms Map " + farmsMapping);
            System.out.println("Waiting " + waitingAct);

            // if there are activity in waiting list
            while (waitingList.size() >= 0) {
                
            }
            
        } catch (FileNotFoundException | JSONException e) {
        }

    }
    
    public int generateActivitiesSeq(String userID, List<String> userFarms, int index, int numOfActivities) throws FileNotFoundException, JSONException {
        Utility util = new Utility();
        Random rand = new Random();
        try {
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
            
            // generate activities
            for (int i = 0; i < numOfActivities; i++) {
                GenerateActivitySeq randAct = new GenerateActivitySeq(userFarms, farmObj ,farmsMapping, waitingAct);
                String date = randAct.getDate();
                String action = randAct.getAction();
                String type = randAct.getType();
                String quantity = randAct.getQuantity();
                String unit = randAct.getUnit();
                String field = randAct.getField();
                String row = randAct.getRow();
                boolean isWaitingExist = randAct.isWaitingExist();
                
                // if the activity did not enter the waiting hashmap, insert the activity to the db and activity log
                if (!isWaitingExist) {
                    waitingAct = randAct.getWaitingAct();
                    Activity waitingActivity = new Activity(String.valueOf(index), date, action, type, unit, quantity, field, row, farmID, userID);
                    waitingList.add(waitingActivity);
                    waitingCounter++;
                } else {
                    farmsMapping = randAct.getMapping();
                    Activity act = new Activity(String.valueOf(index), date, action, type, unit, quantity, field, row, farmID, userID);
                    activityDA actDA = new dbConnection().getActivityDA();
                    actDA.addActivities(act);

                    util.writeLog("Success: " + date + " " + action + " " + type + " successfully inserted");
                }
                System.out.println(index + ": Farmer " + userID + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + field + " " + row + " " + farmID + " " + userID);

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
