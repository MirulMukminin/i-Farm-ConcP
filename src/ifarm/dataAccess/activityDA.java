/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifarm.dataAccess;

import ifarm.data.Activity;
import ifarm.dbConnection;

/**
 *
 * @author user
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class activityDA {
    
      public String truncateActivities() throws SQLException {
        Connection conn = dbConnection.createCon();
        PreparedStatement stmt = conn.prepareStatement("TRUNCATE activities");
        String message = "";
        if (stmt.execute()) {
            message = "Activities Table is Truncated";
        }
        return message;
    }
    
        public void addActivities(Activity act) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = dbConnection.createCon();
            stmt = conn.prepareStatement("INSERT INTO activities(`activities_id`, `date`, `action`, `type`, `unit`, `quantity`, `field`, `row`, `farmId`, `userId`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, act.getId());
            stmt.setString(2, act.getDate());
            stmt.setString(3, act.getAction());
            stmt.setString(4, act.getType());
            stmt.setString(5, act.getUnit());
            stmt.setString(6, act.getQuantity());
            stmt.setString(7, act.getField());
            stmt.setString(8, act.getRow());
            stmt.setString(9, act.getFarmId());
            stmt.setString(10, act.getUserId());
            stmt.executeUpdate();
        } finally {
            conn.close();
        }
        
    }
    
}
