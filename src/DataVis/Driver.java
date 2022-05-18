/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataVis;

import ifarm.data.Activity;
import ifarm.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Driver {
    
    

    Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws SQLException{
        
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
        Activity activity = new Activity();
        Connection con = dbConnection.createCon();
        System.out.println("Please enter a farm ID");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM activities WHERE farmid = '" + id + "'");
        ResultSet rst = stmt.executeQuery();
        System.out.println("------------------------------------------------------------------");
        try{
            while(rst.next()){
                activity.setId(rst.getString("activities_id"));
                activity.setDate(rst.getString("date"));
                activity.setAction(rst.getString("action"));
                activity.setType(rst.getString("type"));
                activity.setUnit(rst.getString("unit"));
                activity.setQuantity(rst.getString("quantity"));
                activity.setField(rst.getString("field"));
                activity.setRow(rst.getString("row"));
                activity.setFarmId(rst.getString("farmId"));
                activity.setUserId(rst.getString("userId"));
                
                System.out.println("Activity ID: " + activity.getId());
                System.out.println("Date: " + activity.getDate());
                System.out.println("Type of activity: " + activity.getAction());
                System.out.println("Materials used: " + activity.getType());
                System.out.println("Quantity used: " + activity.getQuantity() + activity.getUnit());
                System.out.println("Location: Farm " + activity.getFarmId() + ", Field " + activity.getField() + ", Row " + activity.getRow());
                System.out.println("Person in charge: Farmer ID " + activity.getUserId());
                System.out.println("------------------------------------------------------------------");
                
            }
            
            
        }finally{
            con.close();
        }
        
    }
    
    public static void option2() throws SQLException{
        Activity activity = new Activity();
        //System.out.println("2 selected");
        Connection con = dbConnection.createCon();
        System.out.println("Please enter a farmer ID");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM activities WHERE userid = '" + id + "'");
        ResultSet rst = stmt.executeQuery();
        
        try{
            while(rst.next()){
                activity.setId(rst.getString("activities_id"));
                activity.setDate(rst.getString("date"));
                activity.setAction(rst.getString("action"));
                activity.setType(rst.getString("type"));
                activity.setUnit(rst.getString("unit"));
                activity.setQuantity(rst.getString("quantity"));
                activity.setField(rst.getString("field"));
                activity.setRow(rst.getString("row"));
                activity.setFarmId(rst.getString("farmId"));
                activity.setUserId(rst.getString("userId"));
                
                System.out.println("Activity ID: " + activity.getId());
                System.out.println("Date: " + activity.getDate());
                System.out.println("Type of activity: " + activity.getAction());
                System.out.println("Materials used: " + activity.getType());
                System.out.println("Quantity used: " + activity.getQuantity() + activity.getUnit());
                System.out.println("Location: Farm " + activity.getFarmId() + ", Field " + activity.getField() + ", Row " + activity.getRow());
                System.out.println("Person in charge: Farmer ID " + activity.getUserId());
                System.out.println("------------------------------------------------------------------");
                
            }
        }finally{
            con.close();
        }
    }
    
    public static void option3() throws SQLException{
        Activity activity = new Activity();
        //System.out.println("3 selected");
        Connection con = dbConnection.createCon();
        
        System.out.println("Please enter a farm ID");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        
        //choose element apa dia nak
        String table = choice();
        System.out.println("Please enter the id of the " + table);
        int itemID = sc.nextInt();
        
        PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM " + table + " WHERE " + table +"_id =" +itemID);
        ResultSet rst1 = stmt1.executeQuery();
        rst1.next();
        String itemName = rst1.getString("name");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM activities WHERE farmid = '" + id + "' AND type = \"" + itemName +"\"");
        ResultSet rst = stmt.executeQuery();
        
        try{
                while(rst.next()){
                activity.setId(rst.getString("activities_id"));
                activity.setDate(rst.getString("date"));
                activity.setAction(rst.getString("action"));
                activity.setType(rst.getString("type"));
                activity.setUnit(rst.getString("unit"));
                activity.setQuantity(rst.getString("quantity"));
                activity.setField(rst.getString("field"));
                activity.setRow(rst.getString("row"));
                activity.setFarmId(rst.getString("farmId"));
                activity.setUserId(rst.getString("userId"));
                
                System.out.println("Activity ID: " + activity.getId());
                System.out.println("Date: " + activity.getDate());
                System.out.println("Type of activity: " + activity.getAction());
                System.out.println("Materials used: " + activity.getType());
                System.out.println("Quantity used: " + activity.getQuantity() + activity.getUnit());
                System.out.println("Location: Farm " + activity.getFarmId() + ", Field " + activity.getField() + ", Row " + activity.getRow());
                System.out.println("Person in charge: Farmer ID " + activity.getUserId());
                System.out.println("------------------------------------------------------------------");
                }
        }finally{
            con.close();
        }
    }
    
    public static void option4() throws SQLException{
        //System.out.println("4 selected");
        Activity activity = new Activity();
        Connection con = dbConnection.createCon();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = null, endDate = null;
        System.out.println("Please enter a farm ID");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        
        //choose element apa dia nak
        String table = choice();
        System.out.println("Please enter the id of the " + table);
        int itemID = sc.nextInt();
        
        System.out.println("Please enter the starting date (YYYY-MM-DD):");
        String start = sc.next();
        
        
        System.out.println("Please enter the ending date (YYYY-MM-DD):");
        String end = sc.next();
        
        
        PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM " + table + " WHERE " + table +"_id =" +itemID);
        ResultSet rst1 = stmt1.executeQuery();
        rst1.next();
        String itemName = rst1.getString("name");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM activities WHERE farmid = '" + id + "' AND type = \"" 
                + itemName +"\" AND date BETWEEN '" + start + "' AND '" + end +"'" );
        ResultSet rst = stmt.executeQuery();
        
        try{
            while(rst.next()){
                activity.setId(rst.getString("activities_id"));
                activity.setDate(rst.getString("date"));
                activity.setAction(rst.getString("action"));
                activity.setType(rst.getString("type"));
                activity.setUnit(rst.getString("unit"));
                activity.setQuantity(rst.getString("quantity"));
                activity.setField(rst.getString("field"));
                activity.setRow(rst.getString("row"));
                activity.setFarmId(rst.getString("farmId"));
                activity.setUserId(rst.getString("userId"));
                
                System.out.println("Activity ID: " + activity.getId());
                System.out.println("Date: " + activity.getDate());
                System.out.println("Type of activity: " + activity.getAction());
                System.out.println("Materials used: " + activity.getType());
                System.out.println("Quantity used: " + activity.getQuantity() + activity.getUnit());
                System.out.println("Location: Farm " + activity.getFarmId() + ", Field " + activity.getField() + ", Row " + activity.getRow());
                System.out.println("Person in charge: Farmer ID " + activity.getUserId());
                System.out.println("------------------------------------------------------------------");
            }
        }finally{
            con.close();
        }
    }
    
    public static void option5() throws SQLException{
        Activity activity = new Activity();
        Connection con = dbConnection.createCon();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = null, endDate = null;
        System.out.println("Please enter a farm ID");
        Scanner sc = new Scanner(System.in);
        int farmID = sc.nextInt();
        
        //choose element apa dia nak
        String table = choice();
        System.out.println("Please enter the id of the " + table);
        int itemID = sc.nextInt();
        
        System.out.println("Please enter the starting date (YYYY-MM-DD):");
        String start = sc.next();
        try{
            startDate = dateFormat.parse(start);
        }catch(ParseException e){
            System.out.println("Unparseable using " + dateFormat); 
        }
        
        System.out.println("Please enter the ending date (YYYY-MM-DD):");
        String end = sc.next();
        try{
            endDate = dateFormat.parse(end);
        }catch(ParseException e){
            System.out.println("Unparseable using " + dateFormat); 
        }
        
        PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM " + table + " WHERE " + table +"_id =" +itemID);
        ResultSet rst1 = stmt1.executeQuery();
        rst1.next();
        String itemName = rst1.getString("name");
        System.out.println(itemName);
        PreparedStatement stmt = con.prepareStatement("SELECT *, SUM(quantity) AS \"Total\" FROM activities WHERE farmId = " + farmID + " AND type = '" 
        + itemName + "' AND date BETWEEN '" + start + "' AND '" + end + "'");
        ResultSet rst = stmt.executeQuery();
        
        try{
            while(rst.next()){
                activity.setId(rst.getString("activities_id"));
                activity.setDate(rst.getString("date"));
                activity.setAction(rst.getString("action"));
                activity.setType(rst.getString("type"));
                activity.setUnit(rst.getString("unit"));
                activity.setQuantity(rst.getString("quantity"));
                activity.setField(rst.getString("field"));
                activity.setRow(rst.getString("row"));
                activity.setFarmId(rst.getString("farmId"));
                activity.setUserId(rst.getString("userId"));
                activity.setTotal(rst.getDouble("Total"));
                
                System.out.println("Activity ID: " + activity.getId());
                System.out.println("Date: " + start + " until " + end);
                System.out.println("Type of activity: " + activity.getAction());
                System.out.println("Materials used: " + activity.getType());
                //System.out.println("Quantity used: " + activity.getQuantity() + activity.getUnit());
                System.out.println("Location: Farm " + activity.getFarmId() + ", Field " + activity.getField() + ", Row " + activity.getRow());
                System.out.println("Person in charge: Farmer ID " + activity.getUserId());
                System.out.println("Total quantity used: " + activity.getTotal() + " " + activity.getUnit());
                System.out.println("------------------------------------------------------------------");
            }
        }finally{
            con.close();
        }
    }
    
    public static void option0(){
        System.out.println("0 selected");
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
}
