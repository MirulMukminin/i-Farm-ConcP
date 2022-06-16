
package ifarm.dataAccess;

import ifarm.data.Farmers;
import ifarm.data.Pesticides;
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

public class pestDA implements pestDAInt{
    
    public final String FOLDER_PATH = "";
    public int generatePesticidesDataToTxt(String filename) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        Farmers farmer = null;
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        try {
            conn = dbConnection.createCon();
            stmt = conn.prepareStatement("SELECT * FROM pesticides ORDER BY ABS(pesticides_id)");
            rst = stmt.executeQuery();;
            int count = 0;

            while (rst.next()) {
                // Save the row from database to JSONObject
                JSONObject plant = new JSONObject();
                plant.put("id", rst.getString("pesticides_id"));
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

        public Pesticides getPestByID(String id) throws SQLException{
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rst = null;

            try{
                con = dbConnection.createCon();
                stmt = con.prepareStatement("SELECT * FROM pesticides WHERE pesticides_id = ?");
                stmt.setString(1, id);
                rst = stmt.executeQuery();

                Pesticides pest = new Pesticides();
                while(rst.next()){
                    pest.setPestID(rst.getString("pesticides_id"));
                    pest.setName(rst.getString("name"));
                    pest.setUnitType(rst.getString("unitType"));
                }
                
                return pest;
            }finally{
             con.close();
            }  
    }
}
