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
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogAccess{
    
private int indexLog;
private String date;
private String action;
private String type;
private String unit;
private String quantity;
private String field;
private String row;
private String farmID;

private String userID;
List<String> userFarm = new ArrayList<>();
HashMap<String, String> map = new HashMap<String, String>();
private String prevPlant;

    ReentrantLock lock = new ReentrantLock();
    Random rand = new Random();
    Utility util = new Utility();
    
    public LogAccess() {
    }

    public LogAccess(String userID, List<String> userFarm, int indexLog, HashMap map) {
        this.userID = userID;
        this.userFarm = userFarm;
        this.indexLog = indexLog;
        this.map = map;
    }
    
    public void generateRandActivities(){
        lock.lock();
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
            
            JSONObject farmObj = null;
            JSONObject plantObj = null;
            JSONObject fertObj = null;
            JSONObject pestObj = null;
            
            String farmPlants;
            String farmFertilizers;
            String farmPesticides;
            
            String[] actions = {"sowing", "harvest", "pesticide", "fertilizer", "sales"};
            action = actions[rand.nextInt(actions.length)];
            date = util.getRandomDate(action, 0);
            
            // get any random farm belong to the farmer        
            if (!userFarm.isEmpty()) {
                farmID = userFarm.get(rand.nextInt(userFarm.size()));
                // get the selected farm details
                farmObj = farmArr.getJSONObject(Integer.valueOf(farmID)-1);
            }
            // initialize random value for quantity, field and row
            quantity = String.valueOf(rand.nextInt(100) + 1);
            field = String.valueOf(rand.nextInt(4) + 4);
            row = String.valueOf(rand.nextInt(4) + 4);            
            
            action = checkRowAvail(map, action);
      
            ArrayList<String> farmPlantArr;
            ArrayList<String> farmPestArr;
            ArrayList<String> farmFertArr;
            if (action.equalsIgnoreCase("sowing") || action.equalsIgnoreCase("harvest") || action.equalsIgnoreCase("sales")) {
                if (prevPlant == null) {
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
        writeLog();
        } catch (JSONException | FileNotFoundException | SQLException   ex) {
            Logger.getLogger(LogAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
    }
    }
     
    
    
public String checkRowAvail(HashMap map, String action) throws SQLException{
    lock.lock();
    try {
        // check if the mapID does not exist in the hashmap
        // insert the activity (if sowing) in the hashmap
        String mapID = "F" + farmID + "FD" + field + "R" + row;
        if(!map.containsKey(mapID)){
            // insert if the action is sowing     
            if (action.equalsIgnoreCase("sowing")) {
                map.put(mapID, action);
                System.out.println("Insert " + mapID + " - " + action);
                return action;
            } else {
                // wait if the action on the empty mapID is not sowing
                 System.out.println("Waiting " + mapID + " - " + action);
                //generateRandActivities();
            }
        } else {
            System.out.println("Row occupied.");
            // get the current action of the mapID
            String mapAction = (String) map.get(mapID);
            // the previous action must be sowing, the mapID must be occupied before proceeding with other action
            if (mapAction.equalsIgnoreCase("sowing")) {
                // check if the action on the mapID 
                if (action.equals("pesticide") || action.equals("fertilizer")) {
                    map.put(mapID, action);
                    System.out.println("Insert " + mapID + " - " + action);
                    return action;
                } else if (action.equals("harvest")){
                    // get the plant type from db that had been sowing
                    activityDA act = new dbConnection().getActivityDA();
                    Activity prevAct = null;
                    prevAct = act.getActivitiesByFarm(farmID, field, row, "sowing");
                    prevPlant = prevAct.getType();
                    // set type and unit for the sowed plant to be harvest
                    type = prevAct.getType();
                    unit = prevAct.getUnit();
                    map.put(mapID, action);
                    System.out.println("Insert " + mapID + " - " + action);
                } else {
                    // action = sales need to wait, plant needs to be harvested
                    System.out.println("Waiting " + mapID + " - " + action);
                }
            } else if (mapAction.equalsIgnoreCase("harvest")) {
                if (action.equals("sales")) {
                    // get the plant type from db that had been sowing
                    activityDA act = new dbConnection().getActivityDA();
                    Activity prevAct = null;
                    prevAct = act.getActivitiesByFarm(farmID, field, row, "sowing");
                    prevPlant = prevAct.getType();
                    // set type and unit for the sowed plant to be harvest
                    type = prevAct.getType();
                    unit = prevAct.getUnit();
                    map.put(mapID, action);
                    System.out.println("Insert " + mapID + " - " + action);
                }
            } else {
                // mapAction = pesticide, fertilizer, sales
                if (mapAction.equalsIgnoreCase("sales")) {
                    // increase year if the previous lot had been occupied
                    date = util.getRandomDate(action, 1);
                } else {
                    map.put(mapID, action);
                    System.out.println("Insert " + mapID + " - " + action);
                }
                System.out.println("Waiting " + mapID + " - " + action);
            }
        }
    } finally {
        lock.unlock();
    }
    return action;
    }
    
    public void writeLog(){
    lock.lock();
    try {
        //Write to database
//            Activity act = new Activity(String.valueOf(indexDb), date, action, type, unit, quantity, field, row, farmID, userID);
//            activityDA actDA = new dbConnection().getActivityDA();
//            actDA.addActivities(act);
        //Write to log
        util.writeLog(Thread.currentThread().getName() + " " + indexLog +" Success: " + date + " " + action + " " + type + " successfully inserted");
        System.out.println(Thread.currentThread().getName() + " " +indexLog + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + farmID + " " + field + " " + row + " " + userID);
    } finally {
        lock.unlock();
    }
    }
    
}
