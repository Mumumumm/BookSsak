package DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DBLibrary extends DBConnect{

    public void inputWishList(String userid, Book book) {
        String checkSql = "select count(*) as count from userwishlist where userid = ? and bookid = ?"; // 중복체크 쿼리
        String sql = "insert into userwishlist values (?, ?)"; // insert 쿼리
        try { //중복체크 시작
            PreparedStatement checkDuplicate = this.conn.prepareStatement(checkSql);
            checkDuplicate.setString(1, userid);
            checkDuplicate.setString(2, book.getBookid());
            ResultSet rs = checkDuplicate.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") > 0) {
                    System.out.println("이미 찜 리스트에 있는 책입니다.\n");
                    return;
                }    // 중복체크 끝
            }
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, book.getBookid());
            pstmt.executeUpdate(); // 찜 리스트 insert 끝
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("📚" + book.getTitle() + " 이(가) 찜 리스트에 추가되었습니다.");
        System.out.println();
    }



    // 찜 리스트에서 삭제
    public void deleteWishList(String userid, Book book) {
        String isInsql = "select count(*) as count from userwishlist WHERE userid = ? and bookid = ?";
        String sql = "DELETE FROM userwishlist WHERE userid = ? and bookid = ?";
        try {
            PreparedStatement isPstmt = this.conn.prepareStatement(isInsql);
            isPstmt.setString(1, userid);
            isPstmt.setString(2, book.getBookid());
            ResultSet rs = isPstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") == 0) {
                    System.out.println("책을 찾을 수 없습니다.");
                    return;
                }    // 중복체크 끝
            }
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, book.getBookid());
            pstmt.executeUpdate();
            System.out.println("📚" + book.getTitle() + " 이(가) 찜 리스트에서 삭제되었습니다!");
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 도서 검색
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
                                    rs.getInt("pages"),
                                    rs.getInt("price")
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


    // 도서 추천 리스트
    public HashMap<String, Book> moodBook(String p_keyword) {
        HashMap<String, Book> recommenderBook = new HashMap<>();
        String sql = "select * from books where keyword like ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + p_keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String bookid = rs.getString("bookid");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String introduce = rs.getString("introduce");
                String category = rs.getString("category");
                String keyword = rs.getString("keyword");
                int pages = rs.getInt("pages");
                int price = rs.getInt("price");

                recommenderBook.put(bookid, new Book(bookid, title, author, publisher, introduce, category, keyword, pages, price));
            }
            rs.close(); // 결과셋 담는 메모리 정리
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommenderBook;
    }


    // 인기 도서 리스트
    public HashMap<String, Book> getPopularBooks() {
        HashMap<String, Book> popularBooks = new HashMap<>();
        String sql = "select b.bookid, b.title, b.author, b.publisher, b.introduce, b.category, b.keyword, b.pages, b.price, count(*) as read_count from userlibrary u join books b on u.bookid = b.bookid group by b.bookid order by read_count desc limit 5";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String bookid = rs.getString("bookid");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String introduce = rs.getString("introduce");
                String category = rs.getString("category");
                String keyword = rs.getString("keyword");
                int pages = rs.getInt("pages");
                int price = rs.getInt("price");

                popularBooks.put(bookid, new Book(bookid, title, author, publisher, introduce, category, keyword, pages, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return popularBooks;
    }

}
