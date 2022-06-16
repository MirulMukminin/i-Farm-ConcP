

package DataVis;

import ifarm.controller.Timer;
import ifarm.data.Activity;
import ifarm.data.Farmers;
import ifarm.data.Farms;
import ifarm.data.Fertilizers;
import ifarm.data.Pesticides;
import ifarm.data.Plants;
import ifarm.data.Quantity;
import ifarm.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.json.JSONException;

/**
 *
 * @author User
 */
public class DriverStream {
    
    static List<Activity> activityList= new ArrayList();
    static List<Farmers> farmerList= new ArrayList();
    static List<Farms> farmList= new ArrayList();
    static List<Plants> plantList= new ArrayList();
    static List<Fertilizers> fertList= new ArrayList();
    static List<Pesticides> pestList= new ArrayList();
    static List<Quantity> quantityList= new ArrayList();
    Scanner sc = new Scanner(System.in);
    static Timer timer = new Timer();
    static double totalTime ;
    public static void main(String[] args) throws SQLException, JSONException{
        
        Connection con = dbConnection.createCon();
        //to insert user data to userList
        PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM users");
        ResultSet rst1 = stmt1.executeQuery();        
        
        while(rst1.next()){
            Farmers farmer = new Farmers();
            farmer.setFarmerID(rst1.getString("users_id"));
            farmer.setName(rst1.getString("name"));
            farmerList.add(farmer);
        }
        //to insert farm data to farmList
        PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM farms");
        ResultSet rst2 = stmt2.executeQuery();    
        
        while(rst2.next()){
            Farms farm = new Farms();
            farm.setFarmID(rst2.getString("farms_id"));
            farm.setName(rst2.getString("name"));
            farmList.add(farm);
        }
        
        //to insert plants data to plantList
        PreparedStatement stmt3 = con.prepareStatement("SELECT * FROM plants");
        ResultSet rst3 = stmt3.executeQuery();            
        while(rst3.next()){
            Plants plant = new Plants();
            plant.setPlantID(rst3.getString("plants_id"));
            plant.setName(rst3.getString("name"));
            plantList.add(plant);
        }
        
        //to insert fertilizers data to fertList
        PreparedStatement stmt4 = con.prepareStatement("SELECT * FROM fertilizers");
        ResultSet rst4 = stmt4.executeQuery();    
        
        while(rst4.next()){
            Fertilizers fert = new Fertilizers();
            fert.setFertID(rst4.getString("fertilizers_id"));
            fert.setName(rst4.getString("name"));
            fertList.add(fert);
        }
        
        //to insert pesticides data to pestList
        PreparedStatement stmt5 = con.prepareStatement("SELECT * FROM pesticides");
        ResultSet rst5 = stmt5.executeQuery();    
        
        while(rst5.next()){
            Pesticides pest = new Pesticides();
            pest.setPestID(rst5.getString("pesticides_id"));
            pest.setName(rst5.getString("name"));
            pestList.add(pest);
        }
        
        //to insert activity data to activityList
        PreparedStatement stmt6 = con.prepareStatement("SELECT * FROM activities");
        ResultSet rst6 = stmt6.executeQuery();
        
        while(rst6.next()){
                Activity activity = new Activity();
                
                activity.setId(rst6.getString("activities_id"));
                activity.setDate(rst6.getString("date"));
                activity.setAction(rst6.getString("action"));
                activity.setType(rst6.getString("type"));
                activity.setUnit(rst6.getString("unit"));
                activity.setQuantity(rst6.getString("quantity"));
                activity.setField(rst6.getString("field"));
                activity.setRow(rst6.getString("row"));
                activity.setFarmId(rst6.getString("farmId"));
                activity.setUserId(rst6.getString("userId"));               
                activityList.add(activity);
        }
        
        //to insert quantity from activity table to quantityList
        PreparedStatement stmt7 = con.prepareStatement("SELECT * FROM activities");
        ResultSet rst7 = stmt7.executeQuery();
        
        try{
            while(rst7.next()){
                Quantity q = new Quantity();
                
                q.setActivityID(rst7.getString("activities_id"));
                q.setItemType(rst7.getString("type"));
                q.setDate(rst7.getString("date"));
                q.setItemQuantity(Integer.parseInt(rst7.getString("quantity")));
                quantityList.add(q);
            }
        }finally{
            con.close();
        }
        
        while(true){
            System.out.println("Please choose an option: ");
        System.out.println("1: Display all activity logs for a farm.");
        System.out.println("2: Display all activity logs for a farmer.");
        System.out.println("3: Display all activity logs for a farm and a specific plant/fertilizer/pesticide.");
        System.out.println("4: Display all activity logs for a farm and a specific plant/fertilizer/pesticide between 2 dates (inclusive)");
        System.out.println("5: Display summarized activity logs for a farm and a specific plant/fertilizer/pesticide between 2 dates (inclusive)");
        System.out.println("0: Exit");
        Scanner sc = new Scanner(System.in);
        String ans = sc.next();
        switch (ans){
                case "1":
                    option1();
                    break;
                case "2":
                    option2();
                    break;
                case "3":
                    option3();
                    break;
                case "4":
                    option4();
                    break;
                case "5":
                    option5();
                    break;
                case "0":
                    option0();
                    break;
                default:
                    System.out.println("That is not a valid option, please try again.");                                      
        }
        }       
    }
       
