package ifarm;

import ifarm.controller.ActivityLog;
import ifarm.controller.Farmer;
import ifarm.controller.FarmerSeq;
import ifarm.controller.FarmerSimulator;
import ifarm.data.Farmers;
import ifarm.dataAccess.activityDA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;

public class IFarm {

    public static void main(String[] args) throws SQLException, JSONException {
        
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
            int numOfFarmers = rand.nextInt(10) + 10;
            System.out.printf("Number of Farmers: %d \n", numOfFarmers);
            // generate random number of activities for each farmers, x > 1000
            int[] numOfActivities = new int[numOfFarmers];
            for (int i = 0; i < numOfFarmers; i++) {
                numOfActivities[i] =  rand.nextInt(10) + 100;
                System.out.printf("Farmer %d : %d activities \n", i, numOfActivities[i]);
            }
            
            //int numOfFarmers = 1;
            // load data from database to txt file
            farmer = farmerSimulator.generateFarmers(numOfFarmers);
            user = farmerSimulator.generateFarmerFile();
            status = farmerSimulator.generateFarmFile();
            pestStatus = farmerSimulator.generatePesticidesFile();
            fertStatus = farmerSimulator.generateFertilizersFile();
            plantStatus = farmerSimulator.generatePlantFile();

            // generate farmers and activities sequentially
            FarmerSeq fs = new FarmerSeq();
            fs.generateFarmersActivitiesSeq(numOfFarmers, numOfActivities);
        
            // generate farmers and activities concurrently
//            ActivityLog actlog = new ActivityLog();
//            ExecutorService pool = Executors.newFixedThreadPool(numOfFarmers);
//                for(int i = 0; i<numOfFarmers; i++){
//                   pool.execute(new Farmer(actlog));
//                }
//
//            pool.shutdown();
//            pool.awaitTermination(5, TimeUnit.SECONDS);

        } catch (SQLException | JSONException e) {
        }


        
        
        
    }
    

}
