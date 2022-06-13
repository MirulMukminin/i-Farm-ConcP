package ifarm.controller;
//test
import ifarm.data.Activity;
import ifarm.dataAccess.activityDA;
import ifarm.dbConnection;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ActivityLog {
private int indexDb;

    ReentrantLock lock = new ReentrantLock();
    
        
    public int generateActivities(String userID, int index, int numOfActivities, ActivityLog Actlog) throws FileNotFoundException, InterruptedException {
    lock.lock();
        try {
            // generate activities
            for (int i = 0; i < numOfActivities; i++) {
                GenerateActivity randAct = new GenerateActivity();
                String date = randAct.getDate();
                String action = randAct.getAction();
                String type = randAct.getType();
                String quantity = randAct.getQuantity();
                String unit = randAct.getUnit();
                String field = randAct.getField();
                String row = randAct.getRow();
                String farmID = randAct.getFarmID();
                
               //Write to database
                Activity act = new Activity(String.valueOf(indexDb), date, action, type, unit, quantity, field, row, farmID, userID);
                activityDA actDA = new dbConnection().getActivityDA();
                actDA.addActivities(act);
                
                //Write to log
                Actlog.writeLog(Thread.currentThread().getName() + " " + index +" Success: " + date + " " + action + " " + type + " successfully inserted");
                System.out.println(Thread.currentThread().getName() + " " +index + " - " + date + " " + action + " " + type + " " + unit + " " + quantity + " " + field + " " + row + " " + farmID + " " + userID);
                //System.out.println(indexDb);
                
                // increment indexDb
                indexDb++;
                // increment index for thread
                index++;
            }
        } catch (SQLException ex) {
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
