package shantanu.housemate.Data;

/**
 * Created by SHAAN on 20-04-17.
 */
public class GenerateListData {
    private String itemName;
    private String itemPrice;
    private String quantity;
    private String username;

    public GenerateListData() {
        this.itemName = "empty";
        this.itemPrice = "empty";
        this.quantity = "";
        this.username = "empty";
    }

    public GenerateListData(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = "";
        this.username = "empty";
    }

    public GenerateListData(String itemName, String itemPrice, String quantity, String username) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
        this.username = username;
    }

    public GenerateListData(String itemName, String itemPrice, String username) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = "";
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