    public static void option1() throws SQLException{ 
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        Scanner sc = new Scanner(System.in);
        String id ;
                       
        System.out.println("Enter the farm's name:");
        String name = sc.nextLine();
        timer.setStartTime(); //timer start
        Optional<Farms> matchingObject = 
            farmList
                    .parallelStream()
                    .filter(farm -> farm.getName().equals(name))
                    .findFirst();
            if(matchingObject.isPresent()){
                Farms fm = matchingObject.get();
                id = fm.getFarmID();
            }else{
                id = null;
            }
        
        if(id == null){
            System.out.println("Farm not found");
        }
        activityList.parallelStream()
                .filter(act -> act.getFarmId().equals(String.valueOf(id)))
                .forEach(act -> 
                     printLog(act.getId(), LocalDate.parse(act.getDate(), formatter), act.getAction(), act.getType(), act.getFarmId() , act.getField(), act.getRow(), act.getUserId(), Integer.parseInt(act.getQuantity()), act.getUnit())                    
                );
        timer.setEndTime(); //stop timer
        totalTime += timer.calcDuration(); 
    }
    
    public static void option2() throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        Scanner sc = new Scanner(System.in);
        String id;
        
        System.out.println("Enter the farmer's name:");
        String name = sc.next();
        timer.setStartTime(); //start timer
        Optional<Farmers> matchingObject = 
            farmerList
                    .parallelStream()
                    .filter(farmer -> farmer.getName().equals(String.valueOf(name)))
                    .findFirst();
            if(matchingObject.isPresent()){
                Farmers fm = matchingObject.get();
                id = fm.getFarmerID();
            }else{
                id = null;
            }

