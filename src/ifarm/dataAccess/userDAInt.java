/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifarm.dataAccess;

import ifarm.data.Farmers;
import java.sql.SQLException;

/**
 *
 * @author Nur Amirah
 */
public interface userDAInt {
    public Farmers getFarmerByID(String userId) throws SQLException;
}
