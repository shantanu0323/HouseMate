package shantanu.housemate.Data;

/**
 * Created by SHAAN on 20-04-17.
 */
public class GroceryData {
    private String itemName;
    private String itemPrice;

    public GroceryData() {
        this.itemName = "empty";
        this.itemPrice = "empty";
    }

    public GroceryData(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
