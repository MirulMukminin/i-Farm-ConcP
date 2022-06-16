/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifarm;

import DataVis.Driver;
import DataVis.DriverStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;

/**
 *
 * @author User
 */
public class MainMenu {
    
    public static void main(String[] args) throws SQLException, JSONException, IOException, ExecutionException {
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi. Welcome to i-Farm. Please choose an option.");
        System.out.println("1. Generate activity log text file.");
        System.out.println("2. View data visualization. ");
        System.out.println("3. View data visualization using stream.");
        System.out.println("4. Disaster simulation.");
        System.out.println("0. Exit");
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                IFarm.GenerateActivity();
                break;
            case 2:
                Driver.main(args);
            case 3:
                DriverStream.main(args);
            case 4:
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
