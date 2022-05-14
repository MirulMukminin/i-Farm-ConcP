
package ifarm.dataAccess;

import ifarm.data.Farms;
import ifarm.dbConnection;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class farmDA implements farmDAInt{
    public Farms getFarmByID(String id) throws SQLException{
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rst = null;

            try{
                con = dbConnection.createCon();
                stmt = con.prepareStatement("SELECT * FROM farms WHERE farms_id = '" + id + "'");
                rst = stmt.executeQuery();

                //get all farm data
                Farms farm = new Farms();
                while(rst.next()){
                    farm.setName(rst.getString("name"));
                    farm.setAddress(rst.getString("address"));
                    farm.setPlants(rst.getString("plants"));
                    farm.setFertilizers(rst.getString("fertilizers"));
                    farm.setPesticides(rst.getString("pesticides"));
                }
                
                return farm;
            }finally{
             con.close();
            }  
    }
    
    public final String FOLDER_PATH = "";
    public int generateFarmDataToTxt(String filename) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        JSONArray jsonArr = new JSONArray();

        try {
            conn = dbConnection.createCon();
            stmt = conn.prepareStatement("SELECT * FROM farms");
            rst = stmt.executeQuery();
            int count = 0;

            while (rst.next()) {
                // Save the row from database to JSONObject
                JSONObject farm = new JSONObject();
                farm.put("id", rst.getString("farms_id"));
                farm.put("name", rst.getString("name"));
                farm.put("address", rst.getString("address"));
                farm.put("plants", rst.getString("plants"));
                farm.put("fertilizers", rst.getString("fertilizers"));
                farm.put("pesticides", rst.getString("pesticides"));
                // Save the JSONObject to JSONArray
                jsonArr.put(farm);
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
}
