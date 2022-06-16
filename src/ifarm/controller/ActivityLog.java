package ifarm.controller;
//test
import ifarm.data.Activity;
import ifarm.dataAccess.activityDA;
import ifarm.dbConnection;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ActivityLog {
private int indexDb;

    ReentrantLock lock = new ReentrantLock();
    
    //for disaster simulation
    public int generateActivities1(String userID, int index, String farmID, JSONObject farmObj) throws FileNotFoundException, InterruptedException {
    lock.lock();
        try {
            // generate activities
            Utility util = new Utility();
            Random rand = new Random();
            // get the farm belong to the farmer
            // get farms from farmer
                         
            
            GenerateActivity randAct = new GenerateActivity(farmID, farmObj);
            String date = randAct.getDate();
            String action = randAct.getAction();
            String type = randAct.getType();
            String quantity = randAct.getQuantity();
            String unit = randAct.getUnit();
            String field = randAct.getField();
            String row = randAct.getRow();
                
            //Write to database
            Activity act = new Activity(String.valueOf(indexDb), date, action, type, unit, quantity, field, row, farmID, userID);
            activityDA actDA = new dbConnection().getActivityDA();
            actDA.addActivities(act);

            //Write to log
            writeLog(Thread.currentThread().getName() + " " + index +" Success: " + date + " " + action + " " + type + " successfully inserted");
            System.out.println(Thread.currentThread().getName() + " " +index + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + field + " " + row + " " + farmID + " " + userID);

            // increment indexDb
            indexDb++;
            // increment index for thread
        } catch (SQLException ex) {
            Logger.getLogger(ActivityLog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
        return index;
    }
    
    public int generateActivities(String userID, int index, List<String> userFarms) throws FileNotFoundException, InterruptedException {
    lock.lock();
        try {
            // generate activities
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
            
            GenerateActivity randAct = new GenerateActivity(userFarms, farmObj);
            String date = randAct.getDate();
            String action = randAct.getAction();
            String type = randAct.getType();
            String quantity = randAct.getQuantity();
            String unit = randAct.getUnit();
            String field = randAct.getField();
            String row = randAct.getRow();
                
            //Write to database
            Activity act = new Activity(String.valueOf(indexDb), date, action, type, unit, quantity, field, row, farmID, userID);
            activityDA actDA = new dbConnection().getActivityDA();
            actDA.addActivities(act);

            //Write to log
            writeLog(Thread.currentThread().getName() + " " + index +" Success: " + date + " " + action + " " + type + " successfully inserted");
            System.out.println(Thread.currentThread().getName() + " " +index + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + field + " " + row + " " + farmID + " " + userID);

            // increment indexDb
            indexDb++;
            // increment index for thread
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(ActivityLog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
        return index;
    }
    
        public void writeLog(String text) throws InterruptedException {
        lock.lock();
            try {
              Utility util = new Utility();
              util.writeLog(text);
            } finally {
               lock.unlock();
        }
    }
}
