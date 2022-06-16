package ifarm;

import ifarm.controller.ActivityLog;
import ifarm.controller.Farmer;
import ifarm.controller.Farmer1;
import ifarm.controller.FarmerSeq;
import ifarm.controller.FarmerSimulator;
import ifarm.controller.Timer;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            int numOfFarmers = 100;
            System.out.printf("Number of Farmers: %d \n", numOfFarmers);
            // generate random number of activities for each farmers, x > 1000
            int[] numOfActivities = new int[numOfFarmers];
            for (int i = 1; i <=numOfFarmers; i++) {
                numOfActivities[i-1] =  rand.nextInt(10) + 1000;
                //System.out.printf("Farmer %d : %d activities \n", i, numOfActivities[i-1]);
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
    
    private String action = "";
    private String type = "";
    private String quantity = "";
    private String unit = "";
    private String field = "";
    private String row = "";
    private String farmID = ""; 
    private String date; 
    private JSONObject farmObj;
    
    public static void main(String[] args) throws SQLException, JSONException, IOException, ExecutionException {
        
        FarmerSimulator farmerSimulator = new FarmerSimulator();
        Farmers[] farmer = null;
        int user = 0;
        int status = 0;
        int pestStatus = 0;
        int fertStatus = 0;
        int plantStatus = 0;

        activityDA actDA = new dbConnection().getActivityDA();
        Timer timer = new Timer();
        // truncate activities table
        String message = actDA.truncateActivities();
        System.out.println(message);
        // generate random number of farmers, x > 100
        Random rand = new Random();
        Utility util = new Utility();
        //int numOfFarmers = rand.nextInt(10) + 10;
        int numOfFarmers = 100;
        System.out.printf("Number of Farmers: %d \n", numOfFarmers);
        // generate random number of activities for each farmers, x > 1000
        int[] numOfActivities = new int[numOfFarmers];
        for (int i = 1; i <= numOfFarmers; i++) {
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
        // convert text file content to JSONArray
        JSONArray farmerArr = farmerSimulator.getTextToJsonArray("farmer.txt");
        JSONArray farmsArr = farmerSimulator.getTextToJsonArray("farms.txt");
        JSONArray plantsArr = farmerSimulator.getTextToJsonArray("plants.txt");
        JSONArray fertArr = farmerSimulator.getTextToJsonArray("fertilizers.txt");
        JSONArray pestArr = farmerSimulator.getTextToJsonArray("pesticides.txt");
        //             generate farmers and activities sequentially
        FarmerSeq fs = new FarmerSeq();
        timer.setStartTime();
        fs.generateFarmersActivitiesSeq(numOfFarmers, numOfActivities, farmerArr, farmsArr, plantsArr, fertArr, pestArr);
        timer.setEndTime();
        System.out.println("Time for sequential: " + timer.calcDuration());
        // generate farmers and activities concurrently
//           ActivityLog actlog = new ActivityLog();
//            ExecutorService pool = Executors.newFixedThreadPool(numOfFarmers);
//                for(int i = 0; i<numOfFarmers; i++){
//                  pool.execute(new Farmer(numOfActivities[i], actlog));
//               }
//
//           pool.shutdown();
//           pool.awaitTermination(5, TimeUnit.SECONDS);

//            truncate activities table
//            String messageC = actDA.truncateActivities();
//            System.out.println(messageC);
//            ExecutorService executor = Executors.newFixedThreadPool(numOfFarmers);
//                    ActivityLog actlog = new ActivityLog();
//                    timer.setStartTime();
//                    for(int i = 0; i<numOfFarmers; i++){
//                        Future<String> future = executor.submit(new Farmer1(numOfActivities[i], actlog));
//                        System.out.println(future.get());
//                    }
//                    executor.shutdown();
//                    timer.setEndTime();
//                    System.out.println("Time for concurrent: " + timer.calcDuration());
                
                
    }
    

}
