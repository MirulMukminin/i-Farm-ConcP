/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifarm.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import org.json.JSONException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 *
 * @author user
 */
public class Utility {
    
        public String readFile(String filename) throws JSONException, FileNotFoundException {
        String rootPath = "";
        String data = "";

        try {
            File myObj = new File(rootPath + filename);
            Scanner text = new Scanner(myObj);
            while (text.hasNextLine()) {
                data = text.nextLine();
                //System.out.println(data);
            }

            text.close();
        } catch (FileNotFoundException e) {
            System.out.println(filename + " cannot be read");
            e.printStackTrace();
        }
        return data;
    }

    public void writeLog(String text) {
        try {
            // create the log file if it is not existed yet
            // String rootPath = "C:/Users/user/OneDrive - 365.um.edu.my/Degree Life/SEM 6/Concurrent Programming (WIF3003)/Assignment/iFarm v1/";
            File log = new File("ActivityLog.txt");

            if (!log.exists()) {
                log.createNewFile();
                System.out.println("File created: " + log.getName());
            }

            // get current timestamp
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // write the log into the log file
            FileWriter fw = new FileWriter(log.getName(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write( sdf.format(timestamp) + " >> " +  text + "\n");
            bw.close();

        } catch (IOException e) {
            System.out.println("An error occured. The log file cannot be created");
            e.printStackTrace();
        }
    }
    
    public String getRandomDate(String action, int y) {
        // generate random number of month and date
        Random rand = new Random();
        int month = 1;
        // 1-3
        if (action.equalsIgnoreCase("sowing")) {
            month = rand.nextInt(4) + 1;
            // 4-8
        } else if (action.equalsIgnoreCase("pesticide") || action.equalsIgnoreCase("fertilizer")) {
            month = rand.nextInt(7) + 4;
            // 9-10
        } else if (action.equalsIgnoreCase("harvest")) {
            month = rand.nextInt(2) + 9;
            // 10-12
        } else if (action.equalsIgnoreCase("sales")) {
            month = rand.nextInt(2) + 11;
        } 
         
        int day;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            day = rand.nextInt(31) + 1;
        } else if (month == 2) {
            day = rand.nextInt(28) + 1;
        } else {
            day = rand.nextInt(30) + 1;
        }
        // pad number with 0
        // combine the date with yyyy-mm-dd format
        int year = 2021 + y;
        String date = year + "-" + padLeftZeros(String.valueOf(month), 2) + "-" + padLeftZeros(String.valueOf(day), 2);
        return date;
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }    
    
    public ArrayList<String> stringToArray(String data) {
        String array[] = data.replace("[","").replace("]","").replace("\"","").replace(" ","").replace("\\", "").split(",");
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, array);
        return list;
    }
    
}
