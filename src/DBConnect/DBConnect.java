package DBConnect;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBConnect {
    protected String driver = "com.mysql.cj.jdbc.Driver";
    protected String url = "jdbc:mysql://127.0.0.1:3306/booksak?serverTimeZone=UTC";
    protected String user = "root";
    protected String password = "1234";
    protected Connection conn = null;
    protected Statement stmt = null;

    public DBConnect() {
    }

    public void initDBConnect() {
        try {
            Class.forName(this.driver);// 번역된 드라이버 이름을 가져오겠다.
            this.conn = DriverManager.getConnection(this.url, this.user, this.password); // 연결객체 생성
            this.stmt = this.conn.createStatement(); //명령객체 생성
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User checkLogin(String inputId, String inputPw) {
        User loginUser = null;
        String sql = "select * from users where userid = ? and userpw = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, inputId);
            pstmt.setString(2, inputPw);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("userid");
                    String userName = rs.getString("username");
                    Date userBirth = rs.getDate("birth");
                    String  addr = rs.getString("addr");
                    loginUser = new User(userId, userName, userBirth, addr);
                    System.out.println("🤗로그인 성공!\n");
                } else {
                    System.out.println("❌계정 정보가 잘못되었습니다.");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 오류가 발생했습니다.");
        }
        return loginUser;
    }

    public boolean checkUserId (String inputId){
        String sql = "select userid from users where userid = ?";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, inputId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public void signUp(String id, String pw, String name, String birth, String addr){
        String sql = "insert into users values (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, name);
            pstmt.setString(4, birth);
            pstmt.setString(5, addr);
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // 북첼린지 , 라이브러리 공용
    public HashMap<String, Book> selectWishList(String userid) {
        String sql = "select uw.userid, b.bookid, b.title, b.author, b.publisher, b.introduce, b.category, b.keyword, b.pages, b.price from userwishlist uw join books b where userid = ? and uw.bookid = b.bookid;";
        HashMap<String, Book> wishList = new HashMap<>();
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                wishList.put(rs.getString("bookid"),
                        new Book(
                                rs.getString("bookid"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("publisher"),
                                rs.getString("introduce"),
                                rs.getString("category"),
                                rs.getString("keyword"),
                                rs.getInt("pages"),
                                rs.getInt("price")
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wishList; // 비어있으면 리턴받은곳에서 isEmpty 뭐시기 써서 비어있는거 확인하고 비어있다고 해줘야함
    }

    // DB 끊는 함수
    public void releaseDB() {
        try {
            this.stmt.close();
            this.conn.close();
            // db와 연결 단절;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 이모지
    public String reandomEmoji() {
        int r = (int) (Math.random() * 5);
        String[] emojibook = new String[5];
        emojibook[0] = "📕";
        emojibook[1] = "📗";
        emojibook[2] = "📘";
        emojibook[3] = "📙";
        emojibook[4] = "📒";

        return emojibook[r];
    }
}