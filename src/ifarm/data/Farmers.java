
package ifarm.data;

import ifarm.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Farmers {
    private String farmID;
    private String name;
    private String email;
    private String phoneNum;
    private String password;
    private String farms;

    public Farmers(String farmID, String name, String email, String phoneNum, String password, String farms) {
        this.farmID = farmID;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
        this.farms = farms;
    }

    public Farmers() {
        this.farmID = farmID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.farms = farms;
    }

    public String getFarmID() {
        return farmID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public String getFarms() {
        return farms;
    }

    public void setFarmID(String farmID) {
        this.farmID = farmID;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFarms(String farms) {
        this.farms = farms;
    }
    
    
}
