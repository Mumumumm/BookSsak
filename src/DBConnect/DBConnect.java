package DBConnect;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBConnect {
    // ============================= DB 연결 strat =============================
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
    // ============================= DB 연결 end =============================


    // ============================= 로그인 strat =============================
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
    // ============================= 로그인 end =============================


    // ============================= 독서 챌린지 strat =============================
    public void currentReadBook(String userid) {
        String sql = "select count(*) as count from userlibrary where userid = ? and current = true";
        String sql2 = "select * from userlibrary where userid = ? and current = true";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") == 0) {
                    System.out.println("현재 읽고 있는 책이 없어요.");
                    System.out.println();
                    return;
                }
            }
            PreparedStatement pstmt2 = this.conn.prepareStatement(sql2);
            pstmt2.setString(1, userid);
            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();
            // 진행률 만들기
            int readPages = rs2.getInt("read_pages");
            int totalPages = rs2.getInt("pages");
            double progress = (double) readPages / totalPages;
            String progressPercent = String.format("%.2f", progress * 100);
            int percentBar = (int) (progress * 10);
            StringBuilder progressBar = new StringBuilder();

            // 내용들
            System.out.print(rs2.getString("bookid") + " / " +
                    rs2.getString("title") + " / " +
                    rs2.getString("author") + " / " +
                    rs2.getString("publisher") + " / " +
                    rs2.getString("category") + "\n");
            System.out.println(rs2.getString("introduce"));
            System.out.print("읽은 페이지 : " + rs2.getInt("read_pages") + "쪽 / " +
                    rs2.getInt("pages") + "쪽" + "\n");
            System.out.println("총 읽은 시간 : " + rs2.getTime("reading_time"));
            System.out.print("독서 날짜 : " + rs2.getDate("start_date") + " ~ " +
                    rs2.getDate("end_date") + "\n");

            // 퍼센트 바
            for (int i = 0; i < 10; i++) {
                if (i < percentBar) {
                    progressBar.append("🟩");
                } else {
                    progressBar.append("⬜");
                }
            }
            System.out.println(progressBar + " " + progressPercent + "%");

            // 진행률에 따른 프린트
            if (progress > 0.8) {
                System.out.println("🔥 얼마 안남았습니다! ");
            } else if (progress > 0.5) {
                System.out.println("📖 이제 절반을 넘겼어요! ");
            } else if (progress > 0.3) {
                System.out.println("💪 열심히 읽고 있습니다! ");
            } else {
                System.out.println("🌱 이제 시작입니다! ");
            }

            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 독서 시작
    public void updateReadRecord(String userid, String time, int pages ) {
        try {
            String sql = "UPDATE userlibrary SET reading_time = ADDTIME(reading_time, ?), start_date = NOW(), read_pages = read_pages + ?, end_date = curdate() WHERE userid = ? and current = true";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, time); // TIME 타입 문자열 "HH:MM:SS"
            pstmt.setInt(2, pages);
            pstmt.setString(3, userid);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 독서 종료
    public void updateEndDate(String userid) {
        try {
            String sql = "UPDATE userlibrary SET end_date = NOW() WHERE userid = ? AND current = true";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 책 등록
    public boolean inputReadBook(String userid, String bookid) {
        String sql = "update userlibrary set current = true, start_date = curdate() where userid = ? and bookid = ?";
        String sql2 = "select count(*) as count from userlibrary where current = true and userid = ?";
        try {
            PreparedStatement pstmt2 = this.conn.prepareStatement(sql2);
            pstmt2.setString(1, userid);
            pstmt2.executeQuery();
            ResultSet rs = pstmt2.getResultSet();
            if (rs.next()) {
                if (rs.getInt("count") > 0) {
                    System.out.println();
                    return false;
                }
            }
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, bookid);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean changeReadBook(String userId, String bookId) {
        String sql = "update userlibrary set current = false where userid = ? and current = true";
        String sql2 = "update userlibrary set current = true where userid = ? and bookid = ?";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            PreparedStatement pstmt2 = this.conn.prepareStatement(sql2);
            pstmt2.setString(1, userId);
            pstmt2.setString(2, bookId);
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 총 페이지 수 확인용 (완독 확인)
    public int getTotalPages(String userid) {
        String sql = "SELECT pages FROM userlibrary WHERE userid = ? AND current = true";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pages");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 읽은 페이지 수 반영 및 완독 처리
    public void updateReadPage(String userid, int nowPage) {
        try {
            // 현재 read_pages 가져오기
            String sqlGet = "SELECT read_pages, pages FROM userlibrary WHERE userid = ? AND current = true";
            PreparedStatement pstmtGet = conn.prepareStatement(sqlGet);
            pstmtGet.setString(1, userid);
            ResultSet rs = pstmtGet.executeQuery();

            if (rs.next()) {
                int currentRead = rs.getInt("read_pages");
                int totalPages = rs.getInt("pages");

                // nowPage가 더 작으면 반영하지 않음
                if (nowPage <= currentRead) {
                    System.out.println("이미 읽은 페이지보다 작아서 반영하지 않습니다.");
                    return;
                }

                // 페이지 반영
                String sqlUpdate = "UPDATE userlibrary SET read_pages = ? WHERE userid = ? AND current = true";
                PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                pstmtUpdate.setInt(1, nowPage);
                pstmtUpdate.setString(2, userid);
                pstmtUpdate.executeUpdate();

                // 완독 시 end_date 설정
                if (nowPage >= totalPages) {
                    updateEndDate(userid);
                    System.out.println("🎉완독 했습니다!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================= 독서 챌린지 end =============================


    // ============================= 독서 발자취 strat =============================

    // 내 서재 리스트
    public List<String[]> printMyLibrary(String userid) {
        String sql = "select * from userlibrary where userid = ? and start_date is not null";
        List<String[]> myLibraryList = new ArrayList<>();

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] list = {
                        rs.getString("bookid"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getString("category"),
                        String.valueOf(rs.getDate("start_date")),
                        String.valueOf(rs.getDate("end_date")),
                        String.valueOf(rs.getInt("pages")),
                        String.valueOf(rs.getInt("read_pages")),
                        String.valueOf(rs.getString("reading_time")),
                        rs.getString("introduce")
                };
                myLibraryList.add(list);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myLibraryList;
    }

    // 내 독서 통계
    public void myTotalRecoed(String userid) {
        String sql = "select " +
                "count(*) as total_books, " +
                "sum(read_pages) as total_pages, " +
                "sec_to_time(sum(reading_time)) as total_time, " +
                "sum(case when read_pages >= pages then 1 else 0 end) as finish_books, " +
                "sum(case when read_pages < pages then 1 else 0 end) as reading_books " +
                "from userlibrary " +
                "where userid = ? and start_date is not null";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ; // PreparedStatement 미리 준비
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalBooks = rs.getInt("total_books");
                int totalPages = rs.getInt("total_pages");
                int readingBooks = rs.getInt("reading_books");
                int finishBooks = rs.getInt("finish_books");
                String totalTime = rs.getString("total_time");

                System.out.println("[내 독서 통계]");
                System.out.println("📘 독서중 책 수: " + readingBooks);
                System.out.println("📗 완독한 책 수 : " + finishBooks);
                System.out.println("--------------------------------------");
                System.out.println("📚 총 읽은 책 수: " + totalBooks);
                System.out.println("📄 총 읽은 페이지 수: " + totalPages);

                // 시간, 분, 초 계산
                int totalTimeInSeconds = 0;
                int hours = totalTimeInSeconds / 3600;
                int minutes = (totalTimeInSeconds % 3600) / 60;
                int seconds = totalTimeInSeconds % 60;

                // "HH:MM:SS" 형식으로 출력
                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                System.out.println("⏰ 총 독서 시간: " + totalTime);
            } else {
                System.out.println("독서 기록이 없습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("오류");
        }
        System.out.println();
    }

    // 다독왕 랭킹 리스트
    public List<String[]> printRank() {
        String sql = "select userid, count(*) as finish_books " +
                "from userlibrary " +
                "where start_date is not null and read_pages >= pages " +
                "group by userid " +
                "order by finish_books desc " +
                "limit 5";
        List<String[]> rankList = new ArrayList<>();

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] list = {
                        rs.getString("userid"),
                        String.valueOf(rs.getInt("finish_books"))
                };
                rankList.add(list);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rankList;
    }

    // ============================= 독서 발자취 end =============================


    // ============================= 라이브러리 strat =============================

    // 찜 목록에 추가
    public void inputWishList(String userid, Book book) {
        String checkSql = "select count(*) as count from userlibrary where userid = ? and bookid = ?"; // 중복체크 쿼리
        String sql = "insert into userlibrary values (?, ?, ?, ?, ?, ?, ?, ?, default, default, ?, null, null, default, default)"; // insert 쿼리
        try { //중복체크 시작
            PreparedStatement checkDuplicate = this.conn.prepareStatement(checkSql);
            checkDuplicate.setString(1, userid);
            checkDuplicate.setString(2, book.getBookid());
            ResultSet rs = checkDuplicate.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") > 0) {
                    System.out.println("이미 찜목록에 있는 책입니다.");
                    return;
                }    // 중복체크 끝
            }
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
            System.out.println();
            System.out.println("📚" + book.getTitle() + " 이(가) 찜목록에 추가되었습니다.");
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    // 찜 목록 보기
    public HashMap<String, Book> selectWishList(String userid) {
        String sql = "select * from userlibrary where userid = ?";
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
                                rs.getInt("pages")
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wishList; // 비어있으면 리턴받은곳에서 isEmpty 뭐시기 써서 비어있는거 확인하고 비어있다고 해줘야함
    }

    // 찜 목록에서 삭제
    public void deleteWishList(String userid, Book book) {
        String isInsql = "select count(*) as count from userlibrary WHERE userid = ? and bookid = ?";
        String sql = "DELETE FROM userlibrary WHERE userid = ? and bookid = ?";
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
            System.out.println(book.getTitle() + "이(가) 서재에서 삭제되었습니다.");
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

                recommenderBook.put(bookid, new Book(bookid, title, author, publisher, introduce, category, keyword, pages));
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
        String sql = "select b.bookid, b.title, b.author, b.publisher, b.introduce, b.category, b.keyword, b.pages, count(*) as read_count " +
                "from userlibrary u " +
                "join books b on u.bookid = b.bookid " +
                "where u.start_date is not null " +
                "group by b.bookid " +
                "order by read_count desc " +
                "limit 5";

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

                popularBooks.put(bookid, new Book(bookid, title, author, publisher, introduce, category, keyword, pages));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return popularBooks;
    }

    // ============================= 라이브러리 end =============================


    // ============================= etc =============================

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