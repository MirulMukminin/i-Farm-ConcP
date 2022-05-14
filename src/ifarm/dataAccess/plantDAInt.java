/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifarm.dataAccess;

import java.sql.SQLException;
import org.json.JSONException;

/**
 *
 * @author Nur Amirah
 */
public interface plantDAInt {
    public int generatePlantDataToTxt(String filename) throws SQLException, JSONException;
}
