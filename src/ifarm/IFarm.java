package ifarm;

import ifarm.controller.ActivityLog;
import ifarm.controller.Farmer;
import ifarm.controller.Farmer1;
import ifarm.controller.FarmerSeq;
import ifarm.controller.FarmerSimulator;
import ifarm.controller.UpdateLog;
import ifarm.controller.Utility;
import ifarm.data.Farmers;
import ifarm.dataAccess.activityDA;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;

public class IFarm {

    public static void GenerateActivity() throws SQLException, JSONException{
        FarmerSimulator farmerSimulator = new FarmerSimulator();
        Farmers[] farmer = null;
        int user = 0;
        int status = 0;
        int pestStatus = 0;
        int fertStatus = 0;
        int plantStatus = 0;

        activityDA actDA = new dbConnection().getActivityDA();
        
        try {
            
            // truncate activities table
            String message = actDA.truncateActivities();
            System.out.println(message);
            
            // generate random number of farmers, x > 100
            Random rand = new Random();
            //int numOfFarmers = rand.nextInt(10) + 10;
            int numOfFarmers = 10;
            System.out.printf("Number of Farmers: %d \n", numOfFarmers);
            // generate random number of activities for each farmers, x > 1000
            int[] numOfActivities = new int[numOfFarmers];
            for (int i = 1; i <=numOfFarmers; i++) {
                numOfActivities[i-1] =  rand.nextInt(10) + 100;
                System.out.printf("Farmer %d : %d activities \n", i, numOfActivities[i-1]);
            }
                        
            //int numOfFarmers = 1;
            // load data from database to txt file
            farmer = farmerSimulator.generateFarmers(numOfFarmers);
            user = farmerSimulator.generateFarmerFile();
            status = farmerSimulator.generateFarmFile();
            pestStatus = farmerSimulator.generatePesticidesFile();
            fertStatus = farmerSimulator.generateFertilizersFile();
            plantStatus = farmerSimulator.generatePlantFile();

//             generate farmers and activities sequentially
//            FarmerSeq fs = new FarmerSeq();
//            fs.generateFarmersActivitiesSeq(numOfFarmers, numOfActivities);
        
            // generate farmers and activities concurrently
//           ActivityLog actlog = new ActivityLog();
//            ExecutorService pool = Executors.newFixedThreadPool(numOfFarmers);
//                for(int i = 0; i<numOfFarmers; i++){
//                  pool.execute(new Farmer(numOfActivities[i], actlog));
//               }
//
//           pool.shutdown();
//           pool.awaitTermination(5, TimeUnit.SECONDS);
    }catch (SQLException | JSONException e) {
        }
    }
    public static void main(String[] args) throws SQLException, JSONException, IOException, ExecutionException {
        
        
        
        FarmerSimulator farmerSimulator = new FarmerSimulator();
        Farmers[] farmer = null;
        int user = 0;
        int status = 0;
        int pestStatus = 0;
        int fertStatus = 0;
        int plantStatus = 0;

        activityDA actDA = new dbConnection().getActivityDA();
        
        try {
            
            // truncate activities table
            String message = actDA.truncateActivities();
            System.out.println(message);
            
            // generate random number of farmers, x > 100
            Random rand = new Random();
            //int numOfFarmers = rand.nextInt(10) + 10;
            int numOfFarmers = 1;
            System.out.printf("Number of Farmers: %d \n", numOfFarmers);
            // generate random number of activities for each farmers, x > 1000
            int[] numOfActivities = new int[numOfFarmers];
            for (int i = 1; i <=numOfFarmers; i++) {
                numOfActivities[i-1] = 1000;
                System.out.printf("Farmer %d : %d activities \n", i, numOfActivities[i-1]);
            }
                        
            //int numOfFarmers = 1;
            // load data from database to txt file
            farmer = farmerSimulator.generateFarmers(numOfFarmers);
            user = farmerSimulator.generateFarmerFile();
            status = farmerSimulator.generateFarmFile();
            pestStatus = farmerSimulator.generatePesticidesFile();
            fertStatus = farmerSimulator.generateFertilizersFile();
            plantStatus = farmerSimulator.generatePlantFile();

//             generate farmers and activities sequentially
            FarmerSeq fs = new FarmerSeq();
            fs.generateFarmersActivitiesSeq(numOfFarmers, numOfActivities);
        
            // generate farmers and activities concurrently
//           ActivityLog actlog = new ActivityLog();
//            ExecutorService pool = Executors.newFixedThreadPool(numOfFarmers);
//                for(int i = 0; i<numOfFarmers; i++){
//                  pool.execute(new Farmer(numOfActivities[i], actlog));
//               }
//
//           pool.shutdown();
//           pool.awaitTermination(5, TimeUnit.SECONDS);
        //Disaster Simulator
//        HashMap<String, List<String>> map2 = new HashMap<>(); // to store { userid = farm }
//        HashMap<String, Integer> map1 = new HashMap<String, Integer>(); // to store { userid = activity }
//        HashMap<String, String> map = new HashMap<String, String>(); //  to store { userid = threadname }
//        List<String> name = new ArrayList<String>(); // to store failure threadname
//        Utility util = new Utility();
//        String hist = null;
//        Duration interval = null;
//        String userID = null;
//        int pass = 0;
//        int index = 0;
//        int attempt=0;
//        
//        
//        while(true){
//            ExecutorService executor = Executors.newFixedThreadPool(numOfFarmers);
//            if(map.isEmpty())
//                util.writeLog1("Program Starts");
//           
//            for(int i=0;i<numOfFarmers;i++){
//                
//                Farmer1 thread = new Farmer1(numOfActivities[i], actlog);
//                Future<String> future = executor.submit(thread);
//             
//                if (!map.isEmpty() && attempt>0){
//                    int j = i;
//                    if(pass == 1){
//                        j = index;
//                        System.out.println("index pass: "+j);
//                    }else if(pass == -1){
//                        j = index + 1;
//                        System.out.println("index fail: "+j);
//                    }    
//                    if(j>=map1.size()){
//                        j = map1.size()-1;
//                        System.out.println("index end: "+j);
//                    }
//                    thread.setUserID((String) map1.keySet().toArray()[j]);
//                    thread.setNumOfAct(map1.get(map1.keySet().toArray()[j]));
//                    thread.setUserFarms(map2.get(map2.keySet().toArray()[j]));
//                }
//                
//                
//                Integer newInt = i+1;
//                
//                try {
//                    System.out.println("\nStarted..");                    
//                    //to get exeecution time for a thread
//                    if(attempt==0&&i==0){
//                        Instant start = Instant.now();
//                        System.out.println(future.get());
//                        Instant end = Instant.now();
//                        interval = Duration.between(start, end);
//                        System.out.println("Time: "+interval.getSeconds());
//                        hist = "pool-1-thread-"+newInt.toString()+" Success: User "+thread.getUserID()+" Activity "+thread.getNumOfAct();
//                    }                    
//                    else if(attempt>0){
//                        System.out.println(future.get((interval.getSeconds()+120), TimeUnit.SECONDS));   // Increase the execution time for a failure thread                  
//                        pass = 1;
//                        index = i;
//                        hist = "Recover " + map.get(thread.getUserID()) + " Attempt: "+attempt+ " Success: User "+thread.getUserID()+" Activity "+thread.getNumOfAct()+" threadName: pool-"+(attempt+1)+"-thread-"+(i+1);
//                        map.remove(thread.getUserID());
//                        map1.remove(thread.getUserID());
//                        map2.remove(thread.getUserID());
//                        System.out.println(name.size());
//                    }
//                    // Execute thread for first try
//                    else{
//                        if(i==49){//to simulate fail thread at the middle -> lost internet connection, exc. time < expected
//                            System.out.println(future.get((interval.getSeconds()-4), TimeUnit.SECONDS));
//                        }else
//                            System.out.println(future.get());
//                        hist = "pool-1-thread-"+newInt.toString()+" Success: User "+thread.getUserID()+" Activity "+thread.getNumOfAct(); 
//                    }
//                    
//                    System.out.println("Finished!\n");
//                } 
//                //Fail thread if execution time > expected
//                catch (TimeoutException e) {
//                    userID = thread.getUserID();
//                    // cancel the task
//                    future.cancel(true);
//                    System.out.println("Terminated!\n"+userID);
//                    // fail to recover
//                    if(attempt>0){
//                        pass=-1;
//                        hist = "Recover " + map.get(userID)+ " Attempt: "+ attempt + " Fail: User "+userID+" Activity "+thread.getNumOfAct();
//                        String pool1 = "pool-"+(attempt+1)+"-thread-"+(i+1);
//                        name.add(pool1);
//                        util.writeLog(pool1+" Fail: Timeout");
//                    }
//                    //fail for the first time
//                    else{
//                        map.put(userID,"pool-1-thread-"+newInt.toString());
//                        map1.put(userID, numOfActivities[i]);
//                        map2.put(userID, thread.getUserFarms());
//                        name.add("pool-1-thread-"+newInt.toString());
//                        hist = "pool-1-thread-"+newInt.toString()+" Fail: User "+userID+" Activity "+thread.getNumOfAct();
//                        String index1 = newInt.toString();
//                        util.writeLog("pool-1-thread-"+index1+" Fail: Timeout");
//                    }
//                    
//                }
//                util.writeLog1(hist); // update thread log
//            }
//            executor.shutdownNow();
//            
//            System.out.println(map1);
//            
//            
//            // end the loop when all failure threads are successfully recovered
//            if(map.isEmpty()){
//                util.writeLog1("Program Ends");
//                System.out.println("Program Ends.");
//                break;
//            }
//            // start to recover the failure thread
//            else{
//                //delete the data of failure thread from log
//                UpdateLog uf = new UpdateLog(name);
//                uf.update();
//                attempt++; // count attempt to recover failure thread
//                numOfFarmers = name.size(); // according to the number of failure thread
//                name.removeAll(name); // reset the failure threadpool lot
//                System.out.println(map1); //soon nak buang
//                System.out.println("Program Continue. Repeat: "+map.size());
//            }
//        }   
       
        } catch (SQLException | JSONException e) {
        }
                
                
    }
    

}