package shantanu.housemate.Data;

/**
 * Created by SHAAN on 27-03-17.
 */
public class AdminData {
    private String username;
    private String password;
    private String name;

    public AdminData() {
        this.name = "empty";
        this.password = "empty";
        this.username = "empty";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
