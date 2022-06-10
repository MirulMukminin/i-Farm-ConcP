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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.json.JSONException;

/**
 *
 * @author User
 */
public class DriverStream {
    
    static List<Activity> activityList;
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws SQLException, JSONException{
        
        activityList = new ArrayList();
        Connection con = dbConnection.createCon();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM activities");
        ResultSet rst = stmt.executeQuery();
        
        try{
            while(rst.next()){
                Activity activity = new Activity();
                
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
                
                activityList.add(activity);
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
        
        System.out.println("Please enter a farm ID");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        
        
        activityList.stream()
                .filter(act -> act.getFarmId().equals(String.valueOf(id)))
                .forEach(act ->
                System.out.println(
                        "Activity ID: " + act.getId() +"\n" +
                        "Date: " + act.getDate() +"\n" +
                        "Type of activity: " + act.getAction() + "\n" +
                        "Materials used: " + act.getType() + "\n" +
                        "Quantity used: " + act.getQuantity() + " " + act.getUnit() + "\n" +
                        "Location: Farm " + act.getFarmId() + ", Field " + act.getField() + ", Row " + act.getRow() + "\n" +
                        "Person in charge: Farmer ID " + act.getUserId() + "\n" +
                        "------------------------------------------------------------------"
                )
                );
    }
    
    public static void option2() throws SQLException{
        System.out.println("Please enter a farmer ID");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        
        
        activityList.stream()
                .filter(act -> act.getUserId().equals(String.valueOf(id)))
                .forEach(act ->
                System.out.println(
                        "Activity ID: " + act.getId() +"\n" +
                        "Date: " + act.getDate() +"\n" +
                        "Type of activity: " + act.getAction() + "\n" +
                        "Materials used: " + act.getType() + "\n" +
                        "Quantity used: " + act.getQuantity() + " " + act.getUnit() + "\n" +
                        "Location: Farm " + act.getFarmId() + ", Field " + act.getField() + ", Row " + act.getRow() + "\n" +
                        "Person in charge: Farmer ID " + act.getUserId() + "\n" +
                        "------------------------------------------------------------------"
                )
                );
    }
    
    public static void option3() throws SQLException{
        
    }
    
    public static void option4() throws SQLException{
        
    }
    
    public static void option5() throws SQLException{
        
    }
    
    public static void option0() throws SQLException{
        
    }
    
}
