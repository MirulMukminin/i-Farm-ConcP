
package ifarm.data;

import ifarm.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pesticides {
    
    private String name;
    private String unitType;

    public Pesticides(String name, String unitType) {
        this.name = name;
        this.unitType = unitType;
    }

    public Pesticides() {
    }

    public String getPestID() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        String id = null;
        try{
            con = dbConnection.createCon();
            stmt = con.prepareStatement("SELECT * FROM farms WHERE name ="+getName());
            rst = stmt.executeQuery();
            id = rst.getString("pesticides_id");
//            //get all farm name
//            while(rst.next()){
//                return rst.getString("pesticides_id");
//            }
            
        }catch(SQLException e){
             e.printStackTrace();
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
    
    
}
