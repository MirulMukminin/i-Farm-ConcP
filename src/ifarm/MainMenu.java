/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifarm;

import DataVis.Driver;
import java.sql.SQLException;
import java.util.Scanner;
import org.json.JSONException;

/**
 *
 * @author User
 */
public class MainMenu {
    
    public static void main(String[] args) throws SQLException, JSONException {
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("Hi. Welcome to i-Farm. Please choose an option.");
        System.out.println("1. Generate activity log text file.");
        System.out.println("2. View data visualization.");
        System.out.println("3. Disaster simulation.");
        System.out.println("0. Exit");
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                IFarm.GenerateActivity();
                break;
            case 2:
                Driver.main(args);
            case 3:
                //code kina here
                //call main trus ke?
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