        if(id == null){
            System.out.println("Farmer not found");
        }
        activityList.parallelStream()
                .filter(act -> act.getUserId().equals(String.valueOf(id)))
                .forEach(act -> 
                     printLog(act.getId(), LocalDate.parse(act.getDate(), formatter), act.getAction(), act.getType(), act.getFarmId() , act.getField(), act.getRow(), act.getUserId(), Integer.parseInt(act.getQuantity()), act.getUnit())
                );
        timer.setEndTime();
        totalTime += timer.calcDuration();
    }
    
    public static void option3() throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        Scanner sc = new Scanner(System.in);
        String id;
                     
        System.out.println("Enter the farm's name:");
        String fname = sc.nextLine();
        timer.setStartTime(); //start timer
        Optional<Farms> matchingObject = 
            farmList
                    .parallelStream()
                    .filter(farm -> farm.getName().equals(fname))
                    .findFirst();
            if(matchingObject.isPresent()){
                Farms fm = matchingObject.get();
                id = fm.getFarmID();
            }else{
                id = null;
            }
        
        final String idres1 = id;
        if(id==null){
            System.out.println("Farm not found");
        }else{
            //user choose element 
            String table = choice();
            System.out.println("Enter the name of the " + table);
            String itemID = null;

            String name = sc.nextLine();

            if(table.equals("plants")){
            Optional<Plants> match = 
                plantList
                        .parallelStream()
                        .filter(plant -> plant.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Plants fm = match.get();
                    itemID = fm.getPlantID();
                }else{
                    itemID = null;
                }        
            }else if(table.equals("fertilizers")){
            Optional<Fertilizers> match = 
                fertList
                        .parallelStream()
                        .filter(fert -> fert.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Fertilizers fm = match.get();
                    itemID = fm.getFertID();
                }else{
                    itemID = null;
                } 
            }else if(table.equals("pesticides")){
            Optional<Pesticides> match = 
                pestList
                        .parallelStream()
                        .filter(pest -> pest.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Pesticides fm = match.get();
                    itemID = fm.getPestID();
                }else{
                    itemID = null;
                } 
            }

            String itemName=null;
            final String idres2 = itemID;

            if(idres2==null){
                System.out.println(table +" not found");
            }else{
                if(table.equals("plants")){
                    Optional<Plants> m = 
                        plantList
                                .parallelStream()
                                .filter(plant -> plant.getPlantID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Plants fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }else if(table.equals("fertilizers")){
                    Optional<Fertilizers> m = 
                        fertList
                                .parallelStream()
                                .filter(fert -> fert.getFertID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Fertilizers fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }else if(table.equals("pesticides")){
                    Optional<Pesticides> m = 
                        pestList
                                .parallelStream()
                                .filter(pest -> pest.getPestID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Pesticides fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }

                final String itemres=itemName;
                if(itemres==null){
                    System.out.println(table +" not found");
                }
                activityList.parallelStream()
                        .filter(act -> act.getFarmId().equals(id) && act.getType().equals(itemres))
                        .forEach(act ->
                             printLog(act.getId(), LocalDate.parse(act.getDate(), formatter), act.getAction(), act.getType(), act.getFarmId() , act.getField(), act.getRow(), act.getUserId(), Integer.parseInt(act.getQuantity()), act.getUnit())
                        );
                }
        }
        timer.setEndTime();
        totalTime += timer.calcDuration();
    }
    
    public static void option4() throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        Scanner sc = new Scanner(System.in);
        
        String id;
                     
        System.out.println("Enter the farm's name:");
        String fname = sc.nextLine();
        timer.setStartTime(); //start timer
        Optional<Farms> matchingObject = 
            farmList
                    .parallelStream()
                    .filter(farm -> farm.getName().equals(fname))
                    .findFirst();
            if(matchingObject.isPresent()){
                Farms fm = matchingObject.get();
                id = fm.getFarmID();
            }else{
                id = null;
            }
        
        final String idres1 = id;
        if(id==null){
            System.out.println("Farm not found");
        }else{
            //user choose element 
            String table = choice();
            System.out.println("Enter the name of the " + table);
            String itemID = null;

            String name = sc.nextLine();

            if(table.equals("plants")){
            Optional<Plants> match = 
                plantList
                        .parallelStream()
                        .filter(plant -> plant.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Plants fm = match.get();
                    itemID = fm.getPlantID();
                }else{
                    itemID = null;
                }        
            }else if(table.equals("fertilizers")){
            Optional<Fertilizers> match = 
                fertList
                        .parallelStream()
                        .filter(fert -> fert.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Fertilizers fm = match.get();
                    itemID = fm.getFertID();
                }else{
                    itemID = null;
                } 
            }else if(table.equals("pesticides")){
            Optional<Pesticides> match = 
                pestList
                        .parallelStream()
                        .filter(pest -> pest.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Pesticides fm = match.get();
                    itemID = fm.getPestID();
                }else{
                    itemID = null;
                } 
            }

            String itemName=null;
            final String idres2 = itemID;

            if(idres2==null){
                System.out.println(table +" not found");
            }else{
                if(table.equals("plants")){
                    Optional<Plants> m = 
                        plantList
                                .parallelStream()
                                .filter(plant -> plant.getPlantID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Plants fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }else if(table.equals("fertilizers")){
                    Optional<Fertilizers> m = 
                        fertList
                                .parallelStream()
                                .filter(fert -> fert.getFertID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Fertilizers fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }else if(table.equals("pesticides")){
                    Optional<Pesticides> m = 
                        pestList
                                .parallelStream()
                                .filter(pest -> pest.getPestID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Pesticides fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }

                final String itemres=itemName;
                if(itemres==null){
                    System.out.println(table +" not found");
                }
                    System.out.println("Please enter the starting date (YYYY-MM-DD):");
                    String start = sc.next();
                    LocalDate start_dt = LocalDate.parse(start, formatter);
                    System.out.println("Please enter the ending date (YYYY-MM-DD):");
                    String end = sc.next();
                    LocalDate end_dt = LocalDate.parse(end, formatter);
                    
                    Boolean[] arr = {true};
                    activityList.parallelStream()
                            .filter(act -> act.getFarmId().equals(idres1) && act.getType().equals(itemres))
                            .forEach(act -> {
                                if((LocalDate.parse(act.getDate(), formatter).isAfter(start_dt) || LocalDate.parse(act.getDate(), formatter).isEqual(start_dt)) && (LocalDate.parse(act.getDate(), formatter).isBefore(end_dt) || LocalDate.parse(act.getDate(), formatter).isEqual(end_dt))){
                                    printLog(act.getId(), LocalDate.parse(act.getDate(), formatter), act.getAction(), act.getType(), act.getFarmId() , act.getField(), act.getRow(), act.getUserId(), Integer.parseInt(act.getQuantity()), act.getUnit());
                                }else{
                                    arr[0] = false; 
                                }      
                            });
                    if(!arr[0]) {
                        System.out.println("No record found");
                    }
                }
        }
        timer.setEndTime();
        totalTime += timer.calcDuration();
    }
    
    public static void option5() throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        Scanner sc = new Scanner(System.in);
        
        String id;
        System.out.println("Enter the farm's name:");
        String fname = sc.nextLine();
         timer.setStartTime();
        Optional<Farms> matchingObject = 
            farmList
                    .parallelStream()
                    .filter(farm -> farm.getName().equals(fname))
                    .findFirst();
            if(matchingObject.isPresent()){
                Farms fm = matchingObject.get();
                id = fm.getFarmID();
            }else{
                id = null;
            }
        
        final String idres1 = id;
        if(id==null){
            System.out.println("Farm not found");
        }else{
            //user choose element 
            String table = choice();
            System.out.println("Enter the name of the " + table);
            String itemID = null;

            String name = sc.nextLine();

            if(table.equals("plants")){
            Optional<Plants> match = 
                plantList
                        .parallelStream()
                        .filter(plant -> plant.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Plants fm = match.get();
                    itemID = fm.getPlantID();
                }else{
                    itemID = null;
                }        
            }else if(table.equals("fertilizers")){
            Optional<Fertilizers> match = 
                fertList
                        .parallelStream()
                        .filter(fert -> fert.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Fertilizers fm = match.get();
                    itemID = fm.getFertID();
                }else{
                    itemID = null;
                } 
            }else if(table.equals("pesticides")){
            Optional<Pesticides> match = 
                pestList
                        .parallelStream()
                        .filter(pest -> pest.getName().equals(name))
                        .findFirst();
                if(match.isPresent()){
                    Pesticides fm = match.get();
                    itemID = fm.getPestID();
                }else{
                    itemID = null;
                } 
            }

            String itemName=null;
            final String idres2 = itemID;

            if(idres2==null){
                System.out.println(table +" not found");
            }else{
                if(table.equals("plants")){
                    Optional<Plants> m = 
                        plantList
                                .parallelStream()
                                .filter(plant -> plant.getPlantID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Plants fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }else if(table.equals("fertilizers")){
                    Optional<Fertilizers> m = 
                        fertList
                                .parallelStream()
                                .filter(fert -> fert.getFertID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Fertilizers fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }else if(table.equals("pesticides")){
                    Optional<Pesticides> m = 
                        pestList
                                .parallelStream()
                                .filter(pest -> pest.getPestID().equals(idres2))
                                .findFirst();
                        if(m.isPresent()){
                            Pesticides fm = m.get();
                            itemName = fm.getName();
                        }else{
                            itemName=null;
                        }
                }

                final String itemres=itemName;
                if(itemres==null){
                    System.out.println(table +" not found");
                }
                    System.out.println("Please enter the starting date (YYYY-MM-DD):");
                    String start = sc.next();
                    LocalDate start_dt = LocalDate.parse(start, formatter);
                    System.out.println("Please enter the ending date (YYYY-MM-DD):");
                    String end = sc.next();
                    LocalDate end_dt = LocalDate.parse(end, formatter);
                    final String itemNameres = itemName;
                    int total=0;

                    List<Integer> filtered
                            = quantityList
                            .parallelStream()
                            .filter(qt -> qt.getItemType().equals(itemNameres) && (LocalDate.parse(qt.getDate(), formatter).isAfter(start_dt) || LocalDate.parse(qt.getDate(), formatter).isEqual(start_dt)) && (LocalDate.parse(qt.getDate(), formatter).isBefore(end_dt) || LocalDate.parse(qt.getDate(), formatter).isEqual(end_dt)))
                            .map(qt -> qt.getItemQuantity())
                            .collect(Collectors.toList());
                    total = sum(filtered);
                    final int finalQuantity = total;     
                    final String itemres1=itemName;
        
                    Boolean[] arr = {true};
                    activityList.parallelStream()
                            .filter(act -> act.getFarmId().equals(idres1) && act.getType().equals(itemres1))
                            .forEach(act -> {
                                if((LocalDate.parse(act.getDate(), formatter).isAfter(start_dt) || LocalDate.parse(act.getDate(), formatter).isEqual(start_dt) && (LocalDate.parse(act.getDate(), formatter).isBefore(end_dt) || LocalDate.parse(act.getDate(), formatter).isEqual(end_dt)))){
                                    printSummarizedLog(act.getId(), start_dt, end_dt, act.getAction(), act.getType(), act.getFarmId() , act.getField(), act.getRow(), act.getUserId(), finalQuantity, act.getUnit());
                                }else{
                                    arr[0] = false; 
                                }      
                            });
                    if(!arr[0]) {
                        System.out.println("No record found");
                    }
                }
        }
        timer.setEndTime();
        totalTime += timer.calcDuration();
    }
    
    public static void option0() throws SQLException{
        System.out.println("Thank you");
        System.out.println("Total time: "+ totalTime);
        System.exit(0);
    }
    
    public static String choice(){
        System.out.println("Select a type of material:");
        System.out.println("1: Plant");
        System.out.println("2: Pesticide");
        System.out.println("3: Fertilizer");
        
        Scanner sc = new Scanner(System.in);
        String choice = sc.next();
         
         switch (choice){            
             case "1":
                 return "plants";
             case "2":
                 return "pesticides";
             case "3":
                 return "fertilizers";
             default:
                 choice();           
         }
         return null;
    }
    
    public static int sum(List<Integer> list)
    {
        // iterator for accessing the elements
        Iterator<Integer> it = list.iterator();
 
        int res = 0;
        while (it.hasNext()) {
            int num = it.next();
            res += num;
        }
        return res;
    }
    
    public static void printLog(String actID, LocalDate date, String action, String type, String farmID, String farmField, String farmRow, String userID, int quantity, String unit){
        System.out.println(
                    "Activity ID: " + actID +"\n" +
                    "Date: " + date +"\n" +
                    "Type of activity: " + action + "\n" +
                    "Materials used: " + type + "\n" +
                    "Quantity used: " + quantity + " " + unit + "\n" +
                    "Location: Farm " + farmID + ", Field " + farmField + ", Row " + farmRow + "\n" +
                    "Person in charge: Farmer ID " + userID + "\n" +
                    "------------------------------------------------------------------"
        );
    }
  
    public static void printSummarizedLog(String actID, LocalDate startdate, LocalDate enddate, String action, String type, String farmID, String farmField, String farmRow, String userID, int finalQuantity, String unit){
        System.out.println(
                    "Activity ID: " + actID +"\n" +
                    "Date: " + startdate + " until " + enddate + "\n" +
                    "Type of activity: " + action + "\n" +
                    "Materials used: " + type + "\n" +
                    "Location: Farm " + farmID + ", Field " + farmField + ", Row " + farmRow + "\n" +
                    "Person in charge: Farmer ID " + userID + "\n" +
                    "Total quantity used: " + finalQuantity + " " + unit + "\n" +
                    "------------------------------------------------------------------"
                );
    } 
}
