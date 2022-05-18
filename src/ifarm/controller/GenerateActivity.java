package ifarm.controller;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GenerateActivity {
private String action = "";
private String type = "";
private String quantity = "";
private String unit = "";
private String field = "";
private String row = "";
private String farmID = ""; 
private String date; 

    public GenerateActivity(){
        generateRand();
    }       
    public void generateRand(){
    Random rand = new Random();
    Utility util = new Utility();
        try {
            // read files that return string
            String farms = util.readFile("farms.txt");
            String plants = util.readFile("plants.txt");
            String fertilizer = util.readFile("fertilizers.txt");
            String pesticide = util.readFile("pesticides.txt");

            // convert string to jsonarray
            JSONArray farmArr = new JSONArray(farms);
            JSONArray plantArr = new JSONArray(plants);
            JSONArray fertArr = new JSONArray(fertilizer);
            JSONArray pestArr = new JSONArray(pesticide);
            
            JSONObject farmObj = null;
            JSONObject plantObj = null;
            JSONObject fertObj = null;
            JSONObject pestObj = null;
            
            date = util.getRandomDate();
            String[] actions = {"sowing", "harvest", "pesticide", "fertilizer", "sales"};
            action = actions[rand.nextInt(actions.length)];
            
            quantity = String.valueOf(rand.nextInt(11));
            field = String.valueOf(rand.nextInt(10));
            row = String.valueOf(rand.nextInt(10));
            
            if (farmArr != null) {
                farmObj = farmArr.getJSONObject(rand.nextInt(farmArr.length()));
                farmID = farmObj.getString("id");
            }
            
            if (action.equalsIgnoreCase("sowing") || action.equalsIgnoreCase("harvest") || action.equalsIgnoreCase("sales")) {
                if (plantArr != null) {
                    plantObj = plantArr.getJSONObject(rand.nextInt(plantArr.length()));
                    type = plantObj.getString("name");
                    unit = plantObj.getString("unitType");
                }
            } else if (action.equalsIgnoreCase("pesticide")) {
                if (pestArr != null) {
                    pestObj = fertArr.getJSONObject(rand.nextInt(pestArr.length()));
                    type = pestObj.getString("name");
                    unit = pestObj.getString("unitType");
                }
            } else if (action.equalsIgnoreCase("fertilizer")) {
                
                if (fertArr != null) {
                    fertObj = fertArr.getJSONObject(rand.nextInt(fertArr.length()));
                    type = fertObj.getString("name");
                    unit = fertObj.getString("unitType");
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(GenerateActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getField() {
        return field;
    }

    public String getRow() {
        return row;
    }

    public String getFarmID() {
        return farmID;
    }
    
    public String getDate() {
        return date;
    }

}
