package DBConnect;


import java.sql.*;
import java.util.HashMap;

public class DBConnect {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/booksak?serverTimeZone=UTC";
    private String user = "root";
    private String password = "1234";
    private Connection conn = null;
    private Statement stmt = null;

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


    public HashMap<String, User> allFetchUserTbl() {
        HashMap<String, User> userMap = new HashMap<>();
        String sql = "select * from users";
        try {
            ResultSet rs = this.stmt.executeQuery(sql);
            while (rs.next()) {
                String userId = rs.getNString("userid");
                String userName = rs.getNString("username");
                Date userBirth = rs.getDate("birth");
                userMap.put(userId, new User(userId, userName, userBirth));
            }
            rs.close(); // 결과셋 담는 메모리 정리
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userMap;
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
                    loginUser = new User(userId, userName, userBirth);
                    System.out.println("로그인 성공");
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







    /// //////////////////////////////라이브러리 시작/////////////////////////////////////////////////

    public void inputWishList(String userid, Book book) {
        String checkSql = "select count(*) as count from wishlist where userid = ? and bookid = ?"; // 중복체크 쿼리
        String sql = "insert into wishlist values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // insert 쿼리
        try { //중복체크 시작
            PreparedStatement checkDuplicate = this.conn.prepareStatement(checkSql);
            checkDuplicate.setString(1, userid);
            checkDuplicate.setString(2, book.getBookid());
            ResultSet rs = checkDuplicate.executeQuery();
            rs.next();
            if (rs.getInt("count") > 0) {
                System.out.println("이미 찜목록에 있는 책입니다.");
                return;
            }    // 중복체크 끝
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, book.getBookid());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublisher());
            pstmt.setString(6, book.getIntroduce());
            pstmt.setString(7, book.getCategory());
            pstmt.setString(8, book.getKeyword());
            pstmt.setInt(9, book.getPages());
            pstmt.executeUpdate(); // 찜목록 insert 끝
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println(book.getTitle() + "\t" + book.getAuthor() + "이 찜목록에 추가되었습니다.");
    }

    public HashMap<String, Book> selectWishList(String userid) {
        String sql = "select * from wishList where userid = ?";
        HashMap<String, Book> wishList = new HashMap<>();
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                while (true) {
                    wishList.put(rs.getString("bookid"), new Book(
                            rs.getString("bookid"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getString("introduce"),
                            rs.getString("category"),
                            rs.getString("keyword"),
                            rs.getInt("pages")
                    ));
                    if (rs.next()) {
                        break;
                    }
                }
                return wishList; // 위시리스트 맵으로 리턴
            } else {
                System.out.println("찜목록이 비어있습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wishList; // 비어있으면 리턴받은곳에서 isEmpty 뭐시기 써서 비어있는거 확인하고 비어있다고 해줘야함
    }


    public void deleteWishList(String userid, Book book) {
        String isInsql = "select count(*) as count from wishlist WHERE userid = ? and bookid = ?";
        String sql = "DELETE FROM wishlist WHERE userid = ? and bookid = ?";
        try {
            PreparedStatement isPstmt = this.conn.prepareStatement(isInsql);
            isPstmt.setString(1, userid);
            isPstmt.setString(2, book.getBookid());
            ResultSet rs = isPstmt.executeQuery();
            rs.next();
            if(rs.getInt("count") == 0){
                System.out.println("찜목록에 없는 책입니다.");
                return;
            }
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, book.getBookid());
            pstmt.executeUpdate();
            System.out.println(book.getTitle()+"이 찜목록에서 삭제되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, Book> searchBook(String input) {
        HashMap<String, Book> resultBooks = new HashMap<>();
        String sql = "select * from books where title like ? or author like ? or publisher like ?";
        String inputKeyword = "%" + input + "%";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, inputKeyword);
            pstmt.setString(2, inputKeyword);
            pstmt.setString(3, inputKeyword);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                while (true) {
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
                    if (!rs.next()) {
                        break;
                    }
                }
            } else {
                System.out.println("검색 결과가 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("sql오류");
        }
        return resultBooks;
    }

    public HashMap<String, Book> moodBook(String p_keyword){
        HashMap<String, Book> recommenderBook = new HashMap<>();
        String sql = "select * from books where keyword like ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + p_keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String bookid = rs.getString("bookid");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String introduce = rs.getString("introduce");
                String category = rs.getString("category");
                String keyword = rs.getString("keyword");
                int pages = rs.getInt("pages");

                recommenderBook.put(bookid, new Book(bookid, title, author, publisher, introduce, category, keyword, pages));
            }
            rs.close(); // 결과셋 담는 메모리 정리
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return recommenderBook;
    }
    /// //////////////////////////////라이브러리 끝/////////////////////////////////////////////////

    public void releaseDB() {
        try {
            this.stmt.close();
            this.conn.close();
            // db와 연결 단절;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
