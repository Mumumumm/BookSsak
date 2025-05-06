import DBConnect.DBConnect;
import DBConnect.User;
import DBConnect.Book;

import java.util.*;

public class FootPrints {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;

    public void fooPrintsMenu(User currentUser) {
        Scanner input = new Scanner(System.in);
        System.out.println("===================");
        System.out.println("[독서 발자취]");
        System.out.println("===================");
        System.out.println("1. 내 서재");
        System.out.println("2. 내 독서 통계");
        System.out.println("3. 다독왕 랭킹");
        System.out.println("4. 메뉴로 가기");

        int select = input.nextInt();
        System.out.println();// ❗줄띄우기용

        switch (select) {
            case SELECT1:
                this.myLibrary(currentUser.getUserId());
                break;
            case SELECT2:
                this.myBookStatistics(currentUser.getUserId());
                break;
            case SELECT3:
                this.rank();
                break;
            case SELECT4:
                return;
        }
    }

    // 내 서재
    public void myLibrary(String userid){
        DBConnect db = new DBConnect();
        db.initDBConnect();

        List<String[]> myLibrary = db.printMyLibrary(userid);

        System.out.println("[내 서재]");

        if (myLibrary.isEmpty()) {
            System.out.println("내 서재가 비어있습니다.");
            db.releaseDB();
        } else {
            for (String[] list : myLibrary) {
                System.out.println("- " + list[0] + " / " + list[1] + " / " + list[2] + " / " + list[3] + " / " + list[4] +  " (" + list[5] + " ~ " + list[6] + ")"  );
            }
        }
        System.out.println();

        db.releaseDB();
    }

    // 내 독서 통계
    public void myBookStatistics(String userid){
        DBConnect db = new DBConnect();
        db.initDBConnect();

        db.myTotalRecoed(userid);

        db.releaseDB();
    }

    // 다독왕 랭킹
    public void rank(){
        DBConnect db = new DBConnect();
        db.initDBConnect();

        List<String[]> rank = db.printRank();

        System.out.println("[다독왕 랭킹]");

        if (rank.isEmpty()) {
            System.out.println("랭킹 순위가 없습니다.");
            db.releaseDB();
        } else {
            int i = 1;
            for (String[] list : rank) {
                System.out.println(i + "위: " + list[0] + " (총 " + list[1] +"권 완독)");
                i++;
            }
        }
        System.out.println();

        db.releaseDB();
    }
}
