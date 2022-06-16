
package ifarm.dataAccess;
import ifarm.data.Farmers;
import ifarm.data.Farms;
import ifarm.data.Fertilizers;
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

public class fertDA implements fertDAInt{
    
    public final String FOLDER_PATH = "";
    public int generateFertilizerDataToTxt(String filename) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        Farmers farmer = null;
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        try {
            conn = dbConnection.createCon();
            stmt = conn.prepareStatement("SELECT * FROM fertilizers ORDER BY ABS(fertilizers_id)");
            rst = stmt.executeQuery();
            int count = 0;

            while (rst.next()) {
                // Save the row from database to JSONObject
                JSONObject fert = new JSONObject();
                fert.put("id", rst.getString("fertilizers_id"));
                fert.put("name", rst.getString("name"));
                fert.put("unitType", rst.getString("unitType"));
                // Save the JSONObject to JSONArray
                jsonArr.put(fert);
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
    
    public Fertilizers getFertByID(String id) throws SQLException{
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rst = null;

            try{
                con = dbConnection.createCon();
                stmt = con.prepareStatement("SELECT * FROM fertilizers WHERE fertilizers_id = '" + id + "'");
                rst = stmt.executeQuery();

                Fertilizers fert = new Fertilizers();
                while(rst.next()){
                    fert.setFertID(rst.getString("fertilizers_id"));
                    fert.setName(rst.getString("name"));
                    fert.setUnitType(rst.getString("unitType"));
                }
                
                return fert;
            }finally{
             con.close();
            }  
    }
}
