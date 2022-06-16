
package ifarm.dataAccess;

import ifarm.data.Farmers;
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

public class userDA implements userDAInt{
    
    public Farmers getFarmerByID(String id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        
        try{
            con = dbConnection.createCon();
            stmt = con.prepareStatement("SELECT * FROM users WHERE users_id = ? LIMIT 1");
            stmt.setString(1, id);
            rst = stmt.executeQuery();
            
            //get all farmer data
            Farmers f = new Farmers();
            while(rst.next()){   
                f.setFarmerID(rst.getString("users_id"));
                f.setName(rst.getString("name"));
                f.setEmail(rst.getString("email"));
                f.setPassword(rst.getString("password"));
                f.setPhoneNum(rst.getString("phoneNumber"));
                f.setFarms(rst.getString("farms"));
            }
            return f;

        }finally{
             con.close();
        }
    }
    
    public final String FOLDER_PATH = "";
    public int generateUserDataToTxt(String filename) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        JSONArray jsonArr = new JSONArray();

        try {
            conn = dbConnection.createCon();
            stmt = conn.prepareStatement("SELECT * FROM users ORDER BY ABS(users_id)");
            rst = stmt.executeQuery();;
            int count = 0;

            while (rst.next()) {
                // Save the row from database to JSONObject
                JSONObject user = new JSONObject();
                user.put("id", rst.getString("users_id"));
                user.put("name", rst.getString("name"));
                user.put("email", rst.getString("email"));
                user.put("password", rst.getString("password"));
                user.put("phoneNumber", rst.getString("phoneNumber"));
                user.put("farms", rst.getString("farms"));
                // Save the JSONObject to JSONArray
                jsonArr.put(user);
                count++;
            }
            // Write the JSONArray to a file
            try {
                FileWriter file = new FileWriter(filename + ".txt");
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
