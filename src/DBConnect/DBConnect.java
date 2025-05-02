package DBConnect;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DBConnect {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/booksak?serverTimeZone=UTC";
    private String user = "root";
    private String password = "1234";
    private Connection conn = null;
    private Statement stmt = null;
    private int totalPage;


    public DBConnect() {
    }

    public void initDBConnect() {
        try {
            Class.forName(this.driver);// 번역된 드라이버 이름을 가져오겠다.
            this.conn = DriverManager.getConnection(this.url, this.user, this.password); // 연결객체 생성
            this.stmt = this.conn.createStatement(); //명령객체 생성
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public HashMap<String, User> allFetchUserTbl(){
        HashMap<String, User> userMap = new HashMap<>();
        String sql = "select * from users";
        try{
            ResultSet rs = this.stmt.executeQuery(sql);
            while (rs.next()){
                String userId = rs.getNString("userid");
                String userName = rs.getNString("username");
                Date userBirth = rs.getDate("birth");
                userMap.put(userId, new User(userId, userName, userBirth));
            }
            rs.close(); // 결과셋 담는 메모리 정리
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userMap;
    }

    public User checkLogin(String inputId, String inputPw){
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
                    loginUser = new User(userId, userName, userBirth);
                    System.out.println("========================");
                    System.out.println("안녕하세요 " + loginUser.getUserName() + " 님👨‍👩‍👧‍👦");
                    System.out.println("========================");
                } else {
                    System.out.println("계정 정보가 잘못되었습니다.");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 오류가 발생했습니다.");
        }
        return loginUser;
    }




    public HashMap<String, Book> searchBook(String input){
        HashMap<String, Book> resultBooks = new HashMap<>();
        String sql = "select * from books where title like ? or author like ? or publisher like ?";
        String inputKeyword = "%"+input+"%";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)){
            pstmt.setString(1, inputKeyword);
            pstmt.setString(2, inputKeyword);
            pstmt.setString(3, inputKeyword);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                while(true){
                    resultBooks.put(
                            rs.getString("bookid"),
                            new Book(
                                    rs.getString("bookid"),
                                    rs.getString("title"),
                                    rs.getString("author"),
                                    rs.getString("publisher"),
                                    rs.getString("introduce"),
                                    rs.getString("category"),
                                    rs.getString("keyword"),
                                    rs.getInt("pages")
                            )
                    );
                    if(!rs.next()){
                        break;
                    }
                }
            }else {
                System.out.println("검색 결과가 없습니다.");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql오류");
        }
        return resultBooks;
    }


    public void releaseDB(){
        try {
            this.stmt.close();
            this.conn.close();
            // db와 연결 단절;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    // 이모지
    public String reandomEmoji (){
        int r = (int) (Math.random() * 5);
        String[] emojibook = new String[5];
        emojibook[0] = "📕";
        emojibook[1] = "📗";
        emojibook[2] = "📘";
        emojibook[3] = "📙";
        emojibook[4] = "📒";

        return emojibook[r];
    }

    // 누적시간 메서드
    public void saveUserTime(User user) {
        try {
            initDBConnect(); // 커넥션 열기
            String sql = "UPDATE users SET reading_time = ? WHERE userid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            int totalSeconds = user.getTotalSeconds();
            String formattedTime = String.format("%02d:%02d:%02d",
                    totalSeconds / 3600,
                    (totalSeconds % 3600) / 60,
                    totalSeconds % 60);

            pstmt.setString(1, formattedTime); // TIME 타입은 문자열 형식 "HH:MM:SS" 가능
            pstmt.setString(2, user.getUserId());

            pstmt.executeUpdate();
            pstmt.close();
            releaseDB(); // 커넥션 닫기
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 쪽수 기록하기
    public int reading_page(){
        Scanner input = new Scanner(System.in);
        System.out.println("오늘 읽은 쪽수 기록");
        int readePage = input.nextInt();
        return totalPage += readePage;
    }


}