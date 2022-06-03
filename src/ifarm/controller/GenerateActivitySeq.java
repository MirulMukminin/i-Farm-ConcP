package ifarm.controller;

import ifarm.data.Activity;
import ifarm.dataAccess.activityDA;
import ifarm.dbConnection;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GenerateActivitySeq {
private List<String> userFarms = new ArrayList<String>();
    
private String action = "";
private String type = "";
private String quantity = "";
private String unit = "";
private String field = "";
private String row = "";
private String farmID = "";
private String date = "";
private HashMap<String, String> map;
private HashMap<String, String> waitingAct;
private JSONObject farmObj;
private boolean flag;

    public GenerateActivitySeq() throws SQLException{
        generateRand();
    }       

    public GenerateActivitySeq(List<String> farms, JSONObject farmObj, HashMap<String, String> map, HashMap<String, String> waitingAct) throws SQLException {
        this.userFarms = farms;
        this.map = map;
        this.waitingAct = waitingAct;
        this.farmObj = farmObj;
        generateRand();
    }
    
    
    public void generateRand() throws SQLException{
    Random rand = new Random();
    Utility util = new Utility();
    Activity waitingActivity = new Activity();
    
    try {
        // read files
        String plants = util.readFile("plants.txt");
        String fertilizer = util.readFile("fertilizers.txt");
        String pesticide = util.readFile("pesticides.txt");    
        
        // convert string to jsonarray
        JSONArray plantArr = new JSONArray(plants);
        JSONArray fertArr = new JSONArray(fertilizer);
        JSONArray pestArr = new JSONArray(pesticide);
        
        // initialize variables
        JSONObject plantObj = null;
        JSONObject fertObj = null;
        JSONObject pestObj = null;
        String farmPlants;
        String farmFertilizers;
        String farmPesticides;
        String prevPlant = ""; 
        
        String[] actions = {"sowing", "harvest", "pesticide", "fertilizer", "sales"};
        
        // get random action
        action = actions[rand.nextInt(actions.length)];
        // for the first round of field and row, increment 0 for year
        date = util.getRandomDate(action, 0);
        // get the farm id
        farmID = farmObj.getString("id");
        
        // initialize random value for quantity, field and row
        quantity = String.valueOf(rand.nextInt(100) + 1);
        field = String.valueOf(rand.nextInt(4) + 4);
        row = String.valueOf(rand.nextInt(4) + 4);  

        // generate mapID for the current farm, field and row as key
        String mapID = "F" + farmID + "FD" + field + "R" + row;
        
        // check if the mapID does not exist in the hashmap (first time mapid is occupied)
        // insert the activity (if sowing) in the hashmap
        if (!map.containsKey(mapID) && action.equalsIgnoreCase("sowing")) {
            map.put(mapID, action);
            flag = true;
            System.out.println("Insert " + mapID + " - " + action);
            
        } else if (!map.containsKey(mapID)){
            // if the mapID does not exist in hashmap and action = fertilizer, pesticide, harvest, sale
            waitingAct.put(mapID, action);
            flag = false;
            System.out.println("Waiting " + mapID + " - " + action);
            
        } else {
            // if mapID exist in the hashmap
            // get the current action of the mapID
            String mapAction = map.get(mapID);
            // the previous action must be sowing, the mapID must be occupied before proceeding with other action
            if (mapAction.equalsIgnoreCase("sowing")) {
                // action pesticide/fertilizer can proceed with the random type
                if (action.equalsIgnoreCase("pesticide") || action.equalsIgnoreCase("fertilizer")) {
                    map.put(mapID, action);
                    flag = true;
                    System.out.println("Insert " + mapID + " - " + action);
                } else if (action.equalsIgnoreCase("harvest")) {
                    // action harvest must have the same plant that had been sowed
                    // get the plant type from db
                    activityDA act = new dbConnection().getActivityDA();
                    Activity prevAct = act.getActivitiesByFarm(farmID, field, row, "sowing");
                    prevPlant = prevAct.getType();
                    // set type and unit for the sowed plant to be harvest
                    type = prevAct.getType();
                    unit = prevAct.getUnit();
                    map.put(mapID, action);
                    flag = true;
                    System.out.println("Insert " + mapID + " - " + action);
                } else {
                    // cannot sale plant if not harvested yet, so sale and sowing needs to wait
                    waitingAct.put(mapID, action);
                    flag = false;
                    System.out.println("Waiting " + mapID + " - " + action);
                }
                
            } else if (mapAction.equalsIgnoreCase("harvest")) {
                 //proceed with sale if the previous action is harvest
                if (action.equalsIgnoreCase("sales")) {
                    // action sale must have the same plant that had been sowed and harvest
                    // get the plant type from db
                    activityDA act = new dbConnection().getActivityDA();
                    Activity prevAct = act.getActivitiesByFarm(farmID, field, row, "sowing");
                    prevPlant = prevAct.getType();
                    // set type and unit for the sowed plant to be harvest
                    type = prevAct.getType();
                    unit = prevAct.getUnit();
                    map.put(mapID, action);
                    flag = true;
                    System.out.println("Insert " + mapID + " - " + action);
                } else {
                    // after harvest must be sale, pesticide, fertilizer, harvest wait
                    waitingAct.put(mapID, action);
                    flag = false;
                    System.out.println("Waiting " + mapID + " - " + action);
                }
            } else if (mapAction.equalsIgnoreCase("sales")) {
                 //after the plant had been sold, a new plant can be sowed
                if (action.equalsIgnoreCase("sowing")) {
                    map.put(mapID, action);
                    flag = true;
                    // increase year of the date
                    date = util.getRandomDate(action, 1);
                    System.out.println("Insert " + mapID + " - " + action);
                } else {
                    // pesticide, harvest, fertilizer and sale need to wait
                    waitingAct.put(mapID, action);
                    flag = false;
                    System.out.println("Waiting " + mapID + " - " + action);
                }
            }
        }
        
        // get random type of plant, fertilizer and pesticide
        ArrayList<String> farmPlantArr;
        ArrayList<String> farmPestArr;
        ArrayList<String> farmFertArr;
        
        if (action.equalsIgnoreCase("sowing") || action.equalsIgnoreCase("harvest") || action.equalsIgnoreCase("sales")) {
            if (prevPlant.equalsIgnoreCase("")) {
                if (plantArr.length() != 0) {
                    // get the farm plants
                    farmPlants = farmObj.getString("plants");
                    // convert the string to array
                    farmPlantArr = util.stringToArray(farmPlants);
                    // get random plant that available in the farm
                    String plantID = farmPlantArr.get(rand.nextInt(farmPlantArr.size()));
                    plantObj = plantArr.getJSONObject(Integer.valueOf(plantID)-1);
                    type = plantObj.getString("name");
                    unit = plantObj.getString("unitType");
                }
            }
        } else if (action.equalsIgnoreCase("pesticide")) {
            if (pestArr.length() != 0) {
                // get the farm pesticide
                farmPesticides = farmObj.getString("pesticides");
                // convert the string to array
                farmPestArr = util.stringToArray(farmPesticides);
                // get random pesticide that available in the farm
                String pestID = farmPestArr.get(rand.nextInt(farmPestArr.size()));
                pestObj = fertArr.getJSONObject(Integer.valueOf(pestID)-1);
                type = pestObj.getString("name");
                unit = pestObj.getString("unitType");
            }
        } else if (action.equalsIgnoreCase("fertilizer")) {        
            if (fertArr.length() != 0) {
                // get the farm fertilizer
                farmFertilizers = farmObj.getString("fertilizers");
                // convert the string to array
                farmFertArr = util.stringToArray(farmFertilizers);
                // get random fertilizer that available in the farm
                String fertID = farmFertArr.get(rand.nextInt(farmFertArr.size()));
                fertObj = fertArr.getJSONObject(Integer.valueOf(fertID)-1);
                type = fertObj.getString("name");
                unit = fertObj.getString("unitType");
            }
        }
        
    } catch (FileNotFoundException | NumberFormatException | SQLException | JSONException e) {}
        
    }
    
  
//    public void insertWaitingActivity(Activity waitingActivity, HashMap<String, String> map) {
//        
//        Utility util = new Utility();
//        String mapID = "F" + waitingActivity.getFarmId() + "FD" + waitingActivity.getField() + "R" + waitingActivity.getRow();
//        String waitAction = waitingActivity.getAction();
//        // check if the mapID does not exist in the hashmap (first time mapid is occupied)
//        // insert the activity (if sowing) in the hashmap
//        if (!map.containsKey(mapID) && waitAction.equalsIgnoreCase("sowing")) {
//            map.put(mapID, waitAction);
//            flag = true;
//            System.out.println("Insert " + mapID + " - " + waitAction);
//            // remove waiting activity from waiting hashmap
//            waitingAct.remove(mapID);
//        } else if (!map.containsKey(mapID)){
//            // if the mapID does not exist in hashmap and action = fertilizer, pesticide, harvest, sale
//            waitingAct.put(mapID, waitAction);
//            flag = false;
//            System.out.println("Waiting " + mapID + " - " + waitAction);
//        } else {
//            // if mapID exist in the hashmap
//            // get the current action of the mapID
//            String mapAction = map.get(mapID);
//            // the previous action must be sowing, the mapID must be occupied before proceeding with other action
//            if (mapAction.equalsIgnoreCase("sowing")) {
//                // action pesticide/fertilizer can proceed with the random type
//                if (waitAction.equalsIgnoreCase("pesticide") || action.equalsIgnoreCase("fertilizer")) {
//                    map.put(mapID, waitAction);
//                    flag = true;
//                    System.out.println("Insert " + mapID + " - " + waitAction);
//                } else if (waitAction.equalsIgnoreCase("harvest")) {
//                    // action harvest must have the same plant that had been sowed
//                    // get the plant type from db
//                    activityDA act = new dbConnection().getActivityDA();
//                    Activity prevAct = act.getActivitiesByFarm(farmID, field, row, "sowing");
//                    prevPlant = prevAct.getType();
//                    // set type and unit for the sowed plant to be harvest
//                    type = prevAct.getType();
//                    unit = prevAct.getUnit();
//                    map.put(mapID, waitAction);
//                    flag = true;
//                    System.out.println("Insert " + mapID + " - " + waitAction);
//                } else {
//                    // cannot sale plant if not harvested yet, so sale and sowing needs to wait
//                    waitingAct.put(mapID, action);
//                    flag = false;
//                    System.out.println("Waiting " + mapID + " - " + waitAction);
//                }
//                
//            } else if (mapAction.equalsIgnoreCase("harvest")) {
//                 //proceed with sale if the previous action is harvest
//                if (waitAction.equalsIgnoreCase("sales")) {
//                    // action sale must have the same plant that had been sowed and harvest
//                    // get the plant type from db
//                    activityDA act = new dbConnection().getActivityDA();
//                    Activity prevAct = act.getActivitiesByFarm(farmID, field, row, "sowing");
//                    prevPlant = prevAct.getType();
//                    // set type and unit for the sowed plant to be harvest
//                    type = prevAct.getType();
//                    unit = prevAct.getUnit();
//                    map.put(mapID, waitAction);
//                    flag = true;
//                    System.out.println("Insert " + mapID + " - " + waitAction);
//                } else {
//                    // after harvest must be sale, pesticide, fertilizer, harvest wait
//                    waitingAct.put(mapID, waitAction);
//                    flag = false;
//                    System.out.println("Waiting " + mapID + " - " + waitAction);
//                }
//            } else if (mapAction.equalsIgnoreCase("sales")) {
//                 //after the plant had been sold, a new plant can be sowed
//                if (waitAction.equalsIgnoreCase("sowing")) {
//                    map.put(mapID, waitAction);
//                    flag = true;
//                    // increase year of the date
//                    date = util.getRandomDate(waitAction, 1);
//                    System.out.println("Insert " + mapID + " - " + waitAction);
//                } else {
//                    // pesticide, harvest, fertilizer and sale need to wait
//                    waitingAct.put(mapID, waitAction);
//                    flag = false;
//                    System.out.println("Waiting " + mapID + " - " + waitAction);
//                }
//            }
//        }
//        
//    }
    
    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getField() {
        return field;
    }

    public String getRow() {
        return row;
    }

    public String getFarmID() {
        return farmID;
    }
    
    public String getDate() {
        return date;
    }

    public HashMap<String, String> getMapping() {
        return map;
    }
    
    public boolean isWaitingExist() {
        return flag;
    }
    
    public HashMap<String, String> getWaitingAct() {
        return waitingAct;
    }
    
}
