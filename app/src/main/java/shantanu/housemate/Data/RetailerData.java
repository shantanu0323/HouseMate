package shantanu.housemate.Data;

/**
 * Created by SHAAN on 24-03-17.
 */
public class RetailerData {
    // Data members
    private String shopName;
    private String email;
    private String phoneNo;
    private String address;
    private String city;
    private String pinCode;
    private int radiusOfService;
    private String username;
    private String password;
    private String cnfPassword;

    public RetailerData() {
        this.username = "empty";
    }

    public RetailerData(String shopName, String email, String address, String city, String pinCode,
                        String phoneNo, int radiusOfService, String username, String password,
                        String cnfPassword) {
        this.address = address;
        this.city = city;
        this.cnfPassword = cnfPassword;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.pinCode = pinCode;
        this.radiusOfService = radiusOfService;
        this.shopName = shopName;
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCnfPassword() {
        return cnfPassword;
    }

    public void setCnfPassword(String cnfPassword) {
        this.cnfPassword = cnfPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getRadiusOfService() {
        return radiusOfService;
    }

    public void setRadiusOfService(int radiusOfService) {
        this.radiusOfService = radiusOfService;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
