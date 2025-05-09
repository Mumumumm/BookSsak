package DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBBookChallenge extends DBConnect{
    public void currentReadBook(String userid) {
        String sql = "select count(*) as count from userlibrary where userid = ? and current = true";
        String sql2 = "select * from userlibrary ul join books b on ul.bookid = b.bookid where userid = ? and current = true";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") == 0) {
                    System.out.println("현재 읽고 있는 책이 없어요😥");
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
            System.out.println("[📖현재 읽고 있는 책]");
            System.out.print(rs2.getString("bookid") + " / " +
                    rs2.getString("title") + " / " +
                    rs2.getString("author") + " / " +
                    rs2.getString("publisher") + " / " +
                    rs2.getString("category") + "\n");
            System.out.println(rs2.getString("introduce"));
            System.out.print("읽은 페이지 : " + rs2.getInt("read_pages") + "쪽 / " +
                    rs2.getInt("pages") + "쪽" + "\n");
            System.out.println("총 읽은 시간 : " + rs2.getTime("reading_time"));
            System.out.print("독서 날짜 : ");
            if(rs2.getDate("start_date")!=null){
                System.out.print(rs2.getDate("start_date") + " ~ ");
                System.out.println(rs2.getDate("end_date"));
            }else {
                System.out.println("아직 읽지 않았어요!");
            }

            if(rs2.getInt("read_count")>0){
                System.out.println("👍🏻" + (rs2.getInt("read_count")+1)+"회차 독서 중이에요!");
            }

            System.out.println("----------------------------------------");

            // 퍼센트 바
            for (int i = 0; i < 10; i++) {
                if (i < percentBar) {
                    progressBar.append("🟩");
                } else {
                    progressBar.append("🔳");
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


    /// 독서 시작
    public int checkPages (String userid, int readPages){
        String sql = "select pages from userlibrary where userid = ? and current = true";
        int flag = 0;
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(readPages > rs.getInt("pages")){
                System.out.println("읽은 페이지 수는 책 페이지보다 적게 입력해주세요.");
                flag = 0;
            }
            if(readPages == rs.getInt("pages")){
                flag = 1;
            }else {
                flag = 2;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
        return flag;
    }

    public void updateReadRecord(String userid, String time, int page) {
        try {
            String sql = "UPDATE userlibrary SET reading_time = ADDTIME(reading_time, ?), read_pages = ?, end_date = curdate() WHERE userid = ? and current = true";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, time); // TIME 타입은 문자열 형식 "HH:MM:SS" 가능
            pstmt.setInt(2, page);
            pstmt.setString(3, userid);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 책 등록
    public boolean inputReadBook(String userid, String bookid) {
        String sql = "INSERT INTO userlibrary (userid, bookid, pages, current) SELECT u.userid, b.bookid, b.pages, true FROM books b join users u on u.userid = ? WHERE b.bookid = ?";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, bookid);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean checkWishList(String userid, String bookid){
        String sql = "select * from userlibrary where userid = ? and bookid = ?";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, bookid);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                rs.close();
                return true; // 결과 없을때
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false; // 결과 있을때
    }

    public boolean changeReadBook(String userId, String bookId) {
        String sql = "update userlibrary set current = false where userid = ? and current = true ";
        String sql2 = "update userlibrary set current = true where userid = ? and bookid = ?";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            pstmt.close();
            if(this.checkWishList(userId, bookId)){
                this.inputReadBook(userId, bookId);
            }else {
                PreparedStatement pstmt2 = this.conn.prepareStatement(sql2);
                pstmt2.setString(1, userId);
                pstmt2.setString(2, bookId);
                pstmt2.executeUpdate();
                pstmt2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean checkCurrent(String userid) {
        String sql = "select count(*) as count from userlibrary where userid = ? and current = true";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") == 0) {
                    System.out.println();
                    rs.close();
                    return false;
                }
            }
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}

