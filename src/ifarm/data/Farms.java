
package ifarm.data;

public class Farms {
    private String name;
    private String address;
    private String plants;
    private String fertilizers;
    private String pesticides;
    private String activities;
    private String field;
    private String row;
    
    public Farms(String name, String address, String plants, String fertilizers, String pesticides, String activities, String field, String Row) {
        this.name = name;
        this.address = address;
        this.plants = plants;
        this.fertilizers = fertilizers;
        this.pesticides = pesticides;
        this.activities = activities;
        this.field = field;
        this.row = row;
    }

    public Farms() {
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPlants() {
        return plants;
    }

    public String getFertilizers() {
        return fertilizers;
    }

    public String getPesticides() {
        return pesticides;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPlants(String plants) {
        this.plants = plants;
    }

    public void setFertilizers(String fertilizers) {
        this.fertilizers = fertilizers;
    }

    public void setPesticides(String pesticides) {
        this.pesticides = pesticides;
    }
    
    
}
