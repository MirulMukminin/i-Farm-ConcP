
package ifarm.dataAccess;

import ifarm.data.Farmers;
import ifarm.data.Plants;
import ifarm.dbConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class plantDA implements plantDAInt{
    public final String FOLDER_PATH = "";
    public int generatePlantDataToTxt(String filename) throws SQLException, JSONException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        JSONArray jsonArr = new JSONArray();

        try {
            conn = dbConnection.createCon();
            stmt = conn.prepareStatement("SELECT * FROM plants ORDER BY ABS(plants_id)");
            rst = stmt.executeQuery();
            int count = 0;

            while (rst.next()) {
                // Save the row from database to JSONObject
                JSONObject plant = new JSONObject();
                plant.put("id", rst.getString("plants_id"));
                plant.put("name", rst.getString("name"));
                plant.put("unitType", rst.getString("unitType"));
                // Save the JSONObject to JSONArray
                jsonArr.put(plant);
                count++;
            }
            // Write the JSONArray to a file
            try {
                FileWriter file = new FileWriter(FOLDER_PATH + filename + ".txt");
                file.write(jsonArr.toString());
                file.close();

            }catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        } finally {
            conn.close();
        }
    }
    
    public Plants getPlantByID(String id) throws SQLException{
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rst = null;

            try{
                con = dbConnection.createCon();
                stmt = con.prepareStatement("SELECT * FROM plants WHERE plants_id = ?");
                stmt.setString(1, id);
                rst = stmt.executeQuery();

                Plants plant = new Plants();
                while(rst.next()){
                    plant.setPlantID(rst.getString("plants_id"));
                    plant.setName(rst.getString("name"));
                    plant.setUnitType(rst.getString("unitType"));
                }
                
                return plant;
            }finally{
             con.close();
            }  
    }
}
