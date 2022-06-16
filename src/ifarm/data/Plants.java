
package ifarm.data;

public class Plants {
    private String plantID;
    private String name;
    private String unitType;

    public Plants(String plantID,String name, String unitType) {
        this.plantID = plantID;
        this.name = name;
        this.unitType = unitType;
    }

    public Plants() {
    }
    
    public String getPlantID() {
        return plantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
    
    public void setPlantID(String plantID) {
        this.plantID = plantID;
    }
    
    
}
