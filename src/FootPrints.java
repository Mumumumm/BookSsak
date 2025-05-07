import DBConnect.DBConnect;
import DBConnect.DBFoot;
import DBConnect.User;

import java.util.List;
import java.util.Scanner;

public class FootPrints {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;


    public FootPrints() {
    }

    public void fooPrintsMenu(User currentUser) {
        Scanner input = new Scanner(System.in);
        System.out.println("[\uD83D\uDC63독서 발자취]");
        System.out.println("1. 내 서재");
        System.out.println("2. 내 독서 통계");
        System.out.println("3. 다독왕 랭킹");
        System.out.println("4. 메뉴로 가기");

        int select = input.nextInt();
        System.out.println();// ❗줄띄우기용

        switch (select) {
            case SELECT1:
                myBookLibrary(currentUser.getUserId());
                break;
            case SELECT2:
                myBookStatistics(currentUser.getUserId());
                break;
            case SELECT3:
                rank();
                break;
            case SELECT4:
                return;
        }
    }

    public void myBookLibrary(String userid) {
        DBConnect db = new DBConnect();
        db.initDBConnect();
        List<String[]> myLibrary = db.printMyLibrary(userid);
        db.releaseDB();

        if (myLibrary.isEmpty()) { // isEmpty 비어있는지 확인
            System.out.println("\uD83D\uDCED 현재 내 서재가 비어있습니다");
        } else {
            System.out.println("📚 내 서재 목록:");
            for (String[] book : myLibrary) {
                System.out.print(db.reandomEmoji() + book[0] +
                        " / " + book[1] +
                        " / " + book[2] +
                        " / " + book[3] +
                        " / " + book[4] + "\n");
                System.out.println(book[10]);
                System.out.println("읽은 페이지 : " + book[8] + "쪽 / " + book[7]);
                System.out.println("독서날짜 : " + book[5] + " ~ " + book[6]);
                System.out.println("==================================================");
            }
        }

    }

    public void myBookStatistics(String userid) {
        DBConnect db = new DBConnect();
        db.initDBConnect();
        db.myTotalRecoed(userid);
        db.releaseDB();
    }

    public void rank() {
        DBConnect db = new DBConnect();
        db.initDBConnect();
        List<String[]> bookRank = db.printRank();
        db.releaseDB();

        if (bookRank.isEmpty()) {
            System.out.println("📭 다독왕 랭킹이 없습니다.");
            System.out.println();
        } else {
            System.out.println("🏆 다독왕 랭킹 (Top 5):");
            int rank = 1;
            for (String[] user : bookRank) {
                System.out.println(rank + "위: " + user[0] + " - 완독한 책 수: " + user[1]);
                rank++;
            }
            System.out.println();
        }
    }
}
