
package ifarm.data;

public class Fertilizers {
    private String name;
    private String unitType;

    public Fertilizers(String name, String unitType) {
        this.name = name;
        this.unitType = unitType;
    }

    public Fertilizers() {
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
