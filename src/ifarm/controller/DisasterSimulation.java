package ifarm.controller;

import ifarm.dataAccess.activityDA;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;

public class DisasterSimulation {
    

    public DisasterSimulation(int numOfFarmer, ActivityLog actlog, int[] numOfActivities) throws IOException, SQLException, InterruptedException, ExecutionException {
        disaster(numOfFarmer,actlog, numOfActivities);
    }
    
    
    public void disaster(int numOfFarmers, ActivityLog actlog, int[] numOfActivities) throws IOException, SQLException, InterruptedException, ExecutionException{
        HashMap<String, String> map2 = new HashMap<>(); // to store { userid = farm }
        HashMap<String, Integer> map1 = new HashMap<String, Integer>(); // to store { userid = activity }
        HashMap<String, String> map = new HashMap<String, String>(); //  to store { userid = threadname }
        List<String> name = new ArrayList<String>(); // to store failure threadname
        Utility util = new Utility();
        activityDA del = new activityDA();
        String hist = null;
        Duration interval = null;
        String userID = null;
        int pass = 0;
        int index = 0;
        int attempt=0;
        
        
        while(true){
            ExecutorService executor = Executors.newFixedThreadPool(numOfFarmers);
            if(map.isEmpty())
                util.writeLog1("Program Starts");
           
            for(int i=0;i<numOfFarmers;i++){
                
                Farmer1 thread = new Farmer1(numOfActivities[i], actlog);
                Future<String> future = executor.submit(thread);
             
                if (!map.isEmpty() && attempt>0){
                    int j = i;
                    if(pass == 1){
                        j = index;
                        System.out.println("index pass: "+j);
                    }else if(pass == -1){
                        j = index + 1;
                        System.out.println("index fail: "+j);
                    }    
                    if(j>=map1.size()){
                        j = map1.size()-1;
                        System.out.println("index end: "+j);
                    }
                    thread.setUserID((String) map1.keySet().toArray()[j]);
                    thread.setNumOfAct(map1.get(map1.keySet().toArray()[j]));
                    thread.setFarmID(map2.get(map2.keySet().toArray()[j]));
                }
                
                
                Integer newInt = i+1;
                
                try {
                    System.out.println("\nStarted..");                    
                    //to get exeecution time for a thread
                    if(attempt==0&&i==0){
                        Instant start = Instant.now();
                        System.out.println(future.get());
                        Instant end = Instant.now();
                        interval = Duration.between(start, end);
                        System.out.println("Time: "+interval.getSeconds());
                        hist = "pool-1-thread-"+newInt.toString()+" Success: User "+thread.getUserID()+" Farm "+thread.getFarmID()+" Activity "+thread.getNumOfAct();
                    }                    
                    else if(attempt>0){
                        System.out.println(future.get((interval.getSeconds()+120), TimeUnit.SECONDS));   // Increase the execution time for a failure thread                  
                        pass = 1;
                        index = i;
                        hist = "Recover " + map.get(thread.getUserID()) + " Attempt: "+attempt+ " Success: User "+thread.getUserID()+" Farm "+thread.getFarmID()+" Activity "+thread.getNumOfAct()+" threadName: pool-"+(attempt+1)+"-thread-"+(i+1);
                        map.remove(thread.getUserID());
                        map1.remove(thread.getUserID());
                        map2.remove(thread.getUserID());
                        System.out.println(name.size());
                    }
                    // Execute thread for first try
                    else{
                        if(i==1){//to simulate fail thread at the middle -> lost internet connection, exc. time < expected -4
                            System.out.println(future.get((interval.getSeconds()-4), TimeUnit.SECONDS));
                        }else
                            System.out.println(future.get());
                        hist = "pool-1-thread-"+newInt.toString()+" Success: User "+thread.getUserID()+" Farm "+thread.getFarmID()+" Activity "+thread.getNumOfAct(); 
                    }
                    
                    System.out.println("Finished!\n");
                } 
                //Fail thread if execution time > expected
                catch (TimeoutException e) {
                    userID = thread.getUserID();
                    // cancel the task
                    future.cancel(true);
                    System.out.println("Terminated!\n"+userID);
                    // fail to recover
                    if(attempt>0){
                        pass=-1;
                        hist = "Recover " + map.get(userID)+ " Attempt: "+ attempt + " Fail: User "+userID+" Farm "+thread.getFarmID()+" Activity "+thread.getNumOfAct();
                        String pool1 = "pool-"+(attempt+1)+"-thread-"+(i+1);
                        name.add(pool1);
                        util.writeLog(pool1+" Fail: Timeout");
                    }
                    //fail for the first time
                    else{
                        map.put(userID,"pool-1-thread-"+newInt.toString());
                        map1.put(userID, numOfActivities[i]);
                        map2.put(userID, thread.getFarmID());
                        name.add("pool-1-thread-"+newInt.toString());
                        hist = "pool-1-thread-"+newInt.toString()+" Fail: User "+userID+" Farm "+thread.getFarmID()+" Activity "+thread.getNumOfAct();
                        String index1 = newInt.toString();
                        util.writeLog("pool-1-thread-"+index1+" Fail: Timeout");
                    }
                    
                }
                util.writeLog1(hist); // update thread log
            }
            executor.shutdownNow();
            
            System.out.println(map1);
            
            
            // end the loop when all failure threads are successfully recovered
            if(map.isEmpty()){
                util.writeLog1("Program Ends");
                System.out.println("Program Ends.");
                break;
            }
            // start to recover the failure thread
            else{
                //delete the data of failure thread from log
                UpdateLog uf = new UpdateLog(name);
                uf.update();
                for(int j=0;j<map.size();j++)
                    System.out.println(del.deleteFailActivities((String) map1.keySet().toArray()[j],map2.get(map2.keySet().toArray()[j])));
                attempt++; // count attempt to recover failure thread
                numOfFarmers = name.size(); // according to the number of failure thread
                name.removeAll(name); // reset the failure threadpool lot
                System.out.println("Program Continue.");
            }
        }

    }

}
