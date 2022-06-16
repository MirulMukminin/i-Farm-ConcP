
package ifarm.controller;

import ifarm.dataAccess.userDA;
import ifarm.data.Farmers;
import ifarm.dataAccess.farmDA;
import ifarm.dataAccess.fertDA;
import ifarm.dataAccess.pestDA;
import ifarm.dataAccess.plantDA;
import ifarm.dbConnection;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FarmerSimulator implements FarmerSimulatorInterface{

    @Override
    public Farmers[] generateFarmers(int numberOfFarmers) throws SQLException {
        userDA user= new dbConnection().getUserDA();
        Farmers[] farmers = new Farmers[1];
        Farmers farmer = user.getFarmerByID("1");
        farmers[0] = farmer;
        return farmers;
    }
    
     public int generateFarmFile() throws SQLException {
        farmDA farm = new dbConnection().getFarmDA();
        int status = farm.generateFarmDataToTxt("farms");
        return status;
    }

    public int generateFarmerFile() throws SQLException {
        userDA user = new dbConnection().getUserDA();
        return user.generateUserDataToTxt("farmer");
    }

    public int generatePlantFile() throws SQLException, JSONException {        
        plantDA plant= new dbConnection().getPlantDA();
        return plant.generatePlantDataToTxt("plants");
    }
    
    public int generateFertilizersFile() throws SQLException, JSONException {      
        fertDA fert = new dbConnection().getFertilizersDA();
        return fert.generateFertilizerDataToTxt("fertilizers");
    }
    
    public int generatePesticidesFile() throws SQLException, JSONException {        
        pestDA pest = new dbConnection().getPesticidesDA();
        return pest.generatePesticidesDataToTxt("pesticides");
    }
    
    public JSONArray getTextToJsonArray(String filename) throws JSONException, FileNotFoundException {
        Utility util = new Utility();
        String text = util.readFile(filename);
        JSONArray textArr = new JSONArray(text);
        return textArr;        
    }    
    
    
    
}
