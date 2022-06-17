package ifarm;

import DataVis.Driver;
import DataVis.DriverStream;
import ifarm.controller.ActivityLog;
import ifarm.controller.DisasterSimulation;
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
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;

public class IFarm {
        static Timer timer = new Timer();
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
            System.out.println("Generating the activities sequentially...");
            FarmerSeq fs = new FarmerSeq();
            timer.setStartTime();
            fs.generateFarmersActivitiesSeq(numOfFarmers, numOfActivities);
            timer.setEndTime();   
            System.out.println("Time for sequential approach: "+ timer.calcDuration() +" seconds");
            
            // generate farmers and activities concurrently
            System.out.println("Generating the activities concurrently");
            ActivityLog actlog = new ActivityLog();
            timer.setStartTime();
            ExecutorService pool = Executors.newFixedThreadPool(numOfFarmers);
                for(int i = 0; i<numOfFarmers; i++){
                  pool.execute(new Farmer(numOfActivities[i], actlog));
           }
           pool.shutdown();
           try{
               pool.awaitTermination(1, TimeUnit.DAYS); }
           catch(InterruptedException ex){ 
           }
           timer.setEndTime();        
           System.out.println("Time for concurrent approach: "+ timer.calcDuration() +" seconds");

           pool.shutdown();
           pool.awaitTermination(5, TimeUnit.SECONDS);
    }catch (InterruptedException | SQLException | JSONException e) {
        }
    }
    
    
    public static void main(String[] args) throws SQLException, JSONException, IOException, ExecutionException, InterruptedException {
        
        FarmerSimulator farmerSimulator = new FarmerSimulator();
        Farmers[] farmer = null;
        int user = 0;
        int status = 0;
        int pestStatus = 0;
        int fertStatus = 0;
        int plantStatus = 0;

        // generate random number of farmers, x > 100
        Random rand = new Random();
        //int numOfFarmers = rand.nextInt(10) + 10;
        int numOfFarmers = 10;
        System.out.printf("Number of Farmers: %d \n", numOfFarmers);
        // generate random number of activities for each farmers, x > 1000
        int[] numOfActivities = new int[numOfFarmers];

        activityDA actDA = new dbConnection().getActivityDA();
        ActivityLog actlog = new ActivityLog();
        Scanner sc = new Scanner(System.in);
        try {
            
            boolean flag = true;
            while (flag) {
                System.out.println("\nHi. Welcome to i-Farm. Please choose an option.");
                System.out.println("1. Generate data to text file.");
                System.out.println("2. Comparison of Sequential approach and Concurrent approach.");
                System.out.println("3. Disaster simulation.");
                System.out.println("4. View data visualization. ");
                System.out.println("5. View data visualization using stream.");
                System.out.println("0. Exit");
                int choice = sc.nextInt();
                switch(choice){
            case 1:
                // truncate activities table
                String message = actDA.truncateActivities();
                System.out.println(message);
                // load data from database to txt file
                farmer = farmerSimulator.generateFarmers(numOfFarmers);
                user = farmerSimulator.generateFarmerFile();
                status = farmerSimulator.generateFarmFile();
                pestStatus = farmerSimulator.generatePesticidesFile();
                fertStatus = farmerSimulator.generateFertilizersFile();
                plantStatus = farmerSimulator.generatePlantFile();
                for (int i = 1; i <=numOfFarmers; i++) {
                    numOfActivities[i-1] =  rand.nextInt(10) + 100;
                    System.out.printf("Farmer %d : %d activities \n", i, numOfActivities[i-1]);
                }
                break;
            case 2:
                // truncate the activities table
                actDA.truncateActivities();
                // generate farmers and activities sequentially
                System.out.println("Generating the activities sequentially...");
                timer.setStartTime();
                FarmerSeq fs = new FarmerSeq();
                fs.generateFarmersActivitiesSeq(numOfFarmers, numOfActivities);
                timer.setEndTime();
                System.out.println("Time for sequential approach: "+ timer.calcDuration() +" seconds");

                // truncate the activities table
                // generate farmers and activities concurrently
                actDA.truncateActivities();
                System.out.println("Generating the activities concurrently...");
                timer.setStartTime();
                ExecutorService pool = Executors.newFixedThreadPool(numOfFarmers);
                    for(int i = 0; i<numOfFarmers; i++){
                      pool.execute(new Farmer(numOfActivities[i], actlog));
               }
               pool.shutdown();
               try{
                   pool.awaitTermination(1, TimeUnit.DAYS); }
               catch(InterruptedException ex){ 
               }
                timer.setEndTime();
                System.out.println("Time for concurrent approach: "+ timer.calcDuration() +" seconds");
                break;
            case 3:
                String message3 = actDA.truncateActivities();
                //Disaster Simulation latest version
                DisasterSimulation disaster = new DisasterSimulation(numOfFarmers, actlog, numOfActivities);
                break;
            case 4:
                Driver.main(args);
            case 5:
                DriverStream.main(args);
            case 0:
                System.out.println("Thank you");
                System.exit(0);
            default:
                System.out.println("Invalid option, please try again");
        }
       }
                       
        } catch ( SQLException | JSONException e) {
        }
                
                
    }
    

}
