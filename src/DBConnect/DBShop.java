package DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBShop extends DBConnect{
    // 포인트샵
    public int getUserPoints(String userid){
        String sql = "select userpoints from user_reward where userid = ?";
        int points = 0;
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            points = rs.getInt("userpoints");
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return points;
    }

    public void buyBook(String userid, String isbn, int points, String addr){
        String sql = "update user_reward set userpoints = ? where userid = ?";
        String sql2 = "insert into orders values (null, ?, ?, ?, default)";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setInt(1, points);
            pstmt.setString(2, userid);
            pstmt.executeUpdate();
            pstmt.close();

            PreparedStatement pstmt2 = this.conn.prepareStatement(sql2);
            pstmt2.setString(1, userid);
            pstmt2.setString(2, isbn);
            pstmt2.setString(3, addr);
            pstmt2.executeUpdate();
            pstmt2.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void showOrders(String userid){
        String sql = "select * from orders o join books b on o.bookid = b.bookid join users u on u.userid = o.userid where u.userid = ? order by order_datetime";
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("\n[🧾구매 내역]");
                System.out.println();
                do {
                    System.out.println("주문 번호 : " + rs.getInt("orderid"));
                    System.out.println("주문 일자 : " + rs.getDate("order_datetime"));
                    System.out.println("상품 번호 : " + rs.getString("bookid"));
                    System.out.println("상품 정보 : " + rs.getString("title") + " / " + rs.getString("author") + " / " + rs.getString("publisher") + " / " + rs.getString("category"));
                    System.out.println("배송지 : " + rs.getString("addr"));
                    System.out.println("=================================================="); // 각 주문 사이에 공백 추가 (선택 사항)
                } while (rs.next());
            } else {
                System.out.println("\n🚫주문내역이 없습니다.\n");
            }
            rs.close();
            pstmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println();
    }
}
