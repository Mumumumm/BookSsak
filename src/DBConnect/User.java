package DBConnect;

import java.sql.Date;
import java.util.HashMap;

public class User {
    private String userId;
    private String userName;
    private Date userBirth;
    private String addr;

    public User(String userId, String userName, Date userBirth, String addr) {
        this.userId = userId;
        this.userName = userName;
        this.userBirth = userBirth;
        this.addr = addr;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Date getUserBirth() {
        return userBirth;
    }

    public String getAddr(){return addr;}
}
