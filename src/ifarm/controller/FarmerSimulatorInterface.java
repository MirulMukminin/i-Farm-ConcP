package ifarm.controller;

import ifarm.data.Farmers;
import java.sql.SQLException;

public interface FarmerSimulatorInterface {
    public Farmers[] generateFarmers(int numberOfFarmers) throws SQLException;
}
