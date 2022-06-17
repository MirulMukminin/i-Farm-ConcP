/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifarm;

import DataVis.Driver;
import DataVis.DriverStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;

/**
 *
 * @author User
 */
public class MainMenu {
    
    static int numOfFarmers;
    static int[] numOfActivities;
    public static void main(String[] args) throws SQLException, JSONException, IOException, ExecutionException, InterruptedException {
        
        Random rand = new Random();
            //int numOfFarmers = rand.nextInt(10) + 10;
            numOfFarmers = 10;
            System.out.printf("Number of Farmers: %d \n", numOfFarmers);
            // generate random number of activities for each farmers, x > 1000
            numOfActivities = new int[numOfFarmers];
            for (int i = 1; i <=numOfFarmers; i++) {
                numOfActivities[i-1] =  rand.nextInt(10) + 100;
                System.out.printf("Farmer %d : %d activities \n", i, numOfActivities[i-1]);
            }
            
        menu(args);
        
    }
    
    public static void menu(String[] args) throws SQLException, JSONException, IOException, ExecutionException, InterruptedException{
        Scanner sc = new Scanner(System.in);
        
        while(true){
        System.out.println("Hi. Welcome to i-Farm. Please choose an option.");
        System.out.println("1. Generate activity log text file concurrently.");
        System.out.println("2. Generate activity log text file sequentially.");
        System.out.println("3. View data visualization. ");
        System.out.println("4. View data visualization using stream.");
        System.out.println("5. Disaster simulation.");
        System.out.println("0. Exit");
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                IFarm.GenerateActivity(1,numOfFarmers,numOfActivities);
                break;
            case 2:
                IFarm.GenerateActivity(2,numOfFarmers,numOfActivities);
            case 3:
                Driver.main(args);
            case 4:
                DriverStream.main(args);
            case 5:
                IFarm.main(args);
                break;
            case 0:
                System.out.println("Thank you");
                System.exit(0);
            default:
                System.out.println("Invalid option, please try again");
        }
        }
    }
}
