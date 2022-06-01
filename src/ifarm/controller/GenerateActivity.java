package ifarm.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GenerateActivity {
private List<String> userFarms = new ArrayList<String>();
    
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

    public GenerateActivity(List<String> farms) {
        this.userFarms = farms;
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
            
            String farmPlants;
            String farmFertilizers;
            String farmPesticides;
            
            date = util.getRandomDate();
            String[] actions = {"sowing", "harvest", "pesticide", "fertilizer", "sales"};
            action = actions[rand.nextInt(actions.length)];
            
            // initialize random value for quantity, field and row
            quantity = String.valueOf(rand.nextInt(10) + 1);
            field = String.valueOf(rand.nextInt(16) + 1);
            row = String.valueOf(rand.nextInt(5) + 1);
            
            // get any random farm belong to the farmer
            if (!userFarms.isEmpty()) {
                farmID = userFarms.get(rand.nextInt(userFarms.size()));
                // get the selected farm details
                farmObj = farmArr.getJSONObject(Integer.valueOf(farmID)-1);
            }
            
            ArrayList<String> farmPlantArr;
            ArrayList<String> farmPestArr;
            ArrayList<String> farmFertArr;
            if (action.equalsIgnoreCase("sowing") || action.equalsIgnoreCase("harvest") || action.equalsIgnoreCase("sales")) {
                if (plantArr.length() != 0) {
                    // get the farm plants
                    farmPlants = farmObj.getString("plants");
                    // convert the string to array
                    farmPlantArr = util.stringToArray(farmPlants);
                    // get random plant that available in the farm
                    String plantID = farmPlantArr.get(rand.nextInt(farmPlantArr.size()));
                    plantObj = plantArr.getJSONObject(Integer.valueOf(plantID)-1);
                    type = plantObj.getString("name");
                    unit = plantObj.getString("unitType");
                }
            } else if (action.equalsIgnoreCase("pesticide")) {
                if (pestArr.length() != 0) {
                    // get the farm pesticide
                    farmPesticides = farmObj.getString("pesticides");
                    // convert the string to array
                    farmPestArr = util.stringToArray(farmPesticides);
                    // get random pesticide that available in the farm
                    String pestID = farmPestArr.get(rand.nextInt(farmPestArr.size()));
                    pestObj = fertArr.getJSONObject(Integer.valueOf(pestID)-1);
                    type = pestObj.getString("name");
                    unit = pestObj.getString("unitType");
                }
            } else if (action.equalsIgnoreCase("fertilizer")) {
                
                if (fertArr.length() != 0) {
                    // get the farm fertilizer
                    farmFertilizers = farmObj.getString("fertilizers");
                    // convert the string to array
                    farmFertArr = util.stringToArray(farmFertilizers);
                    // get random fertilizer that available in the farm
                    String fertID = farmFertArr.get(rand.nextInt(farmFertArr.size()));
                    fertObj = fertArr.getJSONObject(Integer.valueOf(fertID)-1);
                    type = fertObj.getString("name");
                    unit = fertObj.getString("unitType");
                }
            }
        } catch (JSONException | FileNotFoundException ex) {
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
//        return "2021-01-05";
    }

}
