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

import java.util.Random;
import java.util.Scanner;

public class FarmerSeq {
    
        public void generateFarmersActivitiesSeq(int numOfFarmers) {

        try {
            Random rand = new Random();
            Utility util = new Utility();
            String users = util.readFile("farmers.txt");
            JSONArray userArr = new JSONArray(users);
            JSONObject userObj = null;
            String userID = "";

            if (userArr != null) {
                userObj = userArr.getJSONObject(rand.nextInt(userArr.length()));
                userID = userObj.getString("id");
            }

            // generate farmers
            int indexDb = 1;
            for (int i = 0; i < numOfFarmers; i++) {
                // generate random number to access any random content from the array
                int numOfActivities = rand.nextInt(10) + 1000;
                indexDb = generateActivitiesSeq(userID, indexDb, numOfActivities);
                indexDb++;
            }

        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

    }
    
    public int generateActivitiesSeq(String userID, int index, int numOfActivities) throws FileNotFoundException {

        Random rand = new Random();
        Utility util = new Utility();
        try {
            // read files that return string
            String farms = util.readFile("farms.txt");
            String plants = util.readFile("plants.txt");
            String fertilizer = util.readFile("fertilizers.txt");
            String pesticide = util.readFile("pesticides.txt");

            // convert string to jsonarray
            JSONArray farmArr = new JSONArray(farms);
            JSONArray plantArr = new JSONArray(plants);
            JSONArray fertArr = new JSONArray(fertilizer);
            JSONArray pestArr = new JSONArray(pesticide);

            // generate activities
            for (int i = 0; i < numOfActivities; i++) {

                String date = util.getRandomDate();
                String action = "";
                String type = "";
                String quantity = "";
                String unit = "";
                String field = "";
                String row = "";
                String farmID = "";
                JSONObject farmObj = null;
                JSONObject plantObj = null;
                JSONObject fertObj = null;
                JSONObject pestObj = null;

                String[] actions = {"sowing", "harvest", "pesticide", "fertilizer", "sales"};
                action = actions[rand.nextInt(actions.length)];

                quantity = String.valueOf(rand.nextInt(11));
                field = String.valueOf(rand.nextInt(10));
                row = String.valueOf(rand.nextInt(10));

                if (farmArr != null) {
                    farmObj = farmArr.getJSONObject(rand.nextInt(farmArr.length()));
                    farmID = farmObj.getString("id");
                }

                if (action.equalsIgnoreCase("sowing") || action.equalsIgnoreCase("harvest") || action.equalsIgnoreCase("sales")) {
                    if (plantArr != null) {
                        plantObj = plantArr.getJSONObject(rand.nextInt(plantArr.length()));
                        type = plantObj.getString("name");
                        unit = plantObj.getString("unitType");
                    }
                } else if (action.equalsIgnoreCase("pesticide")) {
                    if (pestArr != null) {
                        pestObj = fertArr.getJSONObject(rand.nextInt(pestArr.length()));
                        type = pestObj.getString("name");
                        unit = pestObj.getString("unitType");
                    }
                } else if (action.equalsIgnoreCase("fertilizer")) {

                    if (fertArr != null) {
                        fertObj = fertArr.getJSONObject(rand.nextInt(fertArr.length()));
                        type = fertObj.getString("name");
                        unit = fertObj.getString("unitType");
                    }
                }

                Activity act = new Activity(String.valueOf(index), date, action, type, unit, quantity, field, row, farmID, userID);
                activityDA actDA = new dbConnection().getActivityDA();
                actDA.addActivities(act);

                util.writeLog("Success: " + date + " " + action + " " + type + " successfully inserted");
                System.out.println(index + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + field + " " + row + " " + farmID + " " + userID);

                // increment indexDb
                index++;
            }

        } catch (JSONException | SQLException e) {
            util.writeLog("Fail: " + userID + ": The operation is failed executed");
            e.printStackTrace();
        }
        return index;
    }
    
}
