package shantanu.housemate.Data;

/**
 * Created by SHAAN on 30-04-17.
 */
public class OrderData {
    private String customerUsername;
    private String retailerUsername;
    private String bill;
    private String dod; // Date of Delivery
    private String paymentType;


    public OrderData() {
        this.customerUsername = "empty";
        this.retailerUsername = "empty";
        this.bill = "0";
        this.dod = "empty";
        this.paymentType = "empty";
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRetailerUsername() {
        return retailerUsername;
    }

    public void setRetailerUsername(String retailerUsername) {
        this.retailerUsername = retailerUsername;
    }
}
