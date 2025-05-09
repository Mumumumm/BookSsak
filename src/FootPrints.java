import DBConnect.DBFootPrint;
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
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("[\uD83D\uDC63독서 발자취]");
            System.out.println("1. 내 서재");
            System.out.println("2. 내 독서 통계");
            System.out.println("3. 다독왕 랭킹");
            System.out.println("4. 메뉴로 가기");
            System.out.println();
            System.out.print("메뉴선택 : ");

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
                    System.out.println("메인 메뉴로 이동\n");
                    return;
            }
        }
    }

    public void myBookLibrary(String userid) {
        DBFootPrint db = new DBFootPrint();
        db.initDBConnect();
        db.printMyLibrary(userid);
        db.releaseDB();
    }

    public void myBookStatistics(String userid) {
        DBFootPrint db = new DBFootPrint();
        db.initDBConnect();
        db.myTotalRecoed(userid);
        db.releaseDB();
    }

    public void rank() {
        DBFootPrint db = new DBFootPrint();
        db.initDBConnect();
        List<String[]> bookRank = db.printRank();
        db.releaseDB();

        if (bookRank.isEmpty()) {
            System.out.println("📭 다독왕 랭킹이 없습니다.");
        } else {
            System.out.println("🏆 다독왕 랭킹 (Top 5)\n");
            int rank = 1;
            for (String[] user : bookRank) {
                System.out.println(rank + "위: " + user[0] + " - " + user[1] + "권 완독" );
                rank++;
            }
        }
        System.out.println();
    }
}
