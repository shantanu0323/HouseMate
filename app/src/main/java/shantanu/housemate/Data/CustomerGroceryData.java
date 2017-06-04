package shantanu.housemate.Data;

/**
 * Created by SHAAN on 20-04-17.
 */
public class CustomerGroceryData {
    private String itemName;
    private String itemPrice;
    private String selected;
    private String username;

    public CustomerGroceryData() {
        this.itemName = "empty";
        this.itemPrice = "empty";
        this.selected = "false";
        this.username = "empty";
    }

    public CustomerGroceryData(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.selected = "false";
        this.username = "empty";
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
