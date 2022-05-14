
package ifarm;

import ifarm.dataAccess.farmDA;
import ifarm.dataAccess.fertDA;
import ifarm.dataAccess.pestDA;
import ifarm.dataAccess.plantDA;
import ifarm.dataAccess.userDA;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    //public static final String database = "i-farm";
    public static Connection createCon(){
        Connection con = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-farm","Group2","Group_2_iFarm");
            //System.out.println("DB Connected!");
            return con;
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public userDA getUserDA() {
        return new userDA();
    }
    
    public farmDA getFarmDA() {
        return new farmDA();
    }

    public plantDA getPlantDA() {
        return new plantDA();
    }
    
    public fertDA getFertilizersDA() {
        return new fertDA();
    }
    
    public pestDA getPesticidesDA() {
        return new pestDA();
    }
}
