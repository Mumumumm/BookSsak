package DBConnect;

import java.sql.Date;

public class User {
    private String userId;
    private String userName;
    private Date userBirth;
    private int totalSeconds = 0;
    public Book currentBook = null;

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


    public void addReadingTime(int seconds) {
        this.totalSeconds += seconds; // ✅ 누적합 내부에서 처리
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }
}
