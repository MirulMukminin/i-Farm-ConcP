package ifarm;

import ifarm.controller.FarmerSimulator;
import ifarm.data.Farmers;
import java.sql.SQLException;
import org.json.JSONException;

public class IFarm {

    public static void main(String[] args) throws SQLException, JSONException {
        
        FarmerSimulator farmerSimulator = new FarmerSimulator();
        Farmers[] farmer = null;
        int user = 0;
        int status = 0;
        int pestStatus = 0;
        int fertStatus = 0;
        int plantStatus = 0;

        try {
            farmer = farmerSimulator.generateFarmers(1);
            user = farmerSimulator.generateFarmerFile();
            status = farmerSimulator.generateFarmFile();
            pestStatus = farmerSimulator.generatePesticidesFile();
            fertStatus = farmerSimulator.generateFertilizersFile();
            plantStatus = farmerSimulator.generatePlantFile();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        userDA user = new userDA();
//        Farmers a = user.getFarmerByID("10");
//        System.out.println(a.getName());
//        System.out.println(a.getEmail());
//        System.out.println(a.getFarms());
//        System.out.println(a.getPassword());
//        System.out.println(a.getPhoneNum());
        
//        farmDA farm = new farmDA(); 
//        Farms b = farm.getFarmByID("1");
//        System.out.println(b.getName());
//        System.out.println(b.getAddress());
        
        

        
    }
    

}
