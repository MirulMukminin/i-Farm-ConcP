
package ifarm.controller;

import ifarm.dataAccess.userDA;
import ifarm.data.Farmers;
import ifarm.dataAccess.farmDA;
import ifarm.dataAccess.fertDA;
import ifarm.dataAccess.pestDA;
import ifarm.dataAccess.plantDA;
import ifarm.dbConnection;
import java.sql.SQLException;
import org.json.JSONException;

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
    
}
