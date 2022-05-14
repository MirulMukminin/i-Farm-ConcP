
package ifarm.data;

public class Plants {
    private String name;
    private String unitType;

    public Plants(String name, String unitType) {
        this.name = name;
        this.unitType = unitType;
    }

    public Plants() {
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
    
}
