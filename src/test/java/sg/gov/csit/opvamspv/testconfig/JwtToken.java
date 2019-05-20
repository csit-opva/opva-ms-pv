package sg.gov.csit.opvamspv.testconfig;

public class JwtToken {
    private String userName;
    private String password;

    public JwtToken() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}