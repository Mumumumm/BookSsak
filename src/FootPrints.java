import DBConnect.DBConnect;
import DBConnect.DBFoot;
import DBConnect.User;

import java.util.Scanner;

public class FootPrints {
    private final int SELECT1 = 1;
    private final int SELECT2 = 2;
    private final int SELECT3 = 3;
    private final int SELECT4 = 4;


    public FootPrints() {}

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
                myBookList();
                break;
            case SELECT2:
                myBookStatistics(currentUser.getUserId());
                break;
            case SELECT3:

                break;
            case SELECT4:
                return;
        }
    }
    public void myBookList(){

    }

    public void myBookStatistics(String userid){
        DBConnect db = new DBConnect();
        db.initDBConnect();
        db.myTotalRecoed(userid);
    }

    public void rank(){

    }
}
