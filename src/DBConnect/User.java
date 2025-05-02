package DBConnect;

import java.sql.Date;

public class User {
    private String userId;
    private String userName;
    private Date userBirth;

    public User(String userId, String userName, Date userBirth) {
        this.userId = userId;
        this.userName = userName;
        this.userBirth = userBirth;
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
}
