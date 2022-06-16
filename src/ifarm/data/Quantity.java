
package ifarm.data;

public class Quantity {
    private int itemQuantity;
    private String activityID;
    private String itemType;
    private String date;

    public Quantity() {
    }

    public Quantity(int itemQuantity, String activityID, String itemType, String date) {
        this.itemQuantity = itemQuantity;
        this.activityID = activityID;
        this.itemType = itemType;
        this.date = date;
    }

    public String getActivityID() {
        return activityID;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public String getItemType() {
        return itemType;
    }

    public String getDate() {
        return date;
    }
    
    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
