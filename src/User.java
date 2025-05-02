public class User {

    private String userId;
    private String userPw;
    private String username;

    public User(String pUserId , String pUserPw) {
        this.userId = pUserId;
        this.userPw = pUserPw;
    }

    // ID 접근자
    public String getUserId() {
        return userId;
    }

    public void setUserId(String pUserId) {
        this.userId = pUserId;
    }

    // PW 접근자
    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String pUserPw) {
        this.userPw = pUserPw;
    }

    public boolean isLogin(){
        return this.userId != null && this.userPw != null;
    }
}
