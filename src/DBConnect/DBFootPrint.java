package DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBFootPrint extends DBConnect{
    // ============================= 독서 발자취 시작 =============================

    // 내 서재 리스트
    public void printMyLibrary(String userid) {
        String sql = "select * from userlibrary ul join books b on b.bookid = ul.bookid where userid = ? and start_date is not null";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            Boolean flag = false;
            System.out.println("[🧐내 서재]\n");
            while (rs.next()) {
                if(!flag){
                    flag = true;
                }
                System.out.print("\uD83D\uDCD7"+rs.getString("bookid")+" / ");
                System.out.print(rs.getString("title") + " / ");
                System.out.print(rs.getString("author")+ "/" );;
                System.out.print(rs.getString("publisher") + " / ");;
                System.out.println(rs.getString("category"));;
                System.out.println(rs.getString("introduce"));
                System.out.println("현재 읽은 페이지 : " + rs.getString("read_pages") + "쪽 / " + rs.getInt("pages") + "쪽");
                System.out.println("총 읽은 시간 : "+(rs.getString("reading_time")));
                if(rs.getDate("start_date")!=null){
                    System.out.print("독서 날짜 : " + rs.getDate("start_date") + " ~ ");
                    if(rs.getDate("end_date")!=null){
                        System.out.println(rs.getDate("end_date"));
                    }else {
                        System.out.println("읽는 중..");
                    }
                }else {
                    System.out.println("아직 읽지 않은 책입니다.");
                }
                if(rs.getInt("read_count")>0){
                    System.out.println("👍🏻" + rs.getInt("read_count")+"회차 독서 완료!");
                }
                System.out.println("==================================================");
            }
            if (!flag){
                System.out.println("내 서재가 비어있습니다.");
                return;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    // 내 독서 통계
    public void myTotalRecoed(String userid) {
        String sql = "SELECT COUNT(bookid) AS total_books, SUM(read_pages + (pages * read_count)) AS total_pages, SEC_TO_TIME(SUM(TIME_TO_SEC(reading_time))) AS total_time, SUM(CASE WHEN read_count > 0 THEN 1 ELSE 0 END) AS finish_books, SUM(CASE WHEN read_count = 0 THEN 1 ELSE 0 END) AS reading_books FROM userlibrary WHERE userid = ? AND start_date IS NOT NULL;";
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

                System.out.println("[📊내 독서 통계]\n");
                System.out.println("📘 독서중 책 수: " + readingBooks + "권");
                System.out.println("📗 완독한 책 수 : " + finishBooks + "권");
                System.out.println("--------------------------------------");
                System.out.println("📚 총 읽은 책 수: " + totalBooks + "권");
                System.out.println("📄 총 읽은 페이지 수: " + totalPages + "쪽");

                // 시간, 분, 초 계산
                int totalTimeInSeconds = 0;
                int hours = totalTimeInSeconds / 3600;
                int minutes = (totalTimeInSeconds % 3600) / 60;
                int seconds = totalTimeInSeconds % 60;

                // "HH:MM:SS" 형식으로 출력
                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                System.out.println("⏰ 총 독서 시간: " + totalTime);
            } else {
                System.out.println("❌독서 기록이 없습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("오류");
        }
        System.out.println();
    }

    // 다독왕 랭킹 리스트
    public List<String[]> printRank() {
        String sql = "select userid, sum(read_count) as finish_books from userlibrary where start_date is not null group by userid order by finish_books desc limit 5";
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
    // ============================= 독서 발자취 끝 =============================
}
