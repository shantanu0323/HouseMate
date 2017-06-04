package shantanu.housemate.Data;

/**
 * Created by SHAAN on 22-03-17.
 */
public class CustomerData {

    // Data members
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String address;
    private String city;
    private String pinCode;
    private int noOfG;
    private String username;
    private String password;
    private String cnfPassword;

    public CustomerData() {
        this.username = "empty";
    }

    public CustomerData(String firstName, String lastName, String email, String address, String
            city, String phoneNo, String pinCode, int noOfG, String username, String
            password, String cnfPassword) {

        this.address = address;
        this.city = city;
        this.cnfPassword = cnfPassword;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.noOfG = noOfG;
        this.password = password;
        this.phoneNo = phoneNo;
        this.pinCode = pinCode;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNoOfG() {
        return noOfG;
    }

    public void setNoOfG(int noOfG) {
        this.noOfG = noOfG;
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
